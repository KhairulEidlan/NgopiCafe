package com.example.ngopi.apps.model;

import com.google.type.DateTime;

public class Order {
    private String userId;
    private String amount;
    private String orderDate;
    private String status;

    public Order(String userId, String amount, String orderDate, String status) {
        this.userId = userId;
        this.amount = amount;
        this.orderDate = orderDate;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
