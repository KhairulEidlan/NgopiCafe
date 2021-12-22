package com.example.ngopi.apps.model;

public class OrderDetail {
    String menuId;
    String price;
    String type;
    int quantity;

    public OrderDetail(String menuId, String price, String type, int quantity) {
        this.menuId = menuId;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
