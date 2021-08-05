package com.example.copypos.navigationDrawer.services;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.R;
import com.example.copypos.adapter.ServiceListAdapter;
import com.example.copypos.contractView.ProductListView;
import com.example.copypos.contractView.ServiceListView;
import com.example.copypos.controller.ProductController;
import com.example.copypos.controller.ServiceController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Product;
import com.example.copypos.model.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServicesFragment extends Fragment implements ServiceListView, ProductListView, AdapterView.OnItemSelectedListener {
    ConcatAdapter concatAdapter;
    ServiceListAdapter adapterFc, adapterFcS;
    ServiceListAdapter.ItemClickListener itemClickListenerFc, itemClickListenerFcS, itemClickListenerFc2, itemClickListenerFc2S;
    ServiceListAdapter adapterPrint, adapterPrintS;
    ServiceListAdapter.ItemClickListener itemClickListenerPrint, itemClickListenerPrintS, itemClickListenerPrint2, itemClickListenerPrint2S;
    ProgressDialog pDialog;
    ServiceListAdapter adapterOther, adapterOtherS;
    ServiceListAdapter.ItemClickListener itemClickListenerOther, itemClickListenerOtherS, itemClickListenerOther2, itemClickListenerOther2S;
    private Spinner spinServiceType;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerView;
    TextView txtView;

    ProductController pc;
    ServiceController psc;
    ServiceController fcsc;
    ServiceController sc;
    SharedPrefController spc;

    ArrayList<Integer> paperIdList = new ArrayList<>();
    ArrayList<String> paperNameList = new ArrayList<>();
    ArrayAdapter<String> serviceTypeAdapter;
    boolean isSearchService;
    int id;
    int paperId;
    int price;
    int stateServiceTypeEdit = 0;
    int stateServiceTypeView = 0;
    List<Service> fcService,fcServiceS;
    List<Service> printService,printServiceS;
    List<Service> otherService,otherServiceS;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    String key, name, paperName;
    private String[] serviceType = {"Print", "Fotokopi", "Lain - Lain"};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_services, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        spinServiceType = root.findViewById(R.id.spinType);
        swipeRefresh = root.findViewById(R.id.swipeRefresh);
        txtView = root.findViewById(R.id.txtView);

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spc = new SharedPrefController(requireActivity());
        pc = new ProductController(this);
        psc = new ServiceController(this);
        fcsc = new ServiceController(this);
        sc = new ServiceController(this);
        concatAdapter = new ConcatAdapter();
        serviceTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, serviceType);
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinServiceType.setAdapter(serviceTypeAdapter);
        spinServiceType.setOnItemSelectedListener(this);
        swipeRefresh.setOnRefreshListener(() -> {
            int i = stateServiceTypeView;
            if (i == 0) {
                psc.getAllPrint(spc.getSPBranchId(), "");
            } else if (i == 1) {
                fcsc.getAllPhotocopy(spc.getSPBranchId(), "");
            } else {
                sc.getAll(spc.getSPBranchId(), "");
            }
        });
        pc.getPapers(spc.getSPBranchId());
        itemClickListenerPrint = (view12, position) -> {
            id = printService.get(position).getId();
            paperId = printService.get(position).getPaperId();
            name = printService.get(position).getName();
            price = printService.get(position).getSellPrice();
            inputDialog(id, 0);
        };
        itemClickListenerPrintS = (view12, position) -> {
            id = printServiceS.get(position).getId();
            paperId = printServiceS.get(position).getPaperId();
            name = printServiceS.get(position).getName();
            price = printServiceS.get(position).getSellPrice();
            inputDialog(id, 0);
        };

        itemClickListenerPrint2 = (view13, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
            aDialog.setTitle("Hapus Layanan");
            aDialog.setMessage("Apakah anda yakin ingin menghapus layanan " + printService.get(position).getName() + "?");
            aDialog.setNegativeButton("Tidak", (dialogInterface, i) -> {});
            aDialog.setPositiveButton("Ya", (dialog, i) -> {
                psc.deletePrint(printService.get(position).getId());
                dialog.dismiss();
            });
            aDialog.show();
        };

        itemClickListenerPrint2S = (view13, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
            aDialog.setTitle("Hapus Layanan");
            aDialog.setMessage("Apakah anda yakin ingin menghapus layanan " + printServiceS.get(position).getName() + "?");
            aDialog.setNegativeButton("Tidak", (dialogInterface, i) -> {});
            aDialog.setPositiveButton("Ya", (dialog, i) -> {
                psc.deletePrint(printServiceS.get(position).getId());
                dialog.dismiss();
            });
            aDialog.show();
        };

        itemClickListenerFc = (view14, position) -> {
            id = fcService.get(position).getId();
            paperId = fcService.get(position).getPaperId();
            name = fcService.get(position).getName();
            price = fcService.get(position).getSellPrice();
            inputDialog(id, 1);
        };

        itemClickListenerFcS = (view14, position) -> {
            id = fcServiceS.get(position).getId();
            paperId = fcServiceS.get(position).getPaperId();
            name = fcServiceS.get(position).getName();
            price = fcServiceS.get(position).getSellPrice();
            inputDialog(id, 1);
        };

        itemClickListenerFc2 = (view16, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
            aDialog.setTitle("Hapus Layanan");
            aDialog.setMessage("Apakah anda yakin ingin menghapus layanan " + fcService.get(position).getName() + "?");
            aDialog.setNegativeButton("Tidak", (dialogInterface, i) -> {});
            aDialog.setPositiveButton("Ya", (dialog, i) -> {
                fcsc.deletePhotocopy(fcService.get(position).getId());
                dialog.dismiss();
            });
            aDialog.show();
        };

        itemClickListenerFc2S = (view16, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
            aDialog.setTitle("Hapus Layanan");
            aDialog.setMessage("Apakah anda yakin ingin menghapus layanan " + fcServiceS.get(position).getName() + "?");
            aDialog.setNegativeButton("Tidak", (dialogInterface, i) -> {});
            aDialog.setPositiveButton("Ya", (dialog, i) -> {
                fcsc.deletePhotocopy(fcServiceS.get(position).getId());
                dialog.dismiss();
            });
            aDialog.show();
        };

        itemClickListenerOther = (view15, position) -> {
            id = otherService.get(position).getId();
            name = otherService.get(position).getName();
            price = otherService.get(position).getSellPrice();
            inputDialog(id, 2);
        };

        itemClickListenerOtherS = (view15, position) -> {
            id = otherServiceS.get(position).getId();
            name = otherServiceS.get(position).getName();
            price = otherServiceS.get(position).getSellPrice();
            inputDialog(id, 2);
        };

        itemClickListenerOther2 = (view1, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
            aDialog.setTitle("Hapus Layanan");
            aDialog.setMessage("Apakah anda yakin ingin menghapus layanan " + otherService.get(position).getName() + "?");
            aDialog.setNegativeButton("Tidak", (dialogInterface, i) -> {});
            aDialog.setPositiveButton("Ya", (dialog, i) -> {
                sc.delete(otherService.get(position).getId());
                dialog.dismiss();
            });
            aDialog.show();
        };

        itemClickListenerOther2S = (view1, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
            aDialog.setTitle("Hapus Layanan");
            aDialog.setMessage("Apakah anda yakin ingin menghapus layanan " + otherServiceS.get(position).getName() + "?");
            aDialog.setNegativeButton("Tidak", (dialogInterface, i) -> {});
            aDialog.setPositiveButton("Ya", (dialog, i) -> {
                sc.delete(otherServiceS.get(position).getId());
                dialog.dismiss();
            });
            aDialog.show();
        };

        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.editor_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Cari...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                key = query;
                psc.getAllPrint(spc.getSPBranchId(), key);
                recyclerView.setVisibility(View.GONE);
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
                txtView.setVisibility(View.GONE);
                spinServiceType.setVisibility(View.GONE);
                isSearchService = true;
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                txtView.setVisibility(View.VISIBLE);
                spinServiceType.setVisibility(View.VISIBLE);
                isSearchService = false;
                keepShowSameRecyclerView();
                recyclerView.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            inputDialog(0, 0);
            return true;
        }else if (item.getItemId() == R.id.action_search) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void inputDialog(int id, int typeId) {
        AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_service, null);
        dialogView.setPaddingRelative(50, 50, 50, 0);
        aDialog.setTitle("Tambah Layanan");
        aDialog.setView(dialogView);
        TextView lblPaperType = dialogView.findViewById(R.id.lbl_paperType);
        Spinner spinPaperType = dialogView.findViewById(R.id.spinPaperType);
        EditText txtName = dialogView.findViewById(R.id.txtName);
        EditText txtPrice = dialogView.findViewById(R.id.txtPrice);
        txtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number = 0;
                String txtNumber = txtPrice.getText().toString().trim().replace(".", "");
                if (!txtNumber.isEmpty()) {
                    if (txtNumber.length() > 9) {
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
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, paperNameList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPaperType.setAdapter(arrayAdapter);
        spinPaperType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                paperName = paperNameList.get(position);
                paperId = paperIdList.get(position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                paperName = paperNameList.get(0);
                paperId = paperIdList.get(0);
            }
        });
        RadioGroup rdGroupType = dialogView.findViewById(R.id.rdGroupType);
        rdGroupType.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.rdBtnPrint || checkedId == R.id.rdBtnPhotocopy) {
                lblPaperType.setVisibility(View.VISIBLE);
                spinPaperType.setVisibility(View.VISIBLE);
            }else{
                lblPaperType.setVisibility(View.GONE);
                spinPaperType.setVisibility(View.GONE);
            }
        });
        if (id != 0) {
            aDialog.setTitle("Edit Layanan");
            txtName.setText(name);
            txtPrice.setText(dotFormat.format(price));
            if (typeId == 0 || typeId == 1) {
                if (typeId == 0) {
                    rdGroupType.check(R.id.rdBtnPrint);
                } else {
                    rdGroupType.check(R.id.rdBtnPhotocopy);
                }
                lblPaperType.setVisibility(View.VISIBLE);
                spinPaperType.setVisibility(View.VISIBLE);

            } else {
                rdGroupType.check(R.id.rdBtnOther);
                lblPaperType.setVisibility(View.GONE);
                spinPaperType.setVisibility(View.GONE);
            }
            for (int i2 = 0; i2 < rdGroupType.getChildCount(); i2++) {
                rdGroupType.getChildAt(i2).setEnabled(false);
                rdGroupType.getChildAt(i2).setClickable(false);
            }
        }
        aDialog.setNegativeButton("Batal", (dialogInterface, i1) -> {});
        aDialog.setPositiveButton("Simpan", (dialogInterface, i1) -> {

            String inName = txtName.getText().toString().trim();
            String inPrice = txtPrice.getText().toString().trim().replace(".", "");

            if (inName.isEmpty()) {
                Toast.makeText(getActivity(), "Isi nama layanan", Toast.LENGTH_SHORT).show();
            } else if (inPrice.isEmpty()) {
                Toast.makeText(getActivity(), "Isi harga jual layanan", Toast.LENGTH_SHORT).show();
            } else if (id != 0) {
                price = Integer.parseInt(inPrice);
                if (typeId == 0) {
                    psc.updatePrint(id, paperId, inName, price);
                } else if (typeId == 1) {
                    fcsc.updatePhotocopy(id, paperId, inName, price);
                } else {
                    sc.update(id, inName, price);
                }
                stateServiceTypeEdit = typeId;
            }else{
                price = Integer.parseInt(inPrice);
                int inTypeId;

                if (rdGroupType.getCheckedRadioButtonId() == R.id.rdBtnPrint) {
                    psc.createPrint(spc.getSPBranchId(), paperId, inName, price);
                    inTypeId = 0;
                } else if (rdGroupType.getCheckedRadioButtonId() == R.id.rdBtnPhotocopy) {
                    fcsc.createPhotocopy(spc.getSPBranchId(), paperId, inName, price);
                    inTypeId = 1;
                } else {
                    sc.create(spc.getSPBranchId(), inName, price);
                    inTypeId = 2;
                }
                stateServiceTypeEdit = inTypeId;
            }
        });
        aDialog.show();
    }

    public void showLoading() {
        swipeRefresh.setRefreshing(false);
        pDialog.show();
    }

    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
        pDialog.hide();
    }

    public void onGetResultProduct(List<Product> papers) {
        pDialog.hide();
        for (int i = 0; i < papers.size(); i++) {
            paperIdList.add(papers.get(i).getId());
            paperNameList.add(papers.get(i).getName());
        }
    }

    public void onGetResultPrintService(List<Service> services) {
        pDialog.hide();
        if(isSearchService){
            if(printServiceS != null){
                concatAdapter.removeAdapter(adapterPrintS);
            }

            adapterPrintS = new ServiceListAdapter(getContext(), services, itemClickListenerPrintS, itemClickListenerPrint2S);
            adapterPrintS.notifyDataSetChanged();
            printServiceS = services;

            concatAdapter.addAdapter(adapterPrintS);
            fcsc.getAllPhotocopy(spc.getSPBranchId(),key);
        }else{
            adapterPrint = new ServiceListAdapter(getContext(), services, itemClickListenerPrint, itemClickListenerPrint2);
            adapterPrint.notifyDataSetChanged();
            keepShowSameRecyclerView();
            printService = services;
        }
    }

    public void onGetResultPhotocopyService(List<Service> services) {
        pDialog.hide();
        if(isSearchService){
            if(fcServiceS != null){
                concatAdapter.removeAdapter(adapterFcS);
            }

            adapterFcS = new ServiceListAdapter(getContext(), services, itemClickListenerFcS, itemClickListenerFc2S);
            adapterFcS.notifyDataSetChanged();
            fcServiceS = services;

            concatAdapter.addAdapter(adapterFcS);
            sc.getAll(spc.getSPBranchId(), key);
        }else{
            adapterFc = new ServiceListAdapter(getContext(), services, itemClickListenerFc, itemClickListenerFc2);
            adapterFc.notifyDataSetChanged();
            keepShowSameRecyclerView();
            fcService = services;
        }
    }

    public void onGetResultService(List<Service> services) {
        pDialog.hide();

        if(isSearchService){
            if(otherServiceS != null){
                concatAdapter.removeAdapter(adapterOtherS);
            }

            adapterOtherS = new ServiceListAdapter(getContext(), services, itemClickListenerOtherS, itemClickListenerOther2S);
            adapterOtherS.notifyDataSetChanged();
            otherServiceS = services;

            concatAdapter.addAdapter(adapterOtherS);
            updateMergeAdapter();
        }else{
            adapterOther = new ServiceListAdapter(getContext(), services, itemClickListenerOther, itemClickListenerOther2);
            adapterOther.notifyDataSetChanged();
            keepShowSameRecyclerView();
            otherService = services;
        }
    }

    public void onErrorLoading(String message) {
        pDialog.hide();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        pDialog.show();
    }

    public void hideProgress() {
        pDialog.hide();
    }

    public void onSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        int i = stateServiceTypeEdit;
        if (i == 0) {
            psc.getAllPrint(spc.getSPBranchId(), "");
        } else if (i == 1) {
            fcsc.getAllPhotocopy(spc.getSPBranchId(), "");
        } else {
            sc.getAll(spc.getSPBranchId(), "");
        }
    }

    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id2) {
        long typeId2 = spinServiceType.getSelectedItemId();
        if (typeId2 == 0) {
            stateServiceTypeView = 0;
            if (printService == null) {
                psc.getAllPrint(spc.getSPBranchId(), "");
            } else {
                recyclerView.setAdapter(adapterPrint);
            }
        } else if (typeId2 == 1) {
            stateServiceTypeView = 1;
            if (fcService == null) {
                fcsc.getAllPhotocopy(spc.getSPBranchId(), "");
            } else {
                recyclerView.setAdapter(adapterFc);
            }
        } else {
            stateServiceTypeView = 2;
            if (otherService == null) {
                sc.getAll(spc.getSPBranchId(), "");
            } else {
                recyclerView.setAdapter(adapterOther);
            }
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void keepShowSameRecyclerView() {
        int i = stateServiceTypeView;
        if (i == 0) {
            recyclerView.setAdapter(adapterPrint);
        } else if (i == 1) {
            recyclerView.setAdapter(adapterFc);
        } else {
            recyclerView.setAdapter(adapterOther);
        }
    }

    public void updateMergeAdapter(){
        concatAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(concatAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), Integer.toString(concatAdapter.getItemCount()), Toast.LENGTH_LONG).show();
    }
}
