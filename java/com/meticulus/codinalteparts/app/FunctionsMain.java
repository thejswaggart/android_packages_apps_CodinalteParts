package com.meticulus.codinalteparts.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

/**
 * Created by meticulus on 4/7/14.
 */
public class FunctionsMain {

    private static final String TAG = "Codinalte Parts";

    /* General */
    private static final String TEMP_DIR_CMD = "mkdir -p /data/local/tmp";

    /* Venturi Variant */

    private static final String VV_DAT_PATH= "/efs/buyer_code.dat";

    /* DoubleTap2wake */
    private static final String DT2W_PATH = "/sys/module/doubletap2wake/parameters";

    private static final String DT2W_ENABLE_PATH = DT2W_PATH + "/enable";

    private static final String DT2W_WAKELOCK_PATH = DT2W_PATH + "/use_wakelock";

    private static final String DT2W_ENABLE_CMD = "echo 1 > " + DT2W_ENABLE_PATH;

    private static final String DT2W_DISABLE_CMD = "echo 0 > " + DT2W_ENABLE_PATH;

    private static final String DT2W_GET_ENABLE_CMD = "cat " + DT2W_ENABLE_PATH;

    private static final String DT2W_ENABLE_WAKELOCK_CMD = "echo 1 > " + DT2W_WAKELOCK_PATH;

    private static final String DT2W_DISABLE_WAKELOCK_CMD = "echo 0 > " + DT2W_WAKELOCK_PATH;

    private static final String DT2W_GET_WAKELOCK_CMD = "cat " + DT2W_WAKELOCK_PATH;

    /* Sweep2wake */
    private static final String S2W_PATH = "/sys/module/sweep2wake/parameters";

    private static final String S2W_ENABLE_PATH = S2W_PATH + "/enable";

    private static final String S2W_WAKELOCK_PATH = S2W_PATH + "/use_wakelock";

    private static final String S2W_ENABLE_CMD = "echo 1 > " + S2W_ENABLE_PATH;

    private static final String S2W_DISABLE_CMD = "echo 0 > " + S2W_ENABLE_PATH;

    private static final String S2W_GET_ENABLE_CMD = "cat " + S2W_ENABLE_PATH;

    private static final String S2W_ENABLE_WAKELOCK_CMD = "echo 1 > " + S2W_WAKELOCK_PATH;

    private static final String S2W_DISABLE_WAKELOCK_CMD = "echo 0 > " + S2W_WAKELOCK_PATH;

    private static final String S2W_GET_WAKELOCK_CMD = "cat " + S2W_WAKELOCK_PATH;

    /* BLN */
    private static final String BLN_PATH = "/sys/devices/virtual/misc/backlightnotification";

    private static final String BLN_ENABLE_PATH = BLN_PATH + "/enabled";

    private static final String BLN_GET_ENABLE_CMD = "cat " + BLN_ENABLE_PATH;

    private static final String BLN_BLINKMODE_PATH = BLN_PATH + "/blink_mode";

    private static final String CMD_BLN_ENABLE = "echo 1 > " + BLN_ENABLE_PATH;

    private static final String CMD_BLN_DISABLE = "echo 0 > " + BLN_ENABLE_PATH;

    private static final String CMD_BLNBLINK_ENABLE = "echo 1 > " + BLN_BLINKMODE_PATH;

    private static final String CMD_BLNBLINK_DISABLE = "echo 0 > " + BLN_BLINKMODE_PATH;

    private static final String CMD_GET_BLNBLINK_ENABLE = "cat " + BLN_BLINKMODE_PATH;

    /* CPU2 Commands */
    private static final String CPU2_ONLINE_PATH = "/sys/devices/system/cpu/cpu1/online";

    private static final String CPU2_ENABLE_COMMAND = "echo 1 > " + CPU2_ONLINE_PATH;

    private static final String CPU2_DISABLE_COMMAND = "echo 0 >" + CPU2_ONLINE_PATH;

    /* Offline Charging */

    private static final String CHARGER_SETTINGS_PATH = "/data/misc/charger";

    private static final String CHARGER_SHOWDATETIME_PATH = CHARGER_SETTINGS_PATH + "/charger_show_datetime";

    private static final String CHARGER_SHOWDATETIME_ENABLE = "touch " + CHARGER_SHOWDATETIME_PATH;

    private static final String CHARGER_SHOWDATETIME_DISABLE = "rm " + CHARGER_SHOWDATETIME_PATH;

    private static final String CHARGER_NOSUSPEND_PATH = CHARGER_SETTINGS_PATH + "/charger_no_suspend";

    private static final String CHARGER_NOSUSPEND_ENABLE = "touch " + CHARGER_NOSUSPEND_PATH;

    private static final String CHARGER_NOSUSPEND_DISABLE = "rm " + CHARGER_NOSUSPEND_PATH;

    /* Bluetooth Commands */

    private static final String CMD_BTPAN_DHCP = "netcfg bt-pan dhcp";

    private static final String BTPAN_IFACE_NAME = "bt-pan";

    private static final String CMD_BTPAN_IP = "netcfg | grep " +BTPAN_IFACE_NAME +" | awk -F '/' " +
            "'{print $1}' | awk -F ' ' '{print $ 3}'";

    private static final String BTPAN_DNSMASQ_RESOLV_FILE = "/data/local/tmp/resolv.conf";

    private static final String CMD_DNS1 = "echo \"nameserver 8.8.8.8\" > " + BTPAN_DNSMASQ_RESOLV_FILE;

    private static final String CMD_DNS2 = "echo \"nameserver 8.8.4.4\" >> " + BTPAN_DNSMASQ_RESOLV_FILE;

    private static final String BTPAN_DNSMASQ_PID_FILE = "/data/local/tmp/dnsmasq.pid";

    private static final String CMD_DNSMASQ = "dnsmasq-meticulus -x " + BTPAN_DNSMASQ_PID_FILE + " -r " +
            BTPAN_DNSMASQ_RESOLV_FILE;

    private static final String CMD_KILL_DNSMASQ = "kill $(cat " + BTPAN_DNSMASQ_PID_FILE + ")";

    /* h264SoftDec */
    private static final String H264SOFTDEC_PROP = "vu.co.meticulus.h264switch";

    private static final String ENABLE_H264SOFTDEC = "/system/bin/setprop " + H264SOFTDEC_PROP +
            " true";
    private static final String DISABLE_H264SOFTDEC = "/system/bin/setprop " + H264SOFTDEC_PROP +
            " false";

    /* Logging Vars */
    private static final String CMD_KMSG = "cat /proc/kmsg | while read LINE;do " + "" +
            "DATE=$(busybox date -I); echo \"$(busybox date | cut -d ' ' -f5) - $LINE\" >> "+
            "/sdcard/autolog_kmsg_\"$DATE\".txt;done &";

    private static final String CMD_LOGCAT = "logcat | while read LINE;do "+
            "DATE=$(busybox date -I); echo \"$(busybox date | cut -d ' ' -f4) - $LINE\" >> "+
            "/sdcard/autolog_logcat_\"$DATE\".txt;done &";

    private static final String CMD_RILLOG = "cat /dev/log/radio | while read LINE;do "+
            "DATE=$(busybox date -I); echo \"$(busybox date | cut -d ' ' -f4) - $LINE\" >> "+
            "/sdcard/autolog_rillog_\"$DATE\".txt;done &";

    /* Low Memory Killer - Not Killable Processes  */
    private static final String LMKNKP_ENABLE_SYSPROCS = "echo 1 > /sys/module/lowmemorykiller/" +
            "parameters/donotkill_sysproc";

    private static final String LMKNKP_DISABLE_SYSPROCS = "echo 0 > /sys/module/lowmemorykiller/" +
            "parameters/donotkill_sysproc";

    private static final String LMKNKP_PROC_LIST_START = "echo ";

    private static final String LMKNKP_PROC_LIST_TAIL = " > /sys/module/lowmemorykiller/"+
            "parameters/donotkill_sysproc_names";

    private static final String LMKNKP_PROC_LIST = "wpa_supplicant,rild,at_core,at_distributor";


    public static void setVenturiVariantCode(String code){
        try{
            CommandUtility.ExecuteNoReturn("echo " + code + " > " +VV_DAT_PATH,true,false);
        }catch(Exception ex){ex.printStackTrace();}
    }
    public static String getVenturiVariantCode(){

        String retval = "ERR";
        try{
            retval = CommandUtility.ExecuteShellCommandTrimmed("cat " + VV_DAT_PATH,true,false);
        }catch(Exception ex){ex.printStackTrace();}
        return retval;

    }

    public static void setChargerShowDateTime(boolean enabled) {

        try {
            if (enabled) {
                Log.i(TAG, "Enabling ChargerShowDateTime");
                CommandUtility.ExecuteNoReturn("mkdir -p " + CHARGER_SETTINGS_PATH,true,false);
                CommandUtility.ExecuteNoReturn(CHARGER_SHOWDATETIME_ENABLE, true, false);
            } else {
                Log.i(TAG, "Disabling ChargerShowDateTime");
                CommandUtility.ExecuteNoReturn(CHARGER_SHOWDATETIME_DISABLE, true, false);
            }
        }catch(Exception ex){ex.printStackTrace();}

    }

    public static boolean getChargerShowDateTime() {

        return new File(CHARGER_SHOWDATETIME_PATH).exists();

    }
    public static void setChargerNoSuspend(boolean enabled) {

        try {
            if (enabled) {
                Log.i(TAG, "Enabling ChargerShowDateTime");
                CommandUtility.ExecuteNoReturn("mkdir -p " + CHARGER_SETTINGS_PATH,true,false);
                CommandUtility.ExecuteNoReturn(CHARGER_NOSUSPEND_ENABLE, true, false);
            } else {
                Log.i(TAG, "Disabling ChargerShowDateTime");
                CommandUtility.ExecuteNoReturn(CHARGER_NOSUSPEND_DISABLE, true, false);
            }
        }catch(Exception ex){ex.printStackTrace();}

    }

    public static boolean getChargerNoSuspend() {

        return new File(CHARGER_NOSUSPEND_PATH).exists();

    }

    public static void setSweep2Wake(boolean enabled){
        try {
            // Don't do this if the file doesn't exist!
            if(!new File(S2W_ENABLE_PATH).exists())
                return;

            if (enabled) {
                Log.i(TAG,"Enabling Sweep2Wake");
                CommandUtility.ExecuteNoReturn(S2W_ENABLE_CMD, true, false);
            }else {
                Log.i(TAG,"Disabling Sweep2Wake");
                CommandUtility.ExecuteNoReturn(S2W_DISABLE_CMD, true, false);
            }

            Log.i(TAG,"Sweep2Wake status: " + CommandUtility.ExecuteShellCommandTrimmed(S2W_GET_ENABLE_CMD,
                    false,false));
        } catch (Exception ex){ex.printStackTrace();}
    }

    public static void setDoubleTap2Wake(boolean enabled){
        try {
            // Don't do this if the file doesn't exist!
            if(!new File(DT2W_ENABLE_PATH).exists())
                return;

            if (enabled) {
                Log.i(TAG,"Enabling DoubleTap2Wake");
                CommandUtility.ExecuteNoReturn(DT2W_ENABLE_CMD, true, false);
            }else {
                Log.i(TAG,"Disabling DoubleTap2Wake");
                CommandUtility.ExecuteNoReturn(DT2W_DISABLE_CMD, true, false);
            }

            Log.i(TAG,"DoubleTap2Wake status: " + CommandUtility.ExecuteShellCommandTrimmed(DT2W_GET_ENABLE_CMD,
                    false, false));
        } catch (Exception ex){ex.printStackTrace();}
    }

