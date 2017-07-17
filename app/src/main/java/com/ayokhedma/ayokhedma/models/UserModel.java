package com.ayokhedma.ayokhedma.models;

/**
 * Created by MK on 25/06/2017.
 */

public class UserModel {
    private String id,name,email,username,password;

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserModel(String name, String email,String username, String password) {

        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
