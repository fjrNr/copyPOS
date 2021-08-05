package com.example.copypos.navigationDrawer.employees;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.R;
import com.example.copypos.activity.EditEmployeeActivity;
import com.example.copypos.adapter.EmployeeListAdapter;
import com.example.copypos.contractView.EmployeeListView;
import com.example.copypos.controller.EmployeeController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Employee;

import java.util.List;

public class EmployeesFragment extends Fragment implements EmployeeListView{
    private static final int INTENT_ADD = 100;
    private static final int INTENT_EDIT = 200;

    EmployeeListAdapter adapter;
    EmployeeListAdapter.ItemClickListener itemClickListener;
    EmployeeListAdapter.ItemClickListener itemClickListener2;
    ProgressDialog pDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    EmployeeController ec;
    SharedPrefController spc;

    List<Employee> employee;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_employees, container, false);
        swipeRefresh = root.findViewById(R.id.swipeRefresh);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spc = new SharedPrefController(requireActivity());
        ec = new EmployeeController(this);
        ec.getAll(spc.getSPBranchId());
        swipeRefresh.setOnRefreshListener(() -> ec.getAll(spc.getSPBranchId()));
        itemClickListener = (view1, position) -> {
            Intent i = new Intent(getActivity(), EditEmployeeActivity.class);
            i.putExtra("id", employee.get(position).getId());
            i.putExtra("name", employee.get(position).getName());
            i.putExtra("phone", employee.get(position).getPhone());
            i.putExtra("username", employee.get(position).getUsername());
            i.putExtra("imageName", employee.get(position).getImageName());
            i.putExtra("allSell", employee.get(position).getAllowedSellTrans());
            i.putExtra("allPur", employee.get(position).getAllowedPurchaseTrans());
            i.putExtra("allStock", employee.get(position).getAllowedStock());
            i.putExtra("allExp", employee.get(position).getAllowedExpense());
            startActivityForResult(i, 200);
        };
        itemClickListener2 = (view12, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
            aDialog.setTitle("Hapus Data Pegawai");
            aDialog.setMessage("Data pegawai yang sudah dihapus tidak dapat lagi mengakses aplikasi ini./nApakah anda yakin ingin menghapus data " + employee.get(position).getName() + "?");
            aDialog.setNegativeButton("Tidak", ((dialog, i) -> {}));
            aDialog.setPositiveButton("Ya", (dialog, i) -> {
                ec.delete(employee.get(position).getId(), employee.get(position).getImageName());
                dialog.dismiss();
            });
            aDialog.show();
        };
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.editor_menu, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_add) {
            return false;
        }
        startActivityForResult(new Intent(getActivity(), EditEmployeeActivity.class), 100);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100 || requestCode == 200) && resultCode == -1) {
            ec.getAll(spc.getSPBranchId());
        }
    }

    public void showLoading() {
        swipeRefresh.setRefreshing(false);
        pDialog.show();
    }

    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
        pDialog.hide();
    }

    public void onGetResult(List<Employee> employees) {
        adapter = new EmployeeListAdapter(getActivity(), employees, itemClickListener, itemClickListener2);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        employee = employees;
    }

    public void onError(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void onSuccess(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
        ec.getAll(spc.getSPBranchId());
    }
}