    public static void setBLN(boolean enabled){
        try{
            if(enabled){
                Log.i(TAG,"Enabling BLN");
                CommandUtility.ExecuteNoReturn(CMD_BLN_ENABLE,false, false);
            } else {
                Log.i(TAG,"Disabling BLN");
                CommandUtility.ExecuteNoReturn(CMD_BLN_DISABLE, false, false);
            }

            Log.i(TAG,"BLN status: " + CommandUtility.ExecuteShellCommandTrimmed(BLN_GET_ENABLE_CMD,
                    false, false));
        }
        catch(Exception ex){ex.printStackTrace();}

    }

    public static void setBLNBlink(boolean enabled){
        try{
            if(enabled){
                Log.i(TAG,"Enabling BLN Blink");
                CommandUtility.ExecuteNoReturn(CMD_BLNBLINK_ENABLE,false, false);
            } else {
                Log.i(TAG,"Disabling BLN Blink");
                CommandUtility.ExecuteNoReturn(CMD_BLNBLINK_DISABLE, false, false);
            }
            Log.i(TAG,"BLNBlink status: " + CommandUtility.ExecuteShellCommandTrimmed(CMD_GET_BLNBLINK_ENABLE,
                    false,false));
        }
        catch(Exception ex){ex.printStackTrace();}

    }

