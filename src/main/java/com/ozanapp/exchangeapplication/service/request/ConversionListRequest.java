package com.ozanapp.exchangeapplication.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConversionListRequest {
    LocalDate transactionDate;
    String transactionId;
}
