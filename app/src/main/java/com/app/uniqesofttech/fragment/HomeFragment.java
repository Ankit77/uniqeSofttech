package com.app.uniqesofttech.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.uniqesofttech.R;
import com.app.uniqesofttech.util.Utils;

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
    private Spinner spnrPaymentMode;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        return view;
    }

    private void init() {
        etAmont = (EditText) view.findViewById(R.id.fragment_home_et_amount);
        etCusId = (EditText) view.findViewById(R.id.fragment_home_et_cusid);
        etCusName = (EditText) view.findViewById(R.id.fragment_home_et_cusname);
        etPaymentMode = (EditText) view.findViewById(R.id.fragment_home_et_payment_mode);
        etCashMemo = (EditText) view.findViewById(R.id.fragment_home_et_cashmemono);
        btnSubmit = (Button) view.findViewById(R.id.fragment_home_btn_submit);
        spnrPaymentMode = (Spinner) view.findViewById(R.id.fragment_home_spnr_payment_mode);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSubmit) {
            if (TextUtils.isEmpty(etCashMemo.getText().toString())) {
                Utils.displayDialog(getActivity(), "Please enter cashmemo no.");
            } else if (TextUtils.isEmpty(etCusId.getText().toString())) {
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
        }
    }
}
