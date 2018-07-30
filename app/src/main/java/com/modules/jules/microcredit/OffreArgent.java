package com.modules.jules.microcredit;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.modules.jules.microcredit.utils.Var;

import java.util.HashMap;
import java.util.Map;

public class OffreArgent extends AppCompatActivity {

    private Button valid,cancel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre_screen);

        valid = (Button) findViewById(R.id.valider);
        cancel= (Button) findViewById(R.id.annuler);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez");

        final EditText montant = (EditText) findViewById(R.id.montant_offre);

        TextView title         = (TextView) findViewById(R.id.title_offre);
        Func.changeTypeFace(this,title, Var.FONT_ROBOTO_MEDIUM);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Func.isEmptyArrayString(montant.getText().toString().trim())){
                    Func.ToastLong(getApplicationContext(),"Veuillez saisir les données");
                }else{
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.OFFRE_ARGENT,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (!response.isEmpty()) {
                                        if(response.equals("bien")) {
                                            progressDialog.hide();
                                            Func.ToastLong(getApplicationContext(), "Opération reussie !");
                                            finish();
                                        }else{
                                            progressDialog.hide();
                                            Func.ToastLong(getApplicationContext(), response);
                                        }


                                    }else{
                                        Toast.makeText(OffreArgent.this, "Erreur", Toast.LENGTH_LONG).show();
                                        progressDialog.hide();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(OffreArgent.this, "Probleme de connexion au serveur", Toast.LENGTH_LONG).show();
                                    progressDialog.hide();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
                            params.put("telephone", pref.getString("tel",null));
                            params.put("montant", montant.getText().toString());
                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(OffreArgent.this);
                    requestQueue.add(stringRequest);
                }
            }
        });

    }
}
