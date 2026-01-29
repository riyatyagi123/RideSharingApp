package com.ridesharing.model;

public class User {
    private int userid;
    private String name;
    private String mobile;
    private String password;

    public int getUserid() { return userid; }
    public void setUserid(int userid) { this.userid = userid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
