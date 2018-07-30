package com.modules.jules.microcredit;


import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.PrefManager;
import com.modules.jules.microcredit.utils.Var;


//=+


public class SlashScreen extends AppCompatActivity {

    private final int TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash_screen);

        TextView title = (TextView) findViewById(R.id.slash_title);
        TextView stitle= (TextView) findViewById(R.id.slash_subtitle);
        Func.changeTypeFace(this,title, Var.FONT_ROBOTO_MEDIUM);
        Func.changeTypeFace(this,stitle,Var.FONT_ROBOTO_LIGHT);

        final SharedPreferences p = getApplicationContext().getSharedPreferences("infos", 0);



        if (getIntent().getExtras() != null){
            for (String key : getIntent().getExtras().keySet()){
                switch (key) {
                    case "caution":
                        String id = getIntent().getExtras().getString("caution");
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("caution",id);
                        editor.apply();
                        break;

                    case "valide":
                        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("infos", 0);
                        SharedPreferences.Editor editor1 = pref1.edit();
                        editor1.putString("valide", "1");
                        editor1.apply();
                        break;
                }
            }
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(PrefManager.getInstance(SlashScreen.this)
                        .getBoolean(PrefManager.IS_FIRST_TIME_LAUNCH,true)){
                    if (p.getString("code",null) != null && p.getString("code",null).equals("0")){
                        Func.launchActivityAndDestroyEmitter(SlashScreen.this,Code.class);
                        finish();
                    }else
                    Func.launchActivityAndDestroyEmitter(SlashScreen.this,InscriptionScreen.class);
                    finish();
                }else{
                    if (p.getString("valide",null)==null)
                    {
                        Func.launchActivityAndDestroyEmitter(SlashScreen.this,Attente.class);
                        finish();
                    }else {
                        if (getIntent().getExtras() != null){
                            for (String key : getIntent().getExtras().keySet()){
                                switch (key) {
                                    case "caution2":
                                        Func.launchActivityAndDestroyEmitter(SlashScreen.this, Achat.class);
                                        break;
                                    default:
                                        Func.launchActivityAndDestroyEmitter(SlashScreen.this, MenuScreen.class);
                                        finish();
                                }
                            }
                        }else{
                            Func.launchActivityAndDestroyEmitter(SlashScreen.this, MenuScreen.class);
                            finish();
                        }
                    }
                }
            }
        },TIMEOUT);
    }

}
