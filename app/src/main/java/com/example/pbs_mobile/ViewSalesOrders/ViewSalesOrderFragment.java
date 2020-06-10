package com.example.pbs_mobile.ViewSalesOrders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pbs_mobile.ViewSalesOrders.PendingSalesOrders.PendingSalesOrders;
import com.example.pbs_mobile.R;
import com.example.pbs_mobile.ViewSalesOrders.SalesOrdersForThisWeek.SalesOrdersForThisWeek;
import com.example.pbs_mobile.ViewSalesOrders.SalesOrdersForToday.SalesOrdersForToday;
import com.google.android.material.tabs.TabLayout;

public class ViewSalesOrderFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private FragmentTransaction fragmentTransaction;
    private SalesOrdersForToday salesOrdersForToday;
    private PendingSalesOrders pendingSalesOrders;
    private SalesOrdersForThisWeek salesOrdersForThisWeek;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_sales_order_fragment, container, false);

        salesOrdersForToday = new SalesOrdersForToday();
        pendingSalesOrders = new PendingSalesOrders();
        salesOrdersForThisWeek = new SalesOrdersForThisWeek();

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.viewSalesOrdersFrameLayout, salesOrdersForToday).addToBackStack(null).commit();

        tabLayout = view.findViewById(R.id.tabLayout);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                fragmentTransaction = getFragmentManager().beginTransaction();
                switch (tab.getPosition()) {
                    case 1: {
                        fragmentTransaction.replace(R.id.viewSalesOrdersFrameLayout, salesOrdersForToday).addToBackStack(null).commit();
                        break;
                    }
                    case 2: {
                        fragmentTransaction.replace(R.id.viewSalesOrdersFrameLayout, pendingSalesOrders).addToBackStack(null).commit();
                        break;
                    }
                    case 3: {
                        fragmentTransaction.replace(R.id.viewSalesOrdersFrameLayout, salesOrdersForThisWeek).addToBackStack(null).commit();
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
