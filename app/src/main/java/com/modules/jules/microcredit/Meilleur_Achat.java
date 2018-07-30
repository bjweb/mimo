package com.modules.jules.microcredit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.modules.jules.microcredit.utils.Func;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Meilleur_Achat extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Rech_histo_demande> histo = new ArrayList<Rech_histo_demande>();
    private CustomListMeilleure adapter;
    ProgressDialog progressDialog;
    private RelativeLayout mRelativeLayout;
    private PopupWindow mPopupWindow;
    String montant,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meilleure);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recherche de propositions");
        progressDialog.show();

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Meilleure proposition");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        montant = extras.getString("montant");
        code = extras.getString("code");

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rel);

        listView = (ListView) findViewById(R.id.list_demande);
        Button cont = (Button)findViewById(R.id.continuer);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Meilleur_Achat.this,Achat3.class);
                intent.putExtra("montant",montant);
                intent.putExtra("code",code);
                startActivity(intent);
                finish();
            }
        });

        adapter = new CustomListMeilleure(Meilleur_Achat.this, histo);
        listView.setAdapter(adapter);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,Config.MEILLEURE+"?montant="+montant+"&telephone="+pref.getString("tel",null), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                        try {
                            JSONArray result = response.getJSONArray("result1");

                            if (result.length() != 0){

                            for (int i = 0; i < result.length(); i++) {

                                JSONObject obj = (JSONObject) result.get(i);
                                final Rech_histo_demande hist = new Rech_histo_demande();

                                hist.setMontant(obj.getString("montant"));
                                hist.setDate(obj.getString("heure"));
                                hist.setTaux(obj.getString("taux"));
                                hist.setJours(obj.getString("echeance"));

                                histo.add(hist);
                                progressDialog.hide();
/*
                                final ListView lv = (ListView) findViewById(R.id.list2);
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                                        TextView text1 = (TextView) view.findViewById(R.id.lid);
                                        TextView text2 = (TextView) view.findViewById(R.id.letitre2);
                                        TextView text3 = (TextView) view.findViewById(R.id.leprix2);
                                        TextView text4 = (TextView) view.findViewById(R.id.lecon);
                                        TextView text5 = (TextView) view.findViewById(R.id.ladesc);

                                        String id = text1.getText().toString();
                                        String titre = text2.getText().toString();
                                        String prix = text3.getText().toString();
                                        String contact = text4.getText().toString();
                                        String desc = text5.getText().toString();

                                        options(id, titre, desc, prix, contact);
                                    }
                                });
                  */
                            }
                            }else{
                                Toast.makeText(Meilleur_Achat.this, "Aucune proposition sur ce montant", Toast.LENGTH_LONG).show();
                                Func.launchActivityAndDestroyEmitter(Meilleur_Achat.this,OprationScreen.class);
                                progressDialog.hide();
                                Intent intent = new Intent(Meilleur_Achat.this,Achat3.class);
                                intent.putExtra("montant",montant);
                                intent.putExtra("code",code);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Meilleur_Achat.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                finish();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Meilleur_Achat.this);
        requestQueue.add(jsonObjReq);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
