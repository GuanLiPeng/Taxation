package com.example.taxation.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 用户类，用来存放用户信息
 */
public class UserInfo extends LitePalSupport {

    private int id;
    private String username;
    private String password;
    private String login;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
