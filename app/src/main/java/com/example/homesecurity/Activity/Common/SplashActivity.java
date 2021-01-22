package com.example.homesecurity.Activity.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.homesecurity.Activity.Citizen.CitizenMainActivity;
import com.example.homesecurity.Activity.Guard.MainActivity;
import com.example.homesecurity.Activity.Login.LoginActivity;
import com.example.homesecurity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    private String admin,phone;

    String CitizenDetails = "citizenDetails";
    String SP_FLAT = "flat";
    String SP_Admin = "admin";
    String SP_PhoneFamily = "phone";

    String LoginDetails = "loginDetails";
    String accountType = "accountType";

    private DatabaseReference mUserDatabase;
    private DatabaseReference mUserDatabaseNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Check");
        mUserDatabaseNumber = FirebaseDatabase.getInstance().getReference("Master");

        Toast.makeText(SplashActivity.this, "" + getAccountType(), Toast.LENGTH_SHORT).show();

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = (String) snapshot.getValue();

                if (status.equals("true")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (getAccountType().equals("Guard")) {
                                //set login for Guard
                                Intent n = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(n);
                                finish();
                            } else if (getAccountType().equals("Citizen")) {
                                //set login for Citizen
                                //get Account Admin Data
                                getAdmin();
                                Intent n = new Intent(SplashActivity.this, CitizenMainActivity.class);
                                startActivity(n);
                                finish();
                            } if (getAccountType().equals("")){
                                //Login
                                Intent n = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(n);
                                finish();
                            }
                        }
                    }, 1000);
                } else
                    Toast.makeText(SplashActivity.this, "please the validity of the demo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getAdmin() {
        getPhone();
        mUserDatabaseNumber.child(phone).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                admin = (String) snapshot.getValue();
                setSharedPrefrencesData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSharedPrefrencesData() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SP_Admin,admin);

        editor.commit();
    }

    private void getPhone() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        phone = sharedPreferences.getString(SP_PhoneFamily,"");
    }

    private String getAccountType() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);

        String account = sharedPreferences.getString(accountType, "");

        return account;
    }
}