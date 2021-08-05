package com.example.copypos.navigationDrawer.inventory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.activity.EditProductActivity;
import com.example.copypos.activity.StockHistoryActivity;
import com.example.copypos.contractView.BaseView;
import com.example.copypos.contractView.ProductListView;
import com.example.copypos.controller.ProductController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.adapter.ProductListAdapter;
import com.example.copypos.model.Product;
import com.example.copypos.R;
import java.util.List;

public class InventoryFragment extends Fragment implements BaseView, ProductListView{
    private static final int INTENT_ADD = 100;
    private static final int INTENT_EDIT = 200;
    ProductListAdapter adapter;
    ProductListAdapter.ItemClickListener itemClickListener;
    ProductListAdapter.ItemClickListener itemClickListener2;
    ProductListAdapter.ItemClickListener itemClickListener3;
    ProgressDialog pDialog;
    ProductController pc;
    ProductController pc2;
    List<Product> product;
    RecyclerView recyclerView;
    SharedPrefController spc;
    SwipeRefreshLayout swipeRefresh;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inventory, container, false);
        swipeRefresh = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        setHasOptionsMenu(true);
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spc = new SharedPrefController(requireActivity());
        pc = new ProductController((ProductListView) this);
        pc2 = new ProductController((BaseView) this);
        pc.getAll(spc.getSPBranchId(), "");
        swipeRefresh.setOnRefreshListener(() -> pc.getAll(spc.getSPBranchId(), ""));
        itemClickListener = (view1, position) -> {
            Intent i = new Intent(getActivity(), EditProductActivity.class);
            i.putExtra("id", product.get(position).getId());
            i.putExtra("name", product.get(position).getName());
            i.putExtra("stock", product.get(position).getStock());
            i.putExtra("minStock", product.get(position).getMinimumStock());
            i.putExtra("purchasePrice", product.get(position).getPurchasePrice());
            i.putExtra("sellPrice", product.get(position).getSellPrice());
            i.putExtra("isPaper", product.get(position).getPaper());
            i.putExtra("imageName", product.get(position).getImageName());
            startActivityForResult(i, 200);
        };
        itemClickListener2 = (view13, position) -> {
            Intent i = new Intent(getActivity(), StockHistoryActivity.class);
            i.putExtra("id", product.get(position).getId());
            i.putExtra("name", product.get(position).getName());
            i.putExtra("curStk", product.get(position).getStock());
            startActivity(i);
        };
        itemClickListener3 = (view12, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
            aDialog.setTitle("Hapus Barang");
            aDialog.setMessage("Apakah anda yakin ingin menghapus " + product.get(position).getName() + "?");
            aDialog.setPositiveButton("Ya", (dialogInterface, i) -> {
                pc2.delete(product.get(position).getId(), product.get(position).getImageName());
            });
            aDialog.setNegativeButton("Tidak", (dialogInterface, i) -> {});
            aDialog.show();
        };
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.editor_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Cari...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                pc.getAll(spc.getSPBranchId(), query);
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivityForResult(new Intent(getActivity(), EditProductActivity.class), 100);
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100 || requestCode == 200) && resultCode == -1) {
            pc.getAll(spc.getSPBranchId(), "");
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

    public void onGetResultProduct(List<Product> products) {
        adapter = new ProductListAdapter(getContext(), products, itemClickListener, itemClickListener2, itemClickListener3);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        product = products;
    }

    public void onErrorLoading(String message) {
        pDialog.hide();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        pDialog.show();
    }

    public void hideProgress() {
        pDialog.hide();
    }

    public void onSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        pc.getAll(spc.getSPBranchId(), "");
    }

    public void onError(String message) {
        pDialog.hide();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
