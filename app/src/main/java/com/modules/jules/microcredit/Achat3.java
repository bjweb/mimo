package com.modules.jules.microcredit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.PrefManager;
import com.modules.jules.microcredit.utils.Var;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Achat3 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button valid,cancel;
    private ProgressDialog progressDialog;
    int hour;
    String montant,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achat3);

        valid = (Button) findViewById(R.id.valider);
        cancel= (Button) findViewById(R.id.annuler);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez");

        final Spinner taux = (Spinner) findViewById(R.id.taux);
        taux.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
         montant = extras.getString("montant");
         code = extras.getString("code");

        TextView title         = (TextView) findViewById(R.id.title_offre);
        Func.changeTypeFace(this,title, Var.FONT_ROBOTO_MEDIUM);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        List<String> categories1 = new ArrayList<String>();
        categories1.add("7");
        categories1.add("8");
        categories1.add("9");
        categories1.add("10");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Achat3.this, android.R.layout.simple_spinner_item, categories1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taux.setAdapter(dataAdapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String item = parent.getItemAtPosition(position).toString();

        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   if (hour < 21 && hour > 5 ){
                    Intent intent = new Intent(Achat3.this,Achat4.class);
                    intent.putExtra("montant",montant);
                    intent.putExtra("code",code);
                    intent.putExtra("taux",item);
                    startActivity(intent);
           //     }else{
             //       Func.ToastLong(getApplicationContext(),"Disponible de 06h à 20h");
              //  }

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            Intent intent = new Intent(Achat3.this,Histo_demande.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.offre){
            Intent intent = new Intent(Achat3.this,Histo_Offre.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.achat){
             Intent intent = new Intent(Achat3.this,Histo_Achat.class);
             startActivity(intent);
            return true;
        }else if (id == R.id.deco){
            Func.launchActivityAndDestroyEmitter(Achat3.this,Connexion.class);
            PrefManager.getInstance(Achat3.this)
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
