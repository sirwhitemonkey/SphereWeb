package com.xmdevelopments.rest.service;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xmdevelopments.controllers.UserController;
import com.xmdevelopments.model.Response;

/**
 * API->ProductService
 *
 */
@RestController
public class UserService {

	final static Logger logger = Logger.getLogger(UserService.class);

	private UserController userController = new UserController();
	
	public UserService() {
		logger.info("Api->UserService initialised");
	}

	/**
	 * Get all users which are not deleted
	 * @param limit, limit
	 * @param page, pagination
	 * @return {Response} 
	 */
	@RequestMapping(value = "/v1/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> getUsers(@RequestParam(value = "limit", defaultValue = "100") Integer limit,
			@RequestParam(value = "page", defaultValue = "0") Integer page) throws BadRequestException {
		logger.info("getUsers()->called");
		Response response = new Response();
		try {
			response = userController.getUsers(false, page, limit);
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}