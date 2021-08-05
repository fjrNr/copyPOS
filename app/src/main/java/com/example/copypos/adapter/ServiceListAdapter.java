package com.example.copypos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.copypos.model.Service;
import com.example.copypos.R;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.RecyclerViewAdapter> {
    private Context context;
    private ItemClickListener itemClickListener;
    private ItemClickListener itemClickListener2;
    Locale localeID = new Locale("in", "ID");
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(this.localeID);
    private List<Service> services;

    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public ServiceListAdapter(Context context2, List<Service> services2, ItemClickListener itemClickListener3) {
        this.context = context2;
        this.services = services2;
        this.itemClickListener = itemClickListener3;
    }

    public ServiceListAdapter(Context context2, List<Service> services2, ItemClickListener itemClickListener3, ItemClickListener itemClickListener22) {
        this.context = context2;
        this.services = services2;
        this.itemClickListener = itemClickListener3;
        this.itemClickListener2 = itemClickListener22;
    }

    public RecyclerViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_service, parent, false);
        ItemClickListener itemClickListener3 = this.itemClickListener2;
        if (itemClickListener3 == null) {
            return new RecyclerViewAdapter(view, this.itemClickListener);
        }
        return new RecyclerViewAdapter(view, this.itemClickListener, itemClickListener3);
    }

    public void onBindViewHolder(RecyclerViewAdapter holder, int position) {
        Service service = this.services.get(position);
        holder.tv_name.setText(service.getName());
        holder.tv_sellPrice.setText(this.rpFormat.format((long) service.getSellPrice()));
    }

    public int getItemCount() {
        return this.services.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton btnDelete;
        View item;
        ItemClickListener itemClickListener;
        ItemClickListener itemClickListener2;
        TextView tv_name;
        TextView tv_sellPrice;

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener3) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(R.id.name);
            this.tv_sellPrice = (TextView) itemView.findViewById(R.id.sellPrice);
            this.item = itemView.findViewById(R.id.list_item);
            itemView.findViewById(R.id.btn_delete).setVisibility(View.GONE);
            this.itemClickListener = itemClickListener3;
            this.item.setOnClickListener(this);
        }

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener3, ItemClickListener itemClickListener22) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(R.id.name);
            this.tv_sellPrice = (TextView) itemView.findViewById(R.id.sellPrice);
            this.item = itemView.findViewById(R.id.list_item);
            this.btnDelete = (ImageButton) itemView.findViewById(R.id.btn_delete);
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
