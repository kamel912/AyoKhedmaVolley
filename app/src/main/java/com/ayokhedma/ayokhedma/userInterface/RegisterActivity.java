package com.ayokhedma.ayokhedma.userInterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ayokhedma.ayokhedma.connection.MySingleton;
import com.ayokhedma.ayokhedma.models.UserModel;
import com.ayokhedma.ayokhedma.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText name_field,email_field,username_field,password_field;
    private String name,email,username,password,link;
    SharedPreferences sharedpreferences;
    Intent intent;
    ProgressDialog progress;
    private UserModel user;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name_field = (EditText) findViewById(R.id.name_field_reg);
        email_field = (EditText) findViewById(R.id.email_field_reg);
        username_field = (EditText) findViewById(R.id.username_field_reg);
        password_field = (EditText) findViewById(R.id.password_field_reg);

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait ....");
        progress.setMax(100);

        link = "http://www.fatmanoha.com/ayokhedma/reg.php";

        sharedpreferences = getSharedPreferences("userprefences", Context.MODE_PRIVATE);


    }
    public void register(View view) {
        if (name_field == null || email_field == null || username_field == null || password_field == null) {
            Toast.makeText(this, "الرجاء ادخال جميع البيانات", Toast.LENGTH_SHORT).show();
        } else {
            name = name_field.getText().toString();
            email = email_field.getText().toString();
            username = username_field.getText().toString();
            password = password_field.getText().toString();
            registerProcess(name, email, username, password);
        }
    }
    public void registerProcess(String name,String email,String username,String password){
        progress.show();
        user = new UserModel(name,email,username,password);
        gson = new Gson();
        final String registerString = gson.toJson(user);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               if(response.contains("failed")){
                    progress.hide();
                    Toast.makeText(RegisterActivity.this,"تم تسجيل هذا الحساب من قبل",Toast.LENGTH_LONG).show();

                }else{
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userInfo",response);
                    editor.commit();
                    progress.hide();
                    Toast.makeText(RegisterActivity.this,"تم التسجيل بنجاح",Toast.LENGTH_SHORT).show();
                    intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("user",registerString);
                return params;
            }
        };
        MySingleton.getInstance(this).addRequestQueue(stringRequest);

    }

    public void loginLink(View view){
        intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}
