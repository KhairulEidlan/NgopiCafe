package com.example.ngopi.apps.rv;

public class RvMenuModel {
    private String image;
    private String itemName;
    private double itemPrice;

    public RvMenuModel(String image, String itemName, double itemPrice) {
        this.image = image;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
