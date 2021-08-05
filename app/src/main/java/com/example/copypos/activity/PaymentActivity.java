package com.example.copypos.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.copypos.R;
import com.example.copypos.contractView.PaymentView;
import com.example.copypos.controller.PurchaseController;
import com.example.copypos.controller.SaleController;
import com.example.copypos.controller.SharedPrefController;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class PaymentActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, PaymentView, TextWatcher {

    private static final int INTENT_CREATE_SALE = 200;
    private static final int INTENT_CREATE_PURCHASE = 201;
    private static final int INTENT_CREATE_SALE_PAYMENT = 202;
    private static final int INTENT_CREATE_PURCHASE_PAYMENT = 203;

    Button btnDueDate, btnFullCash, btnPartialCash;
    DatePickerDialog datePickerDialog;
    EditText txtAmount, txtDPAmount, txtName, txtPhone;
    LinearLayout llayout1;
    LinearLayout llayout2;
    LinearLayout llayout3;
    ProgressDialog pDialog;
    RadioButton rdBtnPartialCash;
    RadioGroup rdGroup;
    TextView lblAmount;
    TextView lblTotalPrice;
    TextView lblWang;
    TextView lblName;
    TextView lblPhone;

    PurchaseController pc;
    SaleController sc;
    SharedPrefController spc;

    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<Integer> priceList = new ArrayList<>();
    ArrayList<Integer> qtyList = new ArrayList<>();
    ArrayList<String> typeList = new ArrayList<>();
    Calendar cale = Calendar.getInstance(Locale.getDefault());
    int customerId;
    int nowDate = cale.get(Calendar.DAY_OF_MONTH);
    int nowMonth = cale.get(Calendar.MONTH);
    int nowYear = cale.get(Calendar.YEAR);
    int posDate = cale.get(Calendar.DAY_OF_MONTH);
    int posMonth = cale.get(Calendar.MONTH);
    int posYear = cale.get(Calendar.YEAR);
    int purDate;
    int purMonth;
    int purYear;
    int purchaseId;
    int saleId;
    int supplierId;
    int totalPrice;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);
    String name, phone, rawDueDate;



    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //main layout
        lblTotalPrice = findViewById(R.id.lblTotalPrice);
        llayout3 = findViewById(R.id.llayoutPaymentMethod);
        llayout1 = findViewById(R.id.llayoutFullCash);
        llayout2 = findViewById(R.id.llayoutPartialCash);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait..");

        //layout payment method
        rdGroup = findViewById(R.id.rdGroup);
        rdGroup.setOnCheckedChangeListener(this);
        rdBtnPartialCash = findViewById(R.id.rdBtnPartialCash);

        //layout full cash
        txtAmount = findViewById(R.id.txt_amount);
        txtAmount.addTextChangedListener(this);
        lblAmount = findViewById(R.id.lbl_amount);
        btnFullCash = findViewById(R.id.btn_fullCash);
        btnFullCash.setOnClickListener(this);

        //layout partial cash
        lblName = findViewById(R.id.lbl_name);
        txtName = findViewById(R.id.txt_name);
        lblPhone = findViewById(R.id.lbl_phone);
        txtPhone = findViewById(R.id.txt_phone);
        lblWang = findViewById(R.id.lbl_uang_muka);
        txtDPAmount = findViewById(R.id.txt_DPAmount);
        txtDPAmount.addTextChangedListener(this);
        btnDueDate = findViewById(R.id.btn_dueDate);
        btnDueDate.setOnClickListener(this);
        btnPartialCash = findViewById(R.id.btn_partialCash);
        btnPartialCash.setOnClickListener(this);

        //controller
        pc = new PurchaseController(this);
        sc = new SaleController(this);
        spc = new SharedPrefController(this);

        //get intent
        Intent intent = getIntent();
        purDate = intent.getIntExtra("purchaseDate", 0);
        purMonth = intent.getIntExtra("purchaseMonth", 0);
        purYear = intent.getIntExtra("purchaseYear", 0);
        saleId = intent.getIntExtra("saleId", 0);
        purchaseId = intent.getIntExtra("purchaseId", 0);
        customerId = intent.getIntExtra("customerId", 0);
        supplierId = intent.getIntExtra("supplierId", 0);
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        totalPrice = intent.getIntExtra("gTP", 0);
        typeList = intent.getStringArrayListExtra("typeList");
        idList = intent.getIntegerArrayListExtra("idList");
        priceList = intent.getIntegerArrayListExtra("priceList");
        qtyList = intent.getIntegerArrayListExtra("qtyList");
        rawDueDate = intent.getStringExtra("dueDate");
        setDataFromIntent();
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rdBtnFullCash) {
            llayout1.setVisibility(View.VISIBLE);
            llayout2.setVisibility(View.GONE);
        }else{
            llayout1.setVisibility(View.GONE);
            llayout2.setVisibility(View.VISIBLE);
        }
    }

    public void setDataFromIntent(){
        onCheckedChanged(rdGroup, R.id.rdBtnFullCash);
        lblTotalPrice.setText(rpFormat.format((long) totalPrice));

        if(saleId != 0){
            onCheckedChanged(rdGroup, R.id.rdBtnPartialCash);
            llayout3.setVisibility(View.GONE);

            lblName.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            lblPhone.setVisibility(View.GONE);
            txtPhone.setVisibility(View.GONE);

            lblAmount.setText("Jumlah Piutang");
            lblWang.setText("Uang yang diterima (dalam Rupiah)*");

            btnDueDate.setEnabled(false);
            btnDueDate.setText(rawDueDate.substring(8, 10) + "-" + rawDueDate.substring(5,7) + "-" + rawDueDate.substring(0,4));
        }else if(purchaseId != 0){
            onCheckedChanged(rdGroup, R.id.rdBtnPartialCash);
            llayout3.setVisibility(View.GONE);

            lblName.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            lblPhone.setVisibility(View.GONE);
            txtPhone.setVisibility(View.GONE);

            lblAmount.setText("Jumlah Utang");
            lblWang.setText("Uang yang dibayar (dalam Rupiah)*");

            btnDueDate.setEnabled(false);
            btnDueDate.setText(rawDueDate.substring(8, 10) + "-" + rawDueDate.substring(5,7) + "-" + rawDueDate.substring(0,4));
        }else if(typeList != null){
            rdBtnPartialCash.setText("Piutang");
            lblWang.setText("Uang muka yang diterima (dalam Rupiah)*");
            txtName.setText(name);
            txtPhone.setText(phone);

            rawDueDate = posDate + "-" + (posMonth + 1) + "-" + posYear;
            btnDueDate.setText(rawDueDate);
            cale.add(Calendar.DAY_OF_MONTH, 7);
        }else{
            rdBtnPartialCash.setText("Utang");
            lblWang.setText("Uang muka yang dibayar (dalam Rupiah)*");
            txtName.setText(name);
            txtPhone.setText(phone);

            rawDueDate = posDate + "-" + (posMonth + 1) + "-" + posYear;
            btnDueDate.setText(rawDueDate);
            cale.add(Calendar.DAY_OF_MONTH, 7);
        }

    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
        int number = 0;
        EditText editText = null;
        if (s == txtAmount.getEditableText()) {
            editText = txtAmount;
        } else if (s == txtDPAmount.getEditableText()) {
            editText = txtDPAmount;
        }
        String txtNumber = Objects.requireNonNull(editText).getText().toString().trim().replace(".", "");
        if (!txtNumber.isEmpty()) {
            if (txtNumber.length() > 9) {
                txtNumber = txtNumber.substring(0, txtNumber.length() - 1);
            }
            number = Integer.parseInt(txtNumber);
        }
        editText.removeTextChangedListener(this);
        if (number != 0) {
            try {
                editText.setText(dotFormat.format((long) number));
                editText.setSelection(editText.getText().length());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        editText.addTextChangedListener(this);
    }

    public void onClick(View view) {
        if (view == btnDueDate) {
            datePickerDialog = new DatePickerDialog(this, (view1, year, monthOfYear, dayOfMonth) -> {
                posYear = year;
                posMonth = monthOfYear;
                posDate = dayOfMonth;
                rawDueDate = posDate + "-" + (posMonth + 1) + "-" + posYear;
                btnDueDate.setText(rawDueDate);
            }, posYear, posMonth, posDate);
            datePickerDialog.show();
        } else if (view == btnFullCash) {
            String amount = txtAmount.getText().toString().trim().replace(".", "");
            if (amount.isEmpty()) {
                txtAmount.setError("Isi jumlah uang");
            } else if (Integer.parseInt(amount) < totalPrice) {
                txtAmount.setError("Jumlah uang yang diterima harus sama atau lebih dari jumlah belanja");
            } else if (typeList == null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date purchaseDate = new Date();
                try {
                    purchaseDate = dateFormat.parse(purYear + "-" + (purMonth + 1) + "-" + purDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (purchaseDate != null) {
                    pc.create(
                            spc.getSPBranchId(),
                            dateFormat.format(purchaseDate),
                            "",
                            name,
                            Integer.parseInt(amount),
                            phone,
                            totalPrice,

                            idList,
                            priceList,
                            qtyList);
                }else{
                    Toast.makeText(this, "Tanggal penjualan atau tanggal jatuh tempo tidak disertakan", Toast.LENGTH_SHORT).show();
                }
            } else {
                sc.create(spc.getSPBranchId(), "", name, phone, Integer.parseInt(amount), totalPrice, typeList, idList, priceList, qtyList);
            }
        } else if (view == btnPartialCash) {
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            String dpAmount = txtDPAmount.getText().toString().trim().replace(".", "");
            String creditorName = txtName.getText().toString().trim();
            String creditorPhone = txtPhone.getText().toString().trim();
            Date purchaseDate = new Date();
            Date saleDate = new Date();
            Date dueDate = new Date();
            try {
                dueDate = dateFormat2.parse(posYear + "-" + (posMonth + 1) + "-" + posDate);
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            if (dpAmount.isEmpty()) {
                txtDPAmount.setError("Isi jumlah pambayaran");
            }else if (saleId != 0) {
                sc.createPayment(saleId, Integer.parseInt(dpAmount));
            }else if( purchaseId != 0) {
                pc.createPayment(purchaseId, Integer.parseInt(dpAmount));
            }else if (typeList == null) {
                try {
                    purchaseDate = dateFormat2.parse(purYear + "-" + (purMonth + 1) + "-" + purDate);
                } catch (ParseException e3) {
                    e3.printStackTrace();
                }

                if(creditorName.isEmpty()){
                    txtName.setError("Isi nama pemasok");
                }else if(creditorPhone.isEmpty()){
                    txtPhone.setError("Isi nomor telepon");
                }else if (purchaseDate == null || dueDate == null) {
                    Toast.makeText(this, "Tanggal pembelian atau tanggal jatuh tempo tidak disertakan", Toast.LENGTH_SHORT).show();
                } else if (purchaseDate.compareTo(dueDate) >= 0) {
                    Toast.makeText(this, "Tanggal jatuh tempo tidak boleh kurang dari atau sama dengan tanggal pembelian", Toast.LENGTH_SHORT).show();
                } else {
                    pc.create(
                            spc.getSPBranchId(),
                            dateFormat2.format(purchaseDate),
                            dateFormat2.format(dueDate),
                            creditorName,
                            Integer.parseInt(dpAmount),
                            creditorPhone,
                            totalPrice,

                            idList,
                            priceList,
                            qtyList);
                }

            } else {
                try {
                    saleDate = dateFormat2.parse(nowYear + "-" + (nowMonth + 1) + "-" + nowDate);
                } catch (ParseException e4) {
                    e4.printStackTrace();
                }

                if(creditorName.isEmpty()){
                    txtName.setError("Isi nama pelanggan");
                }else if(creditorPhone.isEmpty()){
                    txtName.setError("Isi nomor telepon");
                }if (saleDate == null || dueDate == null) {
                    Toast.makeText(this, "Tanggal penjualan atau tanggal jatuh tempo tidak disertakan", Toast.LENGTH_SHORT).show();
                } else if (saleDate.compareTo(dueDate) >= 0) {
                    Toast.makeText(this, "Tanggal jatuh tempo tidak boleh kurang dari atau sama dengan tanggal penjualan", Toast.LENGTH_SHORT).show();
                } else {
                    sc.create(
                            spc.getSPBranchId(),
                            dateFormat2.format(dueDate),
                            creditorName,
                            creditorPhone,
                            Integer.parseInt(dpAmount),
                            totalPrice,
                            typeList,
                            idList,
                            priceList,
                            qtyList);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == INTENT_CREATE_SALE){
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }else{
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    public void showProgress() {
        pDialog.show();
    }

    public void hideProgress() {
        pDialog.hide();
    }

    public void onSuccess(String message, int charge) {
        Intent i = new Intent(this, PaymentStatusActivity.class);
        i.putExtra("message", message);
        i.putExtra("charge", charge);

        if(saleId != 0){
            i.putExtra("idPay", 1);
            startActivityForResult(i, INTENT_CREATE_SALE_PAYMENT);
        }else if(purchaseId != 0){
            i.putExtra("idPay", 2);
            startActivityForResult(i, INTENT_CREATE_PURCHASE_PAYMENT);
        }else if(typeList != null){
            i.putExtra("idPay", 1);
            startActivityForResult(i, INTENT_CREATE_SALE);
        }else{
            i.putExtra("idPay", 2);
            startActivityForResult(i, INTENT_CREATE_PURCHASE);
        }
    }

    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
