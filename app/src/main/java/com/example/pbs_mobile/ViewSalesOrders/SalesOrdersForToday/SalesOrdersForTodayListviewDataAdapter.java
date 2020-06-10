package com.example.pbs_mobile.ViewSalesOrders.SalesOrdersForToday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbs_mobile.R;
import com.example.pbs_mobile.ViewSalesOrders.ViewSalesOrderDataModels.SalesOrdersForTodayListViewDataModel;

import java.util.List;

public class SalesOrdersForTodayListviewDataAdapter extends RecyclerView.Adapter<SalesOrdersForTodayListviewDataAdapter.ViewHolder> {

    private View view;
    private Context context;
    private List<SalesOrdersForTodayListViewDataModel> salesOrdersForTodayListViewDataModelList;
    private ListViewItemClickListener listViewItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView
                soft_so_number_textview,
                soft_customer_textview,
                soft_qty_textview,
                soft_part_textview,
                soft_line_textview,
                soft_isbn_textview,
                soft_description_textview,
                soft_unit_price_textview,
                soft_bin_number_textview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.soft_so_number_textview = itemView.findViewById(R.id.soft_so_number_textview);
            this.soft_customer_textview = itemView.findViewById(R.id.soft_customer_textview);
            this.soft_qty_textview = itemView.findViewById(R.id.soft_qty_textview);
            this.soft_part_textview = itemView.findViewById(R.id.soft_part_textview_soft);
            this.soft_line_textview = itemView.findViewById(R.id.soft_line_textview_soft);
            this.soft_isbn_textview = itemView.findViewById(R.id.isbn_textview_soft);
            this.soft_description_textview = itemView.findViewById(R.id.soft_description_textview_soft);
            this.soft_unit_price_textview = itemView.findViewById(R.id.soft_unit_price_textview_soft);
            this.soft_bin_number_textview = itemView.findViewById(R.id.soft_bin_number_textview_soft);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listViewItemClickListener != null)
                listViewItemClickListener.onListViewItemClick(v, getAdapterPosition());
        }
    }

    public interface ListViewItemClickListener {
        void onListViewItemClick(View view, int position);
    }

    public void setListViewItemClickListener(ListViewItemClickListener listViewItemClickListener) {
        this.listViewItemClickListener = listViewItemClickListener;
    }

    public SalesOrdersForTodayListviewDataAdapter(List<SalesOrdersForTodayListViewDataModel> salesOrdersForTodayListViewDataModelList) {
        this.salesOrdersForTodayListViewDataModelList = salesOrdersForTodayListViewDataModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_orders_for_today_list_view, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalesOrdersForTodayListViewDataModel salesOrdersForTodayListViewDataModel = salesOrdersForTodayListViewDataModelList.get(position);

        holder.soft_so_number_textview.setText(salesOrdersForTodayListViewDataModel.getSoNumber());
        holder.soft_customer_textview.setText(salesOrdersForTodayListViewDataModel.getCustomer());
        holder.soft_qty_textview.setText(salesOrdersForTodayListViewDataModel.getQty());
        holder.soft_part_textview.setText(salesOrdersForTodayListViewDataModel.getPart());
        holder.soft_line_textview.setText(salesOrdersForTodayListViewDataModel.getLine());
        holder.soft_isbn_textview.setText(salesOrdersForTodayListViewDataModel.getIsbn());
        holder.soft_description_textview.setText(salesOrdersForTodayListViewDataModel.getDescription());
        holder.soft_unit_price_textview.setText(salesOrdersForTodayListViewDataModel.getUnitPrice());
        holder.soft_bin_number_textview.setText(salesOrdersForTodayListViewDataModel.getBinNumber());
    }

    @Override
    public int getItemCount() {
        return salesOrdersForTodayListViewDataModelList.size();
    }
}
