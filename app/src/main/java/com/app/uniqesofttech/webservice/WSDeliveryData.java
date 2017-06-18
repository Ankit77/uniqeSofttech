package com.app.uniqesofttech.webservice;

import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by chandnichudasama on 17/06/17.
 */

public class WSDeliveryData {
    private String message = "";
    private int success = 0;


    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }

    public boolean executeWebservice(String userid, String cashmemono, String paymentmode, String amount) {

        final String url = "http://refillapi.uniquesoftech.com/api/DeliveryData?UseID=" + userid + "&CashMemoNo=" + cashmemono + "&PaymentModeID=" + paymentmode + "&Amount=" + amount;
        try {
            Bundle bundle = new Bundle();
            return parseJSONResponse(WebService.POST(url, generateRequest(bundle)));
//            return parseJSONResponse(WebService.POSTRAWDATA(url, generateJson(email, password).toString()), true);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private RequestBody generateRequest(Bundle bundle) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                if (bundle.get(key) != null && !TextUtils.isEmpty(bundle.get(key).toString())) {
                    formBodyBuilder.add(key, bundle.get(key).toString());
                }
            }
        }
        return formBodyBuilder.build();
    }

    public boolean parseJSONResponse(final String response) {
        try {
            if (!TextUtils.isEmpty(response)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;

        }
    }

}
