package com.modules.jules.microcredit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsFiltre extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /*
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            if(sender.equals("AirtelMoney")){
                String messageBody = smsMessage.getMessageBody();

                String[] decoupage = messageBody.split(" ");
                String montant = decoupage[7];
                String tel = decoupage[10];


            }
        }

        */

        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            final SharedPreferences m = context.getSharedPreferences("wait", 0);
            if (m.getString("tel",null) != null)
            {
                String sender = smsMessage.getDisplayOriginatingAddress();
                if(sender.contains(m.getString("tel",null))){
                    String messageBody = smsMessage.getMessageBody();

                        final SharedPreferences p = context.getSharedPreferences("infos", 0);
                        SharedPreferences.Editor editor = p.edit();
                        editor.putString("code","1");
                        editor.apply();
                        Intent intentu = new Intent(context,Code.class);
                        intentu.putExtra("sms",messageBody);
                        intentu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intentu);

                }
            }
        }

    }

}
