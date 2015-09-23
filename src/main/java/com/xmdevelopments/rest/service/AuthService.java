package com.xmdevelopments.rest.service;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xmdevelopments.controllers.AuthController;
import com.xmdevelopments.model.Response;

/**
 * API->AuthService
 *
 */
@RestController
public class AuthService {

	final static Logger logger = Logger.getLogger(AuthService.class);

	private AuthController authController = new AuthController();
	
	public AuthService() {
		logger.info("Api->AuthService initialised");
	}


	/**
	 * Submit authentication
	 * @return {Response}
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/v1/auth/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> submit() throws BadRequestException {
		logger.info("submit()->called");
		Response response = new Response();
		try {
			response = authController.submit();
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}