package com.example.homesecurity.Model;

public class ModelCitizen {
    String Name;
    String password;
    String Phone;

    public ModelCitizen(String name, String password, String phone) {
        this.Name = name;
        this.password = password;
        this.Phone = phone;
    }

    public ModelCitizen() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
