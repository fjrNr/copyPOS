package com.example.copypos.controller;

import androidx.annotation.NonNull;

import com.example.copypos.contractView.BaseView;
import com.example.copypos.api.ApiClient;
import com.example.copypos.api.ApiInterface;
import com.example.copypos.model.Expense;
import com.example.copypos.contractView.ExpenseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseController {
    private ExpenseView view;

    public ExpenseController(ExpenseView view){
        this.view = view;
    }

    public void create(final int branchId, final String name, final String date, final int amount){
        view.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Expense> call = apiInterface.createExpense(branchId, name, date, amount);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(@NonNull Call<Expense> call, @NonNull Response<Expense> response) {
                view.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        view.onSuccess(response.body().getMessage());
                    }else{
                        view.onError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Expense> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    public void delete(final int id){
        view.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Expense> call = apiInterface.deleteExpense(id);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(@NonNull Call<Expense> call, @NonNull Response<Expense> response) {
                view.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        view.onSuccess(response.body().getMessage());
                    }else{
                        view.onError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Expense> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onError(t.getLocalizedMessage());
            }
        });
    }
    
    public void getAll(final int branchId, final String key){
        view.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Expense>> call = apiInterface.getExpenses(branchId, key);
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                view.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    view.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                view.hideLoading();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    public void update(final int id, final String name, final String date, final int amount){
        view.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Expense> call = apiInterface.updateExpense(id, name, date, amount);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(@NonNull Call<Expense> call, @NonNull Response<Expense> response) {
                view.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        view.onSuccess(response.body().getMessage());
                    }else{
                        view.onError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Expense> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onError(t.getLocalizedMessage());
            }
        });
    }
}
