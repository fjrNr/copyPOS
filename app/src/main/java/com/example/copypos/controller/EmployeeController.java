package com.example.copypos.controller;

import androidx.annotation.NonNull;

import com.example.copypos.contractView.BaseView;
import com.example.copypos.api.ApiClient;
import com.example.copypos.api.ApiInterface;
import com.example.copypos.model.Employee;
import com.example.copypos.contractView.EmployeeListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeController {
    private BaseView view;
    private EmployeeListView viewList;
    
    public EmployeeController(BaseView view){
        this.view = view;
    }
    public EmployeeController(EmployeeListView viewList){
        this.viewList = viewList;
    }

    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        while (true) {
            int read = is.read(buff);
            int len = read;
            if (read == -1) {
                return byteBuff.toByteArray();
            }
            byteBuff.write(buff, 0, len);
        }
    }


    public void create(final int branchId, final String name, final String phone, final String username, final String password, final Boolean allSellTrans, final Boolean allPurTrans, final Boolean allStock, final Boolean allExp, final InputStream is){
        byte[] imageBytes = new byte[0];
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Employee> call;

        view.showProgress();
        if(is != null){
            try {
                imageBytes = getBytes(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image","image.jpg", requestFile);
            call = apiInterface.createEmployeeWithImage(branchId, name, phone, username, password, allSellTrans, allPurTrans, allStock, allExp, image);
        }else{
            call = apiInterface.createEmployee(branchId, name, phone, username, password, allSellTrans, allPurTrans, allStock, allExp);
        }
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                view.hideProgress();
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
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    public void delete(final int id, final String imageName){
        viewList.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Employee> call = apiInterface.deleteEmployee(id, imageName);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                viewList.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        viewList.onSuccess(response.body().getMessage());
                    }else{
                        viewList.onError(response.body().getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                viewList.hideLoading();
                viewList.onError(t.getLocalizedMessage());
            }
        });
    }

    public void getAll(final int branchId){
        viewList.showLoading();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Employee>> call = apiInterface.getEmployees(branchId);
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                viewList.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    viewList.onGetResult(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                viewList.hideLoading();
                viewList.onError(t.getLocalizedMessage());
            }
        });
    }

    public void update(final int id, final String name, final String phone, final String username, final Boolean allSellTrans, final Boolean allPurTrans, final Boolean allStock, final Boolean allExp, final String oldImageName, final InputStream is, final boolean isDeleteImage){
        byte[] imageBytes = new byte[0];
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Employee> call;

        view.showProgress();
        if(is != null){
            try {
                imageBytes = getBytes(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            MultipartBody.Part newImage = MultipartBody.Part.createFormData("image","image.jpg", requestFile);
            call = apiInterface.updateEmployeeChangeImage(id, name, phone, username, allSellTrans, allPurTrans, allStock, allExp, oldImageName, newImage);
        }else{
            call = apiInterface.updateEmployee(id, name, phone, username, allSellTrans, allPurTrans, allStock, allExp, oldImageName, isDeleteImage);
        }
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                view.hideProgress();
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
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }
}
