package com.example.copypos.contractView;

import com.example.copypos.model.Report;

public interface ReportView {
  void hideProgress();
  void onError(String paramString);
  void onGetSummaryResult(Report result);
  void onSuccess(String paramString);
  void showProgress();
}


/* Location:              D:\Softwares\Applications\Programming\dex2jar\classes2-dex2jar.jar!\com\example\copyPOS\contractView\ReportView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */