package com.example.ngopi.apps.model;

public class RvMenu {
    private String itemId;
    private String itemCategory;
    private String itemImage;
    private String itemName;
    private String itemPrice;

    public RvMenu(String itemId, String itemCategory, String itemImage, String itemName, String itemPrice) {
        this.itemId = itemId;
        this.itemCategory = itemCategory;
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
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

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }


}
