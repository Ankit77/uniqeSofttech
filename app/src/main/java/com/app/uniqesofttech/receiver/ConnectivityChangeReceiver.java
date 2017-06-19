package com.app.uniqesofttech.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.model.SyncModel;
import com.app.uniqesofttech.util.Const;
import com.app.uniqesofttech.util.WriteLog;
import com.app.uniqesofttech.webservice.WSDeliveryData;

import java.util.ArrayList;

/**
 * Created by chandnichudasama on 18/06/17.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = intent
                .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (networkInfo != null) {

            // do subroutines here
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                //get the different network states
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    WriteLog.E(ConnectivityChangeReceiver.class.getSimpleName(), "Connect");
                    if (isNetworkAvailable(context)) {
                        ArrayList<SyncModel> syncList = DelTrackApp.getInstance().getDatabaseHelper().getSyncList();
                        if (syncList != null && syncList.size() > 0) {
                            for (int i = 0; i < syncList.size(); i++) {
                                SyncModel syncModel = syncList.get(i);
                                AsyncSubmitData asyncSubmitData = new AsyncSubmitData();
                                asyncSubmitData.execute(syncModel.getCashmemono(), syncModel.getPaymentmode(), syncModel.getAmount(), syncModel.getId());
                            }
                        }
                    }
                } else {
                    WriteLog.E(ConnectivityChangeReceiver.class.getSimpleName(), "disconnect");
                }
            }


        }

    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private class AsyncSubmitData extends AsyncTask<String, Void, Boolean> {
        private String syncId;
        private String dealercode;
        private String cashmemono;
        private String mpaymentmode;
        private String amount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            dealercode = DelTrackApp.getInstance().getSharedPreferences().getString(Const.PREF_DEALERCODE, "");
            cashmemono = params[0];
            mpaymentmode = params[1];
            amount = params[2];
            syncId = params[3];
            WSDeliveryData wsDeliveryData = new WSDeliveryData();
            return wsDeliveryData.executeWebservice(dealercode, cashmemono, mpaymentmode, amount);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean) {
                SyncModel syncModel = new SyncModel();
                syncModel.setDealercode(dealercode);
                syncModel.setCashmemono(cashmemono);
                syncModel.setPaymentmode(mpaymentmode);
                syncModel.setAmount(amount);
                DelTrackApp.getInstance().getDatabaseHelper().insertSync(syncModel);
                DelTrackApp.getInstance().getDatabaseHelper().deleteSyncData(syncId);
            } else {
                DelTrackApp.getInstance().getDatabaseHelper().deleteSyncData(syncId);
            }
        }
    }

}
