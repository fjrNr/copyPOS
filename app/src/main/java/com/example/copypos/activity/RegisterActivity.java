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
import com.example.copypos.contractView.RegisterView;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.controller.UserController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, RegisterView {
    Button btn_register;
    ProgressDialog pDialog;
    EditText txt_confirm_password;
    EditText txt_email;
    EditText txt_name;
    EditText txt_password;
    EditText txt_phone;

    UserController uc;
    SharedPrefController spc;

    String regexEmail = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);
        txt_phone = findViewById(R.id.txt_phone);
        txt_password = findViewById(R.id.txt_password);
        txt_confirm_password = findViewById(R.id.txt_confirmPassword);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait..");
        pDialog.setCancelable(false);
        uc = new UserController(this);
        spc = new SharedPrefController(this);
    }

    public void onClick(View v) {
        if (v == btn_register) {
            String name = txt_name.getText().toString().trim();
            String phone = txt_phone.getText().toString().trim();
            String email = txt_email.getText().toString().trim();
            String password = txt_password.getText().toString().trim();
            String confirm_password = txt_confirm_password.getText().toString().trim();

            Matcher emailMatcher = Pattern.compile(regexEmail).matcher(email);

            if (name.isEmpty()) {
                txt_name.setError("Isi nama");
            } else if (phone.isEmpty()) {
                txt_phone.setError("Isi nomor telepon");
            }else if (phone.length() < 10) {
                txt_phone.setError("Isi nomor telepon minimal 10 digit");
            } else if (email.isEmpty()) {
                txt_email.setError("Isi email");
            } else if (!emailMatcher.matches()){
                txt_email.setError("Isi email dengan format valid");
            } else if (password.isEmpty()) {
                txt_password.setError("Isi password");
            } else if (password.length() < 6) {
                txt_password.setError("Isi password minimal 6 karakter");
            } else if (confirm_password.isEmpty()) {
                txt_confirm_password.setError("Ini password konfirmasi");
            } else if (!confirm_password.equals(password)) {
                txt_confirm_password.setError("Password harus sama");
            } else {
                uc.register(name, phone, email, password);
            }
        }
    }

    public void showProgress() {
        pDialog.show();
    }

    public void hideProgress() {
        pDialog.hide();
    }

    public void onLogged(String message, int id) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, BranchListActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        spc.saveSPBoolean(SharedPrefController.SP_OWNERMODE, true);
        spc.saveSPString(SharedPrefController.SP_FULLUSERNAME, txt_name.getText().toString().trim());
        spc.saveSPInt(SharedPrefController.SP_OWNERID, id);
        spc.saveSPBoolean(SharedPrefController.SP_SUDAH_LOGIN, true);
        startActivity(i);
    }

    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
