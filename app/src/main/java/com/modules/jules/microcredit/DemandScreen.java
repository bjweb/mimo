package com.modules.jules.microcredit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DemandScreen extends AppCompatActivity {

    private Button valid,cancel;
    private ListView listView;
    private ArrayList<Model_Offre> offres = new ArrayList<Model_Offre>();
    private CustomListOffre adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new CustomListOffre(this, offres);
        listView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez");
        progressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Config.OFFRE2+"?all="+1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.toString() != null) {
                    try {
                        JSONArray result = response.getJSONArray("result2");

                        for (int i = 0; i < result.length(); i++) {

                            JSONObject obj = (JSONObject) result.get(i);
                            final Model_Offre resultat = new Model_Offre();

                            resultat.setContact(obj.getString("telephone"));
                            resultat.setEmail(obj.getString("email"));
                            resultat.setMontant(obj.getString("montant"));
                            resultat.setPhoto(Config.OFFRE_IMAGE+obj.getString("photo"));
                            resultat.setTaux("Taux "+obj.getString("taux"));
                            resultat.setDate(obj.getString("dates"));

                            offres.add(resultat);
                            progressDialog.hide();

                            final ListView lv = (ListView) findViewById(R.id.listview);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                                    TextView contact = (TextView)view.findViewById(R.id.contact);
                                    String contact1 = contact.getText().toString();
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + contact1));
                                    if (ActivityCompat.checkSelfPermission(DemandScreen.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    startActivity(intent);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DemandScreen.this, "Echec de connexion", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                finish();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(DemandScreen.this);
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
