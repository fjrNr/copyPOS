package com.example.copypos.navigationDrawer.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.R;
import com.example.copypos.adapter.ServiceListAdapter;
import com.example.copypos.contractView.FragmentCommunicator;
import com.example.copypos.contractView.ServiceListView;
import com.example.copypos.controller.ServiceController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SellPrintFragment extends Fragment implements ServiceListView {
    FragmentCommunicator inter;
    ServiceListAdapter adapter;
    ProgressDialog pDialog;
    ServiceListAdapter.ItemClickListener itemClickListener;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    ServiceController psc;
    SharedPrefController spc;

    int id;
    int price;
    List<Service> service;
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
        pDialog.setMessage("Mengambil daftar print");
        pDialog.setCancelable(false);
        return v;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spc = new SharedPrefController(requireActivity());
        psc = new ServiceController(this);
        psc.getAllPrint(spc.getSPBranchId(), "");
        swipeRefresh.setOnRefreshListener(() -> psc.getAllPrint(spc.getSPBranchId(), ""));
        itemClickListener = (view1, position) -> {
            id = service.get(position).getId();
            price = service.get(position).getSellPrice();
            name = service.get(position).getName();
            type = "print";

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

    public void onGetResultService(List<Service> services) {

    }

    @Override
    public void onSuccess(String paramString) {

    }

    public void onErrorLoading(String message) {
        pDialog.hide();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetResultPhotocopyService(List<Service> paramList) {

    }

    @Override
    public void onGetResultPrintService(List<Service> paramList) {
        pDialog.hide();
        adapter = new ServiceListAdapter(getContext(), paramList, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        service = paramList;
    }

    public void onAttach(Context context) {
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
        SellPrintFragment f = new SellPrintFragment();
        f.setArguments(new Bundle());
        return f;
    }
}
