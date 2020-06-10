package com.example.pbs_mobile.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pbs_mobile.Network.ApiRequest;
import com.example.pbs_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private EditText user_et, pass_et;
    private Button login_btn;
    private String url;
    private HashMap<String, String> postData;
    private RequestQueue requestQueue;
    private StringRequest apiRequest;
    private JSONObject jsonParams, responseData, employeeInfo;
    private WifiManager wifi;
    private ConnectivityManager connectivityManager;
    private NetworkInfo wifiConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        user_et = findViewById(R.id.user_et);
        pass_et = findViewById(R.id.pass_et);
        login_btn = findViewById(R.id.login_btn);
        postData = new HashMap();
        user_et.setText("");
        pass_et.setText("");
        login_btn.setEnabled(true);

        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sharedPreferences = getSharedPreferences("userAuthentication", Context.MODE_PRIVATE); // Shared Preferences - userAuthentication
                        final SharedPreferences.Editor editor = sharedPreferences.edit();

                        wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                        wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                        if (!wifi.isWifiEnabled()){
                            Toast.makeText(getApplicationContext(), "Please open your wifi and connect to the server.", Toast.LENGTH_SHORT).show();
                        } else if (!wifiConnection.isConnected()){
                            Toast.makeText(getApplicationContext(), "Please connect on the server.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (user_et.getText().toString().equals("") || pass_et.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                                pass_et.setText("");
                            } else {
                                login_btn.setEnabled(false);
                                postData.put("user", user_et.getText().toString());
                                postData.put("passwd", pass_et.getText().toString());
                                url = "http://192.168.43.61/_api/_login/";   //Production Server
//                                url = "http://192.168.43.10:8000/_api/_auth/_login/";

                                requestQueue = ApiRequest.getInstance(getApplicationContext()).getRequestQueue();
                                requestQueue.start();
                                apiRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    responseData = new JSONObject(response);
                                                    employeeInfo = new JSONObject(responseData.getString("data"));
                                                    if (!employeeInfo.get("msg").toString().equals("success")) {
                                                        login_btn.setEnabled(true);
                                                        Toast.makeText(getApplicationContext(), employeeInfo.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        editor.putString("name", employeeInfo.getString("name"));
                                                        editor.putString("username", user_et.getText().toString());
                                                        editor.putString("position", employeeInfo.getString("position"));
                                                        editor.putString("level", employeeInfo.getString("level"));
                                                        editor.putString("tempCode", employeeInfo.getString("tempCode"));
                                                        editor.putString("email", employeeInfo.getString("email"));
                                                        editor.apply();
                                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                            }
                                        }) {
                                    @Override
                                    protected Map getParams() {
                                        HashMap hashMap = new HashMap();
                                        jsonParams = new JSONObject(postData);
                                        hashMap.put("params", jsonParams.toString());
                                        return hashMap;
                                    }
                                };
                                requestQueue.add(apiRequest);
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
