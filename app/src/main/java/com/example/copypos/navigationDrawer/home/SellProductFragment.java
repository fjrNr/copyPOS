package com.example.copypos.navigationDrawer.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.R;
import com.example.copypos.adapter.ProductListAdapter;
import com.example.copypos.contractView.FragmentCommunicator;
import com.example.copypos.contractView.ProductListView;
import com.example.copypos.controller.ProductController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Product;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SellProductFragment extends Fragment implements ProductListView {
    FragmentCommunicator inter;
    ProductListAdapter adapter;
    ProductListAdapter.ItemClickListener itemClickListener;
    ProgressDialog pDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    ProductController pc;
    SharedPrefController spc;

    int id;
    int price;
    List<Product> product;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    String name;
    String type;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sell_item, container, false);
        swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Mengambil daftar produk");
        pDialog.setCancelable(false);
        return v;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spc = new SharedPrefController(requireActivity());
        pc = new ProductController((ProductListView) this);
        pc.getAll(spc.getSPBranchId(), "");
        swipeRefresh.setOnRefreshListener(() -> pc.getAll(spc.getSPBranchId(), ""));
        itemClickListener = (view1, position) -> {
            id = product.get(position).getId();
            price = product.get(position).getSellPrice();
            name = product.get(position).getName();
            type = "product";

            HashMap<String, String> item = new HashMap<>();
            item.put("id", Integer.toString(id));
            item.put("name", name);
            item.put("price", Integer.toString(price));
            item.put("type", type);
            inter.onSetData(item);
        };
    }

    public void showLoading() {
        swipeRefresh.setRefreshing(false);
        pDialog.show();
    }

    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
        pDialog.hide();
    }

    public void onGetResultProduct(List<Product> products) {
        pDialog.hide();
        adapter = new ProductListAdapter(getContext(), products, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        product = products;
    }

    public void onErrorLoading(String message) {
        pDialog.hide();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCommunicator) {
            inter = (FragmentCommunicator) context;
            return;
        }
        throw new RuntimeException(context.toString() + " must implement FragmentCommunicator");
    }

    public void onDetach() {
        super.onDetach();
        inter = null;
    }

    public static Fragment newInstance() {
        SellProductFragment f = new SellProductFragment();
        f.setArguments(new Bundle());
        return f;
    }
}
