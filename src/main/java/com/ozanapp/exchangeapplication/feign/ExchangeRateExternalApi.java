package com.ozanapp.exchangeapplication.feign;

import com.ozanapp.exchangeapplication.feign.response.ExchangeRateExternalApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "exchange-service", url = "${exchangeRate.endpoint}")
public interface ExchangeRateExternalApi {

    @GetMapping("/latest")
    ExchangeRateExternalApiResponse getExchangeRate(@RequestParam String access_key,
                                                    @RequestParam String symbols);
}
