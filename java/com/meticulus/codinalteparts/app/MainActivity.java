package com.meticulus.codinalteparts.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.meticulus.codinalteparts.app.FunctionsMain;

public class MainActivity extends Activity {

    TextView kernel, audio, charger, networking, debugging; /* Headers */

    Switch otg, /* Kernel */
            charger_show_datetime, charger_no_suspend, /* Charger */
            autologcat, autokmsg, autoril; /* Debugging */

    ImageView whatis_otg, /* Kernel */
            whatis_charger_show_datetime, whatis_charger_no_suspend, /* Charger */
            whatis_autologcat,whatis_autokmsg, whatis_autorillog; /* Debugging */

    LinearLayout otg_layout, /* Kernel */
            charger_show_datetime_layout, charger_no_suspend_layout, /* Charger */
            autologcat_layout, autokmsg_layout, autoril_layout; /* Debugging */

    SharedPreferences sharedPref;
    String device =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.activity_main);

        /* Headers */
        kernel = (TextView) findViewById(R.id.kernel_textview);
        charger = (TextView) findViewById(R.id.charger_textview);
        debugging = (TextView) findViewById(R.id.debugging_textview);

        /* Assign all switches */
        otg = (Switch) findViewById((R.id.switch_otg)); 
        charger_show_datetime = (Switch) findViewById(R.id.switch_charger_show_datetime);
        charger_no_suspend = (Switch) findViewById(R.id.switch_charger_no_suspend);
        autologcat = (Switch) findViewById(R.id.switch_autologcat);
        autokmsg = (Switch) findViewById(R.id.switch_autokmsg);
        autoril = (Switch)findViewById(R.id.switch_autorillog);

        /* Assign all switches onCheckChanged*/ 
        otg.setOnCheckedChangeListener(switchListener);
        charger_show_datetime.setOnCheckedChangeListener(switchListener);
        charger_no_suspend.setOnCheckedChangeListener(switchListener);
        autologcat.setOnCheckedChangeListener(switchListener);
        autokmsg.setOnCheckedChangeListener(switchListener);
        autoril.setOnCheckedChangeListener(switchListener);

	whatis_otg = (ImageView) findViewById(R.id.whatis_otg);
        whatis_otg.setOnClickListener(switchClickListener);

        whatis_charger_show_datetime = (ImageView) findViewById(R.id.what_is_charger_show_datetime);
        whatis_charger_show_datetime.setOnClickListener(switchClickListener);

        whatis_charger_no_suspend = (ImageView) findViewById(R.id.what_is_charger_no_suspend);
        whatis_charger_no_suspend.setOnClickListener(switchClickListener);

        whatis_autologcat = (ImageView) findViewById(R.id.whatis_autologcat);
        whatis_autologcat.setOnClickListener(switchClickListener);

        whatis_autokmsg = (ImageView) findViewById(R.id.whatis_autokmsg);
        whatis_autokmsg.setOnClickListener(switchClickListener);

        whatis_autorillog = (ImageView) findViewById(R.id.whatis_autorillog);
        whatis_autorillog.setOnClickListener(switchClickListener);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
 
	otg_layout = (LinearLayout) findViewById(R.id.otg_layout);
        charger_show_datetime_layout = (LinearLayout) findViewById(R.id.charger_show_datetime_layout);
        charger_no_suspend_layout = (LinearLayout) findViewById(R.id.charger_no_suspend_layout); 

        prepareUI();
 
    }

    private void prepareUI(){ 
        charger_show_datetime.setChecked(FunctionsMain.getChargerShowDateTime());
        charger_no_suspend.setChecked(FunctionsMain.getChargerNoSuspend());
        autologcat.setChecked(sharedPref.getBoolean("autologcat",false));
        autokmsg.setChecked(sharedPref.getBoolean("autokmsg",false));
        autoril.setChecked(sharedPref.getBoolean("autoril",false));
	if(FunctionsMain.read_usb_reset() == 1)
	    otg.setChecked(true);
	else
	    otg.setChecked(false);

    }

    private View.OnClickListener switchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ImageView thisSwitch = (ImageView)view;
            if(thisSwitch == whatis_otg){
                ShowDialog("USB Host Mode",getString(R.string.otg_desc));
            }
            else if(thisSwitch == whatis_charger_show_datetime){
                ShowDialog("Date and Time in Charger",getString(R.string.charger_showdatetime_desc));
            }
            else if(thisSwitch == whatis_charger_no_suspend){
                ShowDialog("No Suspend in Charger",getString(R.string.charger_nosuspend_desc));
            }
            else if(thisSwitch == whatis_autologcat){
                ShowDialog("Auto Logcat",getString(R.string.autologcat_desc));
            }
            else if(thisSwitch == whatis_autokmsg){
                ShowDialog("Auto kmsg",getString(R.string.autokmsg_desc));
            }
            else if(thisSwitch == whatis_autorillog){
                ShowDialog("Auto kmsg",getString(R.string.autorillog_desc));
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            Switch thisSwitch = (Switch)compoundButton;
            SharedPreferences.Editor editor = sharedPref.edit();
            if(thisSwitch == otg) { 
                try {
		    if(FunctionsMain.read_usb_reset() < 2)
                    	FunctionsMain.set_otg(b);
		    else {
			ShowDialog("Reboot Required","You'll need to reboot before using this again");
		    	otg.setChecked(false);
			otg.setEnabled(false);
		    }
                }
                catch(Exception e){e.printStackTrace();}
	    }
            else if(thisSwitch == charger_show_datetime){
                FunctionsMain.setChargerShowDateTime(b);
            }
            else if(thisSwitch == charger_no_suspend){
                FunctionsMain.setChargerNoSuspend(b);
            } 
            else if(thisSwitch == autologcat){
                if(b != sharedPref.getBoolean("autologcat",false))
                {
                    if(b)
                        ShowDialog("Auto logcat","Will be started on the next reboot.");
                    else
                        ShowDialog("Auto logcat","Will NOT be started on the next reboot. "+
                                "Still running till then");
                }
                editor.putBoolean("autologcat", b);
            }
            else if(thisSwitch == autokmsg){
                if(b != sharedPref.getBoolean("autokmsg",false))
                {
                    if(b)
                        ShowDialog("Auto kmsg","Will be started on the next reboot.");
                    else
                        ShowDialog("Auto kmsg","Will NOT be started on the next reboot. "+
                                "Still running till then");
                }
                editor.putBoolean("autokmsg", b);
            }
            else if(thisSwitch == autoril){
                if(b != sharedPref.getBoolean("autoril",false))
                {
                    if(b)
                        ShowDialog("Auto RIL log","Will be started on the next reboot.");
                    else
                        ShowDialog("Auto RIL log","Will NOT be started on the next reboot. "+
                                "Still running till then");
                }
                editor.putBoolean("autoril", b);
            }
            editor.apply();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public AlertDialog ShowDialog(String title,String message)
    {
        return ShowDialog(title,message,true);
    }
    public AlertDialog ShowDialog(String title,String message, boolean okbtn)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
	if(okbtn)
            builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
	return dialog;
    }

    public ProgressDialog ShowIdProgDialog(String title,String message) {
	ProgressDialog pdialog = new ProgressDialog(this);
	pdialog.setTitle(title);
	pdialog.setMessage(message);
	pdialog.setIndeterminate(true);
	pdialog.setCancelable(false);
	pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	return pdialog;
    }
}
