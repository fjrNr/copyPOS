package com.example.copypos.navigationDrawer.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.copypos.R;
import com.example.copypos.activity.ItemSaleDetailActivity;
import com.example.copypos.adapter.HomePagerAdapter;
import com.example.copypos.adapter.ProductListAdapter;
import com.example.copypos.adapter.ServiceListAdapter;
import com.example.copypos.contractView.ProductListView;
import com.example.copypos.contractView.ServiceListView;
import com.example.copypos.controller.ProductController;
import com.example.copypos.controller.ServiceController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Product;
import com.example.copypos.model.Service;
import com.google.android.material.tabs.TabLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener, ProductListView, ServiceListView {
    Button btnCart;
    HomePagerAdapter pagerAdapter;
    ListView listView;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ViewPager viewPager;
    TabLayout tabLayout;

    ConcatAdapter concatAdapter;
    ServiceListAdapter photocopyListAdapter;
    ServiceListAdapter.ItemClickListener photocopyItemClick;
    ServiceListAdapter printListAdapter;
    ServiceListAdapter.ItemClickListener printItemClick;
    ProductListAdapter productListAdapter;
    ProductListAdapter.ItemClickListener productItemClick;
    ServiceListAdapter serviceListAdapter;
    ServiceListAdapter.ItemClickListener serviceItemClick;

    ProductController pc;
    ServiceController sc;
    SharedPrefController spc;

    ArrayList<Integer> idItemList = new ArrayList<>();
    ArrayList<String> nameItemList = new ArrayList<>();
    ArrayList<Integer> priceItemList = new ArrayList<>();
    ArrayList<Integer> qtyItemList = new ArrayList<>();
    ArrayList<String> typeItemList = new ArrayList<>();
    public int id, price;
    int totalSale = 0;
    List<Service> photocopyService;
    List<Service> printService;
    List<Product> product;
    List<Service> service;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);
    String key;
    public String name, type;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btnCart = root.findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(this);
        listView = root.findViewById(R.id.listView);
        pagerAdapter = new HomePagerAdapter(getContext(), getChildFragmentManager());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading data");
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        concatAdapter = new ConcatAdapter();
        spc = new SharedPrefController(requireActivity());
        pc = new ProductController(this);
        sc = new ServiceController(this);

        productItemClick = (view1, position) -> {
            id = product.get(position).getId();
            price = product.get(position).getSellPrice();
            name = product.get(position).getName();
            type = "product";
            inputDialog();
            Toast.makeText(getActivity(), "ini produk", Toast.LENGTH_LONG).show();
        };
        printItemClick = (view1, position) -> {
            id = printService.get(position).getId();
            price = printService.get(position).getSellPrice();
            name = printService.get(position).getName();
            type = "print";
            inputDialog();
            Toast.makeText(getActivity(), "ini jasa print", Toast.LENGTH_LONG).show();
        };
        photocopyItemClick = (view1, position) -> {
            id = photocopyService.get(position).getId();
            price = photocopyService.get(position).getSellPrice();
            name = photocopyService.get(position).getName();
            type = "photocopy";
            inputDialog();
            Toast.makeText(getActivity(), "ini jasa fotokopi", Toast.LENGTH_LONG).show();
        };
        serviceItemClick = (view1, position) -> {
            id = service.get(position).getId();
            price = service.get(position).getSellPrice();
            name = service.get(position).getName();
            type = "otherService";
            inputDialog();
            Toast.makeText(getActivity(), "ini jasa lain", Toast.LENGTH_LONG).show();
        };
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.editor_menu, menu);
        menu.findItem(R.id.action_add).setVisible(false);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Cari...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                key = query;
                pc.getAll(spc.getSPBranchId(), key);
                viewPager.setVisibility(View.GONE);
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                tabLayout.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                recyclerView.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void inputDialog(){
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPaddingRelative(50, 50, 50, 0);
        AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
        aDialog.setTitle("Jual " + name);
        TextView labelName = new TextView(getContext());
        if(type.equals("product")){
            labelName.setText("Nama barang");
        }else{
            labelName.setText("Nama layanan");
        }
        layout.addView(labelName);
        EditText editTextName = new EditText(getContext());
        editTextName.setInputType(0);
        editTextName.setText(name);
        editTextName.setEnabled(false);
        layout.addView(editTextName);
        TextView labelPrice = new TextView(getContext());
        if(type.equals("product")){
            labelPrice.setText("Harga per barang (Rupiah)");
        }else if(type.equals("otherService")){
            labelPrice.setText("Harga per kali layanan (Rupiah)");
        }else{
            labelPrice.setText("Harga per lembar (Rupiah)");
        }

        layout.addView(labelPrice);
        final EditText editTextPrice = new EditText(getContext());
        editTextPrice.setInputType(2);
        editTextPrice.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        editTextPrice.setText(dotFormat.format((long) price));
        editTextPrice.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                int number = 0;
                String txtNumber = ((EditText) Objects.requireNonNull(editTextPrice)).getText().toString().trim().replace(".", "");
                if (!txtNumber.isEmpty()) {
                    if (txtNumber.length() > 6) {
                        txtNumber = txtNumber.substring(0, txtNumber.length() - 1);
                    }
                    number = Integer.parseInt(txtNumber);
                }
                editTextPrice.removeTextChangedListener(this);
                if (number != 0) {
                    try {
                        editTextPrice.setText(dotFormat.format((long) number));
                        editTextPrice.setSelection(editTextPrice.getText().length());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
                editTextPrice.addTextChangedListener(this);
            }
        });
        layout.addView(editTextPrice);
        TextView labelAmount = new TextView(getContext());
        if(type.equals("product")){
            labelAmount.setText("Jumlah barang");
        }else if(type.equals("otherService")){
            labelAmount.setText("Jumlah layanan");
        }else{
            labelAmount.setText("Jumlah lembar");
        }

        layout.addView(labelAmount);
        final EditText editTextAmount = new EditText(getContext());
        editTextAmount.setInputType(2);
        editTextAmount.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                int number = 0;
                String txtNumber = ((EditText) Objects.requireNonNull(editTextAmount)).getText().toString().trim().replace(".", "");
                if (!txtNumber.isEmpty()) {
                    if (txtNumber.length() > 6) {
                        txtNumber = txtNumber.substring(0, txtNumber.length() - 1);
                    }
                    number = Integer.parseInt(txtNumber);
                }
                editTextAmount.removeTextChangedListener(this);
                if (number != 0) {
                    try {
                        editTextAmount.setText(dotFormat.format((long) number));
                        editTextAmount.setSelection(editTextAmount.getText().length());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
                editTextAmount.addTextChangedListener(this);
            }
        });
        layout.addView(editTextAmount);
        aDialog.setView(layout);
        aDialog.setNegativeButton("Batal", ((dialogInterface, i) -> {}));
        aDialog.setPositiveButton("Tambah", (dialogInterface, i) -> {
            String price2 = editTextPrice.getText().toString().trim().replace(".", "");
            String amount = editTextAmount.getText().toString().trim().replace(".", "");
            if (price2.isEmpty()) {
                Toast.makeText(getActivity(), "Isi harga jual print", Toast.LENGTH_SHORT).show();
            } else if (amount.isEmpty()) {
                Toast.makeText(getActivity(), "Isi jumlah lembar", Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, String> item = new HashMap<>();
                item.put("type", type);
                item.put("id", Integer.toString(id));
                item.put("name", name);
                item.put("price", price2);
                item.put("qty", amount);
                updateCart(item);
            }
        });
        aDialog.show();
    }

    public void updateCart(HashMap<String, String> item){
        int sameItem = 0;
        int addNewItem = 0;
        int index = 0;
        int idItem = Integer.parseInt(Objects.requireNonNull(item.get("id")));
        int priceItem = Integer.parseInt(Objects.requireNonNull(item.get("price")));
        int amountItem = Integer.parseInt(Objects.requireNonNull(item.get("qty")));
        if (idItemList.size() > 0) {
            do {
                if (idItem == idItemList.get(index) && Objects.requireNonNull(item.get("type")).equals(typeItemList.get(index))) {
                    qtyItemList.set(index, qtyItemList.get(index) + amountItem);
                    sameItem = 1;
                }
                index++;
                if (index == idItemList.size()) {
                    addNewItem = 1;
                }
                if (sameItem != 0) {
                    break;
                }
            } while (addNewItem == 0);
        }
        if (sameItem == 0) {
            typeItemList.add(item.get("type"));
            idItemList.add(idItem);
            nameItemList.add(item.get("name"));
            priceItemList.add(priceItem);
            qtyItemList.add(amountItem);
        }
        totalSale += priceItem * amountItem;
        btnCart.setText(rpFormat.format(totalSale));
    }

    @Override
    public void onClick(View view) {
        if (totalSale > 0) {
            Intent i = new Intent(getActivity(), ItemSaleDetailActivity.class);
            i.putStringArrayListExtra("typeItemList", typeItemList);
            i.putIntegerArrayListExtra("idItemList", idItemList);
            i.putStringArrayListExtra("nameItemList", nameItemList);
            i.putIntegerArrayListExtra("priceItemList", priceItemList);
            i.putIntegerArrayListExtra("qtyItemList", qtyItemList);
            startActivity(i);
        }else{
            Toast.makeText(getActivity(), "Kerancang masih kososng, silakan pilih item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.hide();
    }

    @Override
    public void onSuccess(String paramString) {

    }

    @Override
    public void onErrorLoading(String paramString) {
        progressDialog.hide();
        Toast.makeText(getActivity(), "error loading", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetResultProduct(List<Product> products) {
        if(product != null){
            concatAdapter.removeAdapter(productListAdapter);
        }

        productListAdapter = new ProductListAdapter(getContext(), products, productItemClick);
        productListAdapter.notifyDataSetChanged();
        product = products;

        concatAdapter.addAdapter(productListAdapter);
        sc.getAllPrint(spc.getSPBranchId(), key);
    }

    @Override
    public void onGetResultPrintService(List<Service> paramList) {
        if(printService != null){
            concatAdapter.removeAdapter(printListAdapter);
        }

        printListAdapter = new ServiceListAdapter(getContext(), paramList, printItemClick);
        printListAdapter.notifyDataSetChanged();
        printService = paramList;

        concatAdapter.addAdapter(printListAdapter);
        sc.getAllPhotocopy(spc.getSPBranchId(), key);
    }

    @Override
    public void onGetResultPhotocopyService(List<Service> paramList) {
        if(photocopyService != null){
            concatAdapter.removeAdapter(photocopyListAdapter);
        }

        photocopyListAdapter = new ServiceListAdapter(getContext(), paramList, photocopyItemClick);
        photocopyListAdapter.notifyDataSetChanged();
        photocopyService = paramList;

        concatAdapter.addAdapter(photocopyListAdapter);
        sc.getAll(spc.getSPBranchId(), key);
    }

    @Override
    public void onGetResultService(List<Service> paramList) {
        if(service != null){
            concatAdapter.removeAdapter(serviceListAdapter);
        }

        serviceListAdapter = new ServiceListAdapter(getContext(), paramList, serviceItemClick);
        serviceListAdapter.notifyDataSetChanged();
        service = paramList;

        concatAdapter.addAdapter(serviceListAdapter);
        updateAdapter();
    }

    public void updateAdapter(){
        concatAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(concatAdapter);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
