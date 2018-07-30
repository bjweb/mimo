package com.modules.jules.microcredit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Achat extends AppCompatActivity {

    private Button valid,cancel,ajj;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achat);

        valid = (Button) findViewById(R.id.valider);
        cancel= (Button) findViewById(R.id.annuler);
        ajj = (Button) findViewById(R.id.ajj);
        final EditText caution = (EditText) findViewById(R.id.caution);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
        if (pref.getString("caution",null) == null)
        {
            Func.launchActivityAndDestroyEmitter(Achat.this,MontantCaution.class);
        }else{
            caution.setText(pref.getString("caution",null));
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez");

        TextView title         = (TextView) findViewById(R.id.title_offre);
        Func.changeTypeFace(this,title, Var.FONT_ROBOTO_MEDIUM);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ajj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Func.launchActivityAndDestroyEmitter(Achat.this,MontantCaution.class);
                finish();
            }
        });

        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Func.isEmptyArrayString(caution.getText().toString().trim())){
                    Func.ToastLong(getApplicationContext(),"Veuillez saisir les données");
                }else{
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.VERIF_CAUTION,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (!response.isEmpty()) {
                                        if(response.equals("bad")){
                                            Func.ToastLong(getApplicationContext(),"Code caution inexistant");
                                            progressDialog.hide();
                                        }else{
                                            Func.ToastLong(getApplicationContext(),"Opération reussie !");
                                            progressDialog.hide();
                                            Intent intent = new Intent(Achat.this,Achat2.class);
                                            intent.putExtra("caution",response);
                                            intent.putExtra("code",caution.getText().toString().trim());
                                            startActivity(intent);
                                            finish();
                                        }

                                    }else{
                                        Toast.makeText(Achat.this, "Erreur", Toast.LENGTH_LONG).show();
                                        progressDialog.hide();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Achat.this, "Erreur de connexion, veuillez reessayer", Toast.LENGTH_LONG).show();
                                    progressDialog.hide();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("code", caution.getText().toString());
                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(Achat.this);
                    requestQueue.add(stringRequest);
                }
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
            Intent intent = new Intent(Achat.this,Histo_demande.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.offre){
            Intent intent = new Intent(Achat.this,Histo_Offre.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.achat){
             Intent intent = new Intent(Achat.this,Histo_Achat.class);
             startActivity(intent);
            return true;
        }else if (id == R.id.deco){
            Func.launchActivityAndDestroyEmitter(Achat.this,Connexion.class);
            PrefManager.getInstance(Achat.this)
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
