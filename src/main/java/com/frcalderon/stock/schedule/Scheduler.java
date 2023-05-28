package com.frcalderon.stock.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frcalderon.stock.service.StockService;
import com.frcalderon.stock.service.UpdateStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private StockService stockService;

    @Autowired
    private UpdateStockService updateStockService;

    @Scheduled(cron = "0 0 1 ? * *")
    public void updateStockInProductsService() {
        updateStockService.sendRequestToUpdateStock();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void expireStock() throws JsonProcessingException {
        stockService.expireStock();
    }
}
