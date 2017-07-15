package com.app.uniqesofttech.webservice;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.util.Const;
import com.app.uniqesofttech.util.Utils;
import com.app.uniqesofttech.util.WriteLog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.HttpUrl;

/**
 * Created by chandnichudasama on 17/06/17.
 */

public class GetCustomerData {
    private String message = "";
    private int success = 0;


    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }

    public ArrayList<CustomerModel> executeWebservice(String datetime, String userid) {

        String url = "http://refillapi.uniquesoftech.com/api/GetUploadingData?UserID=" + userid + "&Fetchingdate=" + datetime;
        try {
            return parseJSONResponse(WebService.GET(HttpUrl.parse(url)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<CustomerModel> parseJSONResponse(final String response) {
        ArrayList<CustomerModel> cuslist = new ArrayList<>();
        try {
            if (!TextUtils.isEmpty(response)) {
                DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_LASTUPDATE,Utils.getCurrentDate()).commit();
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CustomerModel customerModel = new CustomerModel();
                        customerModel.setCusid(jsonObject.getString("ConsumerID"));
                        customerModel.setName(jsonObject.getString("ConsumerName"));
                        customerModel.setAddress(jsonObject.getString("ConsumerAddress"));
                        customerModel.setCashMemoNo(jsonObject.getString("CashMemoNo"));
                        cuslist.add(customerModel);

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
