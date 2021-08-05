package com.example.copypos.controller;

import com.example.copypos.api.ApiClient;
import com.example.copypos.api.ApiInterface;
import com.example.copypos.contractView.StockListView;
import com.example.copypos.model.StockHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockController {

    private StockListView viewList;

    public StockController(StockListView viewList){this.viewList = viewList;}

    public void getAll(final int productId){
        viewList.showLoading();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<StockHistory>> call = apiInterface.getStockList(productId);
        call.enqueue(new Callback<List<StockHistory>>() {
            @Override
            public void onResponse(Call<List<StockHistory>> call, Response<List<StockHistory>> response) {
                viewList.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    viewList.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<StockHistory>> call, Throwable t) {
                viewList.hideLoading();
                viewList.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}
