package com.example.ngopi.object;

public class User {
    private String fullname,username,email,password,phonenum,usertype;

    public User() {

    }

    public User(String fullname, String username, String email, String password, String phonenum, String usertype) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phonenum = phonenum;
        this.usertype = usertype;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
