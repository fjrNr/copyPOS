package com.example.copypos.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
  // CHOOSE LOCAL OR CLOUD HOSTING
//  private static final String BASE_URL = "http://10.0.2.2/copyPOS/";
//  public static final String BASE_URL_BRANCH_IMAGE = "http://10.0.2.2/copyPOS/images/branch/";
//  public static final String BASE_URL_EMPLOYEE_IMAGE = "http://10.0.2.2/copyPOS/images/employee/";
//  public static final String BASE_URL_OWNER_IMAGE = "http://10.0.2.2/copyPOS/images/owner/";
//  public static final String BASE_URL_PRODUCT_IMAGE = "http://10.0.2.2/copyPOS/images/product/";

  private static final String BASE_URL = "https://copy-pos-2020.000webhostapp.com/";
  public static final String BASE_URL_BRANCH_IMAGE = BASE_URL + "images/branch/";
  public static final String BASE_URL_EMPLOYEE_IMAGE = BASE_URL + "images/employee/";
  public static final String BASE_URL_OWNER_IMAGE = BASE_URL + "images/owner/";
  public static final String BASE_URL_PRODUCT_IMAGE = BASE_URL + "images/product/";

  private static OkHttpClient client;
  
  private static Gson gson;
  
  private static Retrofit retrofit = null;
  
  static {
    client = (new OkHttpClient()).newBuilder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).build();
    gson = (new GsonBuilder()).setLenient().create();
  }
  
  public static Retrofit getApiClient() {
    if (retrofit == null)
      retrofit = (new Retrofit.Builder()).baseUrl(BASE_URL).client(client).addConverterFactory((Converter.Factory)ScalarsConverterFactory.create()).addConverterFactory((Converter.Factory)GsonConverterFactory.create(gson)).build();
    return retrofit;
  }
}


/* Location:              D:\Softwares\Applications\Programming\dex2jar\classes2-dex2jar.jar!\com\example\copyPOS\api\ApiClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */