package com.meticulus.codinalteparts.app;

/**
 * Created by meticulus on 4/7/14.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.meticulus.codinalteparts.app.FunctionsMain;


public class bootreceiver extends BroadcastReceiver  {

    SharedPreferences sharedPref;
    public void onReceive(Context arg0, Intent arg1)
    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(arg0.getApplicationContext());
        //If incallaudio is selected that start service at boot.
        if(sharedPref.getBoolean("incallaudio", arg0.getResources().getBoolean(R.bool.incallaudio_default_enabled)))
        {
            FunctionsMain.startInCallAudioService(arg0.getApplicationContext());
        }
        //If clockfreeze is selected that start service at boot.
        if(sharedPref.getBoolean("clockfreeze", arg0.getResources().getBoolean(R.bool.clockfreeze_default_enabled)))
        {
            FunctionsMain.startClockFreezeMonitorService(arg0.getApplicationContext());
        }
        //If sweep2wake is selected that start service at boot.
        if(sharedPref.getBoolean("sweep2wake", arg0.getResources().getBoolean(R.bool.clockfreeze_default_enabled)))
        {
            FunctionsMain.setSweep2Wake(true);
        }

        if(sharedPref.getBoolean("h264softdec", arg0.getResources().getBoolean(R.bool.h264softdec_default_enabled)))
        {
            FunctionsMain.setH264SoftDec(true);
        }

        if(sharedPref.getBoolean("bln", arg0.getResources().getBoolean(R.bool.bln_default_enabled)))
        {
            FunctionsMain.setBLN(true);
        }
        if(sharedPref.getBoolean("blnblink", arg0.getResources().getBoolean(R.bool.blnblink_default_enabled)))
        {
            FunctionsMain.setBLNBlink(true);
        }

        if(sharedPref.getBoolean("googledns", arg0.getResources().getBoolean(R.bool.googledns_default_enabled)))
        {
            FunctionsMain.setGoogleDNS(arg0,true);
        }

        //Set cpu2 on or off
        //Setting it on if it's already on should make no difference.
        FunctionsMain.SetCPU2(sharedPref.getBoolean("cpu2",true));

        if(sharedPref.getBoolean("LKMNKP", arg0.getResources().getBoolean(R.bool.LMKNKP_default_enabled)))
        {
            FunctionsMain.enableLMKNKP();
            FunctionsMain.setLMKNKPWhitelist();;
        }

        if(sharedPref.getBoolean("autologcat", false))
            FunctionsMain.startAutologcat();

        if(sharedPref.getBoolean("autokmsg", false))
            FunctionsMain.startAutokmsg();
        if(sharedPref.getBoolean("autoril",false))
            FunctionsMain.startAutorillog();


    }
}
