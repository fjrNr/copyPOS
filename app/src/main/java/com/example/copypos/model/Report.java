package com.example.copypos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Report {
    @SerializedName("FCList")
    @Expose
    private List<Service> FCServices;
    @SerializedName("printList")
    @Expose
    private List<Service> printServices;
    @SerializedName("productList")
    @Expose
    private List<Product> products;
    @SerializedName("serviceList")
    @Expose
    private List<Service> services;
    @SerializedName("totalExpense")
    @Expose
    private int totalExpense;
    @SerializedName("totalPurchase")
    @Expose
    private int totalPurchase;
    @SerializedName("totalSale")
    @Expose
    private int totalSale;
    @SerializedName("totalCredit")
    @Expose
    private int totalCredit;
    @SerializedName("totalDebt")
    @Expose
    private int totalDebt;

    public List<Service> getPrintServices() {
        return this.printServices;
    }

    public void setPrintServices(List<Service> printServices2) {
        this.printServices = printServices2;
    }

    public List<Service> getFCServices() {
        return this.FCServices;
    }

    public void setFCServices(List<Service> FCServices2) {
        this.FCServices = FCServices2;
    }

    public int getTotalExpense() {
        return this.totalExpense;
    }

    public void setTotalExpense(int totalExpense2) {
        this.totalExpense = totalExpense2;
    }

    public int getTotalSale() {
        return this.totalSale;
    }

    public void setTotalSale(int totalSale2) {
        this.totalSale = totalSale2;
    }

    public int getTotalPurchase() {
        return this.totalPurchase;
    }

    public void setTotalPurchase(int totalPurchase2) {
        this.totalPurchase = totalPurchase2;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products2) {
        this.products = products2;
    }

    public List<Service> getServices() {
        return this.services;
    }

    public void setServices(List<Service> services2) {
        this.services = services2;
    }

    public int getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(int totalCredit) {
        this.totalCredit = totalCredit;
    }

    public int getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(int totalDebt) {
        this.totalDebt = totalDebt;
    }
}
