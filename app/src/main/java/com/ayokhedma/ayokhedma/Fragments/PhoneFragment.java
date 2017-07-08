package com.ayokhedma.ayokhedma.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ayokhedma.ayokhedma.Connection.MySingleton;
import com.ayokhedma.ayokhedma.Models.ObjectModel;
import com.ayokhedma.ayokhedma.R;
import com.ayokhedma.ayokhedma.UserInterface.ObjectActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneFragment extends Fragment {
    private List<String> phones = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView listView;
    private String link;
    private ObjectModel object;
    private Gson gson;
    ObjectActivity objectActivity;
    String id;
    private TextView emtyView;

    public PhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        listView = (ListView) view.findViewById(R.id.phone_list);
        emtyView = (TextView) view.findViewById(android.R.id.empty);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        objectActivity = (ObjectActivity) getActivity();
        id = objectActivity.getIntent().getStringExtra("id");

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
                        object = gson.fromJson(response, ObjectModel.class);
                        phones = object.getPhone();
                        String phone = phones.get(0);
                        if (phone == null) {
                            listView.setVisibility(View.GONE);
                            emtyView.setVisibility(View.VISIBLE);
                        }else {
                            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, phones);
                            listView.setAdapter(adapter);
                        }
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