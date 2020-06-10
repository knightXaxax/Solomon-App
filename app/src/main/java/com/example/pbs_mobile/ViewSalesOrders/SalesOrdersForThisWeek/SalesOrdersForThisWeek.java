package com.example.pbs_mobile.ViewSalesOrders.SalesOrdersForThisWeek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pbs_mobile.R;

public class SalesOrdersForThisWeek extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sales_orders_for_this_week, container, false);

        return view;
    }
}
