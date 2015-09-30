package com.xmdevelopments.rest.service;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xmdevelopments.controllers.ManagementTransactionsController;
import com.xmdevelopments.controllers.SettingController;
import com.xmdevelopments.model.Response;
import com.xmdevelopments.model.Transaction;

/**
 * API->ManagementTransactionsService
 *
 */
@RestController
public class ManagementTransactionsService {

	final static Logger logger = Logger.getLogger(ManagementTransactionsService.class);

	private ManagementTransactionsController managementTransactionsController = new ManagementTransactionsController();
	
	public ManagementTransactionsService() {
		logger.info("Api->ManagementTransactionsService initialised");
	}


	/**
	 * Submit the transactions
	 * @param transaction, transaction model
	 * @return {Response}
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/v1/transactions/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> submit(@RequestBody Transaction transaction) throws BadRequestException {
		logger.info("submit()->called");
		Response response = new Response();
		try {
			response = managementTransactionsController.submit(transaction);
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}