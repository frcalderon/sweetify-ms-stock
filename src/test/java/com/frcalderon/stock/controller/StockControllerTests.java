package com.frcalderon.stock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frcalderon.stock.controller.dto.StockRequest;
import com.frcalderon.stock.controller.dto.StockResponse;
import com.frcalderon.stock.model.Stock;
import com.frcalderon.stock.service.StockService;
import com.frcalderon.stock.utils.Utils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = StockController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class StockControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    private Stock stock;

    private StockRequest stockRequest;

    private StockResponse stockResponse;

    @BeforeEach
    void setUp() {
        stock = Stock.builder()
                .id(1L)
                .quantity(200.0)
                .expirationDate(LocalDate.parse("31-12-2023", Utils.localDateTimeFormatter()))
                .ingredientId(1L)
                .date(LocalDate.parse("21-05-2023", Utils.localDateTimeFormatter()))
                .build();

        stockRequest = StockRequest.builder()
                .quantity(200.0)
                .expirationDate("15-12-2023")
                .ingredientId(1L)
                .build();

        stockResponse = StockResponse.builder()
                .id(1L)
                .quantity(200.0)
                .expirationDate(LocalDate.parse("31-12-2023", Utils.localDateTimeFormatter()))
                .ingredientId(1L)
                .date(LocalDate.parse("21-05-2023", Utils.localDateTimeFormatter()))
                .build();
    }

    @Test
    public void StockController_GetAllStocks_ReturnListOfStockResponseAndOk() throws Exception {
        List<Stock> stockResponseList = Collections.singletonList(stock);

        when(stockService.getAllStocks()).thenReturn(stockResponseList);

        ResultActions response = mockMvc.perform(get("/stock")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.size()",
                        CoreMatchers.is(stockResponseList.size())
                ));
    }

    @Test
    public void StockController_GetStock_ReturnStockResponseAndOk() throws Exception {
        Long stockId = 1L;
        when(stockService.getStock(stockId)).thenReturn(stock);

        ResultActions response = mockMvc.perform(get("/stock/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id",
                        CoreMatchers.is(stockResponse.getId().intValue())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.quantity",
                        CoreMatchers.is(stockResponse.getQuantity())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.expirationDate",
                        CoreMatchers.is(stockResponse.getExpirationDate().toString())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.ingredientId",
                        CoreMatchers.is(stockResponse.getIngredientId().intValue())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.date",
                        CoreMatchers.is(stockResponse.getDate().toString())
                ));
    }

    @Test
    public void StockController_CreateStock_ReturnStockResponseAndCreated() throws Exception {
        // TODO - Request to ms-products to add stock

        when(stockService.createStock(stockRequest)).thenReturn(stock);

        ResultActions response = mockMvc.perform(post("/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id",
                        CoreMatchers.is(stockResponse.getId().intValue())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.quantity",
                        CoreMatchers.is(stockResponse.getQuantity())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.expirationDate",
                        CoreMatchers.is(stockResponse.getExpirationDate().toString())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.ingredientId",
                        CoreMatchers.is(stockResponse.getIngredientId().intValue())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.date",
                        CoreMatchers.is(stockResponse.getDate().toString())
                ));
    }

    @Test
    public void StockController_UpdateStock_ReturnStockResponseAndOk() throws Exception {
        // TODO - Request to ms-products to consume stock
        // TODO - Request to ms-products to add stock
        Long stockId = 1L;
        when(stockService.updateStock(stockId, stockRequest)).thenReturn(stock);

        ResultActions response = mockMvc.perform(put("/stock/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id",
                        CoreMatchers.is(stockResponse.getId().intValue())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.quantity",
                        CoreMatchers.is(stockResponse.getQuantity())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.expirationDate",
                        CoreMatchers.is(stockResponse.getExpirationDate().toString())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.ingredientId",
                        CoreMatchers.is(stockResponse.getIngredientId().intValue())
                ))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.date",
                        CoreMatchers.is(stockResponse.getDate().toString())
                ));
    }

    @Test
    public void StockController_DeleteStock_ReturnNoContent() throws Exception {
        // TODO - Request to ms-products to consume stock

        Long stockId = 1L;
        doNothing().when(stockService).deleteStock(stockId);

        ResultActions response = mockMvc.perform(delete("/stock/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
