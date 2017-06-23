package com.app.uniqesofttech.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.MainActivity;
import com.app.uniqesofttech.R;
import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.model.PaymentModel;
import com.app.uniqesofttech.util.Const;
import com.app.uniqesofttech.util.Utils;
import com.app.uniqesofttech.webservice.GetCustomerData;
import com.app.uniqesofttech.webservice.GetPaymentMode;
import com.app.uniqesofttech.webservice.WSLogin;

import java.util.ArrayList;

/**
 * Created by ANKIT on 6/17/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText etName, etPassword;
    private Button btnLogin;
    private TextView tvVersion;
    private AsyncLogin asyncLogin;
    private AsyncLoadCustomerData asyncLoadCustomerData;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, null);
        mainActivity=(MainActivity)getActivity();
        init();
        return view;
    }

    private void init() {
        mainActivity.getImgRefresh().setVisibility(View.GONE);
        mainActivity.getImgLogout().setVisibility(View.GONE);
        mainActivity.getTvTitle().setText("Login");
        etName = (EditText) view.findViewById(R.id.fragment_login_et_username);
        etPassword = (EditText) view.findViewById(R.id.fragment_login_et_password);
        btnLogin = (Button) view.findViewById(R.id.fragment_login_btn_login);
        tvVersion = (TextView) view.findViewById(R.id.fragment_login_tv_vesrion);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            if (TextUtils.isEmpty(etName.getText().toString())) {
                Utils.displayDialog(getActivity(), getString(R.string.alert_empty_username));
            } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
                Utils.displayDialog(getActivity(), getString(R.string.alert_empty_password));
            } else {
                if (Utils.isNetworkAvailable(getActivity())) {
                    asyncLogin = new AsyncLogin();
                    asyncLogin.execute(etName.getText().toString(), etPassword.getText().toString());
                } else {
                    Utils.displayDialog(getActivity(), getString(R.string.alert_not_connectivity));
                }
            }

        }
    }

    private class AsyncLogin extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity(), "Loading...");
        }

        @Override
        protected Boolean doInBackground(String... voids) {
            WSLogin wsLogin = new WSLogin();
            return wsLogin.executeWebservice(voids[0], voids[1]);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if (!isCancelled()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    Utils.dismissProgressDialog(progressDialog);
                }

                if (aVoid) {
                    asyncLoadCustomerData = new AsyncLoadCustomerData();
                    asyncLoadCustomerData.execute();
                } else {
                    Utils.displayDialog(getActivity(), "Username or Password is not valid");
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncLogin != null && asyncLogin.getStatus() == AsyncTask.Status.RUNNING) {
            asyncLogin.cancel(true);
        }
        if (asyncLoadCustomerData != null && asyncLoadCustomerData.getStatus() == AsyncTask.Status.RUNNING) {
            asyncLoadCustomerData.cancel(true);
        }
    }

    private class AsyncLoadCustomerData extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity(), "Loading Customer Data");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!isCancelled()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    Utils.dismissProgressDialog(progressDialog);
                }
                HomeFragment homeFragment = new HomeFragment();
                Utils.replaceNextFragment(R.id.activity_main_container, getActivity(), homeFragment);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String userid = DelTrackApp.getInstance().getSharedPreferences().getString(Const.PREF_USERID, "");
           // String userid = "1";
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
}
