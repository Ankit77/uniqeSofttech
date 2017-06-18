package com.app.uniqesofttech.webservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.util.Const;
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

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;

/**
 * Created by ANKIT on 6/17/2017.
 */

public class WSLogin {
    private String message = "";
    private int success = 0;


    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }

    public boolean executeWebservice(String username, String password) {

        final String url = "http://refillapi.uniquesoftech.com/api/GetUser?user=" + username + "&password=" + password;
        try {
            Bundle bundle = new Bundle();
            return parseJSONResponse(WebService.GET(HttpUrl.parse(url)));
//            return parseJSONResponse(WebService.POSTRAWDATA(url, generateJson(email, password).toString()), true);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean parseJSONResponse(final String response) {
        try {
            if (!TextUtils.isEmpty(response)) {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject != null) {
                        DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_USERNAME, jsonObject.getString("UserName")).commit();
                        DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_DEALERCODE, jsonObject.getString("DealerCode")).commit();
                        DelTrackApp.getInstance().getSharedPreferences().edit().putBoolean(Const.PREF_ISREGISTER, true).commit();
                        return true;
                    } else {
                        return false;
                    }
                } else {

                    DelTrackApp.getInstance().getSharedPreferences().edit().putBoolean(Const.PREF_ISREGISTER, true).commit();
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
