package com.example.pbs_mobile.ScannedSalesOrders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pbs_mobile.R;
import com.example.pbs_mobile.VoiceRecognition.Solomon;

public class ScannedSalesOrdersFragment extends Fragment {

    private View view;
    private Bundle dataArgsFromMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.scanned_sales_orders_fragment, container, false);

        dataArgsFromMainActivity = getArguments();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dataArgsFromMainActivity != null) {
            String data = dataArgsFromMainActivity.getString("soNumberApiJsonDataArgs");
            Log.i("com.example.pbs_mobiles", data);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        solomonIsNotListeningAnymore();
    }

    @Override
    public void onStop() {
        super.onStop();
        solomonIsNotListeningAnymore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        solomonIsNotListeningAnymore();
    }

    public void solomonIsNotListeningAnymore() {
        Solomon.ifYouWantSolomonToListen = false;
    }
}
