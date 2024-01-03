package com.ozanapp.exchangeapplication.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExchangeRateRequest {
  String source;
  String target;
}
