package com.example.pbs_mobile.Network;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.pbs_mobile.VoiceRecognition.Solomon;

public class ConnectionAuthService extends IntentService {

    private WifiManager wifi;
    private ConnectivityManager connectivityManager;
    private NetworkInfo wifiConnection;

    public ConnectionAuthService() {
        super("ConnectionAuthService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(!wifi.isWifiEnabled()) {
            Toast.makeText(getApplication().getApplicationContext(), "Please open your wifi and connect to the server.", Toast.LENGTH_SHORT).show();
            Solomon.ifYouWantSolomonToListen = false;
        } else if (!wifiConnection.isConnected()){
            Toast.makeText(getApplicationContext(), "Please connect on the server.", Toast.LENGTH_SHORT).show();
            Solomon.ifYouWantSolomonToListen = false;
        } else {
            Solomon.ifYouWantSolomonToListen = true;
            stopSelf();
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
