package com.app.uniqesofttech;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.app.uniqesofttech.service.CustomerService;
import com.app.uniqesofttech.util.Const;

/**
 * Created by ANKIT on 6/17/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (DelTrackApp.getInstance().getSharedPreferences().getBoolean(Const.PREF_ISREGISTER, false)) {
            Intent intent = new Intent(SplashActivity.this, CustomerService.class);
            startService(intent);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }

}
