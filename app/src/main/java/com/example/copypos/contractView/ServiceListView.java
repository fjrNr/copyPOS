package com.example.copypos.contractView;

import com.example.copypos.model.Service;

import java.util.List;

public interface ServiceListView {
  void hideLoading();
  void onErrorLoading(String paramString);
  void onGetResultPhotocopyService(List<Service> paramList);
  void onGetResultPrintService(List<Service> paramList);
  void onGetResultService(List<Service> paramList);
  void onSuccess(String paramString);
  void showLoading();
}


/* Location:              D:\Softwares\Applications\Programming\dex2jar\classes2-dex2jar.jar!\com\example\copyPOS\contractView\ServiceListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */