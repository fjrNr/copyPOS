package com.example.copypos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Employee {
    @SerializedName("allExp")
    @Expose
    private Boolean allowedExpense;
    @SerializedName("allPurchase")
    @Expose
    private Boolean allowedPurchaseTrans;
    @SerializedName("allSell")
    @Expose
    private Boolean allowedSellTrans;
    @SerializedName("allStock")
    @Expose
    private Boolean allowedStock;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("branchId")
    @Expose
    private int branchId;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("username")
    @Expose
    private String username;

    public Boolean getAllowedExpense() {
        return this.allowedExpense;
    }

    public void setAllowedExpense(Boolean allowedExpense2) {
        this.allowedExpense = allowedExpense2;
    }

    public Boolean getAllowedStock() {
        return this.allowedStock;
    }

    public void setAllowedStock(Boolean allowedStock2) {
        this.allowedStock = allowedStock2;
    }

    public Boolean getAllowedPurchaseTrans() {
        return this.allowedPurchaseTrans;
    }

    public void setAllowedPurchaseTrans(Boolean allowedPurchaseTrans2) {
        this.allowedPurchaseTrans = allowedPurchaseTrans2;
    }

    public Boolean getAllowedSellTrans() {
        return this.allowedSellTrans;
    }

    public void setAllowedSellTrans(Boolean allowedSellTrans2) {
        this.allowedSellTrans = allowedSellTrans2;
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

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName2) {
        this.imageName = imageName2;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
}
