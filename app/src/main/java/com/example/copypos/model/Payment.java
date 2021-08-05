package com.example.copypos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success2) {
        this.success = success2;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date2) {
        this.date = date2;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount2) {
        this.amount = amount2;
    }
}
