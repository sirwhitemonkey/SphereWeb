package com.xmdevelopments.rest.security;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	final static Logger logger = Logger.getLogger(SecurityWebApplicationInitializer.class);

	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SecurityConfig.class };
	}

	@Override
	protected String getDispatcherWebApplicationContextSuffix() {
		logger.info("getDispatcherWebApplicationContextSuffix()->called");
		return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
	}

	@Override
	protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
		logger.info("afterSpringSecurityFilterChain()->called");
		insertFilters(servletContext, new HiddenHttpMethodFilter(), new MultipartFilter(),
				new OpenEntityManagerInViewFilter());
	}

	@Override
	protected boolean enableHttpSessionEventPublisher() {
		return true;
	}
}