package com.example.pbs_mobile.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pbs_mobile.Others.Global;
import com.example.pbs_mobile.Network.ApiRequest;
import com.example.pbs_mobile.Network.ConnectionAuthService;
import com.example.pbs_mobile.Notification.NotificationFragment;
import com.example.pbs_mobile.Overview.OverviewFragment;
import com.example.pbs_mobile.R;
import com.example.pbs_mobile.ScannedSalesOrders.ScannedSalesOrdersFragment;
import com.example.pbs_mobile.Settings.SettingsFragment;
import com.example.pbs_mobile.ViewSalesOrders.ViewSalesOrderFragment;
import com.example.pbs_mobile.VoiceRecognition.Solomon;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavController navController;
    private ActionBarDrawerToggle navToggle;
    private FragmentTransaction fragmentSwitch;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FloatingActionButton solomon_btn;
    final String[] permissions = new String[]{
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO
    };
    private final int MY_PERMISSIONS_REQUEST_ALL_GRANTED = 1;
    private Runnable job;
    private Thread thread;
    private Bundle apiData, dataArgsToBeSendInOverviewFragment;
    private OverviewFragment overviewFragment;
    private ViewSalesOrderFragment viewSalesOrderFragment;
    private SettingsFragment settingsFragment;
    private ScannedSalesOrdersFragment scannedSalesOrdersFragment;
    private NotificationFragment notificationFragment;
    private RequestQueue requestQueue;
    private String user;
    private TextView name_textview, position_textview, email_textview;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sharedPreferences = getSharedPreferences("userAuthentication", Context.MODE_PRIVATE); // Shared Preferences - userAuthentication

//        if(checkIfAppHasAllAppPermissions(MainActivity.this) != true) {
//            ActivityCompat.requestPermissions(MainActivity.this, permissions, MY_PERMISSIONS_REQUEST_ALL_GRANTED);
//        } else {
//            user = sharedPreferences.getString("username", "");
//
//            if (user.equals("") != true) {
                solomon_btn = findViewById(R.id.solomon_btn);

                solomon_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Solomon.ifYouWantSolomonToListen = false;
                        startActivity(new Intent(getBaseContext(), ScannerActivity.class));
                    }
                });

                drawerLayout = findViewById(R.id.drawer);
                navController = Navigation.findNavController(this, R.id.nav_host_fragment);

                navView = findViewById(R.id.nav_view);
                NavigationUI.setupWithNavController(navView, navController);
                navView.setNavigationItemSelectedListener(this);

                navToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
                drawerLayout.setDrawerListener(navToggle);
                navToggle.syncState();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                overviewFragment = new OverviewFragment();
                viewSalesOrderFragment = new ViewSalesOrderFragment();
                settingsFragment = new SettingsFragment();
                scannedSalesOrdersFragment = new ScannedSalesOrdersFragment();
                notificationFragment = new NotificationFragment();

                fragmentSwitch = getSupportFragmentManager().beginTransaction();

                requestQueue  = ApiRequest.getInstance(getApplicationContext()).getRequestQueue();
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.isAppIsClosed = false;
//        checkIfIsConnectedToTheServer();
//        solomonIsListening();
//        if (user.equals("") == true) {
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//        } else {
            String name = sharedPreferences.getString("name", "Fullname");
            String position = sharedPreferences.getString("position", "Position");
            String email = sharedPreferences.getString("email", "Email");

            View header = navView.getHeaderView(0);

            name_textview = header.findViewById(R.id.name_textview);
            position_textview = header.findViewById(R.id.position_textview);
            email_textview = header.findViewById(R.id.email_textview);

            name_textview.setText(name);
            position_textview.setText(position);
            email_textview.setText(email);

            apiData = getIntent().getExtras();
            if (apiData != null) {
                String soNumberApiJsonDataResponse = apiData.getString("soNumberApiJsonDataResponse");

                dataArgsToBeSendInOverviewFragment = new Bundle();
                dataArgsToBeSendInOverviewFragment.putString("soNumberApiJsonDataArgs", soNumberApiJsonDataResponse);

                scannedSalesOrdersFragment.setArguments(dataArgsToBeSendInOverviewFragment);
                fragmentSwitch.replace(R.id.nav_host_fragment, scannedSalesOrdersFragment).addToBackStack(null).commit();
            }
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        solomonIsNotListeningAnymore();
        Global.isAppIsClosed = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        solomonIsNotListeningAnymore();
        Global.isAppIsClosed = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        solomonIsNotListeningAnymore();
        Global.isAppIsClosed = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_menu_drawer, menu);

        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };

        MenuItem searchItem = menu.findItem(R.id.Search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(navToggle.onOptionsItemSelected(item)){
            return true;
        }

        fragmentSwitch = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.Notification: {
                fragmentSwitch.replace(R.id.nav_host_fragment, notificationFragment).addToBackStack(null).commit();
                break;
            }
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        navToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragmentSwitch = null;
        fragmentSwitch = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.Overview : {
                fragmentSwitch.replace(R.id.nav_host_fragment, overviewFragment).addToBackStack(null).commit();
                break;
            }
            case R.id.View_sales_order : {
                fragmentSwitch.replace(R.id.nav_host_fragment, viewSalesOrderFragment).addToBackStack(null).commit();
                break;
            }
            case R.id.Settings : {
                fragmentSwitch.replace(R.id.nav_host_fragment, settingsFragment).addToBackStack(null).commit();
                break;
            }
            case R.id.Logout : {
                WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if(!wifi.isWifiEnabled() || !wifiConnection.isConnected()) {
                    Toast.makeText(getApplication().getApplicationContext(), "Please check your connection to the server.", Toast.LENGTH_LONG).show();
                } else {

                    final String tempCode = sharedPreferences.getString("tempCode", "");
                    if (!tempCode.equals("")) {
                        requestQueue.start();
                        String url = "http://192.168.43.61/_api/_auth/_logout/"; // Production Server
//                        String url = "http://192.168.43.10:8000/_api/_auth/_logout/";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            if (jsonResponse.getString("msg").equals("success")) {
                                                editor = sharedPreferences.edit();
                                                editor.clear();
                                                editor.apply();
                                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
                                HashMap map = new HashMap();
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("tempCode", tempCode);
                                    JSONObject postData = new JSONObject(jsonObject.toString());
                                    map.put("params", postData.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return map;
                            }
                        };
                        requestQueue.add(stringRequest);

                    }
                }
                break;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backStackCount == 0) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_ALL_GRANTED: {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            endTask();
                        }
                    }
                }
            }
        }
    }

    private void endTask() {
        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    private boolean checkIfAppHasAllAppPermissions(Context context) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void checkIfIsConnectedToTheServer() {
        job = new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if (Global.isAppIsClosed == true)
                            break;
                        startService(new Intent(MainActivity.this, ConnectionAuthService.class));
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread = new Thread(job);
        thread.start();
    }

    public void solomonIsListening() {
        Solomon.ifYouWantSolomonToListen = true;
        startService(new Intent(getBaseContext(), Solomon.class));
    }

    public void solomonIsNotListeningAnymore() {
        Solomon.ifYouWantSolomonToListen = false;
    }
}
