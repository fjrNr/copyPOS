package com.example.copypos.controller;

import androidx.annotation.NonNull;

import com.example.copypos.contractView.BaseView;
import com.example.copypos.contractView.ProductListView;
import com.example.copypos.api.ApiClient;
import com.example.copypos.api.ApiInterface;
import com.example.copypos.model.Product;

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

public class ProductController {

	private BaseView view;
	private ProductListView viewList;

	public ProductController(BaseView view){
		this.view = view;
	}
	public ProductController(ProductListView viewList){
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

    public void create(final int branchId, final String name, final int purchasePrice, final int sellPrice, final int minStock, final int stock, final boolean isPaper, final InputStream is){
		byte[] imageBytes = new byte[0];
		ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
		Call<Product> call;

		view.showProgress();
		if(is != null) {
			try {
				imageBytes = getBytes(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
			MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
			call = apiInterface.createProductWithImage(branchId, name, purchasePrice, sellPrice, minStock, stock, isPaper, image);
		}else{
			call = apiInterface.createProduct(branchId, name, purchasePrice, sellPrice, minStock, stock, isPaper);
		}
		call.enqueue(new Callback<Product>() {
			@Override
			public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
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
			public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
				view.hideProgress();
				view.onError(t.getLocalizedMessage());
			}
		});
	}
	public void delete(final int id, final String imageName){
		view.showProgress();
		ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
		Call<Product> call = apiInterface.deleteProduct(id, imageName);
		call.enqueue(new Callback<Product>() {
			@Override
			public void onResponse(Call<Product> call, Response<Product> response) {
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
			public void onFailure(Call<Product> call, Throwable t) {
				view.hideProgress();
				view.onError(t.getLocalizedMessage());
			}
		});
	}
	public void getAll(final int branchId, final String key){
		viewList.showLoading();
		ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
		Call<List<Product>> call = apiInterface.getProducts(branchId, key);
		call.enqueue(new Callback<List<Product>>() {
			@Override
			public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
				viewList.hideLoading();
				if(response.isSuccessful() && response.body() != null){
					viewList.onGetResultProduct(response.body());
				}
			}

			@Override
			public void onFailure(Call<List<Product>> call, Throwable t) {
				viewList.hideLoading();
				viewList.onErrorLoading(t.getLocalizedMessage());
			}
		});
	}

	public void getPapers(final int branchId){
		viewList.showLoading();
		ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
		Call<List<Product>> call = apiInterface.getPapers(branchId);
		call.enqueue(new Callback<List<Product>>() {
			@Override
			public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
				viewList.hideLoading();
				if(response.isSuccessful() && response.body() != null){
					viewList.onGetResultProduct(response.body());
				}
			}

			@Override
			public void onFailure(Call<List<Product>> call, Throwable t) {
				viewList.hideLoading();
				viewList.onErrorLoading(t.getLocalizedMessage());
			}
		});
	}

	public void update(final int id, final String name, final int purchasePrice, final int sellPrice, final int minStock, final int stock, final boolean isPaper, final String oldImageName, final InputStream is, final boolean isDeleteImage){
		byte[] imageBytes = new byte[0];
		ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
		Call<Product> call;

		view.showProgress();
		if(is != null) {
			try {
				imageBytes = getBytes(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
			MultipartBody.Part newImage = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
			call = apiInterface.updateProductChangeImage(id, name, purchasePrice, sellPrice, minStock, stock, isPaper, oldImageName, newImage);
		}else{
			call = apiInterface.updateProduct(id, name, purchasePrice, sellPrice, minStock, stock, isPaper, oldImageName, isDeleteImage);
		}
		call.enqueue(new Callback<Product>() {
			@Override
			public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
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
			public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
				view.hideProgress();
				view.onError(t.getLocalizedMessage());
			}
		});
	}

}
