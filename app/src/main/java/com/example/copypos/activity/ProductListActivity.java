package com.example.copypos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.R;
import com.example.copypos.adapter.ProductListAdapter;
import com.example.copypos.contractView.ProductListView;
import com.example.copypos.controller.ProductController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Product;

import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductListView {
    ProductListAdapter adapter;
    ProductListAdapter.ItemClickListener itemClickListener;
    ProductListAdapter.ItemClickListener itemClickListener2;
    ProgressDialog pDialog;
    ProductController pc;
    List<Product> product;
    RecyclerView recyclerView;
    SharedPrefController spc;
    SwipeRefreshLayout swipeRefresh;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        spc = new SharedPrefController(this);
        pc = new ProductController(this);
        pc.getAll(spc.getSPBranchId(), "");
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public final void onRefresh() {
                pc.getAll(spc.getSPBranchId(), "");
            }
        });
        itemClickListener = (view, position) -> {
            spc.saveSPInt(SharedPrefController.SP_BUYPRODUCTID, product.get(position).getId());
            spc.saveSPString(SharedPrefController.SP_BUYPRODUCTNAME, product.get(position).getName());
            spc.saveSPInt(SharedPrefController.SP_BUYPRODUCTPRICE, product.get(position).getPurchasePrice());
            setResult(-1);
            finish();
        };
        itemClickListener2 = (view, position) -> {
                spc.saveSPInt(SharedPrefController.SP_BUYPRODUCTID, product.get(position).getId());
                spc.saveSPString(SharedPrefController.SP_BUYPRODUCTNAME, product.get(position).getName());
                spc.saveSPInt(SharedPrefController.SP_BUYPRODUCTPRICE, product.get(position).getPurchasePrice());
                setResult(-1);
                finish();
            };
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
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
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivityForResult(new Intent(this, EditProductActivity.class), 100);
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    public void onGetResultProduct(List<Product> products) {
        adapter = new ProductListAdapter(this, products, itemClickListener, itemClickListener2);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        product = products;
    }

    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
