package com.example.microservices.currencyconversionservice.controller;

import com.example.microservices.currencyconversionservice.model.CurrencyConversion;
import com.example.microservices.currencyconversionservice.web.client.CurrencyExchangeProxy;
import com.example.microservices.currencyconversionservice.web.client.model.CurrencyExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class CurrencyConversionController {

    private final Environment environment;
    private final CurrencyExchangeProxy currencyExchangeProxy;
    private final RestTemplate restTemplate;

    @Autowired
    public CurrencyConversionController(Environment environment, CurrencyExchangeProxy currencyExchangeProxy, RestTemplate restTemplate) {
        this.environment = environment;
        this.currencyExchangeProxy = currencyExchangeProxy;
        this.restTemplate = restTemplate;
    }

    @GetMapping(
            value = "/currency-conversion/from/{from}/to/{to}/quantity/{quantity}"
    )
    public CurrencyConversion convertCurrency(@PathVariable String from, @PathVariable String to,
                                              @PathVariable BigDecimal quantity) {
        log.info("ConvertCurrency called with {} to {} with {}", from, to, quantity);

        BigDecimal conversionMultiple = getConversionMultiple(from, to);
        BigDecimal totalCalculatedAmount = quantity.multiply(conversionMultiple);

        return CurrencyConversion.builder()
                .id(1000)
                .from(from)
                .to(to)
                .conversionMultiple(conversionMultiple)
                .quantity(quantity)
                .totalCalculatedAmount(totalCalculatedAmount)
                .environment(getEnvironmentDetails())
                .build();
    }

    private BigDecimal getConversionMultiple(String from, String to) {
        log.info("Using feign service to get conversion-multiple for {} to {}", from, to);
        CurrencyExchange currencyExchange = currencyExchangeProxy.getExchangeValue(from, to).getBody();
        if (currencyExchange == null)
            return null;

        return BigDecimal.valueOf(currencyExchange.getConversionMultiple());
    }

    private BigDecimal getConversionMultipleUsingRestTemplate(String from, String to) {
        log.info("Using rest-template service to get conversion-multiple for {} to {}", from, to);

        Map<String, String> uriPathVariables = new HashMap<>();
        uriPathVariables.put("from", from);
        uriPathVariables.put("to", to);

        CurrencyConversion conversion = restTemplate
                .getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class, uriPathVariables)
                .getBody();

        if (conversion == null)
            return null;

        return conversion.getConversionMultiple();
    }

    private String getEnvironmentDetails() {
        String port = environment.getProperty("local.server.port");
        String host = environment.getProperty("HOSTNAME");
        String version = "v12";

        return port + " " + version + " " + host;
    }
}
