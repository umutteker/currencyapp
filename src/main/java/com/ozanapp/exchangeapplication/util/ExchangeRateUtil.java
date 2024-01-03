package com.ozanapp.exchangeapplication.util;

import com.ozanapp.exchangeapplication.entity.Conversion;
import com.ozanapp.exchangeapplication.service.response.ConversionResponse;
import org.jetbrains.annotations.NotNull;

public class ExchangeRateUtil {
    public ConversionResponse toConversionResponse(@NotNull Conversion conversion){
        ConversionResponse response = new ConversionResponse();
        response.setTransactionId(conversion.getTransactionId());
        response.setTargetAmount(conversion.getTargetAmount());

        return response;
    }

}
