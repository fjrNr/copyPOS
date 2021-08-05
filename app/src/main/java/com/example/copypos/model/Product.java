package com.example.copypos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("isPaper")
    @Expose
    private Boolean isPaper;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("minStock")
    @Expose
    private int minimumStock;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("purchasePrice")
    @Expose
    private int purchasePrice;
    @SerializedName("sellPrice")
    @Expose
    private int sellPrice;
    @SerializedName("stock")
    @Expose
    private int stock;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName2) {
        this.imageName = imageName2;
    }

    public Boolean getPaper() {
        return this.isPaper;
    }

    public void setPaper(Boolean paper) {
        this.isPaper = paper;
    }

    public int getPurchasePrice() {
        return this.purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice2) {
        this.purchasePrice = purchasePrice2;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success2) {
        this.success = success2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock2) {
        this.stock = stock2;
    }

    public int getMinimumStock() {
        return this.minimumStock;
    }

    public void setMinimumStock(int minimumStock2) {
        this.minimumStock = minimumStock2;
    }

    public int getSellPrice() {
        return this.sellPrice;
    }

    public void setSellPrice(int sellPrice2) {
        this.sellPrice = sellPrice2;
    }
}
