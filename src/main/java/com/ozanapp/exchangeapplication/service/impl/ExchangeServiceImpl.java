package com.ozanapp.exchangeapplication.service.impl;

import com.ozanapp.exchangeapplication.entity.Conversion;
import com.ozanapp.exchangeapplication.exception.CustomBadRequestException;
import com.ozanapp.exchangeapplication.exception.CustomInternalServerException;
import com.ozanapp.exchangeapplication.exception.CustomNotFoundException;
import com.ozanapp.exchangeapplication.feign.ExchangeRateExternalApi;
import com.ozanapp.exchangeapplication.feign.response.ExchangeRateExternalApiResponse;
import com.ozanapp.exchangeapplication.repository.ConversionRepository;
import com.ozanapp.exchangeapplication.service.ExchangeService;
import com.ozanapp.exchangeapplication.service.request.ConversionListRequest;
import com.ozanapp.exchangeapplication.service.request.ConversionRequest;
import com.ozanapp.exchangeapplication.service.request.ExchangeRateRequest;
import com.ozanapp.exchangeapplication.service.response.ConversionListResponse;
import com.ozanapp.exchangeapplication.service.response.ConversionResponse;
import com.ozanapp.exchangeapplication.service.response.ExchangeRateResponse;
import com.ozanapp.exchangeapplication.util.ExchangeRateConstant;
import com.ozanapp.exchangeapplication.util.ExchangeRateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {


  @Value("${exchangeRate.accesKey}")
  private String accesKey;
  @Autowired
  ExchangeRateExternalApi ser;

  @Autowired
  private ConversionRepository repository;

  @Override
  public ExchangeRateResponse getExchangeRate( ExchangeRateRequest request) throws Exception {

    if (request == null ) {
      throw new CustomBadRequestException("Request Cannot be null!", "VALIDATION_ERROR");
    }

    if (( request.getSource() == null) || request.getTarget() == null){
      throw new CustomBadRequestException("Base or Target cannot be null!", "VALIDATION_ERROR");
    }

    if ( !request.getSource().equalsIgnoreCase(ExchangeRateConstant.EUR)) {
      throw new CustomBadRequestException("Base rate is only applicable for EUR ", "VALIDATION_ERROR");
    }
    if( request.getTarget().length() != 3){
      throw new CustomBadRequestException("Symbol Rate is not valid, It should be 3 letter abbreviation", "VALIDATION_ERROR");
    }

    ExchangeRateExternalApiResponse rateResponse =null;
    try {
      rateResponse = ser.getExchangeRate(accesKey,request.getTarget());
    }catch (Exception ex){
      throw new CustomInternalServerException(ex.getMessage(),"EXTERNAL_SERVICE_EXCEPTION");
    }

    BigDecimal rate = rateResponse.getRates().get(request.getTarget());
    ExchangeRateResponse response = new ExchangeRateResponse();
    response.setSource(request.getSource());
    response.setRate(rate);
    response.setTarget(request.getTarget());

    return response;
  }

  @Override
  public ConversionResponse convert(ConversionRequest request) throws Exception{
    ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest();
    exchangeRateRequest.setSource(request.getSourceCurrency());
    exchangeRateRequest.setTarget(request.getTargetCurrency());
    ExchangeRateResponse exchangeRateResponse = getExchangeRate(exchangeRateRequest);

    if(request.getSourceAmount()== null ||request.getSourceAmount().compareTo(BigDecimal.ZERO)<=0){
      throw new CustomBadRequestException("Source Amount Cannot be null, Zero or Negative", "INVALID_SOURCE_AMOUNT");
    }

    BigDecimal targetAmount = exchangeRateResponse.getRate().multiply(request.getSourceAmount()).setScale(2, RoundingMode.DOWN);

    Conversion conversion = new Conversion();
    conversion.setTarget(request.getTargetCurrency());
    conversion.setSource(request.getSourceCurrency());
    conversion.setExchangeRate(exchangeRateResponse.getRate());
    conversion.setTargetAmount(targetAmount);
    conversion.setSourceAmount(request.getSourceAmount());
    repository.save(conversion);

    ConversionResponse response = new ConversionResponse();
    response.setTargetAmount(targetAmount);
    response.setTransactionId(conversion.getTransactionId());

    return response;
  }

  @Override
  public ConversionListResponse getConversionList(ConversionListRequest request, int pageNumber, int pageSize) throws Exception {
    ExchangeRateUtil mapper= new ExchangeRateUtil();

    Pageable pageable =  PageRequest.of(pageNumber,pageSize, Sort.by("id"));
    ConversionListResponse response = new ConversionListResponse();

    var queryResponse = repository.findByTransactionIdAndTransactionDate(request.getTransactionId(), request.getTransactionDate(), pageable);
    if(queryResponse.getTotalElements()== 0){
      throw new CustomNotFoundException("No available data for given Transaction Id and Transaction Date","NO_DATA_FOUND");
    }

    List<ConversionResponse> conv = queryResponse.stream()
            .map(mapper::toConversionResponse)
            .collect(Collectors.toList());

    response.setConversionList(conv);
    response.setPageSize(pageSize);
    response.setPageNumber(pageNumber);
    response.setTotalSize(queryResponse.getTotalElements());

    return response;
  }
}
