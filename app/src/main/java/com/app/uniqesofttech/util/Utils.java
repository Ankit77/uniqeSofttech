package com.app.uniqesofttech.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.app.uniqesofttech.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ANKIT on 6/17/2017.
 */

public class Utils {
    public static ProgressDialog displayProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    /**
     * Dismiss current progress dialog
     *
     * @param dialog dialog
     */
    public static void dismissProgressDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public static boolean isNetworkAvailable(Context context) {

        boolean isNetAvailable = false;
        if (context != null) {
            final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mConnectivityManager != null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    final Network[] allNetworks = mConnectivityManager.getAllNetworks();

                    for (Network network : allNetworks) {
                        final NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(network);
                        if (networkInfo != null && networkInfo.isConnected()) {
                            isNetAvailable = true;
                            break;
                        }
                    }

                } else {
                    boolean wifiNetworkConnected = false;
                    boolean mobileNetworkConnected = false;

                    final NetworkInfo mobileInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    final NetworkInfo wifiInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mobileInfo != null) {
                        mobileNetworkConnected = mobileInfo.isConnected();
                    }
                    if (wifiInfo != null) {
                        wifiNetworkConnected = wifiInfo.isConnected();
                    }
                    isNetAvailable = (mobileNetworkConnected || wifiNetworkConnected);
                }
            }
        }
        return isNetAvailable;
    }

    public static String getAppVersion(Context context) {

        PackageInfo pInfo;
        try {

            if (context != null) {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return pInfo.versionName;
            } else
                return "0.0";

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    public static void displayDialog(final Context context, final String message) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setCancelable(false);

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        if (!((Activity) context).isFinishing()) {

            alertDialog.show();
        }
    }

    public static void addNextFragmentNoAnim(int containerid, Activity mActivity, Fragment targetedFragment, Fragment shooterFragment) {

        final FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
        transaction.add(containerid, targetedFragment, targetedFragment.getClass().getSimpleName());
        //curFragment = targetedFragment;
        transaction.hide(shooterFragment);
        transaction.addToBackStack(targetedFragment.getClass().getSimpleName());
        transaction.commit();
    }


    /**
     * Common replace fragent method
     *
     * @param containerid
     * @param mActivity
     * @param targetedFragment
     */
    public static void replaceNextFragment(int containerid, Activity mActivity, Fragment targetedFragment) {
        final FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
        transaction.replace(containerid, targetedFragment, targetedFragment.getClass().getSimpleName());
        //curFragment = targetedFragment;
        transaction.commit();
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
