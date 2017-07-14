package com.ayokhedma.ayokhedma.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ayokhedma.ayokhedma.Models.ObjectModel;
import com.ayokhedma.ayokhedma.Connection.MySingleton;
import com.ayokhedma.ayokhedma.Models.RateModel;
import com.ayokhedma.ayokhedma.Models.UserModel;
import com.ayokhedma.ayokhedma.UserInterface.ObjectActivity;
import com.ayokhedma.ayokhedma.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment{
    TextView address,description,worktime,weekend;
    private String link;
    private ObjectModel object;
    private Gson gson;
    ProgressDialog progress;
    private ObjectActivity objectActivity;
    RatingBar ratingBar;
    SharedPreferences sharedPreferences;
    UserModel user;
    String id;
    RateModel rateModel;

    public DescriptionFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        address = (TextView) view.findViewById(R.id.address);
        description = (TextView) view.findViewById(R.id.description);
        worktime = (TextView) view.findViewById(R.id.worktime);
        weekend = (TextView) view.findViewById(R.id.weekend);
        ratingBar = (RatingBar) view.findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
              if(fromUser){setRate(rating);}
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("userprefences", Context.MODE_PRIVATE);

        objectActivity = (ObjectActivity) getActivity();
        id = objectActivity.getIntent().getStringExtra("id");

        link = "http://www.fatmanoha.com/ayokhedma/object.php?objid=" + id;
        getData();
        getRate();
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
                        String start1 = trimming(object.getStart1()),
                        end1 = trimming(object.getEnd1()),
                        start2 = trimming(object.getStart2()),
                        end2 = trimming(object.getEnd2()),
                        week = object.getWeekend(),
                        work;
                        address.setText("العنوان : شارع " + object.getStreet() + " " + object.getBeside());
                        description.setText("الوصف : " + object.getDescription());
                        if(start2.equals("0:00") && end2.equals("0:00")){
                            work = "أوقات العمل : من " + start1 + " إلى " + end1;
                            worktime.setText(work);
                        }else {
                            work = "أوقات العمل : من " + start1 + " إلى " + end1 + "\n" +
                              " و من " + start2 + " إلى " + end2;
                            worktime.setText(work);
                        }
                        if (!week.equals("")){
                            weekend.setText("يوم العطلة : " + week);
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
    private void getRate(){
        link = "http://www.fatmanoha.com/ayokhedma/rate.php?";
        gson = new Gson();
        String userjson = sharedPreferences.getString("userInfo","");
        user = gson.fromJson(userjson,UserModel.class);
        String userId = user.getId();
        rateModel = new RateModel(id,userId);
        final String rateJson = "{\"objid\" : \" " + rateModel.getObjid() +"\" , \"userid\" : \" " + rateModel.getUserid() + "\"}";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(!response.equals("")) {
                           gson = new Gson();
                           rateModel = gson.fromJson(response, RateModel.class);
                           ratingBar.setRating(rateModel.getRate());
                       }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("rate",rateJson);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addRequestQueue(stringRequest);
    }

    private void setRate(float rate){
        link = "http://www.fatmanoha.com/ayokhedma/rate.php?";
        gson = new Gson();
        String userjson = sharedPreferences.getString("userInfo","");
        user = gson.fromJson(userjson,UserModel.class);
        String userId = user.getId();
        rateModel = new RateModel(id,userId,rate);
        final String rateJson = gson.toJson(rateModel);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gson = new Gson();
                        String done = gson.fromJson(response,String.class);
                        Toast.makeText(getActivity(), done , Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("rate",rateJson);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addRequestQueue(stringRequest);
    }
    private String trimming(String string){
        List<Character> chars = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        String time;
        int j;
        for (int i = 0; i<5;i++){
            char c = string.charAt(i);
            chars.add(c);
        }
        if(string.charAt(0) == '0'){
            j = 1;
        }else{
            j = 0;
        }
        for (int i = j ; i < chars.size(); i++){
            builder.append(chars.get(i));
        }
        time = builder.toString();
        return  time;
    }
}
