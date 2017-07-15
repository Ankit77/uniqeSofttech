package com.app.uniqesofttech.webservice;

import android.content.Context;
import android.text.TextUtils;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.model.PaymentModel;
import com.app.uniqesofttech.util.Const;
import com.app.uniqesofttech.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.HttpUrl;

/**
 * Created by chandnichudasama on 17/06/17.
 */

public class GetPaymentMode {
    private String message = "";
    private int success = 0;


    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }

    public ArrayList<PaymentModel> executeWebservice() {

        String url = "http://refillapi.uniquesoftech.com/api/GetPaymentMode";
        try {
            return parseJSONResponse(WebService.GET(HttpUrl.parse(url)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<PaymentModel> parseJSONResponse(final String response) {
        ArrayList<PaymentModel> cuslist = new ArrayList<>();
        try {
            if (!TextUtils.isEmpty(response)) {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PaymentModel paymentModel = new PaymentModel();
                        paymentModel.setPaymentId(jsonObject.getString("ID"));
                        paymentModel.setPaymentmode(jsonObject.getString("PaymentMode"));
                        cuslist.add(paymentModel);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
        return cuslist;
    }

}
