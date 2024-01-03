package com.ozanapp.exchangeapplication.controller;

import com.ozanapp.exchangeapplication.service.ExchangeService;
import com.ozanapp.exchangeapplication.service.request.ConversionListRequest;
import com.ozanapp.exchangeapplication.service.request.ConversionRequest;
import com.ozanapp.exchangeapplication.service.request.ExchangeRateRequest;
import com.ozanapp.exchangeapplication.service.response.ConversionListResponse;
import com.ozanapp.exchangeapplication.service.response.ConversionResponse;
import com.ozanapp.exchangeapplication.service.response.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
public class ExchangeController {
	@Autowired
	ExchangeService service;
	@GetMapping("/exchange-rate")
	public ResponseEntity<ExchangeRateResponse> getExchangeRate(@RequestBody ExchangeRateRequest request) throws Exception {


		return new ResponseEntity(service.getExchangeRate(request), HttpStatus.OK);
	}

	@PostMapping("/conversion")
	public ResponseEntity<ConversionResponse> convert(@RequestBody ConversionRequest request) throws Exception{

		return new ResponseEntity(service.convert(request), HttpStatus.OK);
	}
	
	@GetMapping("/conversion-list")
	public ResponseEntity<ConversionListResponse> getConversionList(@RequestParam(defaultValue = "0", required = false) int pageNumber,
	                                                                @RequestParam(defaultValue = "10", required = false) int pageSize,
	                                                                @RequestBody ConversionListRequest request) throws Exception {

		return new ResponseEntity(service.getConversionList(request,pageNumber,pageSize), HttpStatus.OK) ;

	}
	
}
