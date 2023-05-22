package com.frcalderon.stock.repository;

import com.frcalderon.stock.model.Stock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StockRepositoryTests {

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void StockRepository_GetAll_ReturnMoreThanOneStock() {
        Stock stock1 = Stock.builder()
                .quantity(50.0)
                .expirationDate(LocalDate.now().plusMonths(5))
                .date(LocalDate.now())
                .ingredientId(1L)
                .build();

        Stock stock2 = Stock.builder()
                .quantity(150.0)
                .expirationDate(LocalDate.now().plusMonths(5))
                .date(LocalDate.now())
                .ingredientId(1L)
                .build();

        stockRepository.save(stock1);
        stockRepository.save(stock2);

        List<Stock> stockList = stockRepository.findAll();

        Assertions.assertThat(stockList).isNotNull();
        Assertions.assertThat(stockList.size()).isEqualTo(2);
    }

    @Test
    public void StockRepository_FindById_ReturnStock() {
        Stock stock = Stock.builder()
                .quantity(50.0)
                .expirationDate(LocalDate.now().plusMonths(5))
                .date(LocalDate.now())
                .ingredientId(1L)
                .build();

        stockRepository.save(stock);

        Stock foundStock = stockRepository.findById(stock.getId()).get();

        Assertions.assertThat(foundStock).isNotNull();
    }

    @Test
    public void StockRepository_Save_ReturnSavedStock() {
        Stock stock = Stock.builder()
                .quantity(50.0)
                .expirationDate(LocalDate.now().plusMonths(5))
                .date(LocalDate.now())
                .ingredientId(1L)
                .build();

        Stock savedStock = stockRepository.save(stock);

        Assertions.assertThat(savedStock).isNotNull();
        Assertions.assertThat(savedStock.getId()).isGreaterThan(0);
    }

    @Test
    public void StockRepository_Update_ReturnStockNotNull() {
        Stock stock = Stock.builder()
                .quantity(50.0)
                .expirationDate(LocalDate.now().plusMonths(5))
                .date(LocalDate.now())
                .ingredientId(1L)
                .build();

        stockRepository.save(stock);

        Stock savedStock = stockRepository.findById(stock.getId()).get();
        savedStock.setQuantity(30.0);
        savedStock.setExpirationDate(LocalDate.now().plusMonths(4));

        Stock updatedStock = stockRepository.save(savedStock);

        Assertions.assertThat(updatedStock.getQuantity()).isEqualTo(savedStock.getQuantity());
        Assertions.assertThat(updatedStock.getExpirationDate()).isEqualTo(savedStock.getExpirationDate());
    }

    @Test
    public void StockRepository_Delete_ReturnStockIsEmpty() {
        Stock stock = Stock.builder()
                .quantity(50.0)
                .expirationDate(LocalDate.now().plusMonths(5))
                .date(LocalDate.now())
                .ingredientId(1L)
                .build();

        stockRepository.save(stock);

        stockRepository.deleteById(stock.getId());

        Optional<Stock> deletedStock = stockRepository.findById(stock.getId());

        Assertions.assertThat(deletedStock).isEmpty();
    }
}
