package com.example.homesecurity.Model;

public class ModelFamily {
    String key;
    String name;
    String phone;
    String password;
    String admin;

    public ModelFamily(String key, String name, String phone, String password, String admin) {
        this.key = key;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.admin = admin;
    }

    public ModelFamily() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
