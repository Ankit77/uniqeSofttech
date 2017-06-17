package com.app.uniqesofttech.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
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

import com.app.uniqesofttech.R;
import com.app.uniqesofttech.util.Utils;
import com.app.uniqesofttech.webservice.WSLogin;

/**
 * Created by ANKIT on 6/17/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText etName, etPassword;
    private Button btnLogin;
    private TextView tvVersion;
    private AsyncLogin asyncLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init() {
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

    private class AsyncLogin extends AsyncTask<String, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity(), "Loading...");
        }

        @Override
        protected Void doInBackground(String... voids) {
            String url = "http://refillapi.uniquesoftech.com/api/GetUser?user=" + voids[0] + "&password=" + voids[1];
            WSLogin wsLogin = new WSLogin();
            wsLogin.executeTown(url, getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null && progressDialog.isShowing()) {
                Utils.dismissProgressDialog(progressDialog);
            }
            HomeFragment homeFragment = new HomeFragment();
            Utils.replaceNextFragment(R.id.activity_main_container, getActivity(), homeFragment);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncLogin != null && asyncLogin.getStatus() == AsyncTask.Status.RUNNING) {
            asyncLogin.cancel(true);
        }
    }
}
