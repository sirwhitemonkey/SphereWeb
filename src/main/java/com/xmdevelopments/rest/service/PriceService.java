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

import com.xmdevelopments.controllers.PriceController;
import com.xmdevelopments.model.Response;

/**
 * API->PriceService
 *
 */
@RestController
public class PriceService {

	final static Logger logger = Logger.getLogger(PriceService.class);

	private PriceController priceController = new PriceController();
	
	public PriceService() {
		logger.info("Api->PriceService initialised");
	}

	/**
	 * Get all prices
	 * @param date_last_updated, date last updated
	 * @param limit, limit
	 * @param page, pagination
	 * @return {Response} 
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/v1/prices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> getPrices(@RequestParam(value = "date_last_updated") String date_last_updated,
			@RequestParam(value = "limit", defaultValue = "100") Integer limit,
			@RequestParam(value = "page", defaultValue = "0") Integer page) throws BadRequestException {
		logger.info("getPrices()->called");
		Response response = new Response();
		try {
			response = priceController.getPrices(date_last_updated, page, limit);
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}