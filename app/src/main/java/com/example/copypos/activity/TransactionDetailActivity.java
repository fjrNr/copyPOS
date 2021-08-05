package com.example.copypos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copypos.NonScrollListView;
import com.example.copypos.R;
import com.example.copypos.contractView.TransactionView;
import com.example.copypos.controller.PurchaseController;
import com.example.copypos.controller.SaleController;
import com.example.copypos.model.Payment;
import com.example.copypos.model.Product;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TransactionDetailActivity extends AppCompatActivity implements TransactionView.Detail, View.OnClickListener {
    private static final int INTENT_PAY_AGAIN = 100;

    Button btnPayAgain;
    ListAdapter paymentAdapter;
    ListAdapter productAdapter;
    NonScrollListView lvNonScrollPayment;
    NonScrollListView lvNonScrollProduct;
    ProgressDialog pDialog;
    TableRow tabRow_dueDate, tabRow_person, tabRow_phone;
    TextView lbl_big_GTP, lbl_big_GTP_Type;
    TextView lbl_date;
    TextView lbl_dateType;
    TextView lbl_dueDate;
    TextView lbl_grandTotalType;
    TextView lbl_invoiceNo;
    TextView lbl_personName;
    TextView lbl_personType;
    TextView lbl_phone;
    TextView lbl_small_grandTotal;

    PurchaseController purc;
    SaleController slc;

    int debt = 0;
    int id;
    int totalPrice;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);
    String dueDateString;
    String invoiceNo;
    String name, phone;
    String paymentStatus;
    String personType;
    String transDateString;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        //layout general
        lbl_big_GTP_Type = findViewById(R.id.lblBig_grandTotalType);
        lbl_big_GTP = findViewById(R.id.lblBig_grandTotal);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //layout identify
        lbl_invoiceNo = findViewById(R.id.lbl_invoiceNo);
        tabRow_person = findViewById(R.id.tabRow_person);
        lbl_personType = findViewById(R.id.lbl_personType);
        lbl_personName = findViewById(R.id.lbl_personName);
        tabRow_phone = findViewById(R.id.tabRow_phone);
        lbl_phone = findViewById(R.id.lbl_phone);
        lbl_dateType = findViewById(R.id.lbl_dateType);
        lbl_date = findViewById(R.id.lbl_date);
        tabRow_dueDate = findViewById(R.id.tabRow_dueDate);
        lbl_dueDate = findViewById(R.id.lbl_dueDate);

        //layout payment list
        lvNonScrollPayment = findViewById(R.id.lv_ns_payment);
        btnPayAgain = findViewById(R.id.btn_pay_again);
        btnPayAgain.setOnClickListener(this);

        //layout product list
        lvNonScrollProduct = findViewById(R.id.lv_ns_product);
        lbl_grandTotalType = findViewById(R.id.lbl_grandTotalType);
        lbl_small_grandTotal = findViewById(R.id.lblSmall_grandTotal);

        //controller
        purc = new PurchaseController(this);
        slc = new SaleController(this);

        //get intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        dueDateString = intent.getStringExtra("dueDate");
        name = intent.getStringExtra("name");
        paymentStatus = intent.getStringExtra("paymentStatus");
        personType = intent.getStringExtra("personType");
        phone = intent.getStringExtra("phone");
        invoiceNo = intent.getStringExtra("invoiceNo");
        transDateString = intent.getStringExtra("date");
        totalPrice = intent.getIntExtra("totalPrice", 0);
        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        Date dueDate = new Date();
        Date transDate = new Date();
        try {
            dueDate = dateFormat2.parse(dueDateString);
            transDate = dateFormat2.parse(transDateString);
        } catch (ParseException e2) {
            e2.printStackTrace();
        }

        lbl_big_GTP.setText(rpFormat.format((double) totalPrice));
        lbl_invoiceNo.setText(invoiceNo);
        lbl_personType.setText(personType);
        lbl_personName.setText(name);
        lbl_phone.setText(phone);
        if (personType.equals("Pemasok")) {
            lbl_big_GTP_Type.setText("Total Pembelian");
            lbl_dateType.setText("Tanggal beli");
            lbl_grandTotalType.setText("Total Pembelian");
            purc.getPaymentList(id);
        } else {
            lbl_big_GTP_Type.setText("Total Penjualan");
            lbl_dateType.setText("Tanggal jual");
            lbl_grandTotalType.setText("Total Penjualan");
            slc.getPaymentList(id);
        }
        if(name.length() > 0){
            tabRow_person.setVisibility(View.VISIBLE);
        }else{
            tabRow_person.setVisibility(View.GONE);
        }
        if(phone.length() > 0){
            tabRow_phone.setVisibility(View.VISIBLE);
        }else{
            tabRow_phone.setVisibility(View.GONE);
        }
        lbl_date.setText(transDateString.substring(8, 10) + "-" + transDateString.substring(5,7) + "-" + transDateString.substring(0,4));
        if (dueDate.compareTo(transDate) > 0) {
            tabRow_dueDate.setVisibility(View.VISIBLE);
            lbl_dueDate.setText(dueDateString.substring(8, 10) + "-" + dueDateString.substring(5,7) + "-" + dueDateString.substring(0,4));
        } else {
            tabRow_dueDate.setVisibility(View.GONE);
        }
        lbl_small_grandTotal.setText(rpFormat.format(totalPrice));
    }

    public void showLoading() {
        pDialog.show();
    }

    public void hideLoading() {
        pDialog.hide();
    }

    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onGetResultPayment(List<Payment> payments) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        debt = totalPrice;
        for (int i = 0; i < payments.size(); i++) {
            HashMap<String, String> payment = new HashMap<>();
            String date = payments.get(i).getDate();
            payment.put("date", date.substring(8, 10) + "-" + date.substring(5,7) + "-" + date.substring(0,4));
            payment.put("amount", rpFormat.format((long) payments.get(i).getAmount()));
            list.add(payment);
            debt -= payments.get(i).getAmount();
        }
        paymentAdapter = new SimpleAdapter(this, list, R.layout.item_payment, new String[]{"date", "amount"}, new int[]{R.id.date, R.id.amount});
        lvNonScrollPayment.setAdapter(paymentAdapter);
        if (debt > 0) {
            btnPayAgain.setVisibility(View.VISIBLE);
        } else {
            btnPayAgain.setVisibility(View.GONE);
        }
        if (personType.equals("Pemasok")) {
            purc.getProductList(id);
        } else {
            slc.getProductList(id);
        }
    }

    public void onGetResultProduct(List<Product> products) {
        int price;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (personType.equals("Pemasok")) {
                price = products.get(i).getPurchasePrice();
            } else {
                price = products.get(i).getSellPrice();
            }
            HashMap<String, String> product = new HashMap<>();
            product.put("name", products.get(i).getName());
            product.put("price", rpFormat.format((long) price));
            product.put("qty", "x " + dotFormat.format((long) products.get(i).getStock()));
            product.put("totalPrice", rpFormat.format((double) (products.get(i).getStock() * price)));
            list.add(product);
        }
        int[] iArr = {R.id.name, R.id.price, R.id.qty, R.id.totalPrice};
        productAdapter = new SimpleAdapter(this, list, R.layout.item_transaction_product, new String[]{"name", "price", "qty", "totalPrice"}, iArr);
        lvNonScrollProduct.setAdapter(productAdapter);
    }

    public void onClick(View v) {
        if (v == btnPayAgain) {
            Intent i = new Intent(this, PaymentActivity.class);
            if (personType.equals("Pemasok")) {
                i.putExtra("purchaseId", id);
            } else {
                i.putExtra("saleId", id);
            }
            i.putExtra("dueDate", dueDateString);
            i.putExtra("name", name);
            i.putExtra("gTP", debt);
            startActivityForResult(i, 100);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_PAY_AGAIN && resultCode == RESULT_OK) {
            if (personType.equals("Pemasok")) {
                purc.getPaymentList(id);
            } else {
                slc.getPaymentList(id);
            }
        }
    }
}
