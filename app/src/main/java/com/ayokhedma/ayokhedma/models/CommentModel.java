package com.ayokhedma.ayokhedma.models;

/**
 * Created by MK on 09/06/2017.
 */

public class CommentModel {
    private String userid, name,subject,commentBody,objid;

    public CommentModel(String userid, String subject, String commentBody, String objid) {
        this.userid = userid;
        this.subject = subject;
        this.commentBody = commentBody;
        this.objid = objid;
    }

    public CommentModel(String userid, String username, String subject, String commentBody, String objid) {
        this.userid = userid;
        this.name = username;
        this.subject = subject;
        this.commentBody = commentBody;
        this.objid = objid;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getcommentBody() {
        return commentBody;
    }


}
