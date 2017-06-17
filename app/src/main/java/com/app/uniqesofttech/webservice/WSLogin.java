package com.app.uniqesofttech.webservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.util.Const;

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

import okhttp3.HttpUrl;

/**
 * Created by ANKIT on 6/17/2017.
 */

public class WSLogin {
    private String message;
    private DelTrackApp delTrackApp;
    private Context context;

    public String getMessage() {
        return message;
    }

    public boolean executeTown(String murl, Context context) {
        URL url = null;
        this.context = context;
        delTrackApp = (DelTrackApp) context.getApplicationContext();
        try {
            url = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();
            return isLogout(stream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isLogout(InputStream stream) {
        boolean isLogout = false;
        String text = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(stream, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("DealerCode")) {
                            delTrackApp.getSharedPreferences().edit().putString(Const.PREF_DEALERCODE, text).commit();
                            // e_sampark.getSharedPreferences().edit().putInt(Const.PREF_CHECKIN_METER, 400).commit();
                        } else if (tagname.equalsIgnoreCase("UserName")) {
                            delTrackApp.getSharedPreferences().edit().putString(Const.PREF_USERNAME, text).commit();
                            delTrackApp.getSharedPreferences().edit().putBoolean(Const.PREF_ISREGISTER, true).commit();
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isLogout;
    }


}
