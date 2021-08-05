package com.example.copypos.contractView;

import com.example.copypos.model.StockHistory;
import java.util.List;

public interface StockListView {
  void hideLoading();
  
  void onErrorLoading(String paramString);
  
  void onGetResult(List<StockHistory> paramList);
  
  void showLoading();
}


/* Location:              D:\Softwares\Applications\Programming\dex2jar\classes2-dex2jar.jar!\com\example\copyPOS\contractView\StockListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */