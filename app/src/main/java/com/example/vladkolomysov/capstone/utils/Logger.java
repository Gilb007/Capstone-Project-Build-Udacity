package com.example.vladkolomysov.capstone.utils;

/**
 * Created by vladislav_kolomysov
 */

import android.util.Log;
// to get logs
public class Logger {

    public static void log(String TAG,String msg){

        Log.wtf(TAG,msg);
    }

    public static void log(Exception e){

        e.printStackTrace();
    }
}
