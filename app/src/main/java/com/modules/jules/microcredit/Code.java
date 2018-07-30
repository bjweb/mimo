package com.modules.jules.microcredit;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.modules.jules.microcredit.utils.Func;

import java.util.Random;

public class Code extends AppCompatActivity {
    String code,email,phone,sms;
    int i1;
    private Button valid,cancel;
    EditText cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        final SharedPreferences pi = getSharedPreferences("wait", 0);
        email = pi.getString("mail",null);
        phone = pi.getString("tel",null);

        final SharedPreferences pp = getApplicationContext().getSharedPreferences("infos", 0);
        SharedPreferences.Editor editorp = pp.edit();
        editorp.putString("code","0");
        editorp.apply();

        final SharedPreferences po = getApplicationContext().getSharedPreferences("infos", 0);
        if (po.getString("t_code",null) == null){
            Random r = new Random();
            i1 = r.nextInt(999999 - 111111 +1) + 111111;
            code = String.valueOf(i1);

            if (phone != null && email != null)
            {
                sendSMS(phone,code);
                final SharedPreferences p = getApplicationContext().getSharedPreferences("infos", 0);
                SharedPreferences.Editor editor = p.edit();
                editor.putString("t_code",code);
                editor.apply();
            }
        }


        valid        = (Button) findViewById(R.id.next_ins);
        cancel       = (Button) findViewById(R.id.renv);
        cod          = (EditText) findViewById(R.id.code);

        Intent i = getIntent();
        if (i!=null) {
            sms = i.getStringExtra("sms");
            if (sms != null) {
                cod.setText(sms);
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences p = getApplicationContext().getSharedPreferences("infos", 0);
                SharedPreferences.Editor editor = p.edit();
                editor.clear();
                editor.apply();

                SharedPreferences p2 = getApplicationContext().getSharedPreferences("wait", 0);
                SharedPreferences.Editor editor2 = p2.edit();
                editor2.clear();
                editor2.apply();
                Func.launchActivityAndDestroyEmitter(Code.this,InscriptionScreen.class);
            }
        });

        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code_ecrit = cod.getText().toString();
                if (code_ecrit.equals(po.getString("t_code",null)))
                {
                    final SharedPreferences p = getApplicationContext().getSharedPreferences("infos", 0);
                    SharedPreferences.Editor editor = p.edit();
                    editor.putString("code","1");
                    editor.apply();
                    Func.launchActivityAndDestroyEmitter(Code.this,InscriptionScreen2.class);
                }else{
                    Toast.makeText(Code.this,"Mauvais code",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    void sendSMS(String tel, String message)
    {

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---L'envoie du sms
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS envoyé",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Echec d'envoi",
                                Toast.LENGTH_LONG).show();
                        Func.launchActivityAndDestroyEmitter(Code.this,InscriptionScreen.class);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Aucun service",
                                Toast.LENGTH_SHORT).show();
                        Func.launchActivityAndDestroyEmitter(Code.this,InscriptionScreen.class);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Aucun PDU",
                                Toast.LENGTH_SHORT).show();
                        Func.launchActivityAndDestroyEmitter(Code.this,InscriptionScreen.class);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        Func.launchActivityAndDestroyEmitter(Code.this,InscriptionScreen.class);
                        break;
                }
            }
        }, new IntentFilter(SENT));


        registerReceiver(new BroadcastReceiver(){
            @Override

            //---Accusé de reception
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Reçu",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS Non reçu",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(tel, null, message, sentPI, deliveredPI);
    }
}
