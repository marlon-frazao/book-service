package com.marlon.proxy;

import com.marlon.dto.Exchange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "exchange-service", url = "localhost:8000")
public interface ExchangeProxy {

    @GetMapping(value = "/exchange-service/{amount}/{from}/{to}")
    public Exchange getExchange(
            @PathVariable Double amount,
            @PathVariable String from,
            @PathVariable String to);


}
