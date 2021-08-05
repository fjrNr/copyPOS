package com.example.copypos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.copypos.contractView.StockListView;
import com.example.copypos.controller.StockController;
import com.example.copypos.model.StockHistory;
import com.example.copypos.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StockHistoryActivity extends AppCompatActivity implements StockListView {
    ListAdapter adapter;
    int currentStock;
    int id;
    ListView listView;
    String name;
    ProgressDialog pDialog;
    StockController sc;
    TextView txtName;
    TextView txtStock;

    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);
        txtName = findViewById(R.id.name);
        txtStock = findViewById(R.id.currentStock);
        listView = findViewById(R.id.listView);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Mengambil riwayat atus stok");
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        currentStock = intent.getIntExtra("curStk", 0);
        name = intent.getStringExtra("name");
        sc = new StockController(this);
        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {
        TextView textView = txtName;
        textView.setText("Nama barang: " + name);
        TextView textView2 = txtStock;
        textView2.setText("Stok saat ini: " + currentStock + " stok");
        sc.getAll(id);
    }

    public void showLoading() {
        pDialog.show();
    }

    public void hideLoading() {
        pDialog.hide();
    }

    public void onGetResult(List<StockHistory> stockHistories) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        for (int i = 0; i < stockHistories.size(); i++) {
            HashMap<String, String> stock = new HashMap<>();
            String date = stockHistories.get(i).getDate();
            stock.put("notes", stockHistories.get(i).getNotes());
            stock.put("changeMethod", stockHistories.get(i).getMethod());
            stock.put("date", date.substring(8, 10) + "-" + date.substring(5,7) + "-" + date.substring(0,4));
            if(stockHistories.get(i).getChangedStock() > 0){
                stock.put("changeAmount", "+" + dotFormat.format(stockHistories.get(i).getChangedStock()) + " stok");
            }else{
                stock.put("changeAmount", dotFormat.format(stockHistories.get(i).getChangedStock()) + " stok") ;
            }
            list.add(stock);
        }
        int[] iArr = {R.id.notes, R.id.changeMethod, R.id.date, R.id.changeAmount};
        adapter = new SimpleAdapter(this, list, R.layout.item_stock, new String[]{"notes", "changeMethod", "date", "changeAmount"}, iArr);
        listView.setAdapter(adapter);
    }

    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
