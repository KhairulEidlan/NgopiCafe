package com.example.ngopi.apps.model;

public class RvOrder {
    private String orderId, userid, orderNo, orderDate, orderPickUp, orderStatus, orderAmount;

    public RvOrder(String orderId,String userid, String orderNo, String orderDate, String orderPickUp, String orderStatus, String orderAmount) {
        this.orderId = orderId;
        this.userid = userid;
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.orderPickUp = orderPickUp;
        this.orderStatus = orderStatus;
        this.orderAmount = orderAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getuserid() {
        return userid;
    }

    public void setuserid(String userid) {
        this.userid = userid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderPickUp() {
        return orderPickUp;
    }

    public void setOrderPickUp(String orderPickUp) {
        this.orderPickUp = orderPickUp;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }
}
