package com.example.login_with_database;

public class Member {
    private String name;
    private String email;
    private String pwd;

    public Member() {
    }
    public Member(String name,String email){
        this.name=name;
        this.email=email;
    }

    public String getN() {
        return name;
    }

    public void setN(String name) {
        this.name = name;
    }

    public String getEm() {
        return email;
    }

    public void setEm(String email) {
        this.email = email;
    }

    public String getPw() {
        return pwd;
    }

    public void setPw(String pwd) {
        this.pwd = pwd;
    }
}
