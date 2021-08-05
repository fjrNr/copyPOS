package com.example.copypos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.copypos.model.Purchase;
import com.example.copypos.R;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PurchaseListAdapter extends RecyclerView.Adapter<PurchaseListAdapter.RecyclerViewAdapter> {
    private Context context;
    private ItemClickListener itemClickListener;
    Locale localeID = new Locale("in", "ID");
    private List<Purchase> purchases;
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(this.localeID);

    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public PurchaseListAdapter(Context context2, List<Purchase> purchases2, ItemClickListener itemClickListener2) {
        this.context = context2;
        this.purchases = purchases2;
        this.itemClickListener = itemClickListener2;
    }

    public RecyclerViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewAdapter(LayoutInflater.from(this.context).inflate(R.layout.item_transaction, parent, false), this.itemClickListener);
    }

    public void onBindViewHolder(RecyclerViewAdapter holder, int position) {
        Purchase purchase = this.purchases.get(position);
        String date = purchase.getDate();
        holder.tv_invoiceNo.setText(purchase.getInvoiceNo());
        holder.tv_gTP.setText(this.rpFormat.format((long) purchase.getTotalPrice()));
        holder.tv_date.setText(date.substring(8, 10) + "-" + date.substring(5,7) + "-" + date.substring(0,4));
        holder.tv_status.setText(purchase.getPaymentStatus());
    }

    public int getItemCount() {
        return this.purchases.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        View item;
        ItemClickListener itemClickListener;
        TextView tv_date;
        TextView tv_gTP;
        TextView tv_invoiceNo;
        TextView tv_status;

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener2) {
            super(itemView);
            this.tv_invoiceNo = (TextView) itemView.findViewById(R.id.lbl_invoiceNo);
            this.tv_gTP = (TextView) itemView.findViewById(R.id.lbl_grandTotal);
            this.tv_date = (TextView) itemView.findViewById(R.id.lbl_date);
            this.tv_status = (TextView) itemView.findViewById(R.id.lbl_payStatus);
            this.item = itemView.findViewById(R.id.list_item);
            this.itemClickListener = itemClickListener2;
            this.item.setOnClickListener(this);
        }

        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
