package com.example.ngopi.apps.rv;

public class RvCartModel {
    private String itemId,itemName,itemImage, itemType;
    private int itemQty;
    private double itemPrice;

    public RvCartModel(String itemId, String itemImage, String itemName, String itemType, int itemQty, double itemPrice) {
        this.itemId = itemId;
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemQty = itemQty;
        this.itemPrice = itemPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
