package com.example.copypos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copypos.R;
import com.example.copypos.contractView.LoginView;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.controller.UserController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {
    Button btn_login;
    Button btn_register;
    EditText txt_email;
    EditText txt_password;
    ProgressDialog pDialog;

    SharedPrefController spc;
    UserController uc;

    String regexEmail = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        uc = new UserController(this);
        spc = new SharedPrefController(this);
        if(spc.getSPSudahLogin()){
            if(spc.getSPOwnerMode()){
                if(spc.getSPBranchId() != 0){
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else{
                    Intent i = new Intent(this, BranchListActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }else{
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
    }

    public void onClick(View v) {
        if (v == btn_login) {
            String email = txt_email.getText().toString().trim();
            String password = txt_password.getText().toString().trim();

            Matcher emailMatcher = Pattern.compile(regexEmail).matcher(email);

            if (email.isEmpty()) {
                txt_email.setError("Isi email atau username");
            } else if (password.isEmpty()) {
                txt_password.setError("Isi password");
            } else if (password.length() < 6) {
                txt_password.setError("Isi password minimal 6 karakter");
            } else if (emailMatcher.matches()){
                uc.loginOwner(email, password);
            } else if (email.length() < 6){
                txt_email.setError("Isi username minimal 6 karakter");
            }else{
                uc.loginEmployee(email, password);
            }
        } else if (v == btn_register) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    public void showProgress() {
        pDialog.show();
    }

    public void hideProgress() {
        pDialog.hide();
    }

    public void onLoggedEmployee(String message, int id, String name, String imageName, int branchId, Boolean allowSale, Boolean allowPurchase, Boolean allowStock, Boolean allowExpense) {
        finish();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        spc.saveSPString(SharedPrefController.SP_FULLUSERNAME, name);
        spc.saveSPString(SharedPrefController.SP_USERIMAGE, imageName);
        spc.saveSPBoolean(SharedPrefController.SP_OWNERMODE, false);
        spc.saveSPBoolean(SharedPrefController.SP_SUDAH_LOGIN, true);

        spc.saveSPInt(SharedPrefController.SP_BRANCHID, branchId);
        spc.saveSPInt(SharedPrefController.SP_EMPLOYEEID, id);
        spc.saveSPBoolean(SharedPrefController.SP_ALLSELL, allowSale);
        spc.saveSPBoolean(SharedPrefController.SP_ALLPUR, allowPurchase);
        spc.saveSPBoolean(SharedPrefController.SP_ALLEXPENSE, allowExpense);
        spc.saveSPBoolean(SharedPrefController.SP_ALLINVENTORY, allowStock);
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onLoggedOwner(String message, int id, String name, String imageName) {
        finish();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        spc.saveSPString(SharedPrefController.SP_FULLUSERNAME, name);
        spc.saveSPString(SharedPrefController.SP_USERIMAGE, imageName);
        spc.saveSPBoolean(SharedPrefController.SP_OWNERMODE, true);
        spc.saveSPBoolean(SharedPrefController.SP_SUDAH_LOGIN, true);
        spc.saveSPInt(SharedPrefController.SP_OWNERID, id);
        startActivity(new Intent(this, BranchListActivity.class));
    }

    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
