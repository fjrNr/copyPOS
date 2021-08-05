package com.example.copypos.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.R;
import com.example.copypos.adapter.BranchListAdapter;
import com.example.copypos.contractView.BranchListView;
import com.example.copypos.controller.BranchController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Branch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BranchListActivity extends AppCompatActivity implements BranchListView {
    private static final int INTENT_ADD = 100;
    private static final int INTENT_EDIT = 200;
    BranchListAdapter adapter;
    List<Branch> branch;
    FloatingActionButton fab;
    BranchListAdapter.ItemClickListener itemClickListener;
    BranchListAdapter.ItemClickListener itemClickListener2;
    BranchListAdapter.ItemClickListener itemClickListener3;
    BranchController lc;
//    ProgressDialog pDialog;
    RecyclerView recyclerView;
    SharedPrefController spc;
    SwipeRefreshLayout swipeRefresh;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_logout) {
            return false;
        }
        AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
        aDialog.setTitle("Keluar");
        aDialog.setMessage("Apakah anda yakin ingin keluar?");
        aDialog.setPositiveButton("Ya", (dialog, which) -> {
            dialog.dismiss();
            spc = new SharedPrefController(getApplicationContext());
            spc.saveSPBoolean(SharedPrefController.SP_OWNERMODE, false);
            spc.saveSPInt(SharedPrefController.SP_OWNERID, 0);
            spc.saveSPString(SharedPrefController.SP_FULLUSERNAME, "");
            spc.saveSPString(SharedPrefController.SP_USERIMAGE, "");
            spc.saveSPInt(SharedPrefController.SP_BRANCHID, 0);
            spc.saveSPString(SharedPrefController.SP_BRANCHNAME, "");
            spc.saveSPBoolean(SharedPrefController.SP_SUDAH_LOGIN, false);
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
        aDialog.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());
        aDialog.show();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_list);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(view -> startActivityForResult(new Intent(this, EditBranchActivity.class), 100));
        spc = new SharedPrefController(this);
        lc = new BranchController(this);
        lc.getAll(spc.getSPOwnerId());
//        pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.setCancelable(false);
        swipeRefresh.setOnRefreshListener(() -> lc.getAll(spc.getSPOwnerId()));
        itemClickListener = ((view, position) -> {
            spc.saveSPInt(SharedPrefController.SP_BRANCHID, branch.get(position).getId());
            spc.saveSPString(SharedPrefController.SP_BRANCHNAME, branch.get(position).getName());
            spc.saveSPString(SharedPrefController.SP_BRANCHIMAGE, branch.get(position).getImageName());
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
        itemClickListener2 = ((view, position) -> {
            Intent i = new Intent(this, EditBranchActivity.class);
            i.putExtra("id", branch.get(position).getId());
            i.putExtra("name", branch.get(position).getName());
            i.putExtra("address", branch.get(position).getAddress());
            i.putExtra("phone", branch.get(position).getPhone());
            i.putExtra("imageName", branch.get(position).getImageName());
            startActivityForResult(i, 200);
        });
        itemClickListener3 = ((view, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
            aDialog.setTitle("Hapus Toko");
            aDialog.setMessage("Apakah anda yakin ingin menghapus toko " + branch.get(position).getName() + "? Seluruh data barang dan transaksi pada toko ini akan dihapus selamanya");
            aDialog.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());
            aDialog.setPositiveButton("Ya", (dialog, which) -> {
                lc.delete(branch.get(position).getId(), branch.get(position).getImageName());
                dialog.dismiss();
            });
            aDialog.show();
        });
    }

    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100 || requestCode == 200) lc.getAll(spc.getSPOwnerId());
        }
    }

    public void showProgress() {
        swipeRefresh.setRefreshing(true);
//        pDialog.show();
    }

    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
//        pDialog.hide();
    }

    public void onGetResult(List<Branch> branches) {
        adapter = new BranchListAdapter(this, branches, itemClickListener, itemClickListener2, itemClickListener3);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        branch = branches;
    }

    @Override
    public void onSuccess(String paramString) {
        Toast.makeText(this, paramString, Toast.LENGTH_SHORT).show();
        lc.getAll(spc.getSPOwnerId());
    }

    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
