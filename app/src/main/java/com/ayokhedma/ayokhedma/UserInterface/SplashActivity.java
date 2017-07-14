package com.ayokhedma.ayokhedma.UserInterface;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ayokhedma.ayokhedma.Connection.MySingleton;
import com.ayokhedma.ayokhedma.Models.UserModel;
import com.ayokhedma.ayokhedma.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    SharedPreferences sharedpreferences;
    Intent  intent;
    Gson gson;
    UserModel user;
    String link;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();

        imageView = (ImageView) findViewById(R.id.splash_logo);

        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash);
        animation.setRepeatMode(Animation.REVERSE);
        imageView.startAnimation(animation);

        sharedpreferences = getSharedPreferences("userprefences", Context.MODE_PRIVATE);
        if (null != actionBar) {
            actionBar.hide();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedpreferences.contains("userInfo")){
                    link = "http://www.fatmanoha.com/ayokhedma/login.php";
                    gson = new Gson();
                    String userjson = sharedpreferences.getString("userInfo","");
                    user = gson.fromJson(userjson,UserModel.class);
                    String username = user.getUsername(),
                    password = user.getPassword();
                    loginProcess(username,password);
                }else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    public void loginProcess(String username,String password){
        user = new UserModel(username,password);
        gson = new Gson();
        final String loginString = gson.toJson(user);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("null")){
                    Toast.makeText(SplashActivity.this,"يوجد خطأ في تسجيل الدخول",Toast.LENGTH_LONG).show();
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(SplashActivity.this,"تم تسجيل الدخول بنجاح",Toast.LENGTH_SHORT).show();
                    intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                SplashActivity.this.finish();
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
                params.put("user",loginString);
                return params;
            }
        };
        MySingleton.getInstance(this).addRequestQueue(stringRequest);

    }


}
