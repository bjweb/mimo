package com.modules.jules.microcredit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.PrefManager;

public class MenuScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        ImageButton opera = (ImageButton) findViewById(R.id.operation);
        ImageButton aide  = (ImageButton) findViewById(R.id.aide);

        aide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        opera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Func.launchActivity(MenuScreen.this,OprationScreen.class);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.demande) {
            Intent intent = new Intent(MenuScreen.this,Histo_demande.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.offre){
             Intent intent = new Intent(MenuScreen.this,Histo_Offre.class);
             startActivity(intent);
            return true;
        }else if (id == R.id.achat){
             Intent intent = new Intent(MenuScreen.this,Histo_Achat.class);
             startActivity(intent);
            return true;
        }else if (id == R.id.deco){
            Func.launchActivityAndDestroyEmitter(MenuScreen.this,Connexion.class);
            PrefManager.getInstance(MenuScreen.this)
                    .putBoolean(PrefManager.IS_FIRST_TIME_LAUNCH,true);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();

            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("wait", 0);
            SharedPreferences.Editor editor1 = pref1.edit();
            editor1.clear();
            editor1.apply();

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
