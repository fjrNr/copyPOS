package com.example.copypos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copypos.R;
import com.example.copypos.controller.ReportController;
import com.example.copypos.model.Product;
import com.example.copypos.model.Report;
import com.example.copypos.model.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PaymentStatusActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnOK;
    TextView lblPaymentStatus, lblCharge;

    int charge, idPay;
    String message;
    Locale localeID = new Locale("in", "ID");
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        lblPaymentStatus = findViewById(R.id.lbl_status);
        lblCharge = findViewById(R.id.lbl_charge);
        btnOK = findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(this);

        Intent intent = getIntent();
        idPay = intent.getIntExtra("idPay",0);
        message = intent.getStringExtra("message");
        charge = intent.getIntExtra("charge", 0);
        setDataFromIntent();
    }

    private void setDataFromIntent(){
        lblPaymentStatus.setText(message);
        if(idPay == 1){
            if(charge >= 0){
                lblCharge.setText("Kembalian " + rpFormat.format(charge));
            }else{
                lblCharge.setText("Piutang " + rpFormat.format(-charge));
            }
        }else{
            if(charge >= 0){
                lblCharge.setText("Kembalian " + rpFormat.format(charge));
            }else{
                lblCharge.setText("Utang " + rpFormat.format(-charge));
            }
        }
    }

    @Override
    public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
