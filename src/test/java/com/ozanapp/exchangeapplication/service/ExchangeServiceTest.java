package com.ozanapp.exchangeapplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ozanapp.exchangeapplication.entity.Conversion;
import com.ozanapp.exchangeapplication.exception.CustomBadRequestException;
import com.ozanapp.exchangeapplication.exception.CustomNotFoundException;
import com.ozanapp.exchangeapplication.feign.ExchangeRateExternalApi;
import com.ozanapp.exchangeapplication.feign.response.ExchangeRateExternalApiResponse;
import com.ozanapp.exchangeapplication.repository.ConversionRepository;
import com.ozanapp.exchangeapplication.service.impl.ExchangeServiceImpl;
import com.ozanapp.exchangeapplication.service.request.ConversionListRequest;
import com.ozanapp.exchangeapplication.service.request.ConversionRequest;
import com.ozanapp.exchangeapplication.service.request.ExchangeRateRequest;
import com.ozanapp.exchangeapplication.service.response.ConversionListResponse;
import com.ozanapp.exchangeapplication.service.response.ConversionResponse;
import com.ozanapp.exchangeapplication.service.response.ExchangeRateResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.framework;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @Mock
    ExchangeRateExternalApi externalRateService;

    @Mock
    ConversionRepository repository;

    @InjectMocks
    ExchangeServiceImpl exchangeService;
    String sourceCurrency = "EUR";
    String targetCurrency = "TRY";
    BigDecimal currencyRate = new BigDecimal(45.57).setScale(2, RoundingMode.DOWN);
    BigDecimal sourceAmount = new BigDecimal(100);
    String transactionId = UUID.randomUUID().toString();
    @Test
    public void getExchangeRate_Successful() throws Exception {
        ExchangeRateRequest exchangeRateRequest = getExchangeRateRequest();
        ExchangeRateResponse response = new ExchangeRateResponse(sourceCurrency,targetCurrency,currencyRate);
        ExchangeRateExternalApiResponse exchangeRateExternalApiResponse =getExchangeRateExternalApiResponse();

        when(externalRateService.getExchangeRate(null,targetCurrency)).thenReturn(exchangeRateExternalApiResponse);

        ExchangeRateResponse returnedResponse = exchangeService.getExchangeRate(exchangeRateRequest);

        assertEquals(response.getRate(),returnedResponse.getRate());
        assertEquals(response.getBase(),returnedResponse.getBase());
        assertEquals(response.getSymbol(),returnedResponse.getSymbol());
    }

    @Test
    public void getExchangeRate_IfRateNotEUR_ThrowBadRequest() throws Exception {
        ExchangeRateRequest exchangeRateRequest = getExchangeRateRequest();
        exchangeRateRequest.setSource("USD");

        CustomBadRequestException exception = assertThrows(CustomBadRequestException.class,
                () -> exchangeService.getExchangeRate(exchangeRateRequest));
    }

    @Test
    public void getExchangeRate_IfSourceIsNull_ThrowBadRequest() throws Exception {

        ExchangeRateRequest exchangeRateRequest = getExchangeRateRequest();
        exchangeRateRequest.setSource(null);


        CustomBadRequestException exception = assertThrows(CustomBadRequestException.class,
                () -> exchangeService.getExchangeRate(exchangeRateRequest));
    }

    @Test
    public void getExchangeRate_IfTargetIsNull_ThrowBadRequest() throws Exception {
        ExchangeRateRequest exchangeRateRequest = getExchangeRateRequest();
        exchangeRateRequest.setTarget(null);

        CustomBadRequestException exception = assertThrows(CustomBadRequestException.class,
                () -> exchangeService.getExchangeRate(exchangeRateRequest));
    }

    @Test
    public void convert_Successful() throws Exception {
        ConversionRequest conversionRequest = getConversionRequest();
        ConversionResponse response = new ConversionResponse(currencyRate.multiply(sourceAmount),transactionId);
        ExchangeRateExternalApiResponse exchangeRateExternalApiResponse =getExchangeRateExternalApiResponse();

        when(externalRateService.getExchangeRate(null,targetCurrency)).thenReturn(exchangeRateExternalApiResponse);

        ConversionResponse returnedResponse = exchangeService.convert(conversionRequest);

        assertEquals(response.getTargetAmount(),returnedResponse.getTargetAmount());
    }

    @Test
    public void convert_whenSourceAmountIsNull_ThrowException() throws Exception {
        ConversionRequest conversionRequest = getConversionRequest();
        conversionRequest.setSourceAmount(null);
        ExchangeRateExternalApiResponse exchangeRateExternalApiResponse =getExchangeRateExternalApiResponse();

        when(externalRateService.getExchangeRate(null,targetCurrency)).thenReturn(exchangeRateExternalApiResponse);

        CustomBadRequestException exception = assertThrows(CustomBadRequestException.class,
                () -> exchangeService.convert(conversionRequest));
    }
    @Test
    public void convert_whenSourceAmountIsNegative_ThrowException() throws Exception {
        ConversionRequest conversionRequest = getConversionRequest();
        conversionRequest.setSourceAmount(new BigDecimal(-5));
        ExchangeRateExternalApiResponse exchangeRateExternalApiResponse =getExchangeRateExternalApiResponse();

        when(externalRateService.getExchangeRate(null,targetCurrency)).thenReturn(exchangeRateExternalApiResponse);

        CustomBadRequestException exception = assertThrows(CustomBadRequestException.class,
                () -> exchangeService.convert(conversionRequest));
    }
    @Test
    public void convert_whenSourceAmountIsZero_ThrowException() throws Exception {
        ConversionRequest conversionRequest = getConversionRequest();
        conversionRequest.setSourceAmount(BigDecimal.ZERO);
        ExchangeRateExternalApiResponse exchangeRateExternalApiResponse =getExchangeRateExternalApiResponse();

        when(externalRateService.getExchangeRate(null,targetCurrency)).thenReturn(exchangeRateExternalApiResponse);

        CustomBadRequestException exception = assertThrows(CustomBadRequestException.class,
                () -> exchangeService.convert(conversionRequest));
    }
    @Test
    public void getConversionList_Successful() throws Exception{
        int pageNumber =0 ;
        int pageSize = 10 ;
        Pageable pageable =  PageRequest.of(pageNumber,pageSize, Sort.by("id"));

        ConversionListRequest conversionListRequest = getConversionListRequest();
        List<Conversion> conversionList =  getConversionList();

        when(repository.findByTransactionIdAndTransactionDate(conversionListRequest.getTransactionId(), conversionListRequest.getTransactionDate(), pageable)).thenReturn(new PageImpl<Conversion>(conversionList));

        ConversionListResponse response = exchangeService.getConversionList(conversionListRequest,pageNumber,pageSize);

        assertEquals(response.getConversionList().size(),conversionList.size());
    }

    @Test
    public void getConversionList_WhenNoDataFound_ThrowCustomNotFound() throws Exception{
        int pageNumber =0 ;
        int pageSize = 10 ;
        Pageable pageable =  PageRequest.of(pageNumber,pageSize, Sort.by("id"));

        ConversionListRequest conversionListRequest = getConversionListRequest();
        List<Conversion> conversionList =  new ArrayList<>();
        when(repository.findByTransactionIdAndTransactionDate(conversionListRequest.getTransactionId(), conversionListRequest.getTransactionDate(), pageable)).thenReturn(new PageImpl<Conversion>(conversionList));

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> exchangeService.getConversionList(conversionListRequest,pageNumber,pageSize));
    }

    @NotNull
    private ExchangeRateRequest getExchangeRateRequest() {
        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest();
        exchangeRateRequest.setSource(sourceCurrency);
        exchangeRateRequest.setTarget(targetCurrency);
        return exchangeRateRequest;
    }

    @NotNull
    private ExchangeRateExternalApiResponse getExchangeRateExternalApiResponse() {

        HashMap<String, BigDecimal> rates = new HashMap<>();
        rates.put(targetCurrency, currencyRate);
        ExchangeRateExternalApiResponse exchangeRateExternalApiResponse = new ExchangeRateExternalApiResponse();
        exchangeRateExternalApiResponse.setRates(rates);
        exchangeRateExternalApiResponse.setBase(sourceCurrency);
        return exchangeRateExternalApiResponse;
    }

    @NotNull
    private ConversionRequest getConversionRequest() {
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setSourceAmount(sourceAmount);
        conversionRequest.setSourceCurrency(sourceCurrency);
        conversionRequest.setTargetCurrency(targetCurrency);
        return conversionRequest;
    }

    @NotNull
    private ConversionListRequest getConversionListRequest() {
        ConversionListRequest conversionListRequest = new ConversionListRequest();
        conversionListRequest.setTransactionDate(LocalDate.now());
        conversionListRequest.setTransactionId(UUID.randomUUID().toString());
        return conversionListRequest;
    }

    @NotNull
    private ConversionResponse getConversionResponse() {
        ConversionResponse conversionResponse = new ConversionResponse();
        conversionResponse.setTargetAmount(sourceAmount);
        conversionResponse.setTransactionId(UUID.randomUUID().toString());
        return conversionResponse;
    }

    @NotNull
    private ConversionListResponse getConversionListResponse() {
        ConversionListResponse conversionListResponse = new ConversionListResponse();
        List<ConversionResponse> conversionResponseList = new ArrayList<ConversionResponse>();
        conversionResponseList.add(getConversionResponse());
        conversionResponseList.add(getConversionResponse());
        conversionResponseList.add(getConversionResponse());

        conversionListResponse.setConversionList(conversionResponseList);
        conversionListResponse.setTotalSize(16L);
        conversionListResponse.setPageSize(10);
        conversionListResponse.setPageNumber(0);
        return conversionListResponse;
    }

    @NotNull
    private List<Conversion> getConversionList() {
        List<Conversion> conversionList = new ArrayList<Conversion>();
        Conversion conversion1 = new Conversion();
        conversion1.setTargetAmount(sourceAmount);
        conversion1.setTransactionId(UUID.randomUUID().toString());

        Conversion conversion2 = new Conversion();
        conversion2.setTargetAmount(sourceAmount);
        conversion2.setTransactionId(UUID.randomUUID().toString());

        Conversion conversion3 = new Conversion();
        conversion3.setTargetAmount(sourceAmount);
        conversion3.setTransactionId(UUID.randomUUID().toString());
        conversionList.add(conversion1);
        conversionList.add(conversion2);
        conversionList.add(conversion3);
        return conversionList;
    }




}
