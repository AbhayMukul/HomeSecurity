package com.example.homesecurity.Activity.Guard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homesecurity.Model.ModelCitizenActive;
import com.example.homesecurity.Model.ModelGuest;
import com.example.homesecurity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Date;

public class PhoneGuardActivity extends AppCompatActivity {
    private TextView tvCitizenNumber;
    private Button btnCall, btnAllow, btnNotAllow;
    String key, name, phone, flat, work, date, Uid, d,CitizenPhone;

    String LoginDetails = "loginDetails";
    String SP_GUARDCODE = "guardcode";
    String code;


    SharedPreferences sharedPreferences;
    String GuestDetails = "guestDetails";

    String SP_KEY = "key";
    String SP_NAME = "name";
    String SP_PHONE = "phone";
    String SP_FLAT = "flat";
    String SP_WORK = "work";

    DatabaseReference mUserDatabaseCitizen;
    DatabaseReference mUserDatabaseGuest;
    String time;

    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_guard);

        initialize();
        getData();

        btnAllow.setEnabled(false);
        btnNotAllow.setEnabled(false);

        Date date = new Date();
        d = (String) android.text.format.DateFormat.format("dd/M/yyyy", date);
        time = (String) android.text.format.DateFormat.format("kk:mm:ss", date);

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen");
        mUserDatabaseGuest = FirebaseDatabase.getInstance().getReference("guest");

        getPhone();
        getGuardID();

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
                btnAllow.setEnabled(true);
                btnNotAllow.setEnabled(true);
            }
        });

        btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAllow();
                btnAllow.setEnabled(false);
                openMain();
            }
        });

        btnNotAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadNotAllow();
                btnNotAllow.setEnabled(false);
                openMain();
            }
        });
    }

    private void getGuardID() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);

        code = sharedPreferences.getString(SP_GUARDCODE,"");
    }

    private void makePhoneCall() {
        if (CitizenPhone.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(PhoneGuardActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PhoneGuardActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + CitizenPhone;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(PhoneGuardActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getPhone() {
        mUserDatabaseCitizen.child(flat).child("info").child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CitizenPhone = (String) snapshot.getValue();
                btnCall.setText("Call Flat :-" + flat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getData() {
        key = sharedPreferences.getString(SP_KEY, "");
        name = sharedPreferences.getString(SP_NAME, "");
        phone = sharedPreferences.getString(SP_PHONE, "");
        work = sharedPreferences.getString(SP_WORK, "");

        flat = sharedPreferences.getString(SP_FLAT, "");
    }

    private void uploadNotAllow() {
        //ModelCitizenActive modelGuestCitizen = new ModelCitizenActive(key, name, phone, work, flat, "", d, false);
        ModelGuest modelGuestGuard = new ModelGuest(key, name, phone, work, flat, "", "", false, false, d, time, true, "", "",code,"");

        //Citizen All
        mUserDatabaseCitizen.child(flat).child("Guest").child("All").child(key).setValue(modelGuestGuard);

        //Guard All
        mUserDatabaseGuest.child("All").child(key).setValue(modelGuestGuard);
    }

    private void uploadAllow() {
        // ModelCitizenActive modelGuestCitizen = new ModelCitizenActive(key, name, phone, work, flat, "", d, true);
        ModelGuest modelGuestGuard = new ModelGuest(key, name, phone, work, flat, "", "", false, true, d, time, false, "", "",code,"");

        //Citizen Active
        mUserDatabaseCitizen.child(flat).child("Guest").child("Active").child(key).setValue(modelGuestGuard);
        //Citizen All
        mUserDatabaseCitizen.child(flat).child("Guest").child("All").child(key).setValue(modelGuestGuard);

        //Guard Active
        mUserDatabaseGuest.child("Active").child(key).setValue(modelGuestGuard);
        //Guard All
        mUserDatabaseGuest.child("All").child(key).setValue(modelGuestGuard);

    }

    private void openMain() {
        startActivity(new Intent(PhoneGuardActivity.this, MainActivity.class));
        finish();
    }

    private void initialize() {
        sharedPreferences = getSharedPreferences(GuestDetails, Context.MODE_PRIVATE);

        btnCall = findViewById(R.id.Btn_PhoneCitizen);
        btnAllow = findViewById(R.id.Btn_CitizenAllow);
        btnNotAllow = findViewById(R.id.Btn_CitizenNotAllow);
    }

}