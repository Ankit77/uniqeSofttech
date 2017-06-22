package com.app.uniqesofttech.webservice;

import android.os.Bundle;
import android.text.TextUtils;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;

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
                        DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_USERID, jsonObject.getString("UserID")).commit();
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