    public static void setGoogleDNS(Context c,boolean enabled){
        ConnectivityManager connManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() && enabled) {
            runBTDNSMasq();
        }
        else{
            killBTDNSMasq();
        }

    }

    private static boolean isdnsmasqRunning(){
        boolean retval = false;
        String pid = "";
        try{
            pid = CommandUtility.ExecuteShellCommandTrimmed("ps | grep dnsmasq-meticulus | awk -F' ' '{print $2}'",false, false);
        }
        catch(Exception ex){ex.printStackTrace();}

        return !pid.equals("");
    }

    public static void startInCallAudioService(Context context)
    {
        Log.i(TAG,"Starting In Call Audio Service.");
        Intent intent = new Intent(context,InCallAudioService.class);
        context.startService(intent);
    }
    public static void startClockFreezeMonitorService(Context context)
    {
        Log.i(TAG,"Starting Clock Freeze Monitor Service.");
        Intent intent = new Intent(context,ClockFreezeMonitor.class);
        context.startService(intent);
    }

    public static void SetCPU2(Boolean enabled)
    {
        try{

            if(enabled)
            {
                Log.i(TAG,"Enabling CPU2");
                CommandUtility.ExecuteNoReturn(CPU2_ENABLE_COMMAND,true, false);
            }
            else
            {
                Log.i(TAG,"Disabling CPU2");
                CommandUtility.ExecuteNoReturn(CPU2_DISABLE_COMMAND, true, false);
            }
        }
        catch (Exception e){e.printStackTrace();}

    }

    public static Boolean areWeBtTetherClient()
    {
        Boolean retval = false;
        try{
            String btpanip = CommandUtility.ExecuteShellCommandTrimmed(CMD_BTPAN_IP, false, false);
            if(btpanip.equals("0.0.0.0"))
            {
                Log.i(TAG, "Found bt-pan ip " + btpanip);
                retval = true;
            }

        }
        catch (Exception e){e.printStackTrace();}
        return retval;

    }

    public static void runBTDHCP()
    {
        try{
            Log.i(TAG,"Running dhcp...");
            /*
            Try to make sure that the temporary dir exists first.
             */
            CommandUtility.ExecuteNoReturn(TEMP_DIR_CMD,true, false);
            CommandUtility.ExecuteNoReturn(CMD_BTPAN_DHCP, true, false);
        }
        catch (Exception e){e.printStackTrace();}
    } 
    public static void runBTDNSMasq()
    {
        try
        {

            if(isdnsmasqRunning()){
                killBTDNSMasq();
            }
            /*
            Try to make sure that the temporary dir exists first.
             */
            CommandUtility.ExecuteNoReturn(TEMP_DIR_CMD,true, false);
            /*
            Create resolv.conf file.
             */
            CommandUtility.ExecuteNoReturn(CMD_DNS1,true, false);
            CommandUtility.ExecuteNoReturn(CMD_DNS2, true, false);
            Log.i(TAG, "Running dnsmasq...");
            CommandUtility.ExecuteNoReturn(CMD_DNSMASQ, true, true);
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void killBTDNSMasq(){

        try{
            if(new File(BTPAN_DNSMASQ_PID_FILE).exists()) {
                CommandUtility.ExecuteNoReturn(CMD_KILL_DNSMASQ, true, false);
                Log.i(TAG, "Killing dnsmasq...");
            }
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void setH264SoftDec(boolean on){

        try{
            if(on) {
                CommandUtility.ExecuteNoReturn(ENABLE_H264SOFTDEC, true, false);
                Log.i(TAG, "Enabled h264softdec");
            }
            else {
                CommandUtility.ExecuteNoReturn(DISABLE_H264SOFTDEC, true, false);
                Log.i(TAG, "Disabled h264softdec");
            }
        }
        catch(Exception ex){ex.printStackTrace();}
    }

    public static void startAutokmsg()
    {
        try
        {
            Log.i(TAG, "Running auto kmsg...");
            CommandUtility.ExecuteNoReturn(CMD_KMSG,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }
    public static void startAutologcat()
    {
        try
        {
            Log.i(TAG, "Running auto logcat...");
            CommandUtility.ExecuteNoReturn(CMD_LOGCAT,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }
    public static void startAutorillog()
    {
        try
        {
            Log.i(TAG, "Running auto ril log...");
            CommandUtility.ExecuteNoReturn(CMD_RILLOG,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void enableLMKNKP()
    {
        try
        {
            Log.i(TAG, "Enabling Not Killable Processes...");
            CommandUtility.ExecuteNoReturn(LMKNKP_ENABLE_SYSPROCS,false, false);
        }
        catch(Exception e){e.printStackTrace();}
    }
    public static void disableLMKNKP()
    {
        try
        {
            Log.i(TAG, "Disabling Not Killable Processes...");
            CommandUtility.ExecuteNoReturn(LMKNKP_DISABLE_SYSPROCS,false, false);
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void setLMKNKPWhitelist()
    {
        try
        {
            Log.i(TAG, "Setting Not Killable Processes List...");
            CommandUtility.ExecuteNoReturn(LMKNKP_PROC_LIST_START + LMKNKP_PROC_LIST +
                    LMKNKP_PROC_LIST_TAIL,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }

    private static String create_random_mac() {
        String hex = "0123456789ABCDEF";
        String mac = "";
        Random rnd = new Random();
        while (mac.length() < 17) {
            int offset = rnd.nextInt(15);
            mac = mac + hex.substring(offset, offset + 1);
            offset = rnd.nextInt(15);
            mac = mac + hex.substring(offset, offset + 1);
            mac = mac + ":";
        }
        mac = mac.substring(0, 17);
        return mac;
    }

    public static String set_random_mac(Context context) {

        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (manager.isWifiEnabled()) {
                manager.setWifiEnabled(false);
                Thread.sleep(200);
            }
            String mac = create_random_mac();
            //CommandUtility.ExecuteNoReturn("rmmod dhd", true, false);
            CommandUtility.ExecuteNoReturn("printf " + mac + " > /sys/module/board_codina_sdi/parameters/wlan_mac", true, false);
            manager.setWifiEnabled(true);
	    Thread.sleep(2000);
	    if(manager.isWifiEnabled())	
            	return mac;
	    else
		return set_random_mac(context);
        }
        catch(Exception e){e.printStackTrace();}
        return "ERROR!";
    }

    public static void testProp()
    {
        Log.e(TAG, "Device is: " + Build.MODEL);
    }
}
