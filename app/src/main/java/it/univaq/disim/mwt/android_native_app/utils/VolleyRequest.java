package it.univaq.disim.mwt.android_native_app.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequest {
    private static VolleyRequest instance = null;
    private RequestQueue queue;

    public synchronized static VolleyRequest getInstance(Context context) {
        return (instance == null) ? instance = new VolleyRequest(context) : instance;
    }

    private VolleyRequest(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public RequestQueue getQueue() {
        return this.queue;
    }
}
