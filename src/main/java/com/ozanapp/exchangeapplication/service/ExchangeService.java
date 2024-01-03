package com.ozanapp.exchangeapplication.service;

import com.ozanapp.exchangeapplication.service.request.ConversionListRequest;
import com.ozanapp.exchangeapplication.service.request.ConversionRequest;
import com.ozanapp.exchangeapplication.service.request.ExchangeRateRequest;
import com.ozanapp.exchangeapplication.service.response.ConversionListResponse;
import com.ozanapp.exchangeapplication.service.response.ConversionResponse;
import com.ozanapp.exchangeapplication.service.response.ExchangeRateResponse;

public interface ExchangeService {
  ExchangeRateResponse getExchangeRate(ExchangeRateRequest request) throws Exception;

  ConversionResponse convert(ConversionRequest request) throws Exception;

  ConversionListResponse getConversionList(ConversionListRequest request, int pageNo, int pageSize) throws Exception;
}
