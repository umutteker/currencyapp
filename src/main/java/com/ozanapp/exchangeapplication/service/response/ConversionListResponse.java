package com.ozanapp.exchangeapplication.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConversionListResponse {
    List<ConversionResponse> conversionList;
    int pageNumber;
    int pageSize;
    Long totalSize;
}
