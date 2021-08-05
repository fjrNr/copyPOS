package com.example.copypos.navigationDrawer.transactions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.R;
import com.example.copypos.activity.TransactionDetailActivity;
import com.example.copypos.adapter.SaleListAdapter;
import com.example.copypos.contractView.TransactionView;
import com.example.copypos.controller.SaleController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Sale;

import java.util.List;

public class SaleListFragment extends Fragment implements TransactionView.SaleList, View.OnClickListener {
    Button btnSearch;
    EditText txtSearch;
    SaleListAdapter adapter;
    SaleListAdapter.ItemClickListener itemClickListener;
    ProgressDialog pDialog;
    RecyclerView recyclerView;
    List<Sale> sale;
    SaleController sc;
    SharedPrefController spc;
    SwipeRefreshLayout swipeRefresh;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        btnSearch = v.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Mengambil daftar penjualan");
        pDialog.setCancelable(false);
        txtSearch = v.findViewById(R.id.txtSearch);
        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spc = new SharedPrefController(requireActivity());
        sc = new SaleController(this);
        sc.getAll(spc.getSPBranchId(), "");
        swipeRefresh.setOnRefreshListener(() -> sc.getAll(spc.getSPBranchId(), ""));
        itemClickListener = (view1, position) -> {
            Intent i = new Intent(getActivity(), TransactionDetailActivity.class);
            i.putExtra("id", sale.get(position).getId());
            i.putExtra("date", sale.get(position).getDate());
            i.putExtra("dueDate", sale.get(position).getDueDate());
            i.putExtra("invoiceNo", sale.get(position).getInvoiceNo());
            i.putExtra("name", sale.get(position).getName());
            i.putExtra("personType", "Pelanggan");
            i.putExtra("phone", sale.get(position).getPhone());
            i.putExtra("totalPrice", sale.get(position).getTotalPrice());
            startActivity(i);
        };
    }

    public void showLoading() {
        swipeRefresh.setRefreshing(false);
        pDialog.show();
    }

    public void hideLoading() {
        pDialog.hide();
    }

    public void onErrorLoading(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void onGetResultSale(List<Sale> sales) {
        adapter = new SaleListAdapter(getContext(), sales, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        sale = sales;
    }

    @Override
    public void onClick(View view) {
        sc.getAll(spc.getSPBranchId(), txtSearch.getText().toString());
    }

    public static SaleListFragment newInstance() {
        SaleListFragment f = new SaleListFragment();
        f.setArguments(new Bundle());
        return f;
    }
}
