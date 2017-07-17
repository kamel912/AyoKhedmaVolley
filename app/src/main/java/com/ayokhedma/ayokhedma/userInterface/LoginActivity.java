package com.ayokhedma.ayokhedma.userInterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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

public class LoginActivity extends AppCompatActivity {

    private String username,password,link;
    private EditText username_field,password_field;
    private UserModel user;
    private Gson gson;
    SharedPreferences sharedpreferences;
    Intent intent;
    ProgressDialog progress;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        progress = new ProgressDialog(this);
        progress.setMessage("Please wait ....");
        progress.setMax(100);

        username_field = (EditText) findViewById(R.id.username_field);
        password_field = (EditText) findViewById(R.id.password_field);

        link = "http://www.fatmanoha.com/ayokhedma/login.php";

        sharedpreferences = getSharedPreferences("userprefences", Context.MODE_PRIVATE);
    }

    public void registerLink(View view){
        intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        username = username_field.getText().toString();
        password = password_field.getText().toString();
        if (username.matches("") || password.matches("")) {
            Toast.makeText(this, "الرجاء ادخال بيانات تسجيل الدخول", Toast.LENGTH_SHORT).show();
        } else {
            loginProcess(username, password);
        }
    }
    public void loginProcess(String username,String password){
        progress.show();
        user = new UserModel(username,password);
        gson = new Gson();
        final String loginString = gson.toJson(user);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("null")){
                    progress.hide();
                    Toast.makeText(LoginActivity.this,"بيانات تسجيل الدخول غير صحيحة",Toast.LENGTH_LONG).show();

                }else{
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userInfo",response);
                    editor.commit();
                    progress.hide();
                    Toast.makeText(LoginActivity.this,"تم تسجيل الدخول بنجاح",Toast.LENGTH_SHORT).show();
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "خطأ في الإتصال بالخادم", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("user",loginString);
                return params;
            }
        };
        MySingleton.getInstance(this).addRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "إضغط مرتين لإغلاق التطبيق", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, 2000);

    }
}
