package com.example.copypos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.copypos.R;
import com.example.copypos.api.ApiClient;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.model.Branch;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Objects;

public class BranchListAdapter extends RecyclerView.Adapter<BranchListAdapter.RecyclerViewAdapter> {
    private List<Branch> branches;
    private Context context;
    private ItemClickListener itemClickListener;
    private ItemClickListener itemClickListener2;
    private ItemClickListener itemClickListener3;
    SharedPrefController spc;

    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public BranchListAdapter(Context context2, List<Branch> branches2, ItemClickListener itemClickListener4, ItemClickListener itemClickListener22, ItemClickListener itemClickListener32) {
        this.context = context2;
        this.branches = branches2;
        this.itemClickListener = itemClickListener4;
        this.itemClickListener2 = itemClickListener22;
        this.itemClickListener3 = itemClickListener32;
    }

    public RecyclerViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewAdapter(LayoutInflater.from(this.context).inflate(R.layout.item_branch, parent, false), this.itemClickListener, this.itemClickListener2, this.itemClickListener3);
    }

    public void onBindViewHolder(RecyclerViewAdapter holder, int position) {
        this.spc = new SharedPrefController(Objects.requireNonNull(this.context));
        Branch branch = this.branches.get(position);
        holder.tv_name.setText(branch.getName());
        holder.tv_address.setText(branch.getAddress());
        if (this.spc.getSPBranchId() == branch.getId()) {
            holder.btn_delete.setVisibility(View.GONE);
        }
        Picasso picasso = Picasso.get();
        picasso.load(ApiClient.BASE_URL_BRANCH_IMAGE + branch.getImageName()).fit().into(holder.imgView);
    }

    public int getItemCount() {
        return branches.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton btn_delete;
        ImageButton btn_edit;
        ImageView imgView;
        View item;
        ItemClickListener itemClickListener;
        ItemClickListener itemClickListener2;
        ItemClickListener itemClickListener3;
        TextView tv_address;
        TextView tv_name;

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener4, ItemClickListener itemClickListener22, ItemClickListener itemClickListener32) {
            super(itemView);
            this.tv_name = itemView.findViewById(R.id.name);
            this.tv_address = itemView.findViewById(R.id.address);
            this.imgView = itemView.findViewById(R.id.logo);
            this.item = itemView.findViewById(R.id.list_item);
            this.btn_edit = itemView.findViewById(R.id.btn_edit);
            this.btn_delete = itemView.findViewById(R.id.btn_delete);
            this.itemClickListener = itemClickListener4;
            this.itemClickListener2 = itemClickListener22;
            this.itemClickListener3 = itemClickListener32;
            this.item.setOnClickListener(this);
            this.btn_edit.setOnClickListener(this);
            this.btn_delete.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (v == this.item) {
                this.itemClickListener.onItemClick(v, getAdapterPosition());
            } else if (v == this.btn_edit) {
                this.itemClickListener2.onItemClick(v, getAdapterPosition());
            } else if (v == this.btn_delete) {
                this.itemClickListener3.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
