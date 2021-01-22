package com.example.homesecurity.Model;

public class ModelNotice {
    String key;
    String Notice;
    String Date;
    String Time;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ModelNotice() {
    }

    public String getNotice() {
        return Notice;
    }

    public void setNotice(String notice) {
        Notice = notice;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public ModelNotice(String key, String notice, String date, String time) {
        this.key = key;
        Notice = notice;
        Date = date;
        Time = time;
    }
}
