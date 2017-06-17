package com.app.uniqesofttech.webservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebService {
    public static OkHttpClient client;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static boolean isNetworkAvailable(final Context context) {
        boolean isNetAvailable = false;
        if (context != null) {
            final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager != null) {
                final NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                if (activeNetwork != null) isNetAvailable = true;
            }
        }
        return isNetAvailable;
    }

    public static String POST(String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        return response.body().string();
    }
    public static String POSTRAWDATA(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        return response.body().string();
    }
    public static String GET(HttpUrl url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        return response.body().string();
    }
    public static String GETWithHeader(HttpUrl url,String headername,String headervalue) throws IOException {
        Request request = new Request.Builder()
                .url(url).addHeader(headername,headervalue)
                .get()
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        return response.body().string();
    }

    public static String callServiceHttpPostBulk(String url, String json) {
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = new OkHttpClient().newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }





}
