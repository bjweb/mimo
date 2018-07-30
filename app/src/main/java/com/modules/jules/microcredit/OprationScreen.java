package com.modules.jules.microcredit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.PrefManager;

public class OprationScreen extends AppCompatActivity {

    private ImageButton demander,deman,achat,offre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opration_screen);

        demander = (ImageButton) findViewById(R.id.demander_argent);
        offre = (ImageButton) findViewById(R.id.offre_argent);
        achat = (ImageButton) findViewById(R.id.achat_op);

        demander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Func.launchActivity(OprationScreen.this,DemanderArgent.class);
            }
        });

        offre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Func.launchActivity(OprationScreen.this,OffreArgent.class);
            }
        });

        achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Func.launchActivity(OprationScreen.this,Achat.class);
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
            Intent intent = new Intent(OprationScreen.this,Histo_demande.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.offre){
            Intent intent = new Intent(OprationScreen.this,Histo_Offre.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.achat){
             Intent intent = new Intent(OprationScreen.this,Histo_Achat.class);
             startActivity(intent);
            return true;
        }else if (id == R.id.deco){
            Func.launchActivityAndDestroyEmitter(OprationScreen.this,Connexion.class);
            PrefManager.getInstance(OprationScreen.this)
                    .putBoolean(PrefManager.IS_FIRST_TIME_LAUNCH,true);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
