package com.example.pbs_mobile.Network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;


public class ApiRequest {

    private static ApiRequest mInstance;
    private RequestQueue requestQueue;
    private Context mContext;

    private ApiRequest(Context context) {
        this.mContext = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized ApiRequest getInstance(Context context) {
        if (mInstance == null)
            mInstance = new ApiRequest(context);

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = new RequestQueue(new DiskBasedCache(mContext.getCacheDir(), 3024 * 3024), new BasicNetwork(new HurlStack()));

        return requestQueue;
    }
}
