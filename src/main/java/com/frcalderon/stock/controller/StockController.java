package com.frcalderon.stock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frcalderon.stock.controller.dto.StockRequest;
import com.frcalderon.stock.controller.dto.StockResponse;
import com.frcalderon.stock.model.Stock;
import com.frcalderon.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponse> getAllStocks() {
        List<Stock> stocks = this.stockService.getAllStocks();
        return stocks.stream().map(StockResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StockResponse getStock(@PathVariable Long id) {
        Stock stock = this.stockService.getStock(id);
        return new StockResponse(stock);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockResponse createStock(@RequestBody StockRequest stockRequest) throws JsonProcessingException {
        Stock stock = this.stockService.createStock(stockRequest);
        return new StockResponse(stock);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StockResponse updateStock(@PathVariable Long id, @RequestBody StockRequest stockRequest) throws JsonProcessingException {
        Stock stock = this.stockService.updateStock(id, stockRequest);
        return new StockResponse(stock);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStock(@PathVariable Long id) throws JsonProcessingException {
        this.stockService.deleteStock(id);
    }
}
