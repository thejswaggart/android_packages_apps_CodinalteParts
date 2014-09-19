package com.meticulus.codinalteparts.app;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by meticulus on 4/7/14.
 */
public class CommandUtility {

    static String TAG = "Command Utility";
    private static int BUFF_LEN = 1024 * 100;
    private static Process sh = null;
    private static Process su = null;

    public static void ExecuteNoReturn(String command, Boolean useroot, boolean forcenew) throws Exception {

        Process p;
        DataOutputStream os;

        p = getProcess(useroot,forcenew);

        os = new DataOutputStream(p.getOutputStream());

        os.writeBytes(command+"\n");

        if(forcenew) {
            os.writeBytes("exit\n");
            os.flush();
            os.close();
        }

        //p.waitFor();
    }

    private static Process getProcess(boolean useroot, boolean forcenew)throws Exception{

        if(forcenew && useroot)
            return Runtime.getRuntime().exec(new String[]{"su"});
        else if(forcenew && !useroot)
            return Runtime.getRuntime().exec(new String[]{"sh"});
        else if(useroot && su !=null)
            return su;
        else if(useroot && su == null) {
                su = Runtime.getRuntime().exec(new String[]{"su"});
                return su;
            }
        else if(!useroot && sh != null)
            return sh;
        else if(!useroot && sh == null) {
                sh = Runtime.getRuntime().exec(new String[]{"sh"});
                return sh;
            }

        /* Shouldn't ever actually get here! */
        return Runtime.getRuntime().exec(new String[]{"sh"});

    }

    private static byte[] ExecuteCommand(String command, Boolean useroot, boolean forcenew) throws Exception
    {
        Process p;
        DataOutputStream stdin;
        InputStream stdout;
        ByteArrayOutputStream baos;
        int read;
        byte[] buffer;
        /*
         * If for any reason the command does not print anything we are stuck forever.
         * Make sure that we print SOMETHING ALWAYS!
         */
        command = "RESULT=$(" + command + "); if [[ $RESULT == '' ]]; then echo '#null#';else echo $RESULT;fi\n";

        p = getProcess(useroot, forcenew);

        stdin = new DataOutputStream(p.getOutputStream());
        stdout = p.getInputStream();
        buffer = new byte[BUFF_LEN];
        baos = new ByteArrayOutputStream();

        stdin.writeBytes(command);

        while(true){
            read = stdout.read(buffer);
            baos.write(buffer, 0, read);
            if(read < BUFF_LEN){
                //we have read everything
                break;
            }
        }
        if(forcenew) {
            stdin.writeBytes("exit\n");
            stdin.flush();
            stdin.close();
        }

        //p.waitFor();

        return baos.toByteArray();
    }
    public static String ExecuteShellCommand(String command, Boolean useroot,boolean forcenew) throws Exception
    {
        byte[] bytes = ExecuteCommand(command, useroot, forcenew);
        String tmp = new String(bytes);
        if(tmp.equals("#null#\n"))
            tmp = "";
        return tmp;
    }
    public static String ExecuteShellCommandTrimmed(String command, Boolean useroot, boolean forcenew) throws Exception
    {
        /* This function is just here to trim off the last '\n' */
        String tmp = ExecuteShellCommand(command, useroot, forcenew);
        if(tmp.length() > 0)
            tmp = tmp.substring(0,tmp.length() -1);
        return tmp;
    }

}
