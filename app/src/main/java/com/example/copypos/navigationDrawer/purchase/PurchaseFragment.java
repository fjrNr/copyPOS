package com.example.copypos.navigationDrawer.purchase;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.example.copypos.R;
import com.example.copypos.activity.PaymentActivity;
import com.example.copypos.activity.ProductListActivity;
import com.example.copypos.controller.SharedPrefController;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class PurchaseFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int INTENT_ADD_PRODUCT = 101;
    private static final int INTENT_PAY = 102;

    AppCompatImageButton btnAddProduct;
    Button btnDate, btnPay;
    EditText txtAddFee, txtDiscount, txtName, txtPhone;
    DatePickerDialog datePickerDialog;
    private ListView listView;

    SharedPrefController spc;

    ArrayList<HashMap<String,String>> plainList = new ArrayList<>();
    ArrayList<String> itemNameList = new ArrayList<>();
    ArrayList<Integer> itemIdList, itemPriceList, itemQtyList = new ArrayList<>();

    Calendar cale = Calendar.getInstance(Locale.getDefault());
    int posYear = cale.get(Calendar.YEAR);
    int posMonth = cale.get(Calendar.MONTH);
    int posDate = cale.get(Calendar.DAY_OF_MONTH);
    int grandTotalPrice = 0, supplierId = 0;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);
    String date;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_purchase, container, false);

        listView = root.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        //layout seller
        txtName = root.findViewById(R.id.txt_name);
        txtPhone = root.findViewById(R.id.txt_phone);
        btnDate = root.findViewById(R.id.btn_date);
        btnDate.setOnClickListener(this);
        btnAddProduct = root.findViewById(R.id.btn_addProduct);
        btnAddProduct.setOnClickListener(this);
        btnPay = root.findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(this);
        return root;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        spc = new SharedPrefController(requireActivity());
        date = posDate + "-" + (posMonth+1) + "-" + posYear;
        btnDate.setText(date);
        grandTotalPrice = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == INTENT_ADD_PRODUCT){

                // fix it
//                if(itemIdList.contains(spc.getSPProductId())){
//                    int itemQty = itemQtyList.get(spc.getSPProductId()) + 1;
//                    itemQtyList.set(spc.getSPProductId(), itemQty);
//                }else{
//                    itemIdList.add(spc.getSPProductId());
//                    itemNameList.add(spc.getSPProductName());
//                    itemPriceList.add(spc.getSPProductPrice());
//                    itemQtyList.add(1);
//                }
                grandTotalPrice += spc.getSPProductPrice();

                updateAdapter();
                btnPay.setText("Bayar " + rpFormat.format(grandTotalPrice));

                spc.saveSPInt(SharedPrefController.SP_BUYPRODUCTID, 0);
                spc.saveSPString(SharedPrefController.SP_BUYPRODUCTNAME, "");
                spc.saveSPInt(SharedPrefController.SP_BUYPRODUCTPRICE, 0);

            }else if(requestCode == INTENT_PAY){
                supplierId = 0;
                date = posDate + "-" + (posMonth+1) + "-" + posYear;
                btnDate.setText(date);
                plainList.clear();
                updateAdapter();
                grandTotalPrice = 0;
                btnPay.setText(rpFormat.format(grandTotalPrice));
            }
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        HashMap<String,String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
        Toast.makeText(getActivity(),Integer.toString(position),Toast.LENGTH_SHORT).show();
