package com.example.homesecurity.Model;

public class ModelGuard {
    String ID;
    String Name;
    String Password;
    String Phone;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public ModelGuard(String ID, String name, String password, String phone) {
        this.ID = ID;
        Name = name;
        Password = password;
        Phone = phone;
    }
}
