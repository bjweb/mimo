package com.modules.jules.microcredit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Affiche_achat extends AppCompatActivity {

    private ProgressDialog progressDialog;
    TableRow tr;
    TextView idi,montant2,taux2,eche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affiche_achat);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez");

        final TableLayout tl = (TableLayout) findViewById(R.id.table);

        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView montant = new TextView(this);
        montant.setText("Montant (Fcfa)");
        montant.setTextColor(Color.GRAY);
        montant.setGravity(Gravity.CENTER);
        tr.addView(montant);

        TextView taux = new TextView(this);
        taux.setText("Taux (%)");
        taux.setGravity(Gravity.CENTER);
        taux.setTextColor(Color.GRAY);
        tr.addView(taux);

        TextView echance = new TextView(this);
        echance.setText("Echeance (j)");
        echance.setGravity(Gravity.CENTER);
        echance.setTextColor(Color.GRAY);
        tr.addView(echance);

        final TextView id = new TextView(this);
        id.setText("Heure");
        id.setPadding(5, 5, 5, 0);
        id.setTextColor(Color.GRAY);
        tr.addView(id);

        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        TextView divider = new TextView(this);
        divider.setText("--");
        tr.addView(divider);

        TextView divider2 = new TextView(this);
        divider2.setText("-----------------");
        tr.addView(divider2);

        TextView divider3 = new TextView(this);
        divider3.setText("-----------------");
        tr.addView(divider3);

        TextView divider4 = new TextView(this);
        divider4.setText("-----------------");
        tr.addView(divider4);


        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Config.ACHAT+"?achat="+1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.toString() != null) {
                    try {
                        JSONArray result = response.getJSONArray("result2");

                        for (int i = 0; i < result.length(); i++) {

                            JSONObject obj = (JSONObject) result.get(i);
                            int num = i+1;
                            String nume = String.valueOf(num);

                            tr = new TableRow(Affiche_achat.this);
                            tr.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.FILL_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));

                            montant2 = new TextView(Affiche_achat.this);
                            montant2.setText(obj.getString("montant"));
                            montant2.setTextColor(Color.BLACK);
                            montant2.setGravity(Gravity.CENTER);
                            montant2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(montant2);

                            taux2 = new TextView(Affiche_achat.this);
                            taux2.setText(obj.getString("taux"));
                            taux2.setTextColor(Color.BLACK);
                            taux2.setGravity(Gravity.CENTER);
                            taux2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(taux2);

                            eche = new TextView(Affiche_achat.this);
                            eche.setText(obj.getString("echeance"));
                            eche.setTextColor(Color.BLACK);
                            eche.setGravity(Gravity.CENTER);
                            eche.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(eche);

                            idi = new TextView(Affiche_achat.this);
                            idi.setText(obj.getString("heure"));
                            idi.setPadding(5, 5, 5, 0);
                            idi.setTextColor(Color.RED);
                            idi.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(idi);

                            tl.addView(tr, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.FILL_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));
                            progressDialog.hide();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Affiche_achat.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                finish();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Affiche_achat.this);
        requestQueue.add(jsonObjReq);



    }
}
