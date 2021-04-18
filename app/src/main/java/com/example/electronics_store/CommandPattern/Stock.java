package com.example.electronics_store.CommandPattern;

import com.example.electronics_store.Model.Products;

public class Stock {
    Products products;
    int aQuantity = Integer.parseInt(products.getQuantity());

    private String title = products.getTitle();
    private int quantity = aQuantity;

    public void buy() {
        System.out.println("Stock [ Name: " + title + " Quantity:" + quantity +" ]bought ");
    }

    public void sell() {
        System.out.println("Stock [ Name: " + title + " Quantity:" + quantity +" ]sold ");
    }
}