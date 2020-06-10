package com.example.pbs_mobile.ViewSalesOrders.SalesOrdersForToday;

import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.example.pbs_mobile.Network.ApiRequest;
import com.example.pbs_mobile.R;
import com.example.pbs_mobile.ViewSalesOrders.ViewSalesOrderDataModels.SalesOrdersForTodayCardviewDataModel;
import com.example.pbs_mobile.ViewSalesOrders.ViewSalesOrderDataModels.SalesOrdersForTodayListViewDataModel;

public class SalesOrdersForToday extends Fragment {

    private View view;
    private RecyclerView salesOrdersForTodayRecyclerView;
    private LinearLayoutManager salesOrdersForTodayRecyclerViewLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sales_orders_for_today, container, false);

        salesOrdersForTodayRecyclerView = view.findViewById(R.id.sales_orders_for_today_recylerview);
        if(salesOrdersForTodayRecyclerView != null) {
            salesOrdersForTodayRecyclerView.setHasFixedSize(true);

            salesOrdersForTodayRecyclerViewLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            salesOrdersForTodayRecyclerView.setLayoutManager(salesOrdersForTodayRecyclerViewLayoutManager);

            final CoordinatorLayout bottomAppBarLayout = getActivity().findViewById(R.id.bottomAppBarLayout);
            salesOrdersForTodayRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (bottomAppBarLayout != null) {
                        if (dy > 0 && bottomAppBarLayout.isShown()) {
                            bottomAppBarLayout.setVisibility(View.GONE);
                        } else if (dy < 0 ){
                            bottomAppBarLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            refreshDataInFragment();
        }

        swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh_layout);
        if (swipeRefreshLayout != null){
            swipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            refreshDataInFragment();
                        }
                    }
            );
        }

        return view;
    }

    private class GetSalesOrdersForToday extends AsyncTask<Boolean, Integer, JSONObject> implements SalesOrdersForTodayCardviewDataAdapter.CardviewItemClickListener{

        private RequestQueue requestQueue;
        private String url = "http://192.168.43.10:8000/_api/_so_list/_get_sales_orders_for_today/"; // development
//        private String url = "http://192.168.43.61/_api/_so_list/_get_sales_orders_for_today/"; // production
        private JSONObject salesOrdersForTodayApiResponse;
        private JsonObjectRequest jsonObjectRequest;
        private SalesOrdersForTodayCardviewDataModel salesOrdersForTodayCardviewDataModel;
        private SalesOrdersForTodayListViewDataModel salesOrdersForTodayListViewDataModel;
        private ArrayList<SalesOrdersForTodayCardviewDataModel> salesOrdersForTodayCardviewDataModelArrayList;
        private ArrayList<SalesOrdersForTodayListViewDataModel> salesOrdersForTodayListViewDataModelArrayList;

        @Override
        protected JSONObject doInBackground(Boolean... booleans) {
            for (final boolean bool : booleans)
                if(bool == true) {
                    requestQueue = ApiRequest.getInstance(getActivity().getApplicationContext()).getRequestQueue();
                    requestQueue.start();

                    jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    setSalesOrdersForTodayApiResponse(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    requestQueue.add(jsonObjectRequest);

                    try {
                        TimeUnit.SECONDS.sleep(6);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return getSalesOrdersForTodayApiResponse();
                }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                JSONArray salesOrdersForTodayApiResponseArray = new JSONArray(jsonObject.getString("salesOrdersForToday"));
                salesOrdersForTodayCardviewDataModelArrayList = new ArrayList<>();
                JSONArray salesOrdersForTodayListViewDataModelArrayListContainer = new JSONArray();

                for (int i=0; i<salesOrdersForTodayApiResponseArray.length(); i++) {
                    final JSONObject salesOrdersForTodayApiResponseData = new JSONObject(salesOrdersForTodayApiResponseArray.get(i).toString());

                    salesOrdersForTodayCardviewDataModel = new SalesOrdersForTodayCardviewDataModel();
                    salesOrdersForTodayCardviewDataModel.setStatus(salesOrdersForTodayApiResponseData.getString("status"));

                    salesOrdersForTodayCardviewDataModelArrayList.add(salesOrdersForTodayCardviewDataModel);

                    salesOrdersForTodayListViewDataModelArrayList = new ArrayList<>();
                    JSONArray salesOrdersForTodayApiResponseDataArray = new JSONArray(salesOrdersForTodayApiResponseData.getString("data"));

                    for (int k=0; k<salesOrdersForTodayApiResponseDataArray.length(); k++) {
                        salesOrdersForTodayListViewDataModel = new SalesOrdersForTodayListViewDataModel();
                        JSONObject salesOrdersForTodayApiResponseDataArrayJsonContainer = new JSONObject(salesOrdersForTodayApiResponseDataArray.get(k).toString());

                        salesOrdersForTodayListViewDataModel.setSoNumber(salesOrdersForTodayApiResponseDataArrayJsonContainer.getString("soNumber"));
                        salesOrdersForTodayListViewDataModel.setCustomer(salesOrdersForTodayApiResponseDataArrayJsonContainer.getString("customerId"));
                        salesOrdersForTodayListViewDataModel.setQty(salesOrdersForTodayApiResponseDataArrayJsonContainer.getString("qty"));
                        salesOrdersForTodayListViewDataModel.setPart(salesOrdersForTodayApiResponseDataArrayJsonContainer.getString("itemId"));
                        salesOrdersForTodayListViewDataModel.setLine(salesOrdersForTodayApiResponseDataArrayJsonContainer.getString("line"));
                        salesOrdersForTodayListViewDataModel.setIsbn(salesOrdersForTodayApiResponseDataArrayJsonContainer.getString("isbn"));
                        salesOrdersForTodayListViewDataModel.setDescription(salesOrdersForTodayApiResponseDataArrayJsonContainer.getString("description"));
                        salesOrdersForTodayListViewDataModel.setUnitPrice(salesOrdersForTodayApiResponseDataArrayJsonContainer.getString("unitPrice"));
                        salesOrdersForTodayListViewDataModel.setBinNumber(salesOrdersForTodayApiResponseDataArrayJsonContainer.getString("binNumber"));

                        salesOrdersForTodayListViewDataModelArrayList.add(salesOrdersForTodayListViewDataModel);
                    }

                    salesOrdersForTodayListViewDataModelArrayListContainer.put(new JSONObject().put("dataset", salesOrdersForTodayListViewDataModelArrayList));
                }

                if(salesOrdersForTodayRecyclerView != null) {
                    SalesOrdersForTodayCardviewDataAdapter salesOrdersForTodayCardviewDataAdapter = new SalesOrdersForTodayCardviewDataAdapter(salesOrdersForTodayCardviewDataModelArrayList, salesOrdersForTodayListViewDataModelArrayListContainer);
                    salesOrdersForTodayCardviewDataAdapter.setCardviewItemClickListener(this);
                    salesOrdersForTodayRecyclerView.setAdapter(salesOrdersForTodayCardviewDataAdapter);
                }

                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public JSONObject getSalesOrdersForTodayApiResponse() {
            return salesOrdersForTodayApiResponse;
        }

        public void setSalesOrdersForTodayApiResponse(JSONObject salesOrdersForTodayApiResponse) {
            this.salesOrdersForTodayApiResponse = salesOrdersForTodayApiResponse;
        }

        @Override
        public void onCardviewItemClick(View view, int position) {
            ConstraintLayout salesOrdersForTodayCardViewBottomLayout = view.findViewById(R.id.sales_orders_for_today_cardview_bottom_layout);
            if (salesOrdersForTodayCardViewBottomLayout.getVisibility() == View.GONE) {
                salesOrdersForTodayCardViewBottomLayout.setVisibility(view.VISIBLE);
            } else {
                salesOrdersForTodayCardViewBottomLayout.setVisibility(view.GONE);
            }
        }
    }

    private void refreshDataInFragment() {
        GetSalesOrdersForToday getSalesOrdersForToday = new GetSalesOrdersForToday();
        getSalesOrdersForToday.execute(true);
    }
}
