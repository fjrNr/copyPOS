package com.example.copypos.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copypos.R;
import com.example.copypos.api.ApiClient;
import com.example.copypos.contractView.BaseView;
import com.example.copypos.controller.ProductController;
import com.example.copypos.controller.SharedPrefController;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class EditProductActivity extends AppCompatActivity implements BaseView, CompoundButton.OnCheckedChangeListener, View.OnClickListener, TextWatcher {
    private static final int GALLERY_REQUEST = 100;

    ImageButton btnImage;
    ImageView imageView;
    EditText editTxtMinStock;
    EditText editTxtName;
    EditText editTxtPurchasePrice;
    EditText editTxtSellPrice;
    EditText editTxtStock;
    CheckBox cbIsPaper;
    CheckBox cbIsManageProduct;
    LinearLayout llayoutManageProduct;
    ProgressDialog pDialog;

    ProductController pc;
    SharedPrefController spc;

    boolean isPaper;
    InputStream imageIs;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    int id, minStock, purchasePrice, sellPrice, stock;
    String name, imageName;
    Uri uri;


    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        imageView = findViewById(R.id.logo);
        imageView.setOnClickListener(this);
        btnImage = findViewById(R.id.btn_image);
        btnImage.setOnClickListener(this);
        editTxtName = findViewById(R.id.txt_productName);
        editTxtStock = findViewById(R.id.txt_productStock);
        editTxtMinStock = findViewById(R.id.txt_productMinStock);
        editTxtPurchasePrice = findViewById(R.id.txt_productPurchasePrice);
        editTxtSellPrice = findViewById(R.id.txt_productSellPrice);
        editTxtStock.addTextChangedListener(this);
        editTxtMinStock.addTextChangedListener(this);
        editTxtPurchasePrice.addTextChangedListener(this);
        editTxtSellPrice.addTextChangedListener(this);
        cbIsPaper = findViewById(R.id.cb_isPaper);
        cbIsManageProduct = findViewById(R.id.cb_isManageProduct);
        cbIsManageProduct.setOnCheckedChangeListener(this);
        llayoutManageProduct = findViewById(R.id.llayoutManageProduct);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait..");
        pDialog.setCancelable(false);

        pc = new ProductController(this);
        spc = new SharedPrefController(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        stock = intent.getIntExtra("stock", 0);
        minStock = intent.getIntExtra("minStock", 0);
        purchasePrice = intent.getIntExtra("purchasePrice", 0);
        sellPrice = intent.getIntExtra("sellPrice", 0);
        isPaper = intent.getBooleanExtra("isPaper", false);
        imageName = intent.getStringExtra("imageName");
        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {
        if (id != 0) {
            editTxtName.setText(name);
            editTxtPurchasePrice.setText(dotFormat.format(purchasePrice));
            editTxtSellPrice.setText(dotFormat.format(sellPrice));
            editTxtStock.setText(dotFormat.format(stock));
            editTxtMinStock.setText(dotFormat.format(minStock));
            Objects.requireNonNull(getSupportActionBar()).setTitle("Update Produk");
            cbIsPaper.setChecked(isPaper);
            if (!(stock == 0 && minStock == 0)) {
                cbIsManageProduct.setVisibility(View.GONE);
                llayoutManageProduct.setVisibility(View.VISIBLE);
            }
            if (isPaper) {
                editTxtName.setEnabled(false);
                cbIsPaper.setEnabled(false);
            }
            if (!imageName.equals("")) {
                btnImage.setVisibility(View.VISIBLE);
                Picasso picasso = Picasso.get();
                picasso.load(ApiClient.BASE_URL_PRODUCT_IMAGE + imageName).fit().into(imageView);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit, menu);
        return true;
    }

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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_submit) {
            String name = editTxtName.getText().toString().trim();
            String purchasePrice = editTxtPurchasePrice.getText().toString().trim().replace(".", "");
            String sellPrice = editTxtSellPrice.getText().toString().trim().replace(".", "");
            boolean isPaper = cbIsPaper.isChecked();
            int buyPr; int sellPr;

            if(purchasePrice.isEmpty()){
                buyPr = 0;
            }else{
                buyPr = Integer.parseInt(purchasePrice);
            }
            if(sellPrice.isEmpty()){
                sellPr = 0;
            }else{
                sellPr = Integer.parseInt(sellPrice);
            }

            if (name.isEmpty()) {
                editTxtName.setError("Isi nama produk");
            } else if (purchasePrice.isEmpty()) {
                editTxtPurchasePrice.setError("Isi harga beli produk");
            } else if (sellPrice.isEmpty()) {
                editTxtSellPrice.setError("Isi harga jual produk");
            } else if (llayoutManageProduct.getVisibility() == View.GONE) {
                if (id != 0) {
                    if (imageIs != null) {
                        pc.update(id, name, buyPr, sellPr, 0, 0, isPaper, imageName, imageIs, false);
                    } else if (imageName.equals("") || imageView.getDrawable() != null) {
                        pc.update(id, name, buyPr, sellPr, 0, 0, isPaper, imageName, imageIs, false);
                    } else {
                        pc.update(id, name, buyPr, sellPr, 0, 0, isPaper, imageName, imageIs, true);
                    }
                } else {
                    pc.create(spc.getSPBranchId(), name, buyPr, sellPr, 0, 0, isPaper, imageIs);
                }
            } else {
                String minStock = editTxtMinStock.getText().toString().trim().replace(".", "");
                String stock = editTxtStock.getText().toString().trim().replace(".", "");
                int stk; int minStk;

                if (stock.isEmpty()) {
                    editTxtStock.setError("Isi jumlah stok produk saat ini");
                }else if(minStock.isEmpty()){
                    editTxtMinStock.setError("Isi jumlah stok minimum produk");
                }else {
                    stk = Integer.parseInt(stock);
                    minStk = Integer.parseInt(minStock);

                    if(id != 0) {
                        if (imageIs != null) {
                            pc.update(id, name, buyPr, sellPr, minStk, stk, isPaper, imageName, imageIs, false);
                        } else if (imageName.equals("") || imageView.getDrawable() != null) {
                            pc.update(id, name, buyPr, sellPr, minStk, stk, isPaper, imageName, imageIs, false);
                        } else {
                            pc.update(id, name, buyPr, sellPr, minStk, stk, isPaper, imageName, imageIs, true);
                        }
                    }else {
                        pc.create(spc.getSPBranchId(), name, buyPr, sellPr, minStk, stk, isPaper, imageIs);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
        int number = 0;
        EditText editText = null;
        if (s == editTxtPurchasePrice.getEditableText()) {
            editText = editTxtPurchasePrice;
        } else if (s == editTxtSellPrice.getEditableText()) {
            editText = editTxtSellPrice;
        } else if (s == editTxtStock.getEditableText()) {
            editText = editTxtStock;
        } else if (s == editTxtMinStock.getEditableText()) {
            editText = editTxtMinStock;
        }
        String txtNumber = Objects.requireNonNull(editText).getText().toString().trim().replace(".", "");
        if (!txtNumber.isEmpty()) {
            if (txtNumber.length() > 6) {
                txtNumber = txtNumber.substring(0, txtNumber.length() - 1);
            }
            try {
                number = Integer.parseInt(txtNumber);
            } catch (NumberFormatException nfe) {
                number = 1;
            }
        }
        editText.removeTextChangedListener(this);
        if (number != 0) {
            try {
                editText.setText(dotFormat.format(number));
                editText.setSelection(editText.getText().length());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        editText.addTextChangedListener(this);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == cbIsManageProduct) {
            if (buttonView.isChecked()) {
                llayoutManageProduct.setVisibility(View.VISIBLE);
            } else {
                llayoutManageProduct.setVisibility(View.GONE);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
