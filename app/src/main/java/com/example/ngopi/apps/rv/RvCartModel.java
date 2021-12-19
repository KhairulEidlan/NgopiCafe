package com.example.ngopi.apps.rv;

public class RvCartModel {
    private String itemName,itemImage;
    private double itemPrice;

    public RvCartModel(String itemImage, String itemName, double itemPrice) {
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
