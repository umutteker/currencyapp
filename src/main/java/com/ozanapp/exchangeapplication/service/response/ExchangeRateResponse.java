package com.ozanapp.exchangeapplication.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponse {
  private String source;
  private String target;
  private  BigDecimal rate;
}
