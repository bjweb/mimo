package com.modules.jules.microcredit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

public class MontantCaution extends AppCompatActivity {
    private Button valid,cancel;
    private ProgressDialog progressDialog;
    EditText montant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montant_caution);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez");

        montant = (EditText) findViewById(R.id.montantcaution);

        final String token = FirebaseInstanceId.getInstance().getToken();

        valid = (Button) findViewById(R.id.valider);
        cancel= (Button) findViewById(R.id.annuler);

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

                    Func.ToastLong(getApplicationContext(),"Votre requête a été prise en compte par MIMO");
                    progressDialog.hide();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MontantCaution.this, R.style.Theme_AppCompat_Light_Dialog_Alert));
                    builder.setMessage("Veuillez saisir le montant et votre mot de passe");
                    builder.setCancelable(false);

                    final EditText mont = new EditText(MontantCaution.this);
                    mont.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    mont.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mont.setHint("Montant");

                    final EditText mdp = new EditText(MontantCaution.this);
                    mdp.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    mdp.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    mdp.setHint("Mot de passe");

                    final TextView air = new TextView(MontantCaution.this);
                    mdp.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    air.setText("Powered by Airtel Money");

                    LinearLayout layout = new LinearLayout(MontantCaution.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    layout.addView(mont);
                    layout.addView(mdp);
                    layout.addView(air);
                    builder.setView(layout);

                    builder.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialogo, int id) {
                                    if (mont.getText().toString().equals(montant.getText().toString()))
                                    {
                                        progressDialog.show();
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.TRANSFERT,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (!response.isEmpty()) {
                                                            if (response.equals("bien"))
                                                            {
                                                                progressDialog.hide();
                                                                dialogo.cancel();
                                                                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MontantCaution.this, R.style.Theme_AppCompat_Light_Dialog_Alert));
                                                                builder.setMessage("Vous avez envoyé "+mont.getText().toString()+" au code M01MM");
                                                                builder.setCancelable(false);

                                                                builder.setPositiveButton(
                                                                        "OK",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                dialog.cancel();
                                                                                finish();
                                                                            }
                                                                        });

                                                                AlertDialog alert = builder.create();
                                                                alert.show();
                                                            }else if (response.equals("bade")){
                                                                Func.ToastLong(getApplicationContext(),"Mot de passe incorrect.");
                                                                progressDialog.hide();
                                                            }else if (response.equals("bad")){
                                                                Func.ToastLong(getApplicationContext(),"Solde inssufisant.");
                                                                progressDialog.hide();
                                                            }else{
                                                                Func.ToastLong(getApplicationContext(),response);
                                                                progressDialog.hide();
                                                            }

                                                        }else{
                                                            Toast.makeText(MontantCaution.this, "Erreur", Toast.LENGTH_LONG).show();
                                                            progressDialog.hide();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(MontantCaution.this, "Erreur de connexion, veuillez reessayer", Toast.LENGTH_LONG).show();
                                                        progressDialog.hide();
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
                                                params.put("montant", montant.getText().toString());
                                                params.put("telephone", pref.getString("tel",null));
                                                params.put("mdp", mdp.getText().toString());
                                                return params;
                                            }

                                        };

                                        RequestQueue requestQueue = Volley.newRequestQueue(MontantCaution.this);
                                        requestQueue.add(stringRequest);
                                    }else{
                                        Toast.makeText(MontantCaution.this,"Ce montant ne correspond pas à votre caution",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

    }
}
