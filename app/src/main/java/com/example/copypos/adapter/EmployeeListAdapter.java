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
import com.example.copypos.model.Employee;
import com.example.copypos.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.RecyclerViewAdapter> {
    private Context context;
    private List<Employee> employees;
    private ItemClickListener itemClickListener;
    private ItemClickListener itemClickListener2;

    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public EmployeeListAdapter(Context context2, List<Employee> employees2, ItemClickListener itemClickListener3, ItemClickListener itemClickListener22) {
        this.context = context2;
        this.employees = employees2;
        this.itemClickListener = itemClickListener3;
        this.itemClickListener2 = itemClickListener22;
    }

    public RecyclerViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewAdapter(LayoutInflater.from(this.context).inflate(R.layout.item_employee, parent, false), this.itemClickListener, this.itemClickListener2);
    }

    public void onBindViewHolder(RecyclerViewAdapter holder, int position) {
        Employee employee = this.employees.get(position);
        holder.tv_name.setText(employee.getName());
        holder.tv_phone.setText(employee.getPhone());
        Picasso picasso = Picasso.get();
        picasso.load(ApiClient.BASE_URL_EMPLOYEE_IMAGE + employee.getImageName()).fit().into(holder.imgView);
    }

    public int getItemCount() {
        return this.employees.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton bt_del;
        ImageView imgView;
        View item;
        ItemClickListener itemClickListener;
        ItemClickListener itemClickListener2;
        TextView tv_name;
        TextView tv_phone;

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener3, ItemClickListener itemClickListener22) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(R.id.name);
            this.tv_phone = (TextView) itemView.findViewById(R.id.phone);
            this.item = itemView.findViewById(R.id.list_item);
            this.imgView = (ImageView) itemView.findViewById(R.id.logo);
            this.bt_del = (ImageButton) itemView.findViewById(R.id.btn_delete);
            this.itemClickListener = itemClickListener3;
            this.itemClickListener2 = itemClickListener22;
            this.item.setOnClickListener(this);
            this.bt_del.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (v == this.item) {
                this.itemClickListener.onItemClick(v, getAdapterPosition());
            } else if (v == this.bt_del) {
                this.itemClickListener2.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
