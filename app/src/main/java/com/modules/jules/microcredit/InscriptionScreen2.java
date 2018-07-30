package com.modules.jules.microcredit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class InscriptionScreen2 extends AppCompatActivity {


    private TextView info_profil_insc,title_bottom;
    private String email,phone;
    private Button valid,cancel;
    private CircleImageView profil;
    private Uri fileUri        ;
    private File compressedImage;
    private String filePath = null;
    GalleryPhoto galleryPhoto;
    long poids = 0;
    int i1;
    private String recent_token ;
    private ProgressBar progressBar;
    private TextView pourcentage;
    private final int INTENT_CHOOSER = 100;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private final int INTENT_CAPTURE = 101;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final String TAG = InscriptionScreen2.class.getSimpleName();

    final int GALLERY_REQUEST = 22131;

    private RequestCredit credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_screen2);

        title_bottom = (TextView) findViewById(R.id.bottom_info);
        info_profil_insc = (TextView) findViewById(R.id.info_profil_insc);
        pourcentage = (TextView) findViewById(R.id.pourcentag);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        galleryPhoto = new GalleryPhoto(getApplicationContext());
        checkAndRequestPermissions();

        recent_token = FirebaseInstanceId.getInstance().getToken();

        final SharedPreferences pi = getSharedPreferences("wait", 0);
        email = pi.getString("mail",null);
        phone = pi.getString("tel",null);


        valid        = (Button) findViewById(R.id.next_ins);

        profil        = (CircleImageView) findViewById(R.id.picture_profils_insc);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        if (!isDeviceSupportCamera()) {
            Toast.makeText(InscriptionScreen2.this,
                    "Desole, votre portable ne supporte pas la camera",
                    Toast.LENGTH_LONG).show();
        }

        Func.changeTypeFace(this,title_bottom, Var.FONT_ROBOTO_LIGHT);
        Func.changeTypeFace(this,info_profil_insc, Var.FONT_ROBOTO_LIGHT);

        Intent i = getIntent();
        if (i != null){
            filePath = i.getStringExtra("filePath");
            phone = i.getStringExtra("tel");
            email = i.getStringExtra("mail");
        }


        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Veuillez prendre une photo de votre carte d'identité !", Toast.LENGTH_LONG).show();
        }


        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(filePath != null){
                        if (recent_token != null) {
                            new UploadFileToServer().execute();
                        }else{
                            Toast.makeText(InscriptionScreen2.this, "Echec d'identification de votre appareil. Recommencez !", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(InscriptionScreen2.this,InscriptionScreen2.class);
                            intent.putExtra("mail",email);
                            intent.putExtra("tel",phone);
                            finish();
                        }
                       // Func.launchActivity(InscriptionScreen.this,ContratScreen.class);
                    }else {
                        Toast.makeText(getApplicationContext(),"Veuillez Prendre une photo",Toast.LENGTH_LONG).show();
                    }
            }
        });

        //registerForContextMenu(profil);


    }

    private void previewMedia(boolean isImage) {
        if (isImage) {
            profil.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            profil.setImageBitmap(bitmap);
        } else {
            profil.setVisibility(View.GONE);
        }
    }

    private boolean isDeviceSupportCamera() {
        if (this.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode ,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {

            launchUploadActivity(true);


        } else if (resultCode == RESULT_CANCELED) {

            Toast.makeText(this,
                    "Photo annulé", Toast.LENGTH_SHORT)
                    .show();

        } else {
            Toast.makeText(this,
                    "Erreur de capture", Toast.LENGTH_SHORT)
                    .show();
        }

        }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri();

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void launchUploadActivity(boolean isImage){
        Intent i = new Intent(this, InscriptionScreen2.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        i.putExtra("tel", phone);
        i.putExtra("mail", email);
        startActivity(i);
        finish();
    }


    public Uri getOutputMediaFileUri() {
        //return FileProvider.getUriForFile(InscriptionScreen.this,BuildConfig.APPLICATION_ID + ".provider",getOutPutMediaFile());
        return Uri.fromFile(getOutPutMediaFile());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
        outState.putString("tel", phone);
        outState.putString("mail", email);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
        phone = savedInstanceState.getString("tel");
        email = savedInstanceState.getString("mail");
    }

    public static File  getOutPutMediaFile(){
        File mediaStorage = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"Lined"
        );

        if(!mediaStorage.exists()){
            if(!mediaStorage.mkdir()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorage.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        return mediaFile;

    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            valid.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            pourcentage.setVisibility(View.VISIBLE);

            progressBar.setProgress(progress[0]);

            pourcentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.INSCRIPTION);

            final SharedPreferences pi = getSharedPreferences("wait", 0);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) poids) * 100));
                            }
                        });


                File sourceFile = new File(filePath);

                compressedImage = new Compressor(InscriptionScreen2.this).compressToFile(sourceFile);

                entity.addPart("image", new FileBody(compressedImage));
                entity.addPart("email", new StringBody(pi.getString("mail",null)));
                entity.addPart("telephone", new StringBody(pi.getString("tel",null)));
                entity.addPart("token", new StringBody(recent_token));
                entity.addPart("code", new StringBody(recent_token));


                poids = entity.getContentLength();
                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Erreur : "
                            + statusCode;
                    cancel.setVisibility(View.VISIBLE);
                    valid.setVisibility(View.VISIBLE);
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Reponse du serveur : ",result);
            if (result.contains("Reussie")){
                final SharedPreferences pi = getSharedPreferences("wait", 0);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("infos", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("email",pi.getString("mail",null));
                editor.putString("tel",pi.getString("tel",null));
                editor.apply();
                Intent intent = new Intent(InscriptionScreen2.this,ContratScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else{
                valid.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                pourcentage.setVisibility(View.INVISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(InscriptionScreen2.this);
                builder.setMessage("Erreur de connexion au serveur").setTitle("Erreur")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            super.onPostExecute(result);
        }

    }

    private  boolean checkAndRequestPermissions() {
        int internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int phone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
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
