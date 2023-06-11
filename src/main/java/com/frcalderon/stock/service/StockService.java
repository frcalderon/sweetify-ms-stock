package com.frcalderon.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frcalderon.stock.model.UpdateStockRequest;
import com.frcalderon.stock.controller.dto.StockRequest;
import com.frcalderon.stock.exceptions.StockNotFoundException;
import com.frcalderon.stock.model.Stock;
import com.frcalderon.stock.model.UpdateStock;
import com.frcalderon.stock.repository.StockRepository;
import com.frcalderon.stock.repository.UpdateStockRepository;
import com.frcalderon.stock.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockService {

    @Value("${ms-stock.ms-products.add-stock-uri:stock/add}")
    private String ADD_STOCK_URI;

    @Value("${ms-stock.ms-products.consume-stock-uri:stock/consume}")
    private String CONSUME_STOCK_URI;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UpdateStockRepository updateStockRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Stock> getAllStocks() {
        return this.stockRepository.findAll();
    }

    public Stock getStock(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(StockNotFoundException::new);
    }

    public Stock createStock(StockRequest stockRequest) throws JsonProcessingException {
        Stock stock = Stock.builder()
                .quantity(stockRequest.getQuantity())
                .expirationDate(LocalDate.parse(stockRequest.getExpirationDate(), Utils.localDateTimeFormatter()))
                .ingredientId(stockRequest.getIngredientId())
                .date(LocalDate.now())
                .build();

        UpdateStock updateStock = UpdateStock.builder()
                .request(
                        objectMapper.writeValueAsString(
                            UpdateStockRequest.builder()
                                    .ingredientId(stock.getIngredientId())
                                    .stock(stock.getQuantity())
                                    .build()
                        )
                )
                .uri(ADD_STOCK_URI)
                .sent(false)
                .build();

        updateStockRepository.save(updateStock);

        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, StockRequest stockRequest) throws JsonProcessingException {
        Stock stockToUpdate = stockRepository.findById(id)
                .orElseThrow(StockNotFoundException::new);

        UpdateStock consumeUpdateStock = UpdateStock.builder()
                .request(
                        objectMapper.writeValueAsString(
                                UpdateStockRequest.builder()
                                        .ingredientId(stockToUpdate.getIngredientId())
                                        .stock(stockToUpdate.getQuantity())
                                        .build()
                        )
                )
                .uri(CONSUME_STOCK_URI)
                .sent(false)
                .build();

        stockToUpdate.setQuantity(stockRequest.getQuantity());
        stockToUpdate.setExpirationDate(LocalDate.parse(stockRequest.getExpirationDate(), Utils.localDateTimeFormatter()));
        stockToUpdate.setIngredientId(stockRequest.getIngredientId());

        UpdateStock addUpdateStock = UpdateStock.builder()
                .request(
                        objectMapper.writeValueAsString(
                                UpdateStockRequest.builder()
                                        .ingredientId(stockRequest.getIngredientId())
                                        .stock(stockRequest.getQuantity())
                                        .build()
                        )
                )
                .uri(ADD_STOCK_URI)
                .sent(false)
                .build();

        updateStockRepository.save(consumeUpdateStock);
        updateStockRepository.save(addUpdateStock);

        return stockRepository.save(stockToUpdate);
    }

    public void deleteStock(Long id) throws JsonProcessingException {
        Stock stockToDelete = stockRepository.findById(id)
                .orElseThrow(StockNotFoundException::new);

        UpdateStock consumeUpdateStock = UpdateStock.builder()
                .request(
                        objectMapper.writeValueAsString(
                                UpdateStockRequest.builder()
                                        .ingredientId(stockToDelete.getIngredientId())
                                        .stock(stockToDelete.getQuantity())
                                        .build()
                        )
                )
                .uri(CONSUME_STOCK_URI)
                .sent(false)
                .build();

        updateStockRepository.save(consumeUpdateStock);

        stockRepository.deleteById(id);
    }

    public void expireStock() throws JsonProcessingException {
        List<Stock> expiredStockList = stockRepository.findAllWithExpirationDateTimeBefore(LocalDate.now());

        for (Stock expiredStock : expiredStockList) {
            UpdateStock consumeUpdateStock = UpdateStock.builder()
                    .request(
                            objectMapper.writeValueAsString(
                                    UpdateStockRequest.builder()
                                            .ingredientId(expiredStock.getIngredientId())
                                            .stock(expiredStock.getQuantity())
                                            .build()
                            )
                    )
                    .uri(CONSUME_STOCK_URI)
                    .sent(false)
                    .build();

            updateStockRepository.save(consumeUpdateStock);

            stockRepository.deleteById(expiredStock.getId());
        }
    }
}
