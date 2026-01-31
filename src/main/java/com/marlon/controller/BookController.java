package com.marlon.controller;

import com.marlon.dto.Exchange;
import com.marlon.environment.InstanceInformationService;
import com.marlon.model.Book;
import com.marlon.proxy.ExchangeProxy;
import com.marlon.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("book-service")
public class BookController {

    @Autowired
    private InstanceInformationService informationService;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ExchangeProxy proxy;

    // http://localhost:8100/book-service/1/BRL
   @GetMapping(value = "/{id}/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(
            @PathVariable("id") Long id,
            @PathVariable("currency") String currency
   ) {

       String port = informationService.retrieveServerPort();
       try {

           var book = repository.findById(id).orElseThrow(() -> new RuntimeException("Book not found."));

           Exchange exchange = proxy.getExchange(
                   book.getPrice(),
                   "USD",
                   currency
           );

           book.setEnvironment(port + " FEIGN ");
           book.setPrice(Objects.requireNonNullElse(exchange, new Exchange()).getConvertedValue());
           book.setCurrency(currency);

           return book;
       } catch (Exception e) {
           return new Book(port + " FEIGN ", "ERROR: CURRENCY NOT SUPPORTED.");
       }
    }
}
