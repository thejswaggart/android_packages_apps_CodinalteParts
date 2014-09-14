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
    static int BUFF_LEN = 1024 * 100;

    public static void ExecuteNoReturn(String command, Boolean useroot) throws Exception {
        Process p;
        DataOutputStream os;

        if(useroot)
            p = Runtime.getRuntime().exec(new String[]{"su"});
        else
            p = Runtime.getRuntime().exec(new String[]{"sh"});

        os = new DataOutputStream(p.getOutputStream());

        os.writeBytes(command+"\n");
        os.writeBytes("exit\n");
        os.flush();
        os.close();

        p.waitFor();
    }

    public static Process ExecuteReturnProcess(String command) throws Exception {
        Process p = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        os.writeBytes(command + "\n");
        return p;
    }

    private static byte[] ExecuteCommand(String command, Boolean useroot) throws IOException
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

        if(useroot)
            p = Runtime.getRuntime().exec(new String[]{"su"});
        else
            p = Runtime.getRuntime().exec(new String[]{"sh"});

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
        return baos.toByteArray();
    }
    public static String ExecuteShellCommand(String command, Boolean useroot) throws IOException
    {
        byte[] bytes = ExecuteCommand(command, useroot);
        String tmp = new String(bytes);
        if(tmp.equals("#null#\n"))
            tmp = "";
        return tmp;
    }
    public static String ExecuteShellCommandTrimmed(String command, Boolean useroot) throws IOException
    {
        /* This function is just here to trim off the last '\n' */
        String tmp = ExecuteShellCommand(command, useroot);
        if(tmp.length() > 0)
            tmp = tmp.substring(0,tmp.length() -1);
        return tmp;
    }
}
