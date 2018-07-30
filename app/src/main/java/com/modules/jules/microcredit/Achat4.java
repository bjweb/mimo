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

public class Achat4 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button valid,cancel;
    private ProgressDialog progressDialog;
    int hour, minute,seconde;
    String code, montant, taux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achat4);

        valid = (Button) findViewById(R.id.valider);
        cancel= (Button) findViewById(R.id.annuler);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez");

        final Spinner echeance = (Spinner) findViewById(R.id.echeancier);
        echeance.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        montant = extras.getString("montant");
        code = extras.getString("code");
        taux = extras.getString("taux");

        TextView title         = (TextView) findViewById(R.id.title_offre);
        Func.changeTypeFace(this,title, Var.FONT_ROBOTO_MEDIUM);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        seconde = calendar.get(Calendar.SECOND);

        List<String> categories2 = new ArrayList<String>();
        categories2.add("1");
        categories2.add("2");
        categories2.add("3");
        categories2.add("4");
        categories2.add("5");
        categories2.add("6");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Achat4.this, android.R.layout.simple_spinner_item, categories2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        echeance.setAdapter(dataAdapter2);

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
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Achat4.this, R.style.Theme_AppCompat_Light_Dialog_Alert));
                builder.setTitle("Souhaitez-vous confirmer ?");
                int nMontant = Integer.parseInt(montant);
                int nTaux = Integer.parseInt(taux);
                final int interet = (nMontant * nTaux) / 100;
                int frais = (nMontant * 6) /100;
                int frais2 = ((nMontant * 3) /100) /2;
                final int totFrais = frais + frais2;
                final int remb = nMontant + interet + totFrais;
                builder.setMessage("Montant sollicité : "+nMontant+"\n"+"Taux : "+nTaux+"\n"+"Echeancier : "+item+"\n\nInterêt : "+interet+"\nFrais de transaction : "
                +totFrais+"\nTotal à rembourser : "+remb);
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "OUI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ECHEANCE,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (!response.isEmpty()) {
                                                    if (response.equals("bien"))
                                                    {
                                                        Func.ToastLong(getApplicationContext(),"Achat en cours...");
                                                        progressDialog.hide();
                                                        Intent intent = new Intent(Achat4.this,OprationScreen.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        finish();
                                                    }else{
                                                        Func.ToastLong(getApplicationContext(),"Offre existante");
                                                        progressDialog.hide();
                                                    }

                                                }else{
                                                    Toast.makeText(Achat4.this, "Erreur", Toast.LENGTH_LONG).show();
                                                    progressDialog.hide();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(Achat4.this, "Erreur de connexion, veuillez reessayer", Toast.LENGTH_LONG).show();
                                                progressDialog.hide();
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
                                        params.put("taux", String.valueOf(taux));
                                        params.put("echeance", item);
                                        params.put("montant", String.valueOf(montant));
                                        params.put("heure", hour+":"+minute+":"+seconde);
                                        params.put("telephone", pref.getString("tel",null));
                                        params.put("code", code);
                                        params.put("interet", String.valueOf(interet));
                                        params.put("frais", String.valueOf(totFrais));
                                        params.put("total", String.valueOf(remb));
                                        return params;
                                    }

                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(Achat4.this);
                                requestQueue.add(stringRequest);
                            }
                        });

                builder.setNegativeButton(
                        "Non",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
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
            Intent intent = new Intent(Achat4.this,Histo_demande.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.offre){
            Intent intent = new Intent(Achat4.this,Histo_Offre.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.achat){
             Intent intent = new Intent(Achat4.this,Histo_Achat.class);
             startActivity(intent);
            return true;
        }else if (id == R.id.deco){
            Func.launchActivityAndDestroyEmitter(Achat4.this,Connexion.class);
            PrefManager.getInstance(Achat4.this)
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
