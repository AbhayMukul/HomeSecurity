package com.example.homesecurity.Model;

public class ModelAccountFlat {
    String flat;
    String admin;
    String password;

    public ModelAccountFlat() {
    }

    public ModelAccountFlat(String flat, String admin, String password) {
        this.flat = flat;
        this.admin = admin;
        this.password = password;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
