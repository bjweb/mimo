package com.modules.jules.microcredit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public final class Func {

    private static int counter = 1                   ;
    private static boolean  isTypePhoneNumber = false;

    public static void ToastLong(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void ToastShort(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static boolean isEmptyArrayString(String ...str){
        for(String msg:str){
            if(msg.trim().isEmpty()){
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String msg){
        if(msg.trim().isEmpty()){
            return true;
        }
        return false;
    }

    public static void startService(Activity activity, Class classe){
        Intent intent = new Intent(activity.getApplicationContext(),classe);
        activity.startService(intent);
    }

    public static void launchActivity(Activity activity, Class classe){
        Intent intent = new Intent(activity.getApplicationContext(),classe);
        activity.startActivity(intent);
    }

    public static void launchActivityAndDestroyEmitter(Activity activity, Class classe){
        Intent intent = new Intent(activity.getApplicationContext(),classe);
        activity.startActivity(intent);
        activity.finish();
    }


    public static void call(Activity activity,String number){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        activity.startActivity(callIntent);
    }


    public static void changeTypeFace(Activity activity, TextView source , String font){
        Resources resource   = activity.getResources();
        Typeface typeface    = Typeface.createFromAsset(activity.getAssets(),font);
        source.setTypeface(typeface);
    }

    public static void changeTypeFace(Activity activity, Button source , String font){
        Resources resource   = activity.getResources();
        Typeface typeface    = Typeface.createFromAsset(activity.getAssets(),font);
        source.setTypeface(typeface);
    }

    public static void changeTypeFace(Activity activity, AutoCompleteTextView source , String font){
        Resources resource   = activity.getResources();
        Typeface typeface    = Typeface.createFromAsset(activity.getAssets(),font);
        source.setTypeface(typeface);
    }

    public static void NotifiySound(Context context){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    public static File getOutPutMediaFile(){
        File mediaStorage = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"creditgabon"
        );

        if(!mediaStorage.exists()){
            if(!mediaStorage.mkdir()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyMMMdd_HHmmss").format(new Date());
        return new File(mediaStorage.getPath()+File.separator+"IMG_"+timeStamp+".JPG");

    }


}