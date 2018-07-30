package com.modules.jules.microcredit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by brunelljosny on 26/04/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this,SlashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,mainIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(remoteMessage.getNotification().getTitle());
        notification.setContentText(remoteMessage.getNotification().getBody());
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.mimio);
        notification.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification.build());

        Map<String, String> data = remoteMessage.getData();
        if (data != null)
        {
            String desc= data.get("caution");
            if (desc != null) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("caution", desc);
                editor.apply();

                Bundle go = new Bundle();
                go.putString("type", "caution");
                mainIntent.putExtras(go);
            }

            String code= data.get("valide");
            if (code != null) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("valide", "1");
                    editor.apply();

            }
        }
    }
}
