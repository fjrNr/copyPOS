package com.example.copypos.controller;

import androidx.annotation.NonNull;

import com.example.copypos.api.ApiClient;
import com.example.copypos.api.ApiInterface;
import com.example.copypos.model.Payment;
import com.example.copypos.model.Product;
import com.example.copypos.model.Purchase;
import com.example.copypos.contractView.PaymentView;
import com.example.copypos.contractView.TransactionView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseController {

    private PaymentView view;
    private TransactionView.PurchaseList viewList;
    private TransactionView.Detail viewDetail;

    public PurchaseController(PaymentView view){
        this.view = view;
    }
    public PurchaseController(TransactionView.PurchaseList viewList){ this.viewList = viewList; }
    public PurchaseController(TransactionView.Detail viewDetail){
        this.viewDetail = viewDetail;
    }

    public void create(
            final int branchId,
            final String date,
            final String dueDate,
            final String name,
            final int payAmount,
            final String phone,
            final int totalPrice,

            ArrayList<Integer> idList,
            ArrayList<Integer> priceList,
            ArrayList<Integer> qtyList){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Purchase> call = apiInterface.createPurchase(
                branchId,
                date,
                dueDate,
                name,
                payAmount,
                phone,
                totalPrice,

                idList,
                priceList,
                qtyList);
        call.enqueue(new Callback<Purchase>() {
            @Override
            public void onResponse(@NonNull Call<Purchase> call, @NonNull Response<Purchase> response) {
                view.hideProgress();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        view.onSuccess(response.body().getMessage(), response.body().getTotalPrice());
                    }else{
                        view.onError(response.body().getMessage());
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Purchase> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    public void createPayment(int purchaseId, int amount){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Payment> call = apiInterface.createNewPurchasePayment(purchaseId, amount);
        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                view.hideProgress();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        view.onSuccess(response.body().getMessage(), response.body().getAmount());
                    }else{
                        view.onError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    public void getAll(final int branchId, final String key){
        viewList.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Purchase>> call = apiInterface.getPurchases(branchId, key);
        call.enqueue(new Callback<List<Purchase>>() {
            @Override
            public void onResponse(Call<List<Purchase>> call, Response<List<Purchase>> response) {
                viewList.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    viewList.onGetResultPurchase(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Purchase>> call, Throwable t) {
                viewList.hideLoading();
                viewList.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }

    public void getPaymentList(final int purchaseId){
        viewDetail.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Payment>> call = apiInterface.getPurchasePaymentList(purchaseId);
        call.enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                viewDetail.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    viewDetail.onGetResultPayment(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                viewDetail.hideLoading();
                viewDetail.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }

    public void getProductList(final int purchaseId){
        viewDetail.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Product>> call = apiInterface.getPurchaseProductList(purchaseId);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                viewDetail.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    viewDetail.onGetResultProduct(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                viewDetail.hideLoading();
                viewDetail.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}
