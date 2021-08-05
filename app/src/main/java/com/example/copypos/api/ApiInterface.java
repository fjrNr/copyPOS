package com.example.copypos.api;

import com.example.copypos.model.Branch;
import com.example.copypos.model.Employee;
import com.example.copypos.model.Expense;
import com.example.copypos.model.Owner;
import com.example.copypos.model.Payment;
import com.example.copypos.model.Product;
import com.example.copypos.model.Purchase;
import com.example.copypos.model.Report;
import com.example.copypos.model.Sale;
import com.example.copypos.model.Service;
import com.example.copypos.model.StockHistory;

import java.util.ArrayList;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
  @FormUrlEncoded
  @POST("branch/create.php")
  Call<Branch> createBranch(
          @Field("ownerId") int paramInt,
          @Field("name") String paramString1,
          @Field("address") String paramString2,
          @Field("phone") String paramString3
  );
  @Multipart
  @POST("branch/create.php")
  Call<Branch> createBranchWithImage(
          @Part("ownerId") int paramInt,
          @Part("name") String paramString1,
          @Part("address") String paramString2,
          @Part("phone") String paramString3,
          @Part MultipartBody.Part image
  );
  @FormUrlEncoded
  @POST("branch/delete.php")
  Call<Branch> deleteBranch(
          @Field("id") int paramInt,
          @Field("imageName") String paramString
  );
  @FormUrlEncoded
  @POST("branch/update.php")
  Call<Branch> updateBranch(
          @Field("id") int paramInt,
          @Field("name") String paramString1,
          @Field("address") String paramString2,
          @Field("phone") String paramString3,
          @Field("oldImageName") String paramString4,
          @Field("deleteImage") Boolean paramBoolean
  );
  @Multipart
  @POST("branch/update.php")
  Call<Branch> updateBranchChangeImage(
          @Part("id") int paramInt,
          @Part("name") String paramString1,
          @Part("address") String paramString2,
          @Part("phone") String paramString3,
          @Part("oldImageName") String paramString4,
          @Part MultipartBody.Part paramPart
  );
  @GET("branch/viewAll.php")
  Call<List<Branch>> getBranchs(
          @Query("ownerId") int paramInt
  );

  @FormUrlEncoded
  @POST("employee/create.php")
  Call<Employee> createEmployee(
          @Field("branchId") int paramInt,
          @Field("name") String paramString1,
          @Field("phone") String paramString2,
          @Field("username") String paramString3,
          @Field("password") String paramString4,
          @Field("allSell") Boolean paramBoolean1,
          @Field("allPurchase") Boolean paramBoolean2,
          @Field("allStock") Boolean paramBoolean3,
          @Field("allExp") Boolean paramBoolean4
  );
  @Multipart
  @POST("employee/create.php")
  Call<Employee> createEmployeeWithImage(
          @Part("branchId") int paramInt,
          @Part("name") String paramString1,
          @Part("phone") String paramString2,
          @Part("username") String paramString3,
          @Part("password") String paramString4,
          @Part("allSell") Boolean paramBoolean1,
          @Part("allPurchase") Boolean paramBoolean2,
          @Part("allStock") Boolean paramBoolean3,
          @Part("allExp") Boolean paramBoolean4,
          @Part MultipartBody.Part image
  );
  @FormUrlEncoded
  @POST("employee/delete.php")
  Call<Employee> deleteEmployee(
          @Field("id") int paramInt,
          @Field("imageName") String paramString
  );
  @FormUrlEncoded
  @POST("employee/update.php")
  Call<Employee> updateEmployee(
          @Field("id") int paramInt,
          @Field("name") String paramString1,
          @Field("phone") String paramString2,
          @Field("username") String paramString3,
          @Field("allSell") Boolean paramBoolean1,
          @Field("allPurchase") Boolean paramBoolean2,
          @Field("allStock") Boolean paramBoolean3,
          @Field("allExp") Boolean paramBoolean4,
          @Field("oldImageName") String paramString4,
          @Field("deleteImage") Boolean paramBoolean5
  );
  @Multipart
  @POST("employee/update.php")
  Call<Employee> updateEmployeeChangeImage(
          @Part("id") int paramInt,
          @Part("name") String paramString1,
          @Part("phone") String paramString2,
          @Part("username") String paramString3,
          @Part("allSell") Boolean paramBoolean1,
          @Part("allPurchase") Boolean paramBoolean2,
          @Part("allStock") Boolean paramBoolean3,
          @Part("allExp") Boolean paramBoolean4,
          @Part("oldImageName") String paramString4,
          @Part MultipartBody.Part image
  );
  @GET("employee/viewAll.php")
  Call<List<Employee>> getEmployees(
          @Query("branchId") int paramInt
  );



  @FormUrlEncoded
  @POST("expense/create.php")
  Call<Expense> createExpense(
          @Field("branchId") int paramInt1,
          @Field("name") String paramString1,
          @Field("date") String paramString2,
          @Field("amount") int paramInt2
  );
  @FormUrlEncoded
  @POST("expense/delete.php")
  Call<Expense> deleteExpense(
          @Field("id") int paramInt
  );
  @FormUrlEncoded
  @POST("expense/update.php")
  Call<Expense> updateExpense(
          @Field("id") int paramInt1,
          @Field("name") String paramString1,
          @Field("date") String paramString2,
          @Field("amount") int paramInt2
  );
  @GET("expense/viewAll.php")
  Call<List<Expense>> getExpenses(
          @Query("branchId") int paramInt,
          @Query("key") String paramString
  );



  @FormUrlEncoded
  @POST("loginEmployee.php")
  Call<Employee> loginEmployee(
          @Field("username") String paramString1,
          @Field("password") String paramString2
  );
  @FormUrlEncoded
  @POST("loginOwner.php")
  Call<Owner> loginOwner(
          @Field("email") String paramString1,
          @Field("password") String paramString2
  );



  @FormUrlEncoded
  @POST("photocopyService/create.php")
  Call<Service> createPhotocopyService(
          @Field("branchId") int paramInt1,
          @Field("paperId") int paramInt2,
          @Field("name") String paramString,
          @Field("sellPrice") int paramInt3
  );
  @FormUrlEncoded
  @POST("photocopyService/delete.php")
  Call<Service> deletePhotocopyService(
          @Field("id") int paramInt
  );
  @FormUrlEncoded
  @POST("photocopyService/update.php")
  Call<Service> updatePhotocopyService(
          @Field("id") int paramInt1,
          @Field("paperId") int paramInt2,
          @Field("name") String paramString,
          @Field("sellPrice") int paramInt3
  );
  @GET("photocopyService/viewAll.php")
  Call<List<Service>> getPhotocopyServices(
          @Query("branchId") int paramInt,
          @Query("key") String paramString
  );



  @FormUrlEncoded
  @POST("printService/create.php")
  Call<Service> createPrintService(
          @Field("branchId") int paramInt1,
          @Field("paperId") int paramInt2,
          @Field("name") String paramString,
          @Field("sellPrice") int paramInt3
  );
  @FormUrlEncoded
  @POST("printService/delete.php")
  Call<Service> deletePrintService(
          @Field("id") int paramInt
  );
  @FormUrlEncoded
  @POST("printService/update.php")
  Call<Service> updatePrintService(
          @Field("id") int paramInt1,
          @Field("paperId") int paramInt2,
          @Field("name") String paramString,
          @Field("sellPrice") int paramInt3
  );
  @GET("printService/viewAll.php")
  Call<List<Service>> getPrintServices(
          @Query("branchId") int paramInt,
          @Query("key") String paramString
  );



  @FormUrlEncoded
  @POST("product/create.php")
  Call<Product> createProduct(
          @Field("branchId") int paramInt1,
          @Field("name") String paramString,
          @Field("purchasePrice") int paramInt2,
          @Field("sellPrice") int paramInt3,
          @Field("minStock") int paramInt4,
          @Field("stock") int paramInt5,
          @Field("isPaper") Boolean paramBoolean
  );
  @Multipart
  @POST("product/create.php")
  Call<Product> createProductWithImage(
          @Part("branchId") int paramInt1,
          @Part("name") String paramString,
          @Part("purchasePrice") int paramInt2,
          @Part("sellPrice") int paramInt3,
          @Part("minStock") int paramInt4,
          @Part("stock") int paramInt5,
          @Part("isPaper") Boolean paramBoolean,
          @Part MultipartBody.Part image
  );
  @FormUrlEncoded
  @POST("product/delete.php")
  Call<Product> deleteProduct(
          @Field("id") int paramInt,
          @Field("imageName") String paramString
  );
  @FormUrlEncoded
  @POST("product/update.php")
  Call<Product> updateProduct(
          @Field("id") int paramInt1,
          @Field("name") String paramString1,
          @Field("purchasePrice") int paramInt2,
          @Field("sellPrice") int paramInt3,
          @Field("minStock") int paramInt4,
          @Field("stock") int paramInt5,
          @Field("isPaper") Boolean paramBoolean1,
          @Field("oldImageName") String paramString2,
          @Field("deleteImage") Boolean paramBoolean2
  );
  @Multipart
  @POST("product/update.php")
  Call<Product> updateProductChangeImage(
          @Part("id") int paramInt1,
          @Part("name") String paramString1,
          @Part("purchasePrice") int paramInt2,
          @Part("sellPrice") int paramInt3,
          @Part("minStock") int paramInt4,
          @Part("stock") int paramInt5,
          @Part("isPaper") Boolean paramBoolean,
          @Part("oldImageName") String paramString2,
          @Part MultipartBody.Part paramPart
  );
  @GET("product/viewAll.php")
  Call<List<Product>> getProducts(
          @Query("branchId") int paramInt,
          @Query("key") String paramString
  );
  @GET("product/viewPapers.php")
  Call<List<Product>> getPapers(
          @Query("branchId") int paramInt
  );



  @FormUrlEncoded
  @POST("purchase/create.php")
  Call<Purchase> createPurchase(
          @Field("branchId") int brancId,
          @Field("date") String date,
          @Field("dueDate") String dueDate,
          @Field("name") String name,
          @Field("payAmount") int payAmount,
          @Field("phone") String phone,
          @Field("totalPrice") int totalPrice,

          @Field("idList[]") ArrayList<Integer> paramArrayList1,
          @Field("pList[]") ArrayList<Integer> paramArrayList2,
          @Field("qtyList[]") ArrayList<Integer> paramArrayList3
  );
  @FormUrlEncoded
  @POST("purchase/createNewPayment.php")
  Call<Payment> createNewPurchasePayment(
          @Field("purchaseId") int paramInt1,
          @Field("amount") int paramInt2
  );
  @GET("purchase/viewAll.php")
  Call<List<Purchase>> getPurchases(
          @Query("branchId") int paramInt,
          @Query("key") String paramString
  );
  @GET("purchase/viewPaymentList.php")
  Call<List<Payment>> getPurchasePaymentList(
          @Query("purchaseId") int paramInt
  );
  @GET("purchase/viewProductList.php")
  Call<List<Product>> getPurchaseProductList(
          @Query("purchaseId") int paramInt
  );



  @FormUrlEncoded
  @POST("register.php")
  Call<Owner> register(
          @Field("name") String paramString1,
          @Field("phone") String paramString2,
          @Field("email") String paramString3,
          @Field("password") String paramString4
  );



  @GET("report/print.php")
  Call<ResponseBody> printReport(
          @Query("branchId") int paramInt1,
          @Query("periodeTypeId") int paramInt2,
          @Query("periode") String paramString1,
          @Query("reportType") String paramString2
  );
  @GET("report/viewBestSellerItem.php")
  Call<Report> getBestSellerItem(
          @Query("branchId") int paramInt1,
          @Query("periodeTypeId") int paramInt2,
          @Query("periode") String paramString,
          @Query("itemType") int paramInt3
  );
  @GET("report/viewSummary.php")
  Call<Report> getReportSummary(
          @Query("branchId") int paramInt1,
          @Query("periodeTypeId") int paramInt2,
          @Query("periode") String paramString
  );



  @FormUrlEncoded
  @POST("sale/create.php")
  Call<Sale> createSale(
          @Field("branchId") int branchId,
          @Field("dueDate") String date,
          @Field("name") String name,
          @Field("phone") String phone,
          @Field("payAmount") int payAmount,
          @Field("totalPrice") int totalPrice,

          @Field("typeList[]") ArrayList<String> paramArrayList,
          @Field("idList[]") ArrayList<Integer> paramArrayList1,
          @Field("pList[]") ArrayList<Integer> paramArrayList2,
          @Field("qtyList[]") ArrayList<Integer> paramArrayList3
  );

  @FormUrlEncoded
  @POST("sale/createNewPayment.php")
  Call<Payment> createNewSalePayment(
          @Field("saleId") int paramInt1,
          @Field("amount") int paramInt2
  );
  @GET("sale/viewAll.php")
  Call<List<Sale>> getSales(
          @Query("branchId") int paramInt,
          @Query("key") String paramString
  );
  @GET("sale/viewPaymentList.php")
  Call<List<Payment>> getSalePaymentList(
          @Query("saleId") int paramInt
  );
  @GET("sale/viewProductList.php")
  Call<List<Product>> getSaleProductList(
          @Query("saleId") int paramInt
  );



  @FormUrlEncoded
  @POST("service/create.php")
  Call<Service> createService(
          @Field("branchId") int paramInt1,
          @Field("name") String paramString,
          @Field("sellPrice") int paramInt2
  );
  @FormUrlEncoded
  @POST("service/delete.php")
  Call<Service> deleteService(
          @Field("id") int paramInt
  );
  @FormUrlEncoded
  @POST("service/update.php")
  Call<Service> updateService(
          @Field("id") int paramInt1,
          @Field("name") String paramString,
          @Field("sellPrice") int paramInt2
  );
  @GET("service/viewAll.php")
  Call<List<Service>> getServices(
          @Query("branchId") int paramInt,
          @Query("key") String paramString
  );



  @GET("stock/viewAll.php")
  Call<List<StockHistory>> getStockList(
          @Query("productId") int paramInt
  );
}