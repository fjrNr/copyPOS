package com.example.copypos.navigationDrawer.expenses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.copypos.R;
import com.example.copypos.adapter.ExpenseListAdapter;
import com.example.copypos.contractView.ExpenseView;
import com.example.copypos.controller.ExpenseController;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Expense;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpensesFragment extends Fragment implements ExpenseView{
    ExpenseListAdapter adapter;
    ExpenseListAdapter.ItemClickListener itemClickListener;
    ExpenseListAdapter.ItemClickListener itemClickListener2;
    ProgressDialog pDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    ExpenseController ec;
    SharedPrefController spc;

    Calendar cale = Calendar.getInstance();
    List<Expense> expense;
    Locale localeID = new Locale("in", "ID");
    int amount;
    int posDate = cale.get(Calendar.DAY_OF_MONTH);
    int posMonth = cale.get(Calendar.MONTH);
    int posYear = cale.get(Calendar.YEAR);
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(localeID);
    String date;
    String name;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_expenses, container, false);
        swipeRefresh = root.findViewById(R.id.swipeRefresh);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        swipeRefresh.setRefreshing(false);
        setHasOptionsMenu(true);
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spc = new SharedPrefController(requireActivity());
        ec = new ExpenseController(this);
        ec.getAll(spc.getSPBranchId(), "");
        swipeRefresh.setOnRefreshListener(() -> ec.getAll(spc.getSPBranchId(), ""));
        date = posYear + "-" + (posMonth + 1) + "-" + posDate;
        itemClickListener = (view1, position) -> {
            name = expense.get(position).getName();
            date = expense.get(position).getDate();
            amount = expense.get(position).getAmount();
            inputDialog(expense.get(position).getId());
        };
        itemClickListener2 = (view12, position) -> {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
            aDialog.setTitle("Hapus Layanan");
            aDialog.setMessage("Apakah anda yakin ingin menghapus " + expense.get(position).getName() + "?");
            aDialog.setPositiveButton("Ya", (dialog, i) -> {
                ec.delete(expense.get(position).getId());
                dialog.dismiss();
            });
            aDialog.setNegativeButton("Tidak", (dialog, i)->{});
            aDialog.show();
        };
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.editor_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Cari...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                ec.getAll(spc.getSPBranchId(), query);
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            inputDialog(0);
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void inputDialog(int id) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setPaddingRelative(50, 50, 50, 0);
        layout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
        aDialog.setTitle("Tambah Biaya Pengeluaran");
        TextView labelName = new TextView(getContext());
        labelName.setText("Nama biaya*");
        EditText txtName = new EditText(getContext());
        txtName.setInputType(1);
        txtName.setHint("Nama");
        TextView labelDate = new TextView(getContext());
        labelDate.setText("Tanggal bayar (dd-mm-yyyy)");
        Button btnDate = new Button(getContext());
        btnDate.setOnClickListener(view -> new DatePickerDialog(requireActivity(), (datePicker, year, monthOfYear, dayOfMonth) -> {
            posYear = year;
            posMonth = monthOfYear;
            posDate = dayOfMonth;
            btnDate.setText(posDate + "-" + (posMonth + 1) + "-" + posYear);
            date = posYear + "-" + (posMonth + 1) + "-" + posDate;
        }, posYear, posMonth, posDate).show());
        TextView labelAmount = new TextView(getContext());
        labelAmount.setText("Jumlah bayar (dalam Rupiah)*");
        EditText txtAmount = new EditText(getContext());
        txtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number = 0;
                String txtNumber = txtAmount.getText().toString().trim().replace(".", "");
                if (!txtNumber.isEmpty()) {
                    if (txtNumber.length() > 9) {
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
        txtAmount.setInputType(2);
        txtAmount.setHint("Jumlah (Rp)");

        if (id != 0) {
            aDialog.setTitle("Edit Biaya Pengeluaran");
            txtName.setText(name);
            String tempDate = date.trim().replace("-","");
            posYear = Integer.parseInt(tempDate.substring(0,4));
            posMonth = Integer.parseInt(tempDate.substring(4,6)) - 1;
            posDate = Integer.parseInt(tempDate.substring(6));
            txtAmount.setText(dotFormat.format(amount));
        }else{
            posDate = cale.get(Calendar.DAY_OF_MONTH);
            posMonth = cale.get(Calendar.MONTH);
            posYear = cale.get(Calendar.YEAR);
            date = posYear + "-" + (posMonth + 1) + "-" + posDate;
        }
        btnDate.setText(posDate + "-" + (posMonth+1) + "-" + posYear);

        layout.addView(labelName);
        layout.addView(txtName);
        layout.addView(labelDate);
        layout.addView(btnDate);
        layout.addView(labelAmount);
        layout.addView(txtAmount);
        aDialog.setView(layout);
        aDialog.setPositiveButton("Simpan", (dialogInterface, i) -> {
            String name2 = txtName.getText().toString().trim();
            String amount2 = txtAmount.getText().toString().trim().replace(".", "");
            if (name2.isEmpty()) {
                Toast.makeText(getActivity(), "Isi nama pengeluaran", Toast.LENGTH_SHORT).show();
            } else if (amount2.isEmpty()) {
                Toast.makeText(getActivity(), "Isi jumlah pengeluaran", Toast.LENGTH_SHORT).show();
            } else if (id != 0) {
                ec.update(id, name2, date, Integer.parseInt(amount2));
            } else {
                ec.create(spc.getSPBranchId(), name2, date, Integer.parseInt(amount2));
            }
        });
        aDialog.setNegativeButton("Batal", (dialogInterface, i) -> {});
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

    public void onGetResult(List<Expense> expenses) {
        pDialog.hide();
        adapter = new ExpenseListAdapter(getContext(), expenses, itemClickListener, itemClickListener2);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        expense = expenses;
    }

    public void onSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        ec.getAll(spc.getSPBranchId(), "");
    }

    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
