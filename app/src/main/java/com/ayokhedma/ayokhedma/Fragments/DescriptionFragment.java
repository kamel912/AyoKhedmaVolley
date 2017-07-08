package com.ayokhedma.ayokhedma.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ayokhedma.ayokhedma.Models.ObjectModel;
import com.ayokhedma.ayokhedma.Connection.MySingleton;
import com.ayokhedma.ayokhedma.UserInterface.ObjectActivity;
import com.ayokhedma.ayokhedma.R;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment{
    TextView address;
    private String link;
    private ObjectModel object;
    private Gson gson;
    ProgressDialog progress;
    private ObjectActivity objectActivity;



    public DescriptionFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        address = (TextView) view.findViewById(R.id.address);




        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        objectActivity = (ObjectActivity) getActivity();
        String id = objectActivity.getIntent().getStringExtra("id");

        link = "http://10.0.2.2/myapp/object.php?objid=" + id;
        getData();
    }

    private void getData(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gson = new Gson();
                        object = gson.fromJson(response,ObjectModel.class);
                        address.setText("العنوان : شارع " + object.getStreet() + " " + object.getBeside());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "خطأ في الإتصال بالخادم", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        MySingleton.getInstance(getContext()).addRequestQueue(stringRequest);
    }
}
