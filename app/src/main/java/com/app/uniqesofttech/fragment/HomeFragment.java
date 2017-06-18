package com.app.uniqesofttech.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.R;
import com.app.uniqesofttech.SpinnerAdapter;
import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.model.PaymentModel;
import com.app.uniqesofttech.model.SyncModel;
import com.app.uniqesofttech.util.Const;
import com.app.uniqesofttech.util.Utils;
import com.app.uniqesofttech.webservice.WSDeliveryData;

import java.util.ArrayList;

/**
 * Created by ANKIT on 6/17/2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText etCashMemo;
    private EditText etCusId;
    private EditText etCusName;
    private EditText etPaymentMode;
    private EditText etAmont;
    private Button btnSubmit;
    private Button btnGetInfo;
    private Spinner spnrPaymentMode;
    private SpinnerAdapter spinnerAdapter;
    private ArrayList<PaymentModel> paymentlist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        init();
        return view;
    }

    private void init() {
        etAmont = (EditText) view.findViewById(R.id.fragment_home_et_amount);
        etCusId = (EditText) view.findViewById(R.id.fragment_home_et_cusid);
        etCusName = (EditText) view.findViewById(R.id.fragment_home_et_cusname);
        etPaymentMode = (EditText) view.findViewById(R.id.fragment_home_et_payment_mode);
        etCashMemo = (EditText) view.findViewById(R.id.fragment_home_et_cashmemono);
        btnSubmit = (Button) view.findViewById(R.id.fragment_home_btn_submit);
        btnGetInfo = (Button) view.findViewById(R.id.fragment_home_btn_get);
        etPaymentMode.setClickable(true);
        etPaymentMode.setFocusable(false);
        etPaymentMode.setFocusableInTouchMode(false);
        spnrPaymentMode = (Spinner) view.findViewById(R.id.fragment_home_spnr_payment_mode);
        paymentlist = DelTrackApp.getInstance().getDatabaseHelper().getPaymentList();
        if (paymentlist != null) {
            spinnerAdapter = new SpinnerAdapter(getActivity(), paymentlist);
            spnrPaymentMode.setAdapter(spinnerAdapter);
        }
        spnrPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etPaymentMode.setText(paymentlist.get(position).getPaymentmode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSubmit.setOnClickListener(this);
        btnGetInfo.setOnClickListener(this);
        etPaymentMode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSubmit) {
            if (TextUtils.isEmpty(etCusId.getText().toString())) {
                Utils.displayDialog(getActivity(), "Consumerid cant be blank.");
            } else if (TextUtils.isEmpty(etCusName.getText().toString())) {
                Utils.displayDialog(getActivity(), "Consumer name cant be blank.");
            } else if (TextUtils.isEmpty(etPaymentMode.getText().toString())) {
                Utils.displayDialog(getActivity(), "Please select payment mode");
            } else if (TextUtils.isEmpty(etAmont.getText().toString())) {
                if (Utils.isNetworkAvailable(getActivity())) {

                } else {
                    Utils.displayDialog(getActivity(), getString(R.string.alert_not_connectivity));

                }
            }
        } else if (view == btnGetInfo) {
            if (TextUtils.isEmpty(etCashMemo.getText().toString())) {
                Utils.displayDialog(getActivity(), "Please enter cashmemo no.");
            } else {
                CustomerModel customerModel = DelTrackApp.getInstance().getDatabaseHelper().getCustomer(etCashMemo.getText().toString());
                if (customerModel != null) {
                    etCusId.setText(customerModel.getCusid());
                    etCusName.setText(customerModel.getName());
                }
            }
        } else if (view == etPaymentMode) {
            spnrPaymentMode.performClick();
        }
    }

    private class AsyncLoadData extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progressDialog;
        private String dealercode;
        private String cashmemono;
        private String paymentmode;
        private String amount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity(), "Sending data to server");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            dealercode = DelTrackApp.getInstance().getSharedPreferences().getString(Const.PREF_DEALERCODE, "");
            cashmemono = params[0];
            paymentmode = params[1];
            amount = params[2];
            WSDeliveryData wsDeliveryData = new WSDeliveryData();
            return wsDeliveryData.executeWebservice(dealercode, cashmemono, paymentmode, amount);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!isCancelled()) {
                Utils.dismissProgressDialog(progressDialog);

            }
            if (!aBoolean) {
                SyncModel syncModel = new SyncModel();
                syncModel.setDealercode(dealercode);
                syncModel.setCashmemono(cashmemono);
                syncModel.setPaymentmode(paymentmode);
                syncModel.setAmount(amount);
                DelTrackApp.getInstance().getDatabaseHelper().insertSync(syncModel);
            }
        }
    }
}
