package com.example.copypos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.copypos.api.ApiClient;
import com.example.copypos.model.Product;
import com.example.copypos.R;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.RecyclerViewAdapter> {
    private Context context;
    private ItemClickListener itemClickListener;
    private ItemClickListener itemClickListener2;
    private ItemClickListener itemClickListener3;
    Locale localeID = new Locale("in", "ID");
    NumberFormat dotFormat = NumberFormat.getIntegerInstance(this.localeID);
    private List<Product> products;
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(this.localeID);

    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public ProductListAdapter(Context context2, List<Product> products2, ItemClickListener itemClickListener4) {
        this.context = context2;
        this.products = products2;
        this.itemClickListener = itemClickListener4;
    }

    public ProductListAdapter(Context context2, List<Product> products2, ItemClickListener itemClickListener4, ItemClickListener itemClickListener22) {
        this.context = context2;
        this.products = products2;
        this.itemClickListener = itemClickListener4;
        this.itemClickListener2 = itemClickListener22;
    }

    public ProductListAdapter(Context context2, List<Product> products2, ItemClickListener itemClickListener4, ItemClickListener itemClickListener22, ItemClickListener itemClickListener32) {
        this.context = context2;
        this.products = products2;
        this.itemClickListener = itemClickListener4;
        this.itemClickListener2 = itemClickListener22;
        this.itemClickListener3 = itemClickListener32;
    }

    public RecyclerViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_product, parent, false);
        if (this.itemClickListener2 == null && this.itemClickListener3 == null) {
            return new RecyclerViewAdapter(view, this.itemClickListener);
        }
        ItemClickListener itemClickListener4 = this.itemClickListener2;
        if (itemClickListener4 != null && this.itemClickListener3 == null) {
            return new RecyclerViewAdapter(view, this.itemClickListener, itemClickListener4);
        }
        return new RecyclerViewAdapter(view, this.itemClickListener, this.itemClickListener2, this.itemClickListener3);
    }

    public void onBindViewHolder(RecyclerViewAdapter holder, int position) {
        Product product = this.products.get(position);
        holder.tv_name.setText(product.getName());
        TextView textView = holder.tv_stock;
        textView.setText("Stock: " + this.dotFormat.format((long) product.getStock()));
        if (this.itemClickListener2 == null || this.itemClickListener3 != null) {
            holder.tv_price.setText(this.rpFormat.format((long) product.getSellPrice()));
        } else {
            holder.tv_price.setText(this.rpFormat.format((long) product.getPurchasePrice()));
        }
        Picasso picasso = Picasso.get();
        picasso.load(ApiClient.BASE_URL_PRODUCT_IMAGE + product.getImageName()).fit().into(holder.imgView);
    }

    public int getItemCount() {
        return this.products.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton bt_chart;
        ImageButton bt_del;
        ImageView imgView;
        View item;
        ItemClickListener itemClickListener;
        ItemClickListener itemClickListener2;
        ItemClickListener itemClickListener3;
        TextView tv_name;
        TextView tv_price;
        TextView tv_stock;

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener4) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(R.id.name);
            this.tv_stock = (TextView) itemView.findViewById(R.id.stock);
            this.tv_price = (TextView) itemView.findViewById(R.id.price);
            this.imgView = (ImageView) itemView.findViewById(R.id.image);
            this.item = itemView.findViewById(R.id.list_item);
            itemView.findViewById(R.id.btn_chart).setVisibility(View.GONE);
            itemView.findViewById(R.id.btn_delete).setVisibility(View.GONE);
            this.itemClickListener = itemClickListener4;
            this.item.setOnClickListener(this);
        }

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener4, ItemClickListener itemClickListener22) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(R.id.name);
            this.tv_stock = (TextView) itemView.findViewById(R.id.stock);
            this.tv_price = (TextView) itemView.findViewById(R.id.price);
            this.imgView = (ImageView) itemView.findViewById(R.id.image);
            this.item = itemView.findViewById(R.id.list_item);
            this.bt_chart = (ImageButton) itemView.findViewById(R.id.btn_chart);
            itemView.findViewById(R.id.btn_chart).setVisibility(View.GONE);
            itemView.findViewById(R.id.btn_delete).setVisibility(View.GONE);
            this.itemClickListener = itemClickListener4;
            this.itemClickListener2 = itemClickListener22;
            this.item.setOnClickListener(this);
            this.bt_chart.setOnClickListener(this);
        }

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener4, ItemClickListener itemClickListener22, ItemClickListener itemClickListener32) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(R.id.name);
            this.tv_stock = (TextView) itemView.findViewById(R.id.stock);
            this.tv_price = (TextView) itemView.findViewById(R.id.price);
            this.imgView = (ImageView) itemView.findViewById(R.id.image);
            this.item = itemView.findViewById(R.id.list_item);
            this.bt_chart = (ImageButton) itemView.findViewById(R.id.btn_chart);
            this.bt_del = (ImageButton) itemView.findViewById(R.id.btn_delete);
            this.itemClickListener = itemClickListener4;
            this.itemClickListener2 = itemClickListener22;
            this.itemClickListener3 = itemClickListener32;
            this.item.setOnClickListener(this);
            this.bt_chart.setOnClickListener(this);
            this.bt_del.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (v == this.item) {
                this.itemClickListener.onItemClick(v, getAdapterPosition());
            } else if (v == this.bt_chart) {
                this.itemClickListener2.onItemClick(v, getAdapterPosition());
            } else if (v == this.bt_del) {
                this.itemClickListener3.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
