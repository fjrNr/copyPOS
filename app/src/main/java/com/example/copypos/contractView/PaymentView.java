package com.example.copypos.contractView;

public interface PaymentView {
  void hideProgress();
  void onError(String paramString);
  void onSuccess(String paramString, int paramInt);
  void showProgress();
}