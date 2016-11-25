package com.example.sandip.imagelistview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private Button log_out,change_pic;
    private TextView name_txt,email_txt;
    private ImageView img;
    String email,name;
    GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name_txt= (TextView) findViewById(R.id.textView11);
        email_txt= (TextView) findViewById(R.id.textView13);
        img= (ImageView) findViewById(R.id.imageView);
        change_pic= (Button) findViewById(R.id.upload_pic);
        log_out= (Button) findViewById(R.id.log_out_btn);
        Intent pre=getIntent();
        email=pre.getStringExtra("user");
        name=pre.getStringExtra("name");
        name_txt.setText(name);
        email_txt.setText(email);

        change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next=new Intent(MainActivity.this,Upload.class);
                next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                next.putExtra("user",email);
                next.putExtra("name",name);
                startActivity(next);
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                                   new ResultCallback<Status>() {
                                                       @Override
                                                       public void onResult(Status status) {
                                                           Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                                                           Intent i = new Intent(MainActivity.this, Login.class);
                                                           i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                           finish();
                                                           startActivity(i);
                                                           finish();
                                                       }
                                                   });
                                       }
                                   });
        getImage();
    }
    private void getImage() {

        class GetImage extends AsyncTask<String,Void,Bitmap> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                loading.dismiss();
                img.setImageBitmap(b);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String add = "http://democontact.esy.es/Upload%20Image/upload/"+email+".jpg";
                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute(email);
    }


        protected void onStart() {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
            super.onStart();
        }


    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}