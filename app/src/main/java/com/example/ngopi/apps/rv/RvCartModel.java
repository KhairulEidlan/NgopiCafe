package com.example.ngopi.apps.rv;

public class RvCartModel {
    private int image;
    private String itemName,itemType;
    private double itemPrice;

    public RvCartModel(int image, String itemName, String itemType, double itemPrice) {
        this.image = image;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemPrice = itemPrice;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
