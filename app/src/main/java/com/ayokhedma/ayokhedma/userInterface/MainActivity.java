package com.ayokhedma.ayokhedma.userInterface;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ayokhedma.ayokhedma.adapters.CategoryAdapter;
import com.ayokhedma.ayokhedma.connection.MySingleton;
import com.ayokhedma.ayokhedma.models.CategoryModel;
import com.ayokhedma.ayokhedma.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String limit = "6";
    List<CategoryModel> categories = new ArrayList<>();
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    private CategoryAdapter adapter;
    private Gson gson;
    private String link = "http://www.fatmanoha.com/ayokhedma/category.php?limit="+limit;
    ProgressDialog progress;
    private String image_path = "http://www.fatmanoha.com/ayokhedma/images/mainlogo.png";
    private ImageView logo;
    SearchView searchView;
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait ....");
        progress.setMax(100);
        progress.show();

        layoutManager  = new GridLayoutManager(MainActivity.this, 3);
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        categories();

        logo = (ImageView) findViewById(R.id.imageView);
        Glide.with(this).load(image_path).into(logo);

        sharedPreferences = getSharedPreferences("userprefences",Context.MODE_PRIVATE);

    }
    private void categories(){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("null")) {
                            Toast.makeText(MainActivity.this,"لا توجد أماكن",Toast.LENGTH_SHORT).show();
                            progress.hide();
                        }else {
                            gson = new Gson();
                            categories = Arrays.asList(gson.fromJson(response, CategoryModel[].class));
                            adapter = new CategoryAdapter(MainActivity.this, categories);
                            recyclerView.setAdapter(adapter);
                            progress.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.hide();
                        Toast.makeText(MainActivity.this, "خطأ في الإتصال بالخادم", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        MySingleton.getInstance(this).addRequestQueue(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);

        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchItem.getActionView();
       // searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
        if(searchView != null) {
          searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
          searchView.setIconified(true);
      }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout :
                if(sharedPreferences.contains("userInfo")){

                sharedPreferences.edit().clear().commit();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                }
        }
        return super.onOptionsItemSelected(item);
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
