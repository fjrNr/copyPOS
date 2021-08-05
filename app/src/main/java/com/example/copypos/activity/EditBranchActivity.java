package com.example.copypos.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copypos.R;
import com.example.copypos.api.ApiClient;
import com.example.copypos.contractView.BaseView;
import com.example.copypos.controller.BranchController;
import com.example.copypos.controller.SharedPrefController;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class EditBranchActivity extends AppCompatActivity implements View.OnClickListener, BaseView {
    private static final int GALLERY_REQUEST = 100;
    String address;
    BranchController bc;
    ImageButton btnImage;
    Button btnSave;
    EditText editTxtAddress;
    EditText editTxtName;
    EditText editTxtPhone;
    int id = 0;
    InputStream imageIs;
    String imageName;
    ImageView imageView;
    String name;
    ProgressDialog pDialog;
    String phone;
    SharedPrefController spc;
    Uri uri;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_branch);
        imageView = findViewById(R.id.logo);
        imageView.setOnClickListener(this);
        editTxtName = findViewById(R.id.txt_branchName);
        editTxtAddress = findViewById(R.id.txt_branchAddress);
        editTxtPhone = findViewById(R.id.txt_branchPhone);
        btnSave = findViewById(R.id.btn_saveBranch);
        btnSave.setOnClickListener(this);
        btnImage = findViewById(R.id.btn_image);
        btnImage.setOnClickListener(this);
        bc = new BranchController(this);
        spc = new SharedPrefController(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait..");
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        phone = intent.getStringExtra("phone");
        imageName = intent.getStringExtra("imageName");
        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {
        if (id != 0) {
            editTxtName.setText(name);
            editTxtAddress.setText(address);
            editTxtPhone.setText(phone);
            if (!imageName.equals("")) {
                btnImage.setVisibility(View.VISIBLE);
                Picasso picasso = Picasso.get();
                picasso.load(ApiClient.BASE_URL_BRANCH_IMAGE + imageName).fit().into(imageView);
            }
        }
    }

    public void onClick(View v) {
        if (v == btnSave) {
            String name2 = editTxtName.getText().toString().trim();
            String address2 = editTxtAddress.getText().toString().trim();
            String phone2 = editTxtPhone.getText().toString().trim();
            if (name2.isEmpty()) {
                editTxtPhone.setError("Isi nama toko");
            } else if (address2.isEmpty()) {
                editTxtAddress.setError("Isi alamat toko");
            } else if (phone2.isEmpty()) {
                editTxtPhone.setError("Isi nomor telepon toko");
            } else if (phone2.length() < 10) {
                editTxtPhone.setError("Isi nomor telepon toko minimal 10 digit");
            } else {
                if (id != 0) {
                    if (imageIs != null) {
                        bc.update(id, name2, address2, phone2, imageName, imageIs, false);
                    } else if (imageName.equals("") || imageView.getDrawable() != null) {
                        bc.update(id, name2, address2, phone2, imageName, imageIs, false);
                    } else {
                        bc.update(id, name2, address2, phone2, imageName, imageIs, true);
                    }
                } else {
                    bc.create(spc.getSPOwnerId(), name2, address2, phone2, imageIs);
                }
            }
        } else if (v == imageView || v == btnImage) {
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
            }else{
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

    public void showProgress() {
        pDialog.show();
    }

    public void hideProgress() {
        pDialog.hide();
    }

    public void onSuccess(String message) {
        String name2 = editTxtName.getText().toString().trim();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (id == spc.getSPBranchId()) {
            spc.saveSPString(SharedPrefController.SP_BRANCHNAME, name2);
            spc.saveSPString(SharedPrefController.SP_BRANCHIMAGE, imageName);
        }
        setResult(RESULT_OK);
        finish();
    }

    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
