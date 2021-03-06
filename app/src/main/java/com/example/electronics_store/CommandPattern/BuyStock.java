package com.example.electronics_store.CommandPattern;

import com.example.electronics_store.Interface.Order;

public class BuyStock implements Order {
    private Stock aStock;

    public BuyStock(Stock aStock){
        this.aStock = aStock;
    }

    public void execute() {
        aStock.buy();
    }
}