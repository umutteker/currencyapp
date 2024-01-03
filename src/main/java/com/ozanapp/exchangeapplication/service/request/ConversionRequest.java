package com.ozanapp.exchangeapplication.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConversionRequest {
    String sourceCurrency;
    String targetCurrency;
    BigDecimal sourceAmount;

}
