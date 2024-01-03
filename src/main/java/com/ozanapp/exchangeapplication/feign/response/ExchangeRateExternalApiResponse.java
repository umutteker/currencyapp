package com.ozanapp.exchangeapplication.feign.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
@Data
public class ExchangeRateExternalApiResponse {

        Boolean success;
        long timestamp;
        String base;
        HashMap<String, BigDecimal> rates;
}
