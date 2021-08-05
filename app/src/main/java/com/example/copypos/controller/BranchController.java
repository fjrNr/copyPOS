package com.example.copypos.controller;

import androidx.annotation.NonNull;

import com.example.copypos.api.ApiClient;
import com.example.copypos.api.ApiInterface;
import com.example.copypos.contractView.BaseView;
import com.example.copypos.contractView.BranchListView;
import com.example.copypos.model.Branch;

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

public class BranchController {
    private BaseView view;
    private BranchListView viewList;

    public BranchController(BaseView view){
        this.view = view;
    }
    public BranchController(BranchListView viewList){
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

    public void create(final int ownerId, final String name, final String address, final String phone, final InputStream is){
        byte[] imageBytes = new byte[0];
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Branch> call ;

        view.showProgress();
        if(is != null){
            try {
                imageBytes = getBytes(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image","image.jpg", requestFile);
            call = apiInterface.createBranchWithImage(ownerId, name, address, phone, image);
        }else{
            call = apiInterface.createBranch(ownerId, name, address, phone);
        }
        call.enqueue(new Callback<Branch>() {
            @Override
            public void onResponse(@NonNull Call<Branch> call, @NonNull Response<Branch> response) {
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
            public void onFailure(@NonNull Call<Branch> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    public void delete(final int id, final String imageName){
        viewList.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Branch> call = apiInterface.deleteBranch(id, imageName);
        call.enqueue(new Callback<Branch>() {
            @Override
            public void onResponse(Call<Branch> call, Response<Branch> response) {
                viewList.hideProgress();
                if(response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if(success){
                        viewList.onSuccess(response.body().getMessage());
                    }else{
                        viewList.onErrorLoading(response.body().getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<Branch> call, Throwable t) {
                viewList.hideProgress();
                viewList.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }

    public void getAll(final int ownerId){
        viewList.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Branch>> call = apiInterface.getBranchs(ownerId);
        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                viewList.hideProgress();
                if(response.isSuccessful() && response.body() != null){
                    viewList.onGetResult(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                viewList.hideProgress();
                viewList.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }

    public void update(final int id, final String name, final String address, final String phone, final String oldImageName, final InputStream is, final boolean isDeleteImage){
        byte[] imageBytes = new byte[0];
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Branch> call;

        view.showProgress();
        if(is != null){
            try {
                imageBytes = getBytes(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image","image.jpg", requestFile);
            call = apiInterface.updateBranchChangeImage(id, name, address, phone, oldImageName, image);
        }else{
            call = apiInterface.updateBranch(id, name, address, phone, oldImageName, isDeleteImage);
        }
        call.enqueue(new Callback<Branch>() {
            @Override
            public void onResponse(@NonNull Call<Branch> call, @NonNull Response<Branch> response) {
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
            public void onFailure(@NonNull Call<Branch> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }
}
