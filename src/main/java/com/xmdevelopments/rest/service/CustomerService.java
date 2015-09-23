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

import com.xmdevelopments.controllers.CustomerController;
import com.xmdevelopments.model.Response;

/**
 * API->CustomerService
 *
 */
@RestController
public class CustomerService {

	final static Logger logger = Logger.getLogger(CustomerService.class);

	private CustomerController customerController = new CustomerController();
	
	public CustomerService() {
		logger.info("Api->CustomerService initialised");
	}

	/**
	 * Get all customers
	 * @param limit, limit
	 * @param page, pagination
	 * @return {Response} 
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/v1/customers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> getCustomers(@RequestParam(value = "limit", defaultValue = "100") Integer limit,
			@RequestParam(value = "page", defaultValue = "0") Integer page) throws BadRequestException {
		logger.info("getCustomers()->called");
		Response response = new Response();
		try {
			response = customerController.getCustomers(page, limit);
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}