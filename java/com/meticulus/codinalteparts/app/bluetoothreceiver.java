package com.meticulus.codinalteparts.app;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by meticulus on 4/8/14.
 */
public class bluetoothreceiver extends BroadcastReceiver {

    SharedPreferences sharedPref;
    public void onReceive(Context arg0, Intent arg1)
    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(arg0.getApplicationContext());

        if(!sharedPref.getBoolean("bttether", arg0.getResources().getBoolean(R.bool.bttether_default_enabled)))
            return;

        if(arg1.getAction().equals("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED") )
        {
            int connected = arg1.getExtras().getInt("android.bluetooth.adapter.extra.CONNECTION_STATE");
            if(connected == 2)
            {
                if(FunctionsMain.areWeBtTetherClient())
                {
                    FunctionsMain.runBTDHCP();
                    FunctionsMain.runBTDNSMasq();
                }
            }
            else {
                FunctionsMain.killBTDNSMasq();
            }
        }
        else if(arg1.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED"))
        {
            if(arg1.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF)
                FunctionsMain.killBTDNSMasq();
        }
    }
}
