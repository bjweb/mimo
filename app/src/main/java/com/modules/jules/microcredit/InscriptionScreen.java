package com.modules.jules.microcredit;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.modules.jules.microcredit.utils.Func;
import com.modules.jules.microcredit.utils.RequestCredit;
import com.modules.jules.microcredit.utils.Var;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class InscriptionScreen extends AppCompatActivity {


    private TextView info_profil_insc,title_bottom;
    private EditText email,phone;
    private Button valid,cancel;
    private CircleImageView profil;
    private Uri fileUri        ;
    private File compressedImage;
    private String filePath = null;
    GalleryPhoto galleryPhoto;
    long poids = 0;
    private String recent_token ;
    private ProgressDialog progressDialog;
    private TextView pourcentage;
    private final int INTENT_CHOOSER = 100;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private final int INTENT_CAPTURE = 101;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final String TAG = InscriptionScreen.class.getSimpleName();

    final int GALLERY_REQUEST = 22131;

    private RequestCredit credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_screen);

        title_bottom = (TextView) findViewById(R.id.bottom_info);
        pourcentage = (TextView) findViewById(R.id.pourcentag);
         progressDialog = new ProgressDialog(this);

         progressDialog.setMessage("Verification des coordonnées");

        galleryPhoto = new GalleryPhoto(getApplicationContext());
        checkAndRequestPermissions();

        recent_token = FirebaseInstanceId.getInstance().getToken();

        email        = (EditText) findViewById(R.id.email_input);
        phone        = (EditText) findViewById(R.id.phone_input);

        valid        = (Button) findViewById(R.id.next_ins);
        cancel       = (Button) findViewById(R.id.cancel_insc);


        Func.changeTypeFace(this,title_bottom, Var.FONT_ROBOTO_LIGHT);


        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Func.isEmptyArrayString(email.getText().toString().trim(),
                        phone.getText().toString().trim())){
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.VERIF,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("non")){
                                        progressDialog.hide();
                                        Intent intent = new Intent(InscriptionScreen.this,Code.class);
                                        final SharedPreferences p = getSharedPreferences("wait", 0);
                                        SharedPreferences.Editor editor = p.edit();
                                        editor.putString("tel",phone.getText().toString());
                                        editor.putString("mail",email.getText().toString());
                                        editor.apply();
                                        startActivity(intent);
                                        finish();
                                    } else if (response.equals("oui")){
                                        Toast.makeText(InscriptionScreen.this, "Ces coordonnées sont déjà associées à un compte MIMO", Toast.LENGTH_LONG).show();
                                        progressDialog.hide();
                                    }else {
                                        Toast.makeText(InscriptionScreen.this, "Erreur de traitement", Toast.LENGTH_LONG).show();
                                        progressDialog.hide();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(InscriptionScreen.this, "Erreur de connexion, veuillez reessayer", Toast.LENGTH_LONG).show();
                                    progressDialog.hide();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", email.getText().toString());
                            params.put("telephone", phone.getText().toString());
                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(InscriptionScreen.this);
                    requestQueue.add(stringRequest);

                }else{
                    Toast.makeText(getApplicationContext(),"Veuillez saisir les données",Toast.LENGTH_LONG).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Func.launchActivity(InscriptionScreen.this,Connexion.class);
                finish();
            }
        });

        //registerForContextMenu(profil);


    }


    private  boolean checkAndRequestPermissions() {
        int internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int phone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int sms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readsms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int sendsms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int read = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (readsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (sendsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }




}
