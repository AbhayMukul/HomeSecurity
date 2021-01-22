package com.example.homesecurity.Model;

public class ModelGuestActive {
    //common
    String KeyUID;
    String Name;
    String Phone;
    String Work;
    String Flat;

    //specifically Citizen extra Active and All
    String DateOutCitizen, TimeOutCitizen;

    //Guard Active and All
    Boolean ExitCitizen;
    Boolean Allowed;
    String DateInGuard;
    String TimeInGuard;
    Boolean ExitGuard;
    String TimeOutGuard;
    String DateOutGuard;

    String GuardIn;
    String GuardOut;

    public String getGuardIn() {
        return GuardIn;
    }

    public void setGuardIn(String guardIn) {
        GuardIn = guardIn;
    }

    public String getGuardOut() {
        return GuardOut;
    }

    public void setGuardOut(String guardOut) {
        GuardOut = guardOut;
    }

    public ModelGuestActive() {
    }

    public ModelGuestActive(String keyUID, String name, String phone, String work, String flat, String dateOutCitizen, String timeOutCitizen, Boolean exitCitizen, Boolean allowed, String dateInGuard, String timeInGuard, Boolean exitGuard, String timeOutGuard, String dateOutGuard) {
        KeyUID = keyUID;
        Name = name;
        Phone = phone;
        Work = work;
        Flat = flat;
        DateOutCitizen = dateOutCitizen;
        TimeOutCitizen = timeOutCitizen;
        ExitCitizen = exitCitizen;
        Allowed = allowed;
        DateInGuard = dateInGuard;
        TimeInGuard = timeInGuard;
        ExitGuard = exitGuard;
        TimeOutGuard = timeOutGuard;
        DateOutGuard = dateOutGuard;
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

    public String getDateOutCitizen() {
        return DateOutCitizen;
    }

    public void setDateOutCitizen(String dateOutCitizen) {
        DateOutCitizen = dateOutCitizen;
    }

    public String getTimeOutCitizen() {
        return TimeOutCitizen;
    }

    public void setTimeOutCitizen(String timeOutCitizen) {
        TimeOutCitizen = timeOutCitizen;
    }

    public Boolean getExitCitizen() {
        return ExitCitizen;
    }

    public void setExitCitizen(Boolean exitCitizen) {
        ExitCitizen = exitCitizen;
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

    public String getTimeInGuard() {
        return TimeInGuard;
    }

    public void setTimeInGuard(String timeInGuard) {
        TimeInGuard = timeInGuard;
    }

    public Boolean getExitGuard() {
        return ExitGuard;
    }

    public void setExitGuard(Boolean exitGuard) {
        ExitGuard = exitGuard;
    }

    public String getTimeOutGuard() {
        return TimeOutGuard;
    }

    public void setTimeOutGuard(String timeOutGuard) {
        TimeOutGuard = timeOutGuard;
    }

    public String getDateOutGuard() {
        return DateOutGuard;
    }

    public void setDateOutGuard(String dateOutGuard) {
        DateOutGuard = dateOutGuard;
    }
}
