package com.meticulus.codinalteparts.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by meticulus on 9/15/14.
 */
public class wifireceiver extends BroadcastReceiver {

    SharedPreferences sharedPref;
    @Override
    public void onReceive(Context arg0, Intent arg1) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(arg0.getApplicationContext());

        if(sharedPref.getBoolean("googledns", arg0.getResources().getBoolean(R.bool.googledns_default_enabled))) {
            if (arg1.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo =
                        arg1.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected())
                    FunctionsMain.setGoogleDNS(arg0,true);
                else
                    FunctionsMain.setGoogleDNS(arg0,false);
            }
        }
    }
}
