package com.xmdevelopments.rest.security.filter;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.xmdevelopments.model.Response;
import com.xmdevelopments.rest.security.WsseAuthenticationToken;
import com.xmdevelopments.rest.security.WsseCredentials;

public class WsseAuthenticationFilter extends GenericFilterBean {

	private final static Logger logger = LoggerFactory.getLogger(WsseAuthenticationFilter.class);

	private AuthenticationManager authenticationManager;

	@SuppressWarnings("unused")
	private Set<String> managementEndpoints;

	public WsseAuthenticationFilter(AuthenticationManager authenticationManager) {
		logger.info("WsseAuthenticationFilter()->called");
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        String prefix = "doFilter()";
		logger.info(prefix + "->called");
		
		try {
			HttpServletRequest httpRequest = asHttp(request);
			Optional<String> wsseHeader = Optional.fromNullable(httpRequest.getHeader("X-WSSE"));

			if (!wsseHeader.isPresent()) {
				processAuthentication(request, response, chain, null, null, null, null);
		
			} else {
				Matcher matcher = parseWsseHeader(wsseHeader);

				if (!matcher.find()) {
					logger.info(prefix +"->wsse header but NO MATCH");
					processAuthentication(request, response, chain, null, null, null, null);
				}
				String username = matcher.group(1);
				String password = matcher.group(2);
				String nonce = matcher.group(3);
				String created = matcher.group(4);
				processAuthentication(request, response, chain, username, password, nonce, created);
			}

		} catch(IllegalStateException isEx) {
			logger.error(prefix,isEx);
		}
	}
	
	/**
	 * Parse Wsse Header
	 * @param wsseHeader
	 * @return
	 */
	private Matcher parseWsseHeader(Optional<String> wsseHeader) {
		String wsseRegex = "UsernameToken Username=\"(\\S+)\", PasswordDigest=\"(\\S+)\", Nonce=\"(\\w+)\", Created=\"(\\S+)\"";
 
		logger.debug("parseWsseHeader()->Wsse Header: " + wsseHeader.get());
		Pattern pattern = Pattern.compile(wsseRegex);
		return pattern.matcher(wsseHeader.get());
	}

	/**
	 * Process 
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @param username
	 * @param password
	 * @param nonce
	 * @param created
	 * @throws IOException
	 * @throws ServletException
	 */
	@SuppressWarnings("unused")
	private void processAuthentication(ServletRequest request, ServletResponse response, FilterChain chain,
			String username, String password, String nonce, String created) throws IOException, ServletException {

		String prefix = "processAuthentication()";
		logger.info(prefix +"->processAuthentication()->called");
		HttpServletRequest httpRequest = asHttp(request);
		HttpServletResponse httpResponse = asHttp(response);

		String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
	
		try {
			
			Optional<String> usernameOpt = Optional.fromNullable(username);
			Optional<String> passwordOpt = Optional.fromNullable(password);
			Optional<String> nonceOpt = Optional.fromNullable(nonce);
			Optional<String> createdOpt = Optional.fromNullable(created);
			
			logger.info(prefix+"->trying to authenticate user {} for management endpoint by X-wsse method");
			processAuthentication(usernameOpt, passwordOpt, nonceOpt, createdOpt);
			logger.info(prefix +"->managementEndpointAuthenticationFilter is passing request down the filter chain");
			chain.doFilter(request, response);
	
		} catch (AuthenticationException aEx) {
			logger.error(prefix,aEx);
			SecurityContextHolder.clearContext();
			ObjectMapper mapper = new ObjectMapper();
			Response restResponse = new Response(aEx.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.getOutputStream().println(mapper.writeValueAsString(restResponse));
		}
	}

	private HttpServletRequest asHttp(ServletRequest request) {
		return (HttpServletRequest) request;
	}

	private HttpServletResponse asHttp(ServletResponse response) {
		return (HttpServletResponse) response;
	}


	private void processAuthentication(Optional<String> username,
			Optional<String> password, Optional<String> nonce, Optional<String> created) throws IOException {
		logger.info("processAuthentication()->called");

		Authentication resultOfAuthentication = tryToAuthenticate(username, password, nonce, created);
		SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
	}
	
	

	private Authentication tryToAuthenticate(Optional<String> username,
			Optional<String> password, Optional<String> nonce, Optional<String> created) {
		String prefix = "tryToAuthenticate()";
		
		logger.info(prefix +"->tryToAuthenticate()->called");
		
		WsseCredentials wsseCredentials = new WsseCredentials(password, nonce, created);
		Authentication responseAuthentication = authenticationManager.authenticate(
				new WsseAuthenticationToken(username, wsseCredentials)
				);
		
		if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
			throw new InternalAuthenticationServiceException(
					"Unable to authenticate for provided credentials");
		}
		logger.info(prefix + "->" + username.toString() + "->Successfully authenticated");
		return responseAuthentication;
	}
}
