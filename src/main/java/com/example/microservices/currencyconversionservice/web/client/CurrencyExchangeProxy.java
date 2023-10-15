package com.example.microservices.currencyconversionservice.web.client;

import com.example.microservices.currencyconversionservice.web.client.model.CurrencyExchange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="currency-exchange")
public interface CurrencyExchangeProxy {

    @GetMapping(
            value = "currency-exchange/from/{from}/to/{to}"
    )
    ResponseEntity<CurrencyExchange> getExchangeValue(@PathVariable String from, @PathVariable String to);
}
