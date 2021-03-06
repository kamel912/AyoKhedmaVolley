package com.ayokhedma.ayokhedma.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ayokhedma.ayokhedma.adapters.CommentAdapter;
import com.ayokhedma.ayokhedma.connection.MySingleton;
import com.ayokhedma.ayokhedma.models.CommentModel;
import com.ayokhedma.ayokhedma.models.UserModel;
import com.ayokhedma.ayokhedma.R;
import com.ayokhedma.ayokhedma.userInterface.ObjectActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment{

    String id,count;
    private String link;
    private Gson gson;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private List<CommentModel> comments = new ArrayList<>();
    private CommentAdapter adapter;
    private ObjectActivity objectActivity;
    Button add_comment_text,add,cancel;
    CommentModel comment;
    SharedPreferences sharedPreferences;
    UserModel user;
    TextView comment_count;
    RelativeLayout comments_parent,add_comment_parent;
    EditText subject_field,commentBody_field;
    ProgressDialog progress;


    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);


        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView = (RecyclerView) view.findViewById(R.id.comment_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        comment_count = (TextView) view.findViewById(R.id.comment_count);
        add_comment_text = (Button) view.findViewById(R.id.add_comment_text);
        add = (Button) view.findViewById(R.id.add);
        cancel = (Button) view.findViewById(R.id.cancel);
        add_comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Please wait ....");
        progress.setMax(100);
        progress.show();

        subject_field = (EditText) view.findViewById(R.id.subject_field);
        commentBody_field = (EditText) view.findViewById(R.id.comment_field);

        comments_parent = (RelativeLayout) view.findViewById(R.id.comments_parent);
        add_comment_parent = (RelativeLayout) view.findViewById(R.id.add_comment_parent);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("userprefences", Context.MODE_PRIVATE);



        objectActivity = (ObjectActivity) getActivity();
        id = objectActivity.getIntent().getStringExtra("id");


        link = "http://www.fatmanoha.com/ayokhedma/comment.php?objid=" + id;
        getData();
    }
    private void getData(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("null")){
                            recyclerView.setVisibility(View.GONE);
                            comment_count.setText("لا يوجد تعليقات");
                        }else {
                            gson = new Gson();
                            comments = Arrays.asList(gson.fromJson(response, CommentModel[].class));
                            adapter = new CommentAdapter(getActivity(), comments);
                            recyclerView.setAdapter(adapter);
                            count = String.valueOf(comments.size());
                            comment_count.setText(commentCount(count));

                        }
                        progress.hide();

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



    public void add(){
        comments_parent.setVisibility(View.GONE);
        add_comment_parent.setVisibility(View.VISIBLE);
    }
    public void cancel(){
        comments_parent.setVisibility(View.VISIBLE);
        add_comment_parent.setVisibility(View.GONE);
    }
    public void addComment (){
        progress.show();
        gson = new Gson();
        String userjson = sharedPreferences.getString("userInfo","");
        user = gson.fromJson(userjson,UserModel.class);
        String userId = user.getId();
        String subject = subject_field.getText().toString();
        String commentBody = commentBody_field.getText().toString();
        comment = new CommentModel(userId,subject,commentBody,id);
        link = "http://www.fatmanoha.com/ayokhedma/addcomment.php";
        final String Commentjson = gson.toJson(comment);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gson = new Gson();
                        String message = gson.fromJson(response,String.class);
                        progress.hide();
                        displayAlert(message);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "خطأ في الإتصال بالخادم", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("comment",Commentjson);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addRequestQueue(stringRequest);
        subject_field.setText("");
        commentBody_field.setText("");
    }
    private void displayAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Server response ....");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancel();

                }
        });
        builder.create();
        builder.show();
    }
    private String commentCount(String string){
        String c = null;
        if (Integer.parseInt(string) < 4 ) {
            switch (string) {
                case "1":
                    c = "واحد تعليق";
                    break;
                case "2":
                    c = "يوجد تعليقان";
                    break;
                case "3":
                    c = "يوجد ثلاثة تعليقات";
                    break;
            }
        }else {
            c = string + " تعليقات";
        }
        return c;
    }
}
