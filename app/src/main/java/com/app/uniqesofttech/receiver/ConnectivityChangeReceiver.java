package com.app.uniqesofttech.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.app.uniqesofttech.DelTrackApp;
import com.app.uniqesofttech.model.SyncModel;
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
                        ArrayList<SyncModel> syncList= DelTrackApp.getInstance().getDatabaseHelper().getSyncList();
                        if(syncList!=null && syncList.size()>0)
                        {
                            for (int i=0;i<syncList.size();i++)
                            {
                                SyncModel syncModel=syncList.get(i);
                                WSDeliveryData wsDeliveryData=new WSDeliveryData();
                               boolean iscuseess= wsDeliveryData.executeWebservice(syncModel.getDealercode(),syncModel.getCashmemono(),syncModel.getPaymentmode(),syncModel.getAmount());
                                if(iscuseess)
                                {
                                    DelTrackApp.getInstance().getDatabaseHelper().deleteSyncData(syncModel.getId());
                                }else
                                {
                                    DelTrackApp.getInstance().getDatabaseHelper().insertSync(syncModel);
                                    DelTrackApp.getInstance().getDatabaseHelper().deleteSyncData(syncModel.getId());

                                }

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
}
