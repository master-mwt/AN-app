package it.univaq.disim.mwt.android_native_app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyRequest {
    private static VolleyRequest instance = null;
    private RequestQueue queue;
    private ImageLoader imageLoader;

    public synchronized static VolleyRequest getInstance(Context context) {
        return (instance == null) ? instance = new VolleyRequest(context) : instance;
    }

    private VolleyRequest(Context context) {
        queue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public RequestQueue getQueue() {
        return this.queue;
    }

    public ImageLoader getImageLoader(){
        return this.imageLoader;
    }
}
