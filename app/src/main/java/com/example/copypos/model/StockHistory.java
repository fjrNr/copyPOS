package com.example.copypos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockHistory {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("changedStock")
    @Expose
    private int changedStock;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("currStock")
    @Expose
    private int currStock;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

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

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes2) {
        this.notes = notes2;
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

    public int getChangedStock() {
        return changedStock;
    }

    public void setChangedStock(int changedStock) {
        this.changedStock = changedStock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getCurrStock() {
        return currStock;
    }

    public void setCurrStock(int currStock) {
        this.currStock = currStock;
    }

}
