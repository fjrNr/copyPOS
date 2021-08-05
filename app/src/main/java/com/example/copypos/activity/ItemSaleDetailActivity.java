package com.example.copypos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.copypos.NonScrollListView;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.R;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ItemSaleDetailActivity extends AppCompatActivity implements View.OnClickListener {

//    private static final int INTENT_PAY = 100;

    Button btnPay;
    EditText txtName, txtPhone;
    ListAdapter productAdapter;
    NonScrollListView lvNonScrollProduct;
    TextView lblSmall_grandTotal;

    SharedPrefController spc;

    ArrayList<Integer> idItemList = new ArrayList<>();
    ArrayList<Integer> priceItemList = new ArrayList<>();
    ArrayList<Integer> qtyItemList = new ArrayList<>();
    ArrayList<String> nameItemList = new ArrayList<>();
    ArrayList<String> typeItemList = new ArrayList<>();
    int totalPrice = 0;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sale_detail);
        lvNonScrollProduct = (NonScrollListView) findViewById(R.id.lv_ns_product);
        lblSmall_grandTotal = (TextView) findViewById(R.id.lblSmall_grandTotal);
        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        btnPay = (Button) findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(this);
        Intent intent = getIntent();
        typeItemList = intent.getStringArrayListExtra("typeItemList");
        idItemList = intent.getIntegerArrayListExtra("idItemList");
        nameItemList = intent.getStringArrayListExtra("nameItemList");
        priceItemList = intent.getIntegerArrayListExtra("priceItemList");
        qtyItemList = intent.getIntegerArrayListExtra("qtyItemList");
        spc = new SharedPrefController(this);
        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        for (int i = 0; i < idItemList.size(); i++) {
            int totalPrice2 = priceItemList.get(i) * qtyItemList.get(i);
            HashMap<String, String> product = new HashMap<>();
            product.put("name", nameItemList.get(i));
            product.put("price", rpFormat.format(priceItemList.get(i)));
            product.put("qty", "x " + dotFormat.format(qtyItemList.get(i)));
            product.put("totalPrice", rpFormat.format((double) totalPrice2));
            list.add(product);
        }
        productAdapter = new SimpleAdapter(this, list, R.layout.item_transaction_product, new String[]{"name", "price", "qty", "totalPrice"}, new int[]{R.id.name, R.id.price, R.id.qty, R.id.totalPrice});
        lvNonScrollProduct.setAdapter(productAdapter);
        for (int i2 = 0; i2 < idItemList.size(); i2++) {
            totalPrice += priceItemList.get(i2) * qtyItemList.get(i2);
        }
        lblSmall_grandTotal.setText(rpFormat.format((double) totalPrice));
        btnPay.setText("Bayar " + rpFormat.format((double) totalPrice));
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK){
//            if(requestCode == INTENT_PAY){
//                finish();
//            }
//        }
//    }

    public void onClick(View v) {
        if (v == btnPay) {
            String name = txtName.getText().toString().trim();
            String phone = txtPhone.getText().toString().trim();

            Intent i = new Intent(this, PaymentActivity.class);
            i.putStringArrayListExtra("typeList", typeItemList);
            i.putIntegerArrayListExtra("idList", idItemList);
            i.putStringArrayListExtra("nameList", nameItemList);
            i.putIntegerArrayListExtra("priceList", priceItemList);
            i.putIntegerArrayListExtra("qtyList", qtyItemList);
            i.putExtra("name", name);
            i.putExtra("phone", phone);
            i.putExtra("branchId", spc.getSPBranchId());
            i.putExtra("gTP", totalPrice);
            startActivity(i);
//            startActivityForResult(i, INTENT_PAY);
        }
    }
}
