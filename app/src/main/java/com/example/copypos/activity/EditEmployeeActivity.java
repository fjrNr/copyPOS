package com.example.copypos.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copypos.R;
import com.example.copypos.api.ApiClient;
import com.example.copypos.contractView.BaseView;
import com.example.copypos.controller.EmployeeController;
import com.example.copypos.controller.SharedPrefController;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class EditEmployeeActivity extends AppCompatActivity implements BaseView, View.OnClickListener {

    ImageView imageView;
    ImageButton btnImage;
    EditText txtName;
    EditText txtPhone;
    EditText txtUserName;
    EditText txtPassword;
    EditText txtConfirmationPassword;
    CheckBox cbPurchase;
    CheckBox cbSale;
    CheckBox cbInventory;
    CheckBox cbExpense;
    LinearLayout layoutPassword;
    ProgressDialog pDialog;

    EmployeeController ec;
    SharedPrefController spc;

    int id = 0;
    Boolean allowSale, allowPurchase, allowInventory, allowExpense;
    InputStream imageIs;
    String name, phone, username, imageName;
    Uri uri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        imageView = findViewById(R.id.image);
        imageView.setOnClickListener(this);
        btnImage = findViewById(R.id.btn_image);
        btnImage.setOnClickListener(this);
        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        txtUserName = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password);
        txtConfirmationPassword = findViewById(R.id.txt_confirmation_password);
        layoutPassword = findViewById(R.id.llayoutPassword);
        cbSale = findViewById(R.id.cb_sale);
        cbPurchase = findViewById(R.id.cb_purchase);
        cbInventory = findViewById(R.id.cb_inventory);
        cbExpense = findViewById(R.id.cb_expense);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait..");

        ec = new EmployeeController(this);
        spc = new SharedPrefController(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        username = intent.getStringExtra("username");
        imageName = intent.getStringExtra("imageName");
        allowSale = intent.getBooleanExtra("allSell",false);
        allowPurchase = intent.getBooleanExtra("allPur",false);
        allowInventory = intent.getBooleanExtra("allStock",false);
        allowExpense = intent.getBooleanExtra("allExp",false);

        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {
        if(id != 0){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Update Pegawai");

            if(!this.imageName.equals("")) {
                btnImage.setVisibility(View.VISIBLE);
                Picasso picasso = Picasso.get();
                picasso.load(ApiClient.BASE_URL_EMPLOYEE_IMAGE + this.imageName).fit().into(this.imageView);
            }
            txtName.setText(name);
            txtPhone.setText(phone);
            txtUserName.setText(username);
            cbSale.setChecked(allowSale);
            cbPurchase.setChecked(allowPurchase);
            cbInventory.setChecked(allowInventory);
            cbExpense.setChecked(allowExpense);
            layoutPassword.setVisibility(View.GONE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.submit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit) {
            String name = txtName.getText().toString().trim();
            String phone = txtPhone.getText().toString().trim();
            String username = txtUserName.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String confirmPassword = txtConfirmationPassword.getText().toString().trim();
            allowSale = cbSale.isChecked();
            allowPurchase = cbPurchase.isChecked();
            allowInventory = cbInventory.isChecked();
            allowExpense = cbExpense.isChecked();

            if(name.isEmpty()){
                txtName.setError("Isi nama pegawai");
            }else if(phone.isEmpty()){
                txtPhone.setError("Isi nomor telepon pegawai");
            }else if(phone.length() < 10){
                txtPhone.setError("Isi nomor telepon pegawai minimal 10 digit");
            }else if(username.isEmpty()){
                txtUserName.setError("Isi username pegawai");
            }else if (username.length() < 6){
                txtUserName.setError("Isi username minimal 6 karakter");
            }else if(password.isEmpty() && id == 0){
                txtPassword.setError("Isi password pegawai");
            }else if(password.length() < 6 && id == 0){
                txtPassword.setError("Isi password minimal 6 karakter");
            }else if(confirmPassword.isEmpty() && id == 0){
                txtConfirmationPassword.setError("Konfirmasi password harus diisi");
            }else if(!confirmPassword.equals(password) && id == 0){
                txtPassword.setError("Konfirmasi password harus sama dengan password");
            }else if(!allowSale && !allowPurchase && !allowInventory && !allowExpense ){
                cbSale.setError("Ceklist minimal 1");
            }else if(id != 0){
                if(imageIs != null){
                    ec.update(id, name, phone, username, allowSale, allowPurchase, allowInventory, allowExpense,imageName,imageIs, false);
                } else if (imageName.equals("") || imageView.getDrawable() != null) {
                    ec.update(id, name, phone, username, allowSale, allowPurchase, allowInventory, allowExpense,imageName,imageIs, false);
                } else {
                    ec.update(id, name, phone, username, allowSale, allowPurchase, allowInventory, allowExpense,imageName,imageIs, true);
                }
            }else{
                ec.create(spc.getSPBranchId(), name, phone, username, password, allowSale, allowPurchase, allowInventory, allowExpense, imageIs);
            }
            return true;
        }
        return false;
    }


    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == imageView || v == btnImage) {
            if (imageView.getDrawable() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Ubah Gambar");
                builder.setItems(new String[]{"Ambil dari galeri", "Hapus gambar"}, (dialog, which) -> {
                    if (which == 0) {
                        catchFromGallery();
                    } else if (which == 1) {
                        imageView.setImageDrawable(null);
                        btnImage.setVisibility(View.GONE);
                        imageIs = null;
                    }
                });
                builder.create().show();
            }else {
                catchFromGallery();
            }
        }
    }

    public void catchFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{"image/jpeg", "image/jpg", "image/png"});
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 100) {
            uri = data.getData();
            imageView.setImageURI(uri);
            try {
                imageIs = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                btnImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
