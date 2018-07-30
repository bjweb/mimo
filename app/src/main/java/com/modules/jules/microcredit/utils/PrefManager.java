package com.modules.jules.microcredit.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;


public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    //Name of preference
    private static final String PREF_NAME           = "DATA"        ;

    //Config app prefrence


    public static final String IS_FIRST_TIME_LAUNCH = "FIRST_LAUNCH" ;//state to know if it's first time that launch app


    public static final String INTERNET_PERMISSION  = "INTERNET_PERMISSION";
    public static final String CAMERA_PERMISSION    = "CAMERA_PERMISSION"  ;

    public static PrefManager getInstance(Context context){
        return new PrefManager(context);
    }

    private PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void putBoolean(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public void putString(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    public void putGrantedRequest(String key,Set<String> value){
        editor.putStringSet(key,value);
        editor.commit();
    }

    public void putInt(String key , int value){
        editor.putInt(key,value);
        editor.commit();
    }


    public void putLong(String key , long value){
        editor.putLong(key,value);
        editor.commit();
    }

    public boolean getBoolean(String key,boolean value){
        return pref.getBoolean(key,value);
    }

    public int getInt(String key){
        return pref.getInt(key,0);
    }

    public long getLong(String key){
        return pref.getLong(key,0);
    }

    public String getString(String key,String value){
        return pref.getString(key,value);
    }



}



