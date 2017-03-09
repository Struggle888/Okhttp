package com.lpf.hasee.okhttp;

import android.util.Log;

/**
 * Created by hasee on 2017/3/8.
 */

public class LogUtils {

    private static final String TAG = "info";
    private static boolean debug = true;

    public static void i(String msg){

        if (debug){
            Log.i(TAG, msg);
        }
    }

}
