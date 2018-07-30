package com.modules.jules.microcredit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.PrefManager;
import com.modules.jules.microcredit.utils.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Achat2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button valid,cancel;
    private ProgressDialog progressDialog;
    int rmon, rcau;
    String caution, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achat2);

        valid = (Button) findViewById(R.id.valider);
        cancel= (Button) findViewById(R.id.annuler);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        caution = extras.getString("caution");
        code = extras.getString("code");
        rcau = Integer.parseInt(caution);

        progressDialog.show();

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.clearFocus();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Config.SPINNER+"?telephone="+pref.getString("tel",null), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.toString() != null) {
                    try {
                        JSONArray article = response.getJSONArray("result1");

                        List<String> categories = new ArrayList<String>();

                        for (int i = 0; i < article.length(); i++) {

                            JSONObject product = (JSONObject) article.get(i);

                            String montant = product.getString("montant");
                            categories.add(montant);

                            progressDialog.hide();
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Achat2.this, android.R.layout.simple_spinner_item, categories);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dataAdapter.notifyDataSetChanged();
                        spinner.setAdapter(dataAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Achat2.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                finish();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Achat2.this);
        requestQueue.add(jsonObjReq);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

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
                if(Func.isEmptyArrayString(item)){
                    Func.ToastLong(getApplicationContext(),"Veuillez saisir les données");
                }else{
                    rmon = Integer.parseInt(item);
                    if (rcau >= rmon)
                    {
                        Intent intent = new Intent(Achat2.this,Meilleur_Achat.class);
                        intent.putExtra("montant",item);
                        intent.putExtra("code",code);
                        startActivity(intent);
                    }else{
                        Func.ToastLong(getApplicationContext(),"La caution doit etre superieur ou égal au montant");
                    }
                }
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
            Intent intent = new Intent(Achat2.this,Histo_demande.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.offre){
            Intent intent = new Intent(Achat2.this,Histo_Offre.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.achat){
             Intent intent = new Intent(Achat2.this,Histo_Achat.class);
             startActivity(intent);
            return true;
        }else if (id == R.id.deco){
            Func.launchActivityAndDestroyEmitter(Achat2.this,Connexion.class);
            PrefManager.getInstance(Achat2.this)
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
