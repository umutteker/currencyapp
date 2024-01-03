package com.ozanapp.exchangeapplication.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConversionResponse {
  BigDecimal targetAmount;
  String transactionId;
}
