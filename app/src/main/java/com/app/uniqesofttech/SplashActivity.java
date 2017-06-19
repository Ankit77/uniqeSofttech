package com.app.uniqesofttech;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.model.PaymentModel;
import com.app.uniqesofttech.util.Const;
import com.app.uniqesofttech.util.Utils;
import com.app.uniqesofttech.webservice.GetCustomerData;
import com.app.uniqesofttech.webservice.GetPaymentMode;

import java.util.ArrayList;

/**
 * Created by ANKIT on 6/17/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private ProgressBar progressBar;
    private AsyncLoadCustomerData asyncLoadCustomerData;
    private TextView tvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        tvVersion=(TextView)findViewById(R.id.activity_splash_tv_vesrion);
        tvVersion.setText("version "+version);
        progressBar = (ProgressBar) findViewById(R.id.activity_splash_progressBar);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (DelTrackApp.getInstance().getSharedPreferences().getBoolean(Const.PREF_ISREGISTER, false)) {
                    if (Utils.isNetworkAvailable(SplashActivity.this)) {
                        asyncLoadCustomerData = new AsyncLoadCustomerData();
                        asyncLoadCustomerData.execute();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }

    private class AsyncLoadCustomerData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!isCancelled()) {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
          String userid = DelTrackApp.getInstance().getSharedPreferences().getString(Const.PREF_DEALERCODE, "");
            String Date = DelTrackApp.getInstance().getSharedPreferences().getString(Const.PREF_LASTUPDATE, Const.PREF_DEFAULT_DATETIME);
            GetCustomerData getCustomerData = new GetCustomerData();
            ArrayList<CustomerModel> cusList = getCustomerData.executeWebservice(Date, userid);
            if (cusList != null && cusList.size() > 0) {
                DelTrackApp.getInstance().getDatabaseHelper().insertCustomer(cusList);
            }
            GetPaymentMode getPaymentMode = new GetPaymentMode();
            ArrayList<PaymentModel> patmentlist = getPaymentMode.executeWebservice();
            if (patmentlist != null && patmentlist.size() > 0) {
                DelTrackApp.getInstance().getDatabaseHelper().deletePaymentMode();
                DelTrackApp.getInstance().getDatabaseHelper().insertPayment(patmentlist);
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {

        if (asyncLoadCustomerData != null && asyncLoadCustomerData.getStatus() == AsyncTask.Status.RUNNING) {
            asyncLoadCustomerData.cancel(true);
        }
        super.onBackPressed();
    }
}
