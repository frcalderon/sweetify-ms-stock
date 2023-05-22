package com.frcalderon.stock.exceptions;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException() {
        super("Stock not found");
    }
}
