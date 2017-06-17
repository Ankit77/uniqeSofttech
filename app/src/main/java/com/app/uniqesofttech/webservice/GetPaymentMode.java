package com.app.uniqesofttech.webservice;

import android.content.Context;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.model.PaymentModel;

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

public class GetPaymentMode {
    private String message;
    private DelTrackApp delTrackApp;
    private Context context;

    public String getMessage() {
        return message;
    }

    public ArrayList<PaymentModel> executePaymentMode(String murl, Context context) {
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
            return parseResponse(stream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<PaymentModel> parseResponse(InputStream stream) {
        ArrayList<PaymentModel> paymentList = new ArrayList<>();
        PaymentModel paymentModel = null;
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
                        if (tagname.equalsIgnoreCase("api_getPaymentMode_Result")) {
                            paymentModel = new PaymentModel();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("ID")) {
                            paymentModel.setPaymentId(text);
                        } else if (tagname.equalsIgnoreCase("PaymentMode")) {
                            paymentModel.setPaymentmode(text);
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
        return paymentList;
    }

}
