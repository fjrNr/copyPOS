package com.example.copypos.contractView;

import com.example.copypos.model.Payment;
import com.example.copypos.model.Product;
import com.example.copypos.model.Purchase;
import com.example.copypos.model.Sale;

import java.util.List;

public interface TransactionView {
  void hideLoading();
  
  void onErrorLoading(String paramString);
  
  void showLoading();
  
  interface SaleList extends TransactionView{
    void onGetResultSale(List<Sale> list);
  }
  
  interface PurchaseList extends TransactionView{
    void onGetResultPurchase(List<Purchase> list);
  }
  
  interface Detail extends TransactionView{
    void onGetResultProduct(List<Product> list);
    void onGetResultPayment(List<Payment> list);
  }
}