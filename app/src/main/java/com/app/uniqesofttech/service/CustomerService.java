package com.app.uniqesofttech.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.model.PaymentModel;
import com.app.uniqesofttech.util.Const;
import com.app.uniqesofttech.webservice.GetCustomerData;
import com.app.uniqesofttech.webservice.GetPaymentMode;

import java.util.ArrayList;

/**
 * Created by ANKIT on 6/18/2017.
 */

public class CustomerService extends IntentService {

    public CustomerService() {
        super(CustomerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String paymentmodeurl = "http://refillapi.uniquesoftech.com/api/GetPaymentMode";
        GetPaymentMode getPaymentMode = new GetPaymentMode();
        ArrayList<PaymentModel> patmentlist = getPaymentMode.executeWebservice();
        if (patmentlist != null && patmentlist.size() > 0) {
            DelTrackApp.getInstance().getDatabaseHelper().deletePaymentMode();
            DelTrackApp.getInstance().getDatabaseHelper().insertPayment(patmentlist);
        }
        String userid = DelTrackApp.getInstance().getSharedPreferences().getString(Const.PREF_DEALERCODE, "");
        String Date = DelTrackApp.getInstance().getSharedPreferences().getString(Const.PREF_LASTUPDATE, Const.PREF_DEFAULT_DATETIME);
        GetCustomerData getCustomerData = new GetCustomerData();
        ArrayList<CustomerModel> cusList = getCustomerData.executeWebservice(Date, userid);
        if (cusList != null && cusList.size() > 0) {
            DelTrackApp.getInstance().getDatabaseHelper().insertCustomer(cusList);
        }

    }
}
