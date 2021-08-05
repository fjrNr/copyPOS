package com.example.copypos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copypos.R;
import com.example.copypos.contractView.ReportView;
import com.example.copypos.controller.ReportController;
import com.example.copypos.model.Product;
import com.example.copypos.model.Report;
import com.example.copypos.model.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BestSellerListActivity extends AppCompatActivity implements ReportView {

    ListAdapter adapter;
    ListView listview;
    ProgressDialog pDialog;

    ReportController rc;

    int branchId, periodeType, itemType;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);
    String periode;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_seller_list);
        listview = findViewById(R.id.listView);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Mengambil data...");
        pDialog.setCancelable(false);

        rc = new ReportController(this);

        Intent intent = getIntent();
        branchId = intent.getIntExtra("branchId",0);
        periodeType = intent.getIntExtra("periodeType",0);
        periode = intent.getStringExtra("periode");
        itemType = intent.getIntExtra("itemType",0);
        rc.getBestSellerList(branchId, periodeType, periode, itemType);
    }

    @Override
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void onError(String paramString) {
        pDialog.hide();
        Toast.makeText(this,"Gagal ambil data",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetSummaryResult(Report result) {
        pDialog.hide();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String str1 = "name", str2 = "qty";
        String[] strArr = {str1, str2};
        if(itemType == 1){
            List<Product> products = result.getProducts();

            int i = 0;
            while (i < products.size()) {
                HashMap<String, String> product = new HashMap<>();
                product.put(str1, products.get(i).getName());
                StringBuilder sb = new StringBuilder();
                sb.append(dotFormat.format(products.get(i).getStock()));
                sb.append(" barang");
                product.put(str2, sb.toString());
                list.add(product);
                i++;
            }

        }else if(itemType == 2){
            List<Service> printServices = result.getPrintServices();

            for (int i3 = 0; i3 < printServices.size(); i3++) {
                HashMap<String, String> print = new HashMap<>();
                print.put(str1, printServices.get(i3).getName());
                print.put(str2, dotFormat.format(printServices.get(i3).getAmount()) + " lembar");
                list.add(print);
            }

        }else if(itemType == 3){
            List<Service> FCServices = result.getFCServices();

            for (int i3 = 0; i3 < FCServices.size(); i3++) {
                HashMap<String, String> print = new HashMap<>();
                print.put(str1, FCServices.get(i3).getName());
                print.put(str2, dotFormat.format(FCServices.get(i3).getAmount()) + " lembar");
                list.add(print);
            }
        }else{
            List<Service> services = result.getServices();

            for (int i3 = 0; i3 < services.size(); i3++) {
                HashMap<String, String> print = new HashMap<>();
                print.put(str1, services.get(i3).getName());
                print.put(str2, dotFormat.format(services.get(i3).getAmount()) + " kali");
                list.add(print);
            }
        }

        adapter = new SimpleAdapter(this, list, R.layout.item_best_seller, strArr, new int[]{R.id.name, R.id.qty});
        listview.setAdapter(adapter);
    }

    @Override
    public void onSuccess(String paramString) {
        pDialog.hide();
    }

    @Override
    public void showProgress() {
        pDialog.show();
    }
}
