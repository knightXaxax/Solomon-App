package com.example.pbs_mobile.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private float x1, x2;
    private static final int MIN_DISTANCE = 150;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private String url;
    private JSONObject jsonPostData;
    private HashMap postData, params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        requestQueue = new RequestQueue(new DiskBasedCache(getCacheDir(), 1024 * 1024), new BasicNetwork(new HurlStack()));
        postData = new HashMap();
        params = new HashMap();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    if(x1 < x2) {
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                    }
                }
                break;
        }
        return true && super.onTouchEvent(event);
    }

    @Override
    public void handleResult(Result rawResult) {
        if(rawResult.getText() == null) {
            Toast.makeText(this, "Cancelled scanning", Toast.LENGTH_SHORT).show();
        } else {
            postData.put("soNumber", rawResult.getText());
            Toast.makeText(this, "data : "+rawResult.getText(), Toast.LENGTH_LONG ).show();
            jsonPostData = new JSONObject(postData);
            handlesApiRequest();
        }
        scannerView.resumeCameraPreview(this);
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scannerView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void handlesApiRequest() {
        requestQueue.start();

        url = "http://192.168.43.10:8000/_api/_so_list/_get_scanned_so_number_data/";
//        url = "http://192.168.43.61:8000/_api/_so_list/_get_scanned_so_number_data/"; // Production Server

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent passScannedSoNumberDataIntent = new Intent(getBaseContext(), MainActivity.class);
                        passScannedSoNumberDataIntent.putExtra("soNumberApiJsonDataResponse", response);
                        startActivity(passScannedSoNumberDataIntent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
            @Override
            protected Map getParams() {
                params.put("params", jsonPostData.toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
