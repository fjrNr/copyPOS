package com.example.copypos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.copypos.model.Expense;
import com.example.copypos.R;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.RecyclerViewAdapter> {
    private Context context;
    private List<Expense> expenses;
    private ItemClickListener itemClickListener;
    private ItemClickListener itemClickListener2;
    Locale localeID = new Locale("in", "ID");
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(this.localeID);

    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public ExpenseListAdapter(Context context2, List<Expense> expenses2, ItemClickListener itemClickListener3, ItemClickListener itemClickListener22) {
        this.context = context2;
        this.expenses = expenses2;
        this.itemClickListener = itemClickListener3;
        this.itemClickListener2 = itemClickListener22;
    }

    public RecyclerViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewAdapter(LayoutInflater.from(this.context).inflate(R.layout.item_expense, parent, false), this.itemClickListener, this.itemClickListener2);
    }

    public void onBindViewHolder(RecyclerViewAdapter holder, int position) {
        Expense expense = this.expenses.get(position);
        String date = expense.getDate();
        holder.tv_name.setText(expense.getName());
        holder.tv_date.setText(date.substring(8, 10) + "-" + date.substring(5,7) + "-" + date.substring(0,4));
        holder.tv_amount.setText(this.rpFormat.format((long) expense.getAmount()));
    }

    public int getItemCount() {
        return this.expenses.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton btnDelete;
        View item;
        ItemClickListener itemClickListener;
        ItemClickListener itemClickListener2;
        TextView tv_amount;
        TextView tv_date;
        TextView tv_name;

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener3, ItemClickListener itemClickListener22) {
            super(itemView);
            this.tv_name = itemView.findViewById(R.id.name);
            this.tv_amount = itemView.findViewById(R.id.amount);
            this.tv_date = itemView.findViewById(R.id.date);
            this.item = itemView.findViewById(R.id.list_item);
            this.btnDelete = itemView.findViewById(R.id.btn_delete);
            this.itemClickListener = itemClickListener3;
            this.itemClickListener2 = itemClickListener22;
            this.item.setOnClickListener(this);
            this.btnDelete.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (v == this.item) {
                this.itemClickListener.onItemClick(v, getAdapterPosition());
            } else if (v == this.btnDelete) {
                this.itemClickListener2.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
