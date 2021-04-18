package com.example.electronics_store.Model;

public class Cart {
    private String orderID;
    private String productID;
    private String title;
    private String manufacturer;
    private String price;
    private String quantity;
    private String shipmentStatus;

    public Cart() {

    }

    public Cart(String orderID, String productID, String title, String manufacturer, String price, String quantity, String shipmentStatus) {
        this.orderID = orderID;
        this.productID = productID;
        this.title = title;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
        this.shipmentStatus = shipmentStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

}
