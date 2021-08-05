package com.example.copypos.contractView;

public interface LoginView {
  void hideProgress();
  void onError(String paramString);
  void onLoggedEmployee(String paramString1, int paramInt, String paramString2, String paramString3, int paramInt2, Boolean allowSale, Boolean allowPurchase, Boolean allowStock, Boolean allowExpense);
  void onLoggedOwner(String paramString1, int paramInt, String paramString2, String paramString3);
  void showProgress();
}


/* Location:              D:\Softwares\Applications\Programming\dex2jar\classes2-dex2jar.jar!\com\example\copyPOS\contractView\LoginView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */