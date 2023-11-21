package com.gestion.spa.rest.controllers;

import com.gestion.spa.models.Item;
import com.gestion.spa.models.Stock;
import com.gestion.spa.repositories.StockRepository;
import com.gestion.spa.service.StockServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/stock")
public class StockController {
    @Autowired
    private StockRepository stockRepository;
    @PostMapping("add")
    public ResponseEntity<String> AddStock(@RequestParam String name)
    {
        Stock stock = new Stock();
        stock.setName(name);
        stockRepository.save(stock);
        return new ResponseEntity<>("Stock Added",HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<Stock>> seeStocks()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            return new ResponseEntity<>(stockRepository.findAll(), HttpStatus.OK);

    }


}