//        String inId = map.get("id");
//        String inName = map.get("name");
//        String inAmount = map.get("amount") - "Rp";
//        String inPrice = Objects.requireNonNull(map.get("price")).substring(2);
//        String inTotalPrice = Objects.requireNonNull(map.get("totalPrice")).trim().replace("Rp", "").replace(".", "").replace(",", "");

        AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_item_purchase, null);
        dialogView.setPaddingRelative(50, 50, 50, 0);
        aDialog.setTitle("Edit Pembelian");
        aDialog.setView(dialogView);
        EditText txtName = dialogView.findViewById(R.id.txtName);
        EditText txtPrice = dialogView.findViewById(R.id.txtPrice);
        EditText txtAmount = dialogView.findViewById(R.id.txtAmount);

        txtName.setText(itemNameList.get(position));
        txtPrice.setText(dotFormat.format(itemPriceList.get(position)));
        txtAmount.setText(dotFormat.format(itemQtyList.get(position)));

        txtPrice.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                int number = 0;
                String txtNumber = ((EditText) Objects.requireNonNull(txtPrice)).getText().toString().trim().replace(".", "");
                if (!txtNumber.isEmpty()) {
                    if (txtNumber.length() > 6) {
                        txtNumber = txtNumber.substring(0, txtNumber.length() - 1);
                    }
                    number = Integer.parseInt(txtNumber);
                }
                txtPrice.removeTextChangedListener(this);
                if (number != 0) {
                    try {
                        txtPrice.setText(dotFormat.format((long) number));
                        txtPrice.setSelection(txtPrice.getText().length());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
                txtPrice.addTextChangedListener(this);
            }
        });
        txtAmount.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                int number = 0;
                String txtNumber = ((EditText) Objects.requireNonNull(txtAmount)).getText().toString().trim().replace(".", "");
                if (!txtNumber.isEmpty()) {
                    if (txtNumber.length() > 6) {
                        txtNumber = txtNumber.substring(0, txtNumber.length() - 1);
                    }
                    number = Integer.parseInt(txtNumber);
                }
                txtAmount.removeTextChangedListener(this);
                if (number != 0) {
                    try {
                        txtAmount.setText(dotFormat.format((long) number));
                        txtAmount.setSelection(txtAmount.getText().length());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
                txtAmount.addTextChangedListener(this);
            }
        });

        aDialog.setPositiveButton("Simpan", (dialog, which) -> {

            String tempStrAmount = txtAmount.getText().toString().trim().replace(".", "");
            String tempStrPrice = txtPrice.getText().toString().trim().replace(".", "");

            if (tempStrAmount.isEmpty()) {
                Toast.makeText(getActivity(), "Isi jumlah barang", Toast.LENGTH_SHORT).show();
            }else if(tempStrPrice.isEmpty()){
                Toast.makeText(getActivity(), "Isi harga beli barang", Toast.LENGTH_SHORT).show();
            }else{
                int totalPriceBefore = itemPriceList.get(position) * itemQtyList.get(position);

                itemPriceList.set(position, Integer.parseInt(tempStrPrice));
                itemQtyList.set(position, Integer.parseInt(tempStrAmount));

                int totalPriceAfter = itemPriceList.get(position) * itemQtyList.get(position);

                updateAdapter();
                grandTotalPrice = grandTotalPrice - totalPriceBefore + totalPriceAfter;
                btnPay.setText("Bayar " + rpFormat.format(grandTotalPrice));
            }
        });
        aDialog.setNegativeButton("Hapus", (dialog, which) -> {
            plainList.remove(position);
            updateAdapter();
            grandTotalPrice -= (itemPriceList.get(position) * itemQtyList.get(position));
            btnPay.setText("Bayar " + rpFormat.format(grandTotalPrice));
        });
        aDialog.show();
    }

    private void updateAdapter(){
        for (int i = 0; i < itemIdList.size(); i++) {
            int totalPrice = itemPriceList.get(i) * itemQtyList.get(i);

            HashMap<String, String> item = new HashMap<>();
            item.put("name", itemNameList.get(i));
            item.put("price", rpFormat.format(itemPriceList.get(i)));
            item.put("qty", dotFormat.format(itemQtyList.get(i)));
            item.put("totalPrice", rpFormat.format((double) totalPrice));
            plainList.add(item);
        }

        ListAdapter adapter = new SimpleAdapter(
                getContext(), plainList, R.layout.item_purchase,
                new String[]{"name","price","amount","totalPrice"},
                new int[]{R.id.name,R.id.price,R.id.amount,R.id.totalPrice});

        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(view == btnDate){
            datePickerDialog = new DatePickerDialog(requireActivity(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        posYear = year;
                        posMonth = monthOfYear;
                        posDate = dayOfMonth;
                        date = posDate + "-" + (posMonth+1) + "-" + posYear;
                        btnDate.setText(date);
                    }, posYear, posMonth, posDate);
            datePickerDialog.show();
        }else if(view == btnAddProduct){
            startActivityForResult(new Intent(getActivity(), ProductListActivity.class), INTENT_ADD_PRODUCT);
        }else if(view == btnPay){
            if(grandTotalPrice == 0){
                Toast.makeText(getActivity(),"Barang yang akan dibeli harus dipilih",Toast.LENGTH_LONG).show();
            }else{
                String name = txtName.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();

                Intent i = new Intent(getActivity(), PaymentActivity.class);
                i.putExtra("name", name);
                i.putExtra("phone", phone);
                i.putExtra("purchaseDate",posDate);
                i.putExtra("purchaseMonth",posMonth);
                i.putExtra("purchaseYear",posYear);
                i.putExtra("gTP", grandTotalPrice);
                i.putIntegerArrayListExtra("idList", itemIdList);
                i.putIntegerArrayListExtra("priceList", itemPriceList);
                i.putIntegerArrayListExtra("qtyList", itemQtyList);

                startActivityForResult(i, INTENT_PAY);
            }
        }
    }
}
