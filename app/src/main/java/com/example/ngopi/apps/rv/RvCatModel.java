package com.example.ngopi.apps.rv;

public class RvCatModel {
    private int image;
    private String text;

    public RvCatModel(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}