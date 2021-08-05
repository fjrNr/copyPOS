package com.example.copypos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Purchase {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("invoiceNo")
    @Expose
    private String invoiceNo;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("dueDate")
    @Expose
    private String dueDate;
    @SerializedName("addFee")
    @Expose
    private int additionalFee;
    @SerializedName("discount")
    @Expose
    private int discount;
    @SerializedName("totalPrice")
    @Expose
    private int totalPrice;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;


    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo2) {
        this.invoiceNo = invoiceNo2;
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

    public int getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(int totalPrice2) {
        this.totalPrice = totalPrice2;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus2) {
        this.paymentStatus = paymentStatus2;
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

    public String getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(String dueDate2) {
        this.dueDate = dueDate2;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(int additionalFee) {
        this.additionalFee = additionalFee;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
