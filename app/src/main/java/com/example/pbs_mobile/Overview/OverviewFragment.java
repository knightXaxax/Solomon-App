package com.example.pbs_mobile.Overview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.pbs_mobile.Network.ApiRequest;
import com.example.pbs_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class OverviewFragment extends Fragment{

    private View view;
    private RecyclerView overviewRecyclerView;
    private LinearLayoutManager overviewLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;


    private class GetCycleCounts extends AsyncTask<Boolean, Integer, JSONObject> implements  OverviewStatusDataAdapter.ItemClickListener{

        private RequestQueue requestQueue;
        private JsonObjectRequest cycleCountApiRequest;
        private JSONObject cycleCountApiJsonResponse, toBeShownDataStatus, shouldBeHideDataStatus, tempDataShouldBeHide;
        private final String url = "http://192.168.43.10:8000/_api/_so_list/_get_cycle_count/";
//        private final String url = "http://192.168.43.61/_api/_so_list/_get_cycle_count/";

        private OverviewStatusDataModel overviewStatusDataModel;
        private ArrayList<OverviewStatusDataModel> overviewStatusDataModelArrayList;
        private OverviewYearCountDataModel overviewYearCountDataModel;
        private ArrayList<OverviewYearCountDataModel> overviewYearCountDataModelArrayList;
        private OverviewStatusDataAdapter overviewStatusDataAdapter;
        private JSONArray tempDataShouldBeHideArray, overviewYearCountDataModelArrayListArray;

        @Override
        protected JSONObject doInBackground(Boolean... booleans) {
            for (final boolean bool : booleans)
                if (bool == true) {
                    requestQueue = ApiRequest.getInstance(getActivity().getApplicationContext()).getRequestQueue();
                    requestQueue.start();

                    cycleCountApiRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    setCycleCountApiJsonResponse(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("com.example.pbs_mobile", "error");
                                }
                            });
                    requestQueue.add(cycleCountApiRequest);
                }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return getCycleCountApiJsonResponse();
        }

        @Override
        public void onItemClick(View view, int position) {
            final CardView cardView = view.findViewById(R.id.cardview);
            final ConstraintLayout bottomCardViewLayout = view.findViewById(R.id.bottom_cardview_layout);
            if (bottomCardViewLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                bottomCardViewLayout.setVisibility(View.VISIBLE);
            } else {
                bottomCardViewLayout.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            overviewStatusDataModelArrayList = new ArrayList<>();
            overviewYearCountDataModelArrayListArray = new JSONArray();

            String [] statusOverviewShouldHave = new String[] {"For Processing", "For Picking", "Picked", "Packed", "Invoiced"};

            try {
                JSONArray apiResponseDataArray = new JSONArray(jsonObject.getString("cycleCounts"));
                toBeShownDataStatus = new JSONObject();
                shouldBeHideDataStatus = new JSONObject();

                for (int i=0; i<apiResponseDataArray.length(); i++) {
                    JSONObject apiResponseContainer = new JSONObject(apiResponseDataArray.get(i).toString());
                    if (Arrays.asList(statusOverviewShouldHave).contains(apiResponseContainer.getString("status")) == true) {
                        toBeShownDataStatus.put(apiResponseContainer.getString("status"), apiResponseContainer);
                    } else {
                        shouldBeHideDataStatus.put(apiResponseContainer.getString("status"), apiResponseContainer);
                    }
                }

                double alpha = 0, omega = 0, delta = 0;

                for (String statusToBeShown : statusOverviewShouldHave) {
                    JSONObject tempDataToBeShown = new JSONObject(toBeShownDataStatus.getString(statusToBeShown));
                    JSONObject tempData = new JSONObject();
                    String statusFromJson = tempDataToBeShown.getString("status");
                    JSONArray dataFromJson = new JSONArray(tempDataToBeShown.getString("data"));

                    overviewStatusDataModel = new OverviewStatusDataModel();
                    overviewYearCountDataModelArrayList = new ArrayList<>();

                    for (int k=0; k<dataFromJson.length(); k++) {
                        JSONObject cycleCountData = new JSONObject(dataFromJson.get(k).toString());

                        overviewYearCountDataModel = new OverviewYearCountDataModel();
                        overviewYearCountDataModel.setYear(cycleCountData.getString("year"));
                        overviewYearCountDataModel.setCount(cycleCountData.getString("count"));

                        overviewYearCountDataModelArrayList.add(overviewYearCountDataModel);

                        alpha += Integer.parseInt(cycleCountData.getString("count"));
                    }

                    if (statusFromJson.equals("For Processing")) {
                        tempDataShouldBeHide = new JSONObject(shouldBeHideDataStatus.getString("Unserved"));
                        tempDataShouldBeHideArray = new JSONArray(tempDataShouldBeHide.getString("data"));
                        for (int u=0; u<tempDataShouldBeHideArray.length(); u++) {
                            JSONObject tempDataShouldBeHideJson = new JSONObject(tempDataShouldBeHideArray.get(u).toString());
                            omega += Integer.parseInt(tempDataShouldBeHideJson.getString("count"));
                        }

                    } else if (statusFromJson.equals("For Picking")) {
                        tempDataShouldBeHide = new JSONObject(shouldBeHideDataStatus.getString("Unserved"));
                        tempDataShouldBeHideArray = new JSONArray(tempDataShouldBeHide.getString("data"));
                        for (int u=0; u<tempDataShouldBeHideArray.length(); u++) {
                            JSONObject tempDataShouldBeHideJson = new JSONObject(tempDataShouldBeHideArray.get(u).toString());
                            omega += Integer.parseInt(tempDataShouldBeHideJson.getString("count"));
                        }

                    } else if (statusFromJson.equals("Picked")) {
                        tempDataShouldBeHide = new JSONObject(toBeShownDataStatus.getString("For Picking"));
                        tempDataShouldBeHideArray = new JSONArray(tempDataShouldBeHide.getString("data"));
                        for (int u=0; u<tempDataShouldBeHideArray.length(); u++) {
                            JSONObject tempDataShouldBeHideJson = new JSONObject(tempDataShouldBeHideArray.get(u).toString());
                            omega += Integer.parseInt(tempDataShouldBeHideJson.getString("count"));
                        }

                    } else if (statusFromJson.equals("Packed")) {
                        tempDataShouldBeHide = new JSONObject(shouldBeHideDataStatus.getString("For Packing"));
                        tempDataShouldBeHideArray = new JSONArray(tempDataShouldBeHide.getString("data"));
                        for (int u=0; u<tempDataShouldBeHideArray.length(); u++) {
                            JSONObject tempDataShouldBeHideJson = new JSONObject(tempDataShouldBeHideArray.get(u).toString());
                            omega += Integer.parseInt(tempDataShouldBeHideJson.getString("count"));
                        }

                    } else if (statusFromJson.equals("Invoiced")) {
                        tempDataShouldBeHide = new JSONObject(shouldBeHideDataStatus.getString("For Invoicing"));
                        tempDataShouldBeHideArray = new JSONArray(tempDataShouldBeHide.getString("data"));
                        for (int u=0; u<tempDataShouldBeHideArray.length(); u++) {
                            JSONObject tempDataShouldBeHideJson = new JSONObject(tempDataShouldBeHideArray.get(u).toString());
                            omega += Integer.parseInt(tempDataShouldBeHideJson.getString("count"));
                        }
                    }
                    delta = Math.ceil((alpha / (alpha + omega)) * 100);

                    overviewStatusDataModel.setStatus(statusFromJson);
                    overviewStatusDataModel.setCountPercentage(String.valueOf((int)delta));
                    overviewStatusDataModel.setProgressPercentage((int)delta);
                    overviewStatusDataModelArrayList.add(overviewStatusDataModel);

                    tempData.put("yearCount", overviewYearCountDataModelArrayList);
                    overviewYearCountDataModelArrayListArray.put(tempData);
                }

                if (overviewRecyclerView != null) {
                    overviewStatusDataAdapter = new OverviewStatusDataAdapter(overviewStatusDataModelArrayList, overviewYearCountDataModelArrayListArray);
                    overviewStatusDataAdapter.setClickListener(this);
                    overviewRecyclerView.setAdapter(overviewStatusDataAdapter);
                }

                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private JSONObject getCycleCountApiJsonResponse() {
            return cycleCountApiJsonResponse;
        }

        private void setCycleCountApiJsonResponse(JSONObject cycleCountApiJsonResponse) {
            this.cycleCountApiJsonResponse = cycleCountApiJsonResponse;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.overview_fragment, container, false);

        swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh_layout);

        if (swipeRefreshLayout != null){
            swipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            updateOverviewFragment();
                        }
                    }
            );
        }

        overviewRecyclerView = view.findViewById(R.id.overview_recyclerview);
        if (overviewRecyclerView != null) {
            overviewRecyclerView.setHasFixedSize(true);

            overviewLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            overviewRecyclerView.setLayoutManager(overviewLayoutManager);

            updateOverviewFragment();

//            overviewRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//
//                }
//
//                @Override
//                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//
//                    if (coordinatorLayout != null) {
//                        if (dy > 0 && coordinatorLayout.isShown()) {
//                            coordinatorLayout.setVisibility(View.GONE);
//                        } else if (dy < 0 ) {
//                            coordinatorLayout.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//            });
        }

        return view;
    }

    private void updateOverviewFragment() {
        GetCycleCounts getCycleCounts = new GetCycleCounts();
        getCycleCounts.execute(true);
    }
}