package com.example.electronics_store.Model;

public class UserOrders {
    private String orderID;
    private String address;
    private String date;
    private String paymentStatus;
    private String phone;
    private String shipmentName;
    private String shipmentStatus;
    private String totalAmount;

    public UserOrders(){

    }

    public UserOrders(String orderID, String address, String date, String paymentStatus, String phone, String shipmentName, String shipmentStatus, String totalAmount) {
        this.orderID = orderID;
        this.address = address;
        this.date = date;
        this.paymentStatus = paymentStatus;
        this.phone = phone;
        this.shipmentName = shipmentName;
        this.shipmentStatus = shipmentStatus;
        this.totalAmount = totalAmount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
