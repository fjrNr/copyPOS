package com.example.copypos.contractView;

import com.example.copypos.model.Employee;
import java.util.List;

public interface EmployeeListView {
  void hideLoading();
  void onError(String paramString);
  void onGetResult(List<Employee> paramList);
  void onSuccess(String paramString);
  void showLoading();
}


/* Location:              D:\Softwares\Applications\Programming\dex2jar\classes2-dex2jar.jar!\com\example\copyPOS\contractView\EmployeeListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */