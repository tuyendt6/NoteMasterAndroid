package com.rikkie.noteapp.config;

/**
 * Created by tuyenpx on 26/04/2016.
 */


public class Log {

    private static boolean DEBUG = true;

    public static void E(String TAG , String err){
        if(DEBUG){
            android.util.Log.e(TAG,err);
        }
    }

    public static void I(String TAG,String err){
        if(DEBUG){
            android.util.Log.i(TAG,err);
        }
    }

    public static void D(String TAG,String err){
        if(DEBUG){
            android.util.Log.d(TAG,err);
        }
    }
}
