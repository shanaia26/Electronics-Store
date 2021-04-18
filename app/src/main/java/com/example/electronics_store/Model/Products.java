package com.example.electronics_store.Model;

public class Products {
    private String productID;
    private String date;
    private String time;
    private String title;
    private String manufacturer;
    private String price;
    private String category;
    private String image;
    private String quantity;

    public Products(){

    }

    public Products(String productID, String date, String time, String title, String manufacturer, String price, String category, String image, String quantity) {
        this.productID = productID;
        this.date = date;
        this.time = time;
        this.title = title;
        this.manufacturer = manufacturer;
        this.price = price;
        this.category = category;
        this.image = image;
        this.quantity = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
