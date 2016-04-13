package com.milan.recreationapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.milan.recreationapp.database.DBHelper;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/**
 * @author utsav.k This is the application level class for create the database at
 *         first time and open the database connection.
 */
public class ReCreationApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "n5GF8X63P1bLe1PSJWEBjp1NP";
    private static final String TWITTER_SECRET = "AXTPGLM1DELGoply7WaunuXtMEly4gKc0JEgR7W4wBgEFHnwqA";

    private final String TAG = getClass().getSimpleName();
    private RequestQueue mRequestQueue;
    private Context context;
    private DBHelper mDatabase;
    public SharedPreferences sharedPreferences;


    public RequestQueue getRequestQueue() {
       // if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
       // }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        getRequestQueue().add(req);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d(TAG, "onCreate()");
        this.context = this.getApplicationContext();

        sharedPreferences = getSharedPreferences(getString(R.string.app_name),
                Context.MODE_PRIVATE);
        mDatabase = new DBHelper(this);
        try {
            mDatabase.createDataBase();
            mDatabase.openDataBase();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public DBHelper getDatabase() {
        return mDatabase;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
        if (mDatabase != null)
            mDatabase.closeDatabase();
    }
}
