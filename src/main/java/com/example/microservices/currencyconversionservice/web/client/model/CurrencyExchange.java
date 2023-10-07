package com.example.microservices.currencyconversionservice.web.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchange {
    private Long id;
    private String from;
    private String to;
    private Float conversionMultiple;
    private String port;
}
