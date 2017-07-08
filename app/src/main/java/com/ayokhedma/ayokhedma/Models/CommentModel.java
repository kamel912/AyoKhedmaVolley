package com.ayokhedma.ayokhedma.Models;

/**
 * Created by MK on 09/06/2017.
 */

public class CommentModel {
    private String userid,username,subject,commentBody,objid;

    public CommentModel(String userid, String subject, String commentBody, String objid) {
        this.userid = userid;
        this.subject = subject;
        this.commentBody = commentBody;
        this.objid = objid;
    }

    public CommentModel(String userid, String username, String subject, String commentBody, String objid) {
        this.userid = userid;
        this.username = username;
        this.subject = subject;
        this.commentBody = commentBody;
        this.objid = objid;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getSubject() {
        return subject;
    }

    public String getcommentBody() {
        return commentBody;
    }


}
