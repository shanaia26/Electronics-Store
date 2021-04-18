package com.example.electronics_store.CommandPattern;

import com.example.electronics_store.Interface.Order;

public class SellStock implements Order {
    private Stock aStock;

    public SellStock(Stock aStock){
        this.aStock = aStock;
    }

    public void execute() {
        aStock.sell();
    }
}
