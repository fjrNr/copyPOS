package com.example.copypos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("paperId")
    @Expose
    private int paperId;
    @SerializedName("sellPrice")
    @Expose
    private int sellPrice;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount2) {
        this.amount = amount2;
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

    public int getSellPrice() {
        return this.sellPrice;
    }

    public void setSellPrice(int sellPrice2) {
        this.sellPrice = sellPrice2;
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

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }
}
