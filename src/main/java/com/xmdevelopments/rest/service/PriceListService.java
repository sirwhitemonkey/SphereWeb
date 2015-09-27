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

import com.xmdevelopments.controllers.PriceListController;
import com.xmdevelopments.model.Response;

/**
 * API->PriceListService
 *
 */
@RestController
public class PriceListService {

	final static Logger logger = Logger.getLogger(PriceListService.class);

	private PriceListController priceListController = new PriceListController();
	
	public PriceListService() {
		logger.info("Api->PriceListService initialised");
	}

	/**
	 * Get all price lists
	 * @param limit, limit
	 * @param page, pagination
	 * @return {Response} 
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/v1/priceLists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> getPriceLists(@RequestParam(value = "limit", defaultValue = "100") Integer limit,
			@RequestParam(value = "page", defaultValue = "0") Integer page) throws BadRequestException {
		logger.info("getPriceLists()->called");
		Response response = new Response();
		try {
			response = priceListController.getPriceLists(page, limit);
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
    /**
     * Get all price lists using price list code
	 * @param limit, limit
	 * @param page, pagination
	 * @return {Response} 
	 * @throws BadRequestException
     */
	@RequestMapping(value = "/v1/priceLists/code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> getPriceLists(@RequestParam("code") String code,
			@RequestParam(value = "limit", defaultValue = "100") Integer limit,
			@RequestParam(value = "page", defaultValue = "0") Integer page) throws BadRequestException {
		logger.info("getPriceLists()->called");
		Response response = new Response();
		try {
			response = priceListController.getPriceLists(code, page, limit);
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}