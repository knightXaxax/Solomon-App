package com.example.pbs_mobile.Overview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbs_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class OverviewStatusDataAdapter extends RecyclerView.Adapter<OverviewStatusDataAdapter.ViewHolder> {

    private List<OverviewStatusDataModel> overviewStatusDataModelList;
    private List<OverviewYearCountDataModel> overviewYearCountDataModelList;
    private View view;
    private LinearLayoutManager overviewYearCountLayoutManager;
    private Context context;
    private JSONArray jsonArray;
    private ItemClickListener itemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView status_textview, count_percentage_textview;
        private ProgressBar progressBar;
        private RecyclerView year_count_recyclerview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.status_textview = itemView.findViewById(R.id.status_textview);
            this.count_percentage_textview = itemView.findViewById(R.id.count_percentage_textview);
            this.year_count_recyclerview = itemView.findViewById(R.id.year_count_recyclerview);
            this.progressBar = itemView.findViewById(R.id.progressbar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    void setClickListener (ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OverviewStatusDataAdapter(List<OverviewStatusDataModel> overviewStatusDataModelList, JSONArray jsonArray) {
        this.overviewStatusDataModelList = overviewStatusDataModelList;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public OverviewStatusDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_cardview, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewStatusDataAdapter.ViewHolder holder, int position) {
        String [] shouldHaveOrdersAtEnd = new String[] {"Picked", "Packed", "Invoiced"};
        OverviewStatusDataModel overviewStatusDataModel = overviewStatusDataModelList.get(position);
        holder.status_textview.setText(overviewStatusDataModel.getStatus() + (Arrays.asList(shouldHaveOrdersAtEnd).contains(overviewStatusDataModel.getStatus()) ? " Orders" : " ") );
        holder.count_percentage_textview.setText(overviewStatusDataModel.getCountPercentage());

        if (overviewStatusDataModel.getStatus().equals("Unserved")) {
            holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.progressbar));
        } else if (overviewStatusDataModel.getStatus().equals("For Picking")) {
            holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.progressbar2));
        } else if (overviewStatusDataModel.getStatus().equals("Picked")) {
            holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.progressbar3));
        } else if (overviewStatusDataModel.getStatus().equals("Packed")) {
            holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.progressbar4));
        } else if (overviewStatusDataModel.getStatus().equals("Invoiced")) {
            holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.progressbar5));
        }
        holder.progressBar.setProgress(overviewStatusDataModel.getProgressPercentage());

        if (holder.year_count_recyclerview != null) {
            try {
                holder.year_count_recyclerview.setHasFixedSize(true);
                overviewYearCountLayoutManager = new LinearLayoutManager(context);
                holder.year_count_recyclerview.setLayoutManager(overviewYearCountLayoutManager);

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.year_count_recyclerview.getContext(), overviewYearCountLayoutManager.getOrientation());
                holder.year_count_recyclerview.addItemDecoration(dividerItemDecoration);

                JSONObject jsonObject = (JSONObject) jsonArray.get(position);
                overviewYearCountDataModelList = (List<OverviewYearCountDataModel>) jsonObject.get("yearCount");

                OverviewYearCountDataAdapter overviewYearCountDataAdapter = new OverviewYearCountDataAdapter(this.overviewYearCountDataModelList);
                holder.year_count_recyclerview.setAdapter(overviewYearCountDataAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int getItemCount() {
        return overviewStatusDataModelList.size();
    }
}
