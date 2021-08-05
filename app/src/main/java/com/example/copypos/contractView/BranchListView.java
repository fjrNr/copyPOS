package com.example.copypos.contractView;

import com.example.copypos.model.Branch;

import java.util.List;

public interface BranchListView {
  void onErrorLoading(String paramString);
  void onGetResult(List<Branch> paramList);
  void onSuccess(String paramString);
  void showProgress();
  void hideProgress();
}


/* Location:              D:\Softwares\Applications\Programming\dex2jar\classes2-dex2jar.jar!\com\example\copyPOS\contractView\BranchListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */