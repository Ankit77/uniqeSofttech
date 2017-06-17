package com.app.uniqesofttech;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ANKIT on 6/17/2017.
 */

public class DelTrackApp extends Application {
    private SharedPreferences sharedPreferences;
    private static DelTrackApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static DelTrackApp getInstance() {
        return instance;
    }
}
