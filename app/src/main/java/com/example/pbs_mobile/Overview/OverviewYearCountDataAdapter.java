package com.example.pbs_mobile.Overview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbs_mobile.R;

import java.util.List;

public class OverviewYearCountDataAdapter extends RecyclerView.Adapter<OverviewYearCountDataAdapter.ViewHolder> {

    private List<OverviewYearCountDataModel> overviewYearCountDataModelList;
    private View view;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView year_textview, count_textview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.year_textview = itemView.findViewById(R.id.year_textview);
            this.count_textview = itemView.findViewById(R.id.count_textview);
        }
    }

    public OverviewYearCountDataAdapter(List<OverviewYearCountDataModel> overviewYearCountDataModelList) {
        this.overviewYearCountDataModelList = overviewYearCountDataModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_year_count_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OverviewYearCountDataModel overviewYearCountDataModel = overviewYearCountDataModelList.get(position);
        holder.year_textview.setText(overviewYearCountDataModel.getYear());
        holder.count_textview.setText(overviewYearCountDataModel.getCount());
    }

    @Override
    public int getItemCount() {
        return overviewYearCountDataModelList.size();
    }
}
