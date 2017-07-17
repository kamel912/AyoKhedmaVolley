package com.ayokhedma.ayokhedma.userInterface;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ayokhedma.ayokhedma.adapters.ObjectAdapter;
import com.ayokhedma.ayokhedma.connection.MySingleton;
import com.ayokhedma.ayokhedma.models.ObjectModel;
import com.ayokhedma.ayokhedma.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchableActivity extends AppCompatActivity  {
    SearchView searchView;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    String query = "";
    private String link;
    private List<ObjectModel> objects = new ArrayList<>();
    private Gson gson;
    private ObjectAdapter adapter;
    ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);


        progress = new ProgressDialog(this);
        progress.setMessage("Please wait ....");
        progress.setMax(100);
        progress.show();


        layoutManager = new GridLayoutManager(this,1);
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        onNewIntent(getIntent());

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQuery(query, true);
    }


    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }
            link = "http://www.fatmanoha.com/ayokhedma/search.php?search="+ query;
            objects();
        }

    }
    private void objects(){

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("null")) {
                            Toast.makeText(SearchableActivity.this,"لا توجد",Toast.LENGTH_SHORT).show();
                            progress.hide();
                        }else {
                            gson = new Gson();
                            objects = Arrays.asList(gson.fromJson(response, ObjectModel[].class));
                            adapter = new ObjectAdapter(SearchableActivity.this, objects);
                            recyclerView.setAdapter(adapter);
                            progress.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        MySingleton.getInstance(this).addRequestQueue(stringRequest);

    }


}
