package com.example.sandip.imagelistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sandip on 10/22/2016.
 */

public class Register extends AppCompatActivity implements View.OnClickListener{
    private static final String REGISTER_URL = "http://democontact.esy.es/Upload%20Image/register.php";
    public static final String KEY_USERNAME = "Name";
    public static final String KEY_GENDER= "Gender";
    public static final String KEY_EMAIL= "Email";
    public static final String KEY_PASSWORD = "Password";
    private EditText name,pass,email1;
    private RadioButton rb1,rb2;
    String sex;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name= (EditText) findViewById(R.id.name_txt);
        rb1= (RadioButton) findViewById(R.id.male_radio);
        rb2= (RadioButton) findViewById(R.id.female_radio);
        email1= (EditText) findViewById(R.id.email_txt);
        pass= (EditText) findViewById(R.id.password_txt);

        buttonRegister = (Button) findViewById(R.id.submit_btn);

        buttonRegister.setOnClickListener(this);
    }
    private void registerUser() {
        final String Name = name.getText().toString().trim().toLowerCase();
        final String email = email1.getText().toString().trim().toLowerCase();
        final String password = pass.getText().toString().trim().toLowerCase();
        if(rb1.isChecked()) {
            sex = rb1.getText().toString().trim().toLowerCase();
        }
        if(rb2.isChecked()) {
            sex = rb2.getText().toString().trim().toLowerCase();
        }
        final String gender=sex;

        Log.i("My TEST", Name+" "+email+" "+password+" "+gender);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        int code = Integer.parseInt(response);
                        if (code==3){
                            Toast.makeText(Register.this,"You have Registered Successfully !",Toast.LENGTH_LONG).show();
                            //Use intent to go to a activity
                            Intent next=new Intent(Register.this,Login.class);
                            startActivity(next);
                        }
                        if (code==1){
                            Toast.makeText(Register.this,"Please fill all the value !",Toast.LENGTH_LONG).show();
                        }
                        if(code==2){
                            Toast.makeText(Register.this,"Email already exists !",Toast.LENGTH_LONG).show();
                        }
                        if(code==4){
                            Toast.makeText(Register.this,"Please Try Again Later !",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,Name);
                params.put(KEY_GENDER,gender);
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD,password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
    }

}
