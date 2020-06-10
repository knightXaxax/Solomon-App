package com.example.pbs_mobile.ViewSalesOrders.SalesOrdersForToday;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.pbs_mobile.R;
import com.example.pbs_mobile.ViewSalesOrders.ViewSalesOrderDataModels;
import com.example.pbs_mobile.ViewSalesOrders.ViewSalesOrderDataModels.SalesOrdersForTodayCardviewDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SalesOrdersForTodayCardviewDataAdapter extends RecyclerView.Adapter<SalesOrdersForTodayCardviewDataAdapter.ViewHolder> implements SalesOrdersForTodayListviewDataAdapter.ListViewItemClickListener{

    private View view;
    private Context context;
    private List<SalesOrdersForTodayCardviewDataModel> salesOrdersForTodayCardviewDataModelList;
    private JSONArray salesOrdersForTodayListViewDataModelArrayListContainer;
    private CardviewItemClickListener cardviewItemClickListener;

    @Override
    public void onListViewItemClick(View view, int position) {
        ConstraintLayout salesOrdersForTodayListview = view.findViewById(R.id.sales_orders_for_today_cardview_listview_content);
        ConstraintLayout salesOrdersForTodayListviewBottomLayout = view.findViewById(R.id.sales_orders_for_today_list_view_bottom_layout);
        if (salesOrdersForTodayListviewBottomLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(salesOrdersForTodayListview, new AutoTransition());
            salesOrdersForTodayListviewBottomLayout.setVisibility(view.VISIBLE);
        } else {
            salesOrdersForTodayListviewBottomLayout.setVisibility(view.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView status_textview;
        private RecyclerView salesOrdersCardviewContentRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.status_textview = itemView.findViewById(R.id.status_textview_soft);
            this.salesOrdersCardviewContentRecyclerView = itemView.findViewById(R.id.sales_orders_for_today_cardview_content_recyclerview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (cardviewItemClickListener != null)
                cardviewItemClickListener.onCardviewItemClick(v, getAdapterPosition());
        }
    }

    public interface CardviewItemClickListener {
        void onCardviewItemClick(View view, int position);
    }

    public void setCardviewItemClickListener(CardviewItemClickListener cardviewItemClickListener) {
        this.cardviewItemClickListener = cardviewItemClickListener;
    }

    public SalesOrdersForTodayCardviewDataAdapter(List<SalesOrdersForTodayCardviewDataModel> salesOrdersForTodayCardviewDataModelList, JSONArray salesOrdersForTodayListViewDataModelArrayListContainer) {
        this.salesOrdersForTodayCardviewDataModelList = salesOrdersForTodayCardviewDataModelList;
        this.salesOrdersForTodayListViewDataModelArrayListContainer = salesOrdersForTodayListViewDataModelArrayListContainer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_orders_for_today_cardview, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalesOrdersForTodayCardviewDataModel salesOrdersForTodayCardviewDataModel = salesOrdersForTodayCardviewDataModelList.get(position);
        holder.status_textview.setText(salesOrdersForTodayCardviewDataModel.getStatus());

        if (holder.salesOrdersCardviewContentRecyclerView != null) {
            holder.salesOrdersCardviewContentRecyclerView.setHasFixedSize(true);

            LinearLayoutManager salesOrdersCardviewContentRecyclerViewLayoutManager = new LinearLayoutManager(context);
            holder.salesOrdersCardviewContentRecyclerView.setLayoutManager(salesOrdersCardviewContentRecyclerViewLayoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.salesOrdersCardviewContentRecyclerView.getContext(), salesOrdersCardviewContentRecyclerViewLayoutManager.getOrientation());
            holder.salesOrdersCardviewContentRecyclerView.addItemDecoration(dividerItemDecoration);

            try {
                JSONObject salesOrdersForTodayListViewDataModelArrayListContainerJson = (JSONObject) salesOrdersForTodayListViewDataModelArrayListContainer.get(position);
                SalesOrdersForTodayListviewDataAdapter salesOrdersForTodayListviewDataAdapter = new SalesOrdersForTodayListviewDataAdapter((List<ViewSalesOrderDataModels.SalesOrdersForTodayListViewDataModel>) salesOrdersForTodayListViewDataModelArrayListContainerJson.get("dataset"));
                salesOrdersForTodayListviewDataAdapter.setListViewItemClickListener(this);
                holder.salesOrdersCardviewContentRecyclerView.setAdapter(salesOrdersForTodayListviewDataAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return salesOrdersForTodayCardviewDataModelList.size();
    }
}
