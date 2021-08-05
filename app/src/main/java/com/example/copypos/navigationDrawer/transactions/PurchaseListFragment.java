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

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.activity.TransactionDetailActivity;
import com.example.copypos.contractView.TransactionView;
import com.example.copypos.controller.PurchaseController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.adapter.PurchaseListAdapter;
import com.example.copypos.model.Purchase;
import com.example.copypos.R;
import java.util.List;

public class PurchaseListFragment extends Fragment implements TransactionView.PurchaseList, View.OnClickListener {
    Button btnSearch;
    EditText txtSearch;
    PurchaseListAdapter adapter;
    PurchaseListAdapter.ItemClickListener itemClickListener;
    ProgressDialog pDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    PurchaseController pc;
    SharedPrefController spc;

    List<Purchase> purchase;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        btnSearch = v.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefresh = v.findViewById(R.id.swipeRefresh);
        txtSearch = v.findViewById(R.id.txtSearch);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Mengambil daftar pembelian");
        pDialog.setCancelable(false);
        return v;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spc = new SharedPrefController(requireActivity());
        pc = new PurchaseController(this);
        pc.getAll(spc.getSPBranchId(), "");
        swipeRefresh.setOnRefreshListener(() -> pc.getAll(spc.getSPBranchId(), ""));
        itemClickListener = (view1, position) -> {
            Intent i = new Intent(getActivity(), TransactionDetailActivity.class);
            i.putExtra("id", purchase.get(position).getId());
            i.putExtra("date", purchase.get(position).getDate());
            i.putExtra("dueDate", purchase.get(position).getDueDate());
            i.putExtra("invoiceNo", purchase.get(position).getInvoiceNo());
            i.putExtra("name", purchase.get(position).getName());
            i.putExtra("personType", "Pemasok");
            i.putExtra("phone", purchase.get(position).getPhone());
            i.putExtra("totalPrice", purchase.get(position).getTotalPrice());
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

    public void onGetResultPurchase(List<Purchase> purchases) {
        adapter = new PurchaseListAdapter(getContext(), purchases, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        purchase = purchases;
    }

    @Override
    public void onClick(View view) {
        pc.getAll(spc.getSPBranchId(), txtSearch.getText().toString());
    }

    public static PurchaseListFragment newInstance() {
        PurchaseListFragment f = new PurchaseListFragment();
        f.setArguments(new Bundle());
        return f;
    }
}
