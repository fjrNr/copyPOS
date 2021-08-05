package com.example.copypos.controller;

import androidx.annotation.NonNull;

import com.example.copypos.api.ApiClient;
import com.example.copypos.api.ApiInterface;
import com.example.copypos.contractView.PaymentView;
import com.example.copypos.contractView.TransactionView;
import com.example.copypos.model.Payment;
import com.example.copypos.model.Product;
import com.example.copypos.model.Sale;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleController {

    private PaymentView view;
    private TransactionView.SaleList viewList;
    private TransactionView.Detail viewDetail;

    public SaleController(PaymentView view){
        this.view = view;
    }
    public SaleController(TransactionView.SaleList viewList){ this.viewList = viewList; }
    public SaleController(TransactionView.Detail viewDetail){
        this.viewDetail = viewDetail;
    }

    public void create(final int branchId,
                       final String dueDate,
                       final String name,
                       final String phone,
                       final int payAmount,
                       final int totalPrice,
                       ArrayList<String> typeList,
                       ArrayList<Integer> idList,
                       ArrayList<Integer> priceList,
                       ArrayList<Integer> qtyList){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Sale> call = apiInterface.createSale(
                branchId,
                dueDate,
                name,
                phone,
                payAmount,
                totalPrice,
                typeList,
                idList,
                priceList,
                qtyList);
        call.enqueue(new Callback<Sale>() {
            @Override
            public void onResponse(@NonNull Call<Sale> call, @NonNull Response<Sale> response) {
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
            public void onFailure(@NonNull Call<Sale> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    public void createPayment(int SaleId, int amount){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Payment> call = apiInterface.createNewSalePayment(SaleId, amount);
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
        Call<List<Sale>> call = apiInterface.getSales(branchId, key);
        call.enqueue(new Callback<List<Sale>>() {
            @Override
            public void onResponse(Call<List<Sale>> call, Response<List<Sale>> response) {
                viewList.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    viewList.onGetResultSale(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Sale>> call, Throwable t) {
                viewList.hideLoading();
                viewList.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }

    public void getPaymentList(final int SaleId){
        viewDetail.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Payment>> call = apiInterface.getSalePaymentList(SaleId);
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

    public void getProductList(final int SaleId){
        viewDetail.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Product>> call = apiInterface.getSaleProductList(SaleId);
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
