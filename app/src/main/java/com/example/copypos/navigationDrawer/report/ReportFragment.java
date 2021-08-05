package com.example.copypos.navigationDrawer.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.copypos.NonScrollListView;
import com.example.copypos.R;
import com.example.copypos.activity.BestSellerListActivity;
import com.example.copypos.contractView.ReportView;
import com.example.copypos.controller.ReportController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Product;
import com.example.copypos.model.Report;
import com.example.copypos.model.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment implements ReportView, View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 100;
    Button btnBestSellerFCService;
    Button btnBestSellerPrintService;
    Button btnBestSellerProduct;
    Button btnBestSellerService;
    Button btnPeriode;
    ListAdapter FCAdapter;
    ListAdapter printAdapter;
    ListAdapter productAdapter;
    ListAdapter serviceAdapter;
    NonScrollListView lvFC;
    NonScrollListView lvPrint;
    NonScrollListView lvProduct;
    NonScrollListView lvService;
    TextView lblGrossProfit;
    TextView lblNetProfit;
    TextView lblTotalCredit;
    TextView lblTotalDebt;
    TextView lblTotalExpense;
    TextView lblTotalPurchase;
    TextView lblTotalSale;
    ProgressDialog pDialog;

    ReportController rc;
    SharedPrefController spc;

    Calendar cale = Calendar.getInstance();
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);
    int periodeTypeId;
    int posDate = cale.get(Calendar.DAY_OF_MONTH);
    int posMonth = cale.get(Calendar.MONTH);
    int posYear = cale.get(Calendar.YEAR);
    private Integer[] yearNumber = new Integer[201];
    String periode;
    String reportType;
    private String[] monthName = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    private String[] reportTypeLongName = {"Penjualan", "Pembelian", "Penjualan Barang", "Penjualan Print", "Penjualan Fotokopi", "Penjualan Layanan", "Biaya Pengeluaran", "Laba Rugi", "Pembayaran Utang", "Pembayaran Piutang"};
    /* access modifiers changed from: private */
    private String[] reportTypeShortName = {"sales", "purchases", "product_sales", "print_sales", "photocopy_sales", "service_sales", "expenses", "overview", "debt", "credit"};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        btnPeriode = root.findViewById(R.id.btn_timePeriode);
        btnBestSellerProduct = root.findViewById(R.id.btn_bestSellerProduct);
        btnBestSellerPrintService = root.findViewById(R.id.btn_bestSellerPrintService);
        btnBestSellerFCService = root.findViewById(R.id.btn_bestSellerPhotocopyService);
        btnBestSellerService = root.findViewById(R.id.btn_bestSellerService);
        btnPeriode.setOnClickListener(this);
        btnBestSellerProduct.setOnClickListener(this);
        btnBestSellerPrintService.setOnClickListener(this);
        btnBestSellerFCService.setOnClickListener(this);
        btnBestSellerService.setOnClickListener(this);

        lblTotalSale = root.findViewById(R.id.lbl_totalSale);
        lblTotalPurchase = root.findViewById(R.id.lbl_totalPurchase);
        lblGrossProfit = root.findViewById(R.id.lbl_grossProfit);
        lblTotalExpense = root.findViewById(R.id.lbl_totalExpense);
        lblNetProfit = root.findViewById(R.id.lbl_netProfit);
        lblTotalDebt = root.findViewById(R.id.lbl_totalDebt);
        lblTotalCredit = root.findViewById(R.id.lbl_totalCredit);

        //layout sold item
        lvProduct = root.findViewById(R.id.lv_ns_product);
        lvPrint = root.findViewById(R.id.lv_ns_printService);
        lvFC = root.findViewById(R.id.lv_ns_photocopyService);
        lvService = root.findViewById(R.id.lv_ns_service);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rc = new ReportController(this);
        spc = new SharedPrefController(requireActivity());
        periode = posYear + "-" + (posMonth + 1);
        Button button = btnPeriode;
        button.setText(monthName[posMonth] + " " + posYear);
        rc.getSummary(spc.getSPBranchId(), 2, periode);
        for (int i = 0; i <= 200; i++) {
            yearNumber[i] = i + 1900;
        }
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.export, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_export) {
            return false;
        }
        inputDialog(1);
        return true;
    }

    public void onClick(View v) {
        if (v == btnPeriode) {
            inputDialog(0);
        }else if(v == btnBestSellerProduct){
            Intent i = new Intent(getActivity(), BestSellerListActivity.class);
            i.putExtra("branchId", spc.getSPBranchId());
            i.putExtra("periodeType", periodeTypeId);
            i.putExtra("periode", periode);
            i.putExtra("itemType",1);
            startActivity(i);
        }else if(v == btnBestSellerPrintService){
            Intent i = new Intent(getActivity(), BestSellerListActivity.class);
            i.putExtra("branchId", spc.getSPBranchId());
            i.putExtra("periodeType", periodeTypeId);
            i.putExtra("periode", periode);
            i.putExtra("itemType",2);
            startActivity(i);
        }else if(v == btnBestSellerFCService){
            Intent i = new Intent(getActivity(), BestSellerListActivity.class);
            i.putExtra("branchId", spc.getSPBranchId());
            i.putExtra("periodeType", periodeTypeId);
            i.putExtra("periode", periode);
            i.putExtra("itemType",3);
            startActivity(i);
        }else if(v == btnBestSellerService){
            Intent i = new Intent(getActivity(), BestSellerListActivity.class);
            i.putExtra("branchId", spc.getSPBranchId());
            i.putExtra("periodeType", periodeTypeId);
            i.putExtra("periode", periode);
            i.putExtra("itemType",4);
            startActivity(i);
        }
    }

    private void inputDialog(int stateExport) {
        int i;
        int i2 = stateExport;
        AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_report_export, null);
        dialogView.setPaddingRelative(50, 50, 50, 0);
        aDialog.setView(dialogView);
        LinearLayout layoutMontYear = dialogView.findViewById(R.id.layoutMonthYear);
        LinearLayout layoutReportType = dialogView.findViewById(R.id.layoutReportType);
        RadioGroup rdPeriodeType = dialogView.findViewById(R.id.rdPeriodType);
        Button btnDate = dialogView.findViewById(R.id.btnDate);
        Spinner spinMonth = dialogView.findViewById(R.id.spinMonth);
        Spinner spinYear = dialogView.findViewById(R.id.spinYear);
        Spinner spinReportType = dialogView.findViewById(R.id.spinReportType);
        ArrayAdapter<String> adapterReport = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, reportTypeLongName);
        adapterReport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, monthName);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinReportType.setAdapter(adapterReport);
        spinMonth.setAdapter(adapterMonth);
        spinYear.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, yearNumber));
        spinMonth.setSelection(posMonth);
        spinYear.setSelection(posYear - 1900);
        if (i2 == 1) {
            aDialog.setTitle("Ekspor Laporan");
            i = 0;
            layoutReportType.setVisibility(View.VISIBLE);
        } else {
            i = 0;
            aDialog.setTitle("Ubah Periode Laporan");
            layoutReportType.setVisibility(View.GONE);
        }
        btnDate.setVisibility(i);
        StringBuilder sb = new StringBuilder();
        sb.append(posDate);
        sb.append(" - ");
        sb.append(posMonth + 1);
        sb.append(" - ");
        sb.append(posYear);
        btnDate.setText(sb.toString());
        periodeTypeId = 1;
        rdPeriodeType.check(R.id.rdBtnDate);
        rdPeriodeType.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.rdBtnDate) {
                btnDate.setVisibility(View.VISIBLE);
                layoutMontYear.setVisibility(View.GONE);
                btnDate.setText(posDate + " - " + (posMonth + 1) + " - " + posYear);
                periodeTypeId = 1;
            }else{
                btnDate.setVisibility(View.GONE);
                layoutMontYear.setVisibility(View.VISIBLE);
                periodeTypeId = 2;
            }
        });
        btnDate.setOnClickListener(view1 -> new DatePickerDialog(requireActivity(), (datePicker, year, monthOfYear, dayOfMonth) -> {
            posYear = year;
            posMonth = monthOfYear;
            posDate = dayOfMonth;
            btnDate.setText(posDate + " - " + (posMonth + 1) + " - " + posYear);
        }, posYear, posMonth, posDate).show());
        spinReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                reportType = reportTypeShortName[position];
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                reportType = reportTypeShortName[0];
            }
        });
        spinMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                posMonth = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(posMonth);
            }
        });
        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                posYear = position + 1900;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(posYear - 1900);
            }
        });
        aDialog.setNegativeButton("Batal", ((dialog, i1) -> {}));
        if (i2 == 1) {
            aDialog.setPositiveButton("Ekspor", (dialog, i13) -> {
                if (periodeTypeId == 1) {
                    periode = posYear + "-" + (posMonth + 1) + "-" + posDate;
                } else {
                    periode = posYear + "-" + (posMonth + 1);
                }
                if (checkPermission()) {
                    rc.printReport(spc.getSPBranchId(), periodeTypeId, periode, reportType);
                } else {
                    requestPermission();
                }
                dialog.dismiss();
            });
        } else {
            aDialog.setPositiveButton("Simpan", (dialog, i1) -> {
                if (periodeTypeId == 1) {
                    periode = posYear + "-" + (posMonth + 1) + "-" + posDate;
                    Button button = btnPeriode;
                    button.setText(posDate + " - " + (posMonth + 1) + " - " + posYear);
                } else {
                    periode = posYear + "-" + (posMonth + 1);
                    Button button2 = btnPeriode;
                    button2.setText(monthName[posMonth] + " " + posYear);
                }
                rc.getSummary(spc.getSPBranchId(), periodeTypeId, periode);
                dialog.dismiss();
            });
        }
        aDialog.show();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        Toast.makeText(getActivity(), "Sentuh sekali lagi ikon di pojok kanan atas layar untuk menyimpan laporan", Toast.LENGTH_LONG).show();
        return false;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
    }

    public void onSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        pDialog.show();
    }

    public void hideProgress() {
        pDialog.hide();
    }

    public void onGetSummaryResult(Report result) {
        int grossProfit = result.getTotalSale() - result.getTotalPurchase();
        int netProfit = grossProfit - result.getTotalExpense();
        ArrayList<HashMap<String, String>> productList = new ArrayList<>();
        ArrayList<HashMap<String, String>> printList = new ArrayList<>();
        ArrayList<HashMap<String, String>> FCList = new ArrayList<>();
        ArrayList<HashMap<String, String>> serviceList = new ArrayList<>();
        List<Product> products = result.getProducts();
        List<Service> printServices = result.getPrintServices();
        List<Service> FCServices = result.getFCServices();
        List<Service> services = result.getServices();
        lblTotalSale.setText(rpFormat.format( result.getTotalSale()));
        lblTotalPurchase.setText(rpFormat.format(result.getTotalPurchase()));
        lblGrossProfit.setText(rpFormat.format(grossProfit));
        lblTotalExpense.setText(rpFormat.format(result.getTotalExpense()));
        lblNetProfit.setText(rpFormat.format(netProfit));
        lblTotalDebt.setText(rpFormat.format(result.getTotalDebt()));
        lblTotalCredit.setText(rpFormat.format(result.getTotalCredit()));

        int i = 0;
        while (i < products.size()) {
            HashMap<String, String> product = new HashMap<>();
            product.put("name", products.get(i).getName());
            StringBuilder sb = new StringBuilder();
            int grossProfit2 = grossProfit;
            sb.append(dotFormat.format((long) products.get(i).getStock()));
            sb.append(" barang");
            product.put("qty", sb.toString());
            productList.add(product);
            i++;
            netProfit = netProfit;
            grossProfit = grossProfit2;
        }
        int i2 = netProfit;
        List<Product> list = products;
        String str = "name";
        String str2 = "qty";
        List<Service> services2 = services;
        productAdapter = new SimpleAdapter(getContext(), productList, R.layout.item_best_seller, new String[]{"name", "qty"}, new int[]{R.id.name, R.id.qty});
        for (int i3 = 0; i3 < printServices.size(); i3++) {
            HashMap<String, String> print = new HashMap<>();
            print.put(str, printServices.get(i3).getName());
            print.put(str2, dotFormat.format((long) printServices.get(i3).getAmount()) + " lembar");
            printList.add(print);
        }
        String str3 = " lembar";
        String[] strArr = {str, str2};
        List<Service> FCServices2 = FCServices;
        printAdapter = new SimpleAdapter(getContext(), printList, R.layout.item_best_seller, strArr, new int[]{R.id.name, R.id.qty});
        for (int i4 = 0; i4 < FCServices2.size(); i4++) {
            HashMap<String, String> photocopy = new HashMap<>();
            photocopy.put(str, FCServices2.get(i4).getName());
            photocopy.put(str2, dotFormat.format((long) FCServices2.get(i4).getAmount()) + str3);
            FCList.add(photocopy);
        }
        FCAdapter = new SimpleAdapter(getContext(), FCList, R.layout.item_best_seller, new String[]{str, str2}, new int[]{R.id.name, R.id.qty});
        for (int i5 = 0; i5 < services2.size(); i5++) {
            HashMap<String, String> service = new HashMap<>();
            List<Service> services3 = services2;
            service.put(str, services3.get(i5).getName());
            service.put(str2, dotFormat.format((long) services3.get(i5).getAmount()) + " kali");
            serviceList.add(service);
        }
        String[] strArr2 = {str, str2};
        List<Service> list2 = services2;
        serviceAdapter = new SimpleAdapter(getContext(), serviceList, R.layout.item_best_seller, strArr2, new int[]{R.id.name, R.id.qty});
        lvProduct.setAdapter(productAdapter);
        lvPrint.setAdapter(printAdapter);
        lvFC.setAdapter(FCAdapter);
        lvService.setAdapter(serviceAdapter);
    }
}
