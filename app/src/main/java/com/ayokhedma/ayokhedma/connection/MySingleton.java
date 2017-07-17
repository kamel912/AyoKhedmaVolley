package com.ayokhedma.ayokhedma.connection;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by MK on 19/05/2017.
 */

public class MySingleton {
    private static MySingleton myInstance;
    private RequestQueue requestQueue;
    private static Context myContext;

    public MySingleton(Context context){
        myContext = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(myContext.getApplicationContext());

        return requestQueue;
    }
    public static synchronized MySingleton getInstance (Context context){

        if (myInstance == null)
        {
            myInstance = new MySingleton(context);
        }
        return myInstance;
    }
    public<T> void addRequestQueue (Request<T> request) {
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        getRequestQueue().add(request);
    }

}
