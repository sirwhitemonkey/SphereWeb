package com.xmdevelopments.rest.security.provider;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

import com.google.common.base.Optional;
import com.xmdevelopments.controllers.UserController;
import com.xmdevelopments.model.Response;
import com.xmdevelopments.model.entities.User;
import com.xmdevelopments.rest.security.WsseAuthenticationToken;
import com.xmdevelopments.rest.security.WsseCredentials;

public class WsseAuthenticationProvider implements AuthenticationProvider {

    public static final String INVALID_CREDENTIALS = "Invalid Credentials";

    private UserController userController = new UserController();
    
	private Logger logger = Logger.getLogger(WsseAuthenticationProvider.class);
	

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    
    	String prefix = "authenticate()";
    	
        Optional<String> username = (Optional) authentication.getPrincipal();
        WsseCredentials wsseCredentials = (WsseCredentials) authentication.getCredentials();
        
        

      	if (credentialsMissing(username, wsseCredentials)) {
      		logger.info(prefix +"->Credentials missing");  	
      		throw new BadCredentialsException(INVALID_CREDENTIALS);
        }
        
        logger.info(prefix+ "->Username: " + username.get());
                
      	Response response  = userController.getUser(username.get(), false, 1);
      	User user = (User)response.getData();     	
     	if (user == null) {     		
     		logger.info(prefix +"->User not found");     		
     		throw new BadCredentialsException(INVALID_CREDENTIALS);
     	}
     			
     	if (isPasswordInvalid(wsseCredentials, user.getPassword())) {     		
     		logger.info(prefix +"->Passwords don't match");
     		
     		throw new BadCredentialsException(INVALID_CREDENTIALS);
     	}
     	
      	logger.info(prefix +"->--Valid User---");
      	logger.info(prefix +"->User Id: " + user.getID());  	

        return new UsernamePasswordAuthenticationToken("test", null,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_BACKEND_ADMIN"));
    }

    /**
     * Check if the given password is equal to the actual password
     * [TODO] update to WSSE specification
     * 
     * @param wssePassword
     * @param userPassword
     * @return
     */
    @SuppressWarnings("deprecation")
	private boolean isPasswordInvalid(WsseCredentials wsseCredentials, String userPassword) {
    	//encriptions done
    	//sha1(md5($passwd));
        //PasswordDigest = Base64 \ (SHA1 (Nonce + CreationTimestamp + Password))
    	String prefix = "isPasswordInvalid()";
    	logger.info(prefix + "->Nonce: " + wsseCredentials.getNonce().get());
    	logger.info(prefix + "->Created: " + wsseCredentials.getCreated().get());
    	
    	byte[] calculatedPasswordDigest = Base64.encodeBase64(DigestUtils.sha(
    			wsseCredentials.getNonce().get() +
    			wsseCredentials.getCreated().get() +
    			userPassword    			
    			));
    	
    	String calculatedPasswordDigestString = new String(calculatedPasswordDigest);

    	logger.info(prefix +"->Calculated Password Digest: " + calculatedPasswordDigestString);
    	
    	return !calculatedPasswordDigestString.equals(wsseCredentials.getPasswordDigest().get());
    }
    
    /**
     * Check if credentials are missing
     * 
     * @param username
     * @param password
     * @return
     */
    private boolean credentialsMissing(Optional<String> username, WsseCredentials wsseCredentials) {
        return !username.isPresent() || !wsseCredentials.getPasswordDigest().isPresent();
    }

   
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(WsseAuthenticationToken.class);
    }
}