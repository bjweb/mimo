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
import com.google.firebase.iid.FirebaseInstanceId;
import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.PrefManager;
import com.modules.jules.microcredit.utils.Var;

import java.util.HashMap;
import java.util.Map;

public class Connexion extends AppCompatActivity {

    private Button valid,cancel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion_screen);

        valid = (Button) findViewById(R.id.next_ins);
        cancel = (Button) findViewById(R.id.cancel_insc);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez");

        final EditText email = (EditText) findViewById(R.id.email_input);
        final EditText tel = (EditText) findViewById(R.id.phone_input);

        final String recent_token = FirebaseInstanceId.getInstance().getToken();

        TextView title = (TextView) findViewById(R.id.info_profil_insc);
        Func.changeTypeFace(this, title, Var.FONT_ROBOTO_MEDIUM);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Func.launchActivityAndDestroyEmitter(Connexion.this, InscriptionScreen.class);
                finish();
            }
        });

        if (recent_token != null)
        {
            valid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Func.isEmptyArrayString(email.getText().toString().trim()
                            , tel.getText().toString().trim())) {
                        Func.ToastLong(getApplicationContext(), "Veuillez saisir les données");
                    } else {
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONNEXION,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.contains("-")) {
                                            Func.ToastLong(getApplicationContext(), "D'accord!");
                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("email",email.getText().toString());
                                            editor.putString("tel",tel.getText().toString());
                                            editor.putString("caution",response);
                                            editor.putString("code","1");
                                            editor.apply();
                                            progressDialog.hide();
                                            Func.launchActivityAndDestroyEmitter(Connexion.this,MenuScreen.class);
                                            PrefManager.getInstance(Connexion.this)
                                                    .putBoolean(PrefManager.IS_FIRST_TIME_LAUNCH,false);
                                            finish();

                                        } else if (response.equals("no")){
                                            Toast.makeText(Connexion.this, "D'accord!", Toast.LENGTH_LONG).show();
                                            progressDialog.hide();
                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("email",email.getText().toString());
                                            editor.putString("tel",tel.getText().toString());
                                            editor.apply();
                                            Func.launchActivityAndDestroyEmitter(Connexion.this,MenuScreen.class);
                                            PrefManager.getInstance(Connexion.this)
                                                    .putBoolean(PrefManager.IS_FIRST_TIME_LAUNCH,false);
                                            finish();
                                        } else if (response.equals("non")){
                                            Toast.makeText(Connexion.this, "Vous n'êtes pas inscrit sur MIMO", Toast.LENGTH_LONG).show();
                                            progressDialog.hide();
                                        }  else {
                                            Toast.makeText(Connexion.this, "Erreur de traitement", Toast.LENGTH_LONG).show();
                                            progressDialog.hide();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(Connexion.this, "Erreur de connexion, veuillez reessayer", Toast.LENGTH_LONG).show();
                                        progressDialog.hide();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email", email.getText().toString());
                                params.put("telephone", tel.getText().toString());
                                params.put("token", recent_token);
                                return params;
                            }

                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(Connexion.this);
                        requestQueue.add(stringRequest);
                    }
                }
            });

    }else{
            Toast.makeText(Connexion.this, "Patientez nous identifions votre appareil. Connexion internet requise !", Toast.LENGTH_LONG).show();
            Func.launchActivityAndDestroyEmitter(Connexion.this,Connexion.class);
        }

    }
}
