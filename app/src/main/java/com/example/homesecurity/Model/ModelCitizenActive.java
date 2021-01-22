package com.example.homesecurity.Model;

public class ModelCitizenActive {
    String KeyUID;
    String Name;
    String Phone;
    String Work;
    String Flat;
    Boolean Allowed;
    String DateInGuard;
    String DateOutCitizen;

    public ModelCitizenActive(String keyUID, String name, String phone, String work, String flat, String dateOutCitizen, String dateInGuard,Boolean allowed) {
        KeyUID = keyUID;
        Name = name;
        Phone = phone;
        Work = work;
        Flat = flat;
        DateOutCitizen = dateOutCitizen;
        DateInGuard = dateInGuard;
        Allowed = allowed;
    }
    public Boolean getAllowed() {
        return Allowed;
    }

    public void setAllowed(Boolean allowed) {
        Allowed = allowed;
    }

    public String getDateInGuard() {
        return DateInGuard;
    }

    public void setDateInGuard(String dateInGuard) {
        DateInGuard = dateInGuard;
    }

    public String getDateOutCitizen() {
        return DateOutCitizen;
    }

    public void setDateOutCitizen(String dateOutCitizen) {
        DateOutCitizen = dateOutCitizen;
    }

    public String getKeyUID() {
        return KeyUID;
    }

    public void setKeyUID(String keyUID) {
        KeyUID = keyUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getWork() {
        return Work;
    }

    public void setWork(String work) {
        Work = work;
    }

    public String getFlat() {
        return Flat;
    }

    public void setFlat(String flat) {
        Flat = flat;
    }

}
