package com.frcalderon.stock.service;

import com.frcalderon.stock.controller.dto.StockRequest;
import com.frcalderon.stock.exceptions.StockNotFoundException;
import com.frcalderon.stock.model.Stock;
import com.frcalderon.stock.repository.StockRepository;
import com.frcalderon.stock.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;

public class StockServiceTests {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private AutoCloseable closeable;

    private Stock stock;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        stock = Stock.builder()
                .quantity(50.0)
                .expirationDate(LocalDate.now().plusMonths(5))
                .date(LocalDate.now())
                .ingredientId(1L)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void StockService_GetAll_ReturnStockList() {
        when(stockRepository.findAll()).thenReturn(Collections.singletonList(stock));

        List<Stock> result = stockService.getAllStocks();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(stock, result.get(0));

        verify(stockRepository, times(1)).findAll();
    }

    @Test
    public void StockService_FindById_ReturnStock() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        Stock result = stockService.getStock(1L);

        Assertions.assertEquals(stock, result);

        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    public void StockService_FindById_ReturnStockNotFoundException() {
        when(stockRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(StockNotFoundException.class, () -> stockService.getStock(2L));

        verify(stockRepository, times(1)).findById(2L);
    }

    @Test
    public void StockService_Create_ReturnStock() {
        StockRequest request = StockRequest.builder()
                .quantity(50.5)
                .expirationDate("31-12-2023")
                .ingredientId(1L)
                .build();

        LocalDate now = LocalDate.parse("18-05-2023", Utils.localDateTimeFormatter());

        Stock newStock = Stock.builder()
                .quantity(request.getQuantity())
                .expirationDate(LocalDate.parse(request.getExpirationDate(), Utils.localDateTimeFormatter()))
                .ingredientId(request.getIngredientId())
                .date(now)
                .build();

        when(stockRepository.save(any(Stock.class))).thenReturn(newStock);

        Stock result = stockService.createStock(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(50.5, result.getQuantity());
        Assertions.assertTrue(result.getExpirationDate().isEqual(LocalDate.parse("31-12-2023", Utils.localDateTimeFormatter())));
        Assertions.assertEquals(1L, result.getIngredientId());
        Assertions.assertTrue(result.getDate().isEqual(now));
    }

    @Test
    public void StockService_Update_ReturnStock() {
        StockRequest request = StockRequest.builder()
                .quantity(70.5)
                .expirationDate("15-12-2023")
                .ingredientId(2L)
                .build();

        LocalDate now = LocalDate.parse("18-05-2023", Utils.localDateTimeFormatter());

        Stock updatedStock = Stock.builder()
                .quantity(request.getQuantity())
                .expirationDate(LocalDate.parse(request.getExpirationDate(), Utils.localDateTimeFormatter()))
                .ingredientId(request.getIngredientId())
                .date(now)
                .build();

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(updatedStock);

        Stock result = stockService.updateStock(1L, request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(70.5, result.getQuantity());
        Assertions.assertTrue(result.getExpirationDate().isEqual(LocalDate.parse("15-12-2023", Utils.localDateTimeFormatter())));
        Assertions.assertEquals(2L, result.getIngredientId());
        Assertions.assertTrue(result.getDate().isEqual(now));

        verify(stockRepository, times(1)).findById(1L);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    public void StockService_Update_ReturnStockNotFoundException() {
        StockRequest request = StockRequest.builder()
                .quantity(70.5)
                .expirationDate("15-12-2023")
                .ingredientId(2L)
                .build();

        when(stockRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(StockNotFoundException.class, () -> stockService.updateStock(2L, request));

        verify(stockRepository, times(1)).findById(2L);
    }

    @Test
    public void StockService_Delete_ReturnVoid() {
        when(stockRepository.existsById(1L)).thenReturn(true);

        stockService.deleteStock(1L);

        verify(stockRepository, times(1)).existsById(1L);
        verify(stockRepository, times(1)).deleteById(1L);
    }

    @Test
    public void StockService_Delete_ReturnStockNotFoundException() {
        when(stockRepository.existsById(2L)).thenReturn(false);

        Assertions.assertThrows(StockNotFoundException.class, () -> stockService.deleteStock(2L));

        verify(stockRepository, times(1)).existsById(2L);
        verify(stockRepository, times(0)).deleteById(2L);
    }
}
