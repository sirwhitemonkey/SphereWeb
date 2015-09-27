package com.xmdevelopments.rest.service;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xmdevelopments.controllers.SettingController;
import com.xmdevelopments.model.Response;

/**
 * API->SettingService
 *
 */
@RestController
public class SettingService {

	final static Logger logger = Logger.getLogger(SettingService.class);

	private SettingController settingController = new SettingController();
	
	public SettingService() {
		logger.info("Api->AuthService initialised");
	}


	/**
	 * Getting the themes
	 * @return {Response}
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/v1/setting/themes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> getThemes() throws BadRequestException {
		logger.info("getThemes()->called");
		Response response = new Response();
		try {
			response = settingController.getThemes();
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Getting the data defaults
	 * @return {Response}
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/v1/setting/dataDefaults", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> getDataDefaults() throws BadRequestException {
		logger.info("getDataDefaults()->called");
		Response response = new Response();
		try {
			response = settingController.getDataDefaults();
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Checking the connection
	 * @return {Response}
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/v1/setting/checkConnection", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response> checkConnection() throws BadRequestException {
		logger.info("checkConnection()->called");
		Response response = new Response();
		try {
			response = settingController.checkConnection();
		} catch (Exception ex) {
			throw new BadRequestException("Invalid request");
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}