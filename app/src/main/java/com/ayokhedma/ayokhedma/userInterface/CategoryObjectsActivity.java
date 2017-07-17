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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ayokhedma.ayokhedma.adapters.ObjectAdapter;
import com.ayokhedma.ayokhedma.adapters.RegionAdapter;
import com.ayokhedma.ayokhedma.connection.MySingleton;
import com.ayokhedma.ayokhedma.models.ObjectModel;
import com.ayokhedma.ayokhedma.models.RegionModel;
import com.ayokhedma.ayokhedma.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class CategoryObjectsActivity extends AppCompatActivity  {
    private String catid,catname,regid;
    private String link,spinnerLink;
    private List<ObjectModel> objects = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private Gson gson;
    private ObjectAdapter adapter;
    private Spinner spinner;
    private List<RegionModel> regions = new ArrayList<>();
    private RegionAdapter regionAdapter;
    ProgressDialog progress;
    SearchView searchView;
    TextView title;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_objects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        title = (TextView) toolbar.findViewById(R.id.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent intent = getIntent();
        catid = intent.getStringExtra("id");
        catname = intent.getStringExtra("name");
        title.setText(catname);
        getSupportActionBar().setTitle(catname);

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait ....");
        progress.setMax(100);
        progress.show();

        spinnerLink = "http://www.fatmanoha.com/ayokhedma/region.php";
        spinner = (Spinner) findViewById(R.id.spinner);
        layoutManager = new GridLayoutManager(CategoryObjectsActivity.this,1);
        recyclerView = (RecyclerView) findViewById(R.id.objs_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        regions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        regions();
    }

    private void objects(){

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("null")) {
                            Toast.makeText(CategoryObjectsActivity.this,"لا توجد " + catname + " في هذه المنطقة",Toast.LENGTH_SHORT).show();
                            progress.hide();
                        }else {
                            gson = new Gson();
                            objects = Arrays.asList(gson.fromJson(response, ObjectModel[].class));
                            adapter = new ObjectAdapter(CategoryObjectsActivity.this, objects);
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
    private void regions(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                spinnerLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gson = new Gson();
                        regions = Arrays.asList(gson.fromJson(response,RegionModel[].class));
                        regionAdapter = new RegionAdapter(CategoryObjectsActivity.this,android.R.layout.simple_spinner_dropdown_item,regions);
                        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                regid = ((RegionModel) parent.getItemAtPosition(position)).getId();
                                if (regid.equals("1")){
                                    progress.show();
                                    link = "http://www.fatmanoha.com/ayokhedma/category.php?catid=" + catid;
                                    objects();
                                }else {
                                    progress.show();
                                    link = "http://www.fatmanoha.com/ayokhedma/category.php?catid=" + catid + "&regid=" + regid;
                                    objects();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        spinner.setAdapter(regionAdapter);
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


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu,menu);

    final MenuItem searchItem = menu.findItem(R.id.search_item);
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView = (SearchView) searchItem.getActionView();
    searchView.setOnSearchClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setItemsVisibility(menu,searchItem,false);
            title.setVisibility(View.GONE);
        }
    });
    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            setItemsVisibility(menu, searchItem, true);
            title.setVisibility(View.VISIBLE);
            return false;
        }
    });
    if(searchView != null) {
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(true);
    }
    return super.onCreateOptionsMenu(menu);
    }
    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) {
                item.setVisible(visible);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CategoryObjectsActivity.this,CategoriesActivity.class);
        startActivity(intent);
    }
}
