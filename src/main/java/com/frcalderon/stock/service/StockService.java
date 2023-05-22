package com.frcalderon.stock.service;

import com.frcalderon.stock.controller.dto.StockRequest;
import com.frcalderon.stock.exceptions.StockNotFoundException;
import com.frcalderon.stock.model.Stock;
import com.frcalderon.stock.repository.StockRepository;
import com.frcalderon.stock.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<Stock> getAllStocks() {
        return this.stockRepository.findAll();
    }

    public Stock getStock(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(StockNotFoundException::new);
    }

    public Stock createStock(StockRequest stockRequest) {
        Stock stock = Stock.builder()
                .quantity(stockRequest.getQuantity())
                .expirationDate(LocalDate.parse(stockRequest.getExpirationDate(), Utils.localDateTimeFormatter()))
                .ingredientId(stockRequest.getIngredientId())
                .date(LocalDate.now())
                .build();

        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, StockRequest stockRequest) {
        Stock stockToUpdate = stockRepository.findById(id)
                .orElseThrow(StockNotFoundException::new);

        stockToUpdate.setQuantity(stockRequest.getQuantity());
        stockToUpdate.setExpirationDate(LocalDate.parse(stockRequest.getExpirationDate(), Utils.localDateTimeFormatter()));
        stockToUpdate.setIngredientId(stockRequest.getIngredientId());

        return stockRepository.save(stockToUpdate);
    }

    public void deleteStock(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new StockNotFoundException();
        }

        stockRepository.deleteById(id);
    }
}
