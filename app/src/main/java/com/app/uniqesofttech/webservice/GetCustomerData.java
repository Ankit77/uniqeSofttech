package com.app.uniqesofttech.webservice;

import android.content.Context;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.util.Const;
import com.app.uniqesofttech.util.Utils;

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

/**
 * Created by chandnichudasama on 17/06/17.
 */

public class GetCustomerData {
    private String message;
    private DelTrackApp delTrackApp;
    private Context context;

    public String getMessage() {
        return message;
    }

    public ArrayList<CustomerModel> executeTown(String murl, Context context) {
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
            DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_LASTUPDATE, Utils.getCurrentDate()).commit();
            return parseResponse(stream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_LASTUPDATE, Const.PREF_DEFAULT_DATETIME).commit();
            return null;

        } catch (ProtocolException e) {
            e.printStackTrace();
            DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_LASTUPDATE, Const.PREF_DEFAULT_DATETIME).commit();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_LASTUPDATE, Const.PREF_DEFAULT_DATETIME).commit();
            return null;
        }
    }

    public ArrayList<CustomerModel> parseResponse(InputStream stream) {

        ArrayList<CustomerModel> cusList = new ArrayList<>();
        CustomerModel customerModel = null;
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
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("FetchRemainingData_Result")) {
                            customerModel = new CustomerModel();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("ConsumerAddress")) {
                            customerModel.setAddress(text);
                        } else if (tagname.equalsIgnoreCase("ConsumerID")) {
                            customerModel.setCusid(text);
                        } else if (tagname.equalsIgnoreCase("ConsumerName")) {
                            customerModel.setName(text);
                        } else if (tagname.equalsIgnoreCase("FetchRemainingData_Result")) {
                            cusList.add(customerModel);

                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_LASTUPDATE, Const.PREF_DEFAULT_DATETIME).commit();
        } catch (IOException e) {
            e.printStackTrace();
            DelTrackApp.getInstance().getSharedPreferences().edit().putString(Const.PREF_LASTUPDATE, Const.PREF_DEFAULT_DATETIME).commit();
        }
        return cusList;
    }

}
