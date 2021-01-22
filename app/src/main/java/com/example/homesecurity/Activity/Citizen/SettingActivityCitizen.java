package com.example.homesecurity.Activity.Citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.homesecurity.R;

public class SettingActivityCitizen extends AppCompatActivity {
    private EditText edName,edPassword,edPhone,edFlat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_citizen2);
    }
}