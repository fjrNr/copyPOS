package com.example.copypos.controller;

import androidx.annotation.NonNull;

import com.example.copypos.contractView.BaseView;
import com.example.copypos.contractView.LoginView;
import com.example.copypos.api.ApiClient;
import com.example.copypos.api.ApiInterface;
import com.example.copypos.contractView.RegisterView;
import com.example.copypos.model.Employee;
import com.example.copypos.model.Owner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserController {

    private BaseView view;
    private LoginView viewLogin;
    private RegisterView viewRegister;

    public UserController(BaseView view){
        this.view = view;
    }
    public UserController(LoginView viewLogin){
        this.viewLogin = viewLogin;
    }
    public UserController(RegisterView viewRegister) {this.viewRegister = viewRegister;}

    public void getDetail() {

    }

    public void updateUser(final String name, final String phone) {

    }

	public void loginOwner(final String email, final String password) {
        viewLogin.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Owner> call = apiInterface.loginOwner(email, password);
        call.enqueue(new Callback<Owner>() {
            @Override
            public void onResponse(@NonNull Call<Owner> call, @NonNull Response<Owner> response) {
                viewLogin.hideProgress();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        viewLogin.onLoggedOwner(response.body().getMessage(), response.body().getId(), response.body().getName(), response.body().getImageName());
                    }else{
                        viewLogin.onError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Owner> call, @NonNull Throwable t) {
                viewLogin.hideProgress();
                viewLogin.onError(t.getLocalizedMessage());
            }
        });

	}

    public void loginEmployee(final String username, final String password) {
        viewLogin.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Employee> call = apiInterface.loginEmployee(username, password);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                viewLogin.hideProgress();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        viewLogin.onLoggedEmployee(response.body().getMessage(), response.body().getId(), response.body().getName(), response.body().getImageName(), response.body().getBranchId(), response.body().getAllowedSellTrans(), response.body().getAllowedPurchaseTrans(), response.body().getAllowedStock(), response.body().getAllowedExpense());
                    }else{
                        viewLogin.onError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                viewLogin.hideProgress();
                viewLogin.onError(t.getLocalizedMessage());
            }
        });
    }

	public void register(final String name, final String phone, final String email, final String password) {
        viewRegister.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Owner> call = apiInterface.register(name, phone, email, password);
        call.enqueue(new Callback<Owner>() {
            @Override
            public void onResponse(@NonNull Call<Owner> call, @NonNull Response<Owner> response) {
                viewRegister.hideProgress();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        viewRegister.onLogged(response.body().getMessage(), response.body().getId());
                    }else{
                        viewRegister.onError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Owner> call, @NonNull Throwable t) {
                viewRegister.hideProgress();
                viewRegister.onError(t.getLocalizedMessage());
            }
        });
	}
}
