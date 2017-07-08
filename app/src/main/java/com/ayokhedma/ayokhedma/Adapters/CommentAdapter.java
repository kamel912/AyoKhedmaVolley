package com.ayokhedma.ayokhedma.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayokhedma.ayokhedma.Models.CategoryModel;
import com.ayokhedma.ayokhedma.Models.CommentModel;
import com.ayokhedma.ayokhedma.R;
import com.ayokhedma.ayokhedma.UserInterface.CategoryObjectsActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MK on 26/05/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    List<CommentModel> comments = new ArrayList<>();



    public CommentAdapter(Context context, List<CommentModel> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentModel comment = comments.get(position);
        holder.userName.setText(comment.getUsername());
        holder.commentBody.setText(comment.getcommentBody());
        holder.subject.setText(comment.getSubject());

        holder.itemView.setTag(comment);

    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView userName,commentBody,subject;

        public MyViewHolder(final View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.username);
            commentBody = (TextView) itemView.findViewById(R.id.coment_body);
            subject = (TextView) itemView.findViewById(R.id.subject);
        }
    }
}
