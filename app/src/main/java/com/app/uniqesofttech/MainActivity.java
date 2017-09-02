package com.app.uniqesofttech;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.uniqesofttech.fragment.HomeFragment;
import com.app.uniqesofttech.fragment.LoginFragment;
import com.app.uniqesofttech.util.Const;
import com.app.uniqesofttech.util.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgLogout;
    private ImageView imgRefresh;

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getImgLogout() {
        return imgLogout;
    }

    public ImageView getImgRefresh() {
        return imgRefresh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = (TextView) findViewById(R.id.activity_main_tvtitle);
        imgRefresh = (ImageView) findViewById(R.id.activity_main_imgRefresh);
        imgLogout = (ImageView) findViewById(R.id.activity_main_imgLogout);
        imgLogout.setOnClickListener(this);
        imgRefresh.setOnClickListener(this);


        if (DelTrackApp.getInstance().getSharedPreferences().getBoolean(Const.PREF_ISREGISTER, false)) {
            HomeFragment homeFragment = new HomeFragment();
            Utils.replaceNextFragment(R.id.activity_main_container, MainActivity.this, homeFragment);
        } else {
            LoginFragment loginFragment = new LoginFragment();
            Utils.replaceNextFragment(R.id.activity_main_container, MainActivity.this, loginFragment);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == imgLogout) {
            DelTrackApp.getInstance().getDatabaseHelper().deleteAllCustomerData();
            DelTrackApp.getInstance().getDatabaseHelper().deletePaymentMode();
            DelTrackApp.getInstance().getDatabaseHelper().deleteAllSyncData();
            DelTrackApp.getInstance().getSharedPreferences().edit().clear().commit();
            LoginFragment loginFragment = new LoginFragment();
            Utils.replaceNextFragment(R.id.activity_main_container, MainActivity.this, loginFragment);
            tvTitle.setText("Login");
            imgLogout.setVisibility(View.GONE);

        } else if (view == imgRefresh) {
            HomeFragment homeFragment = (HomeFragment) getFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName());
            if (homeFragment != null) {
                homeFragment.refreshData();
            }
        }
    }
}
