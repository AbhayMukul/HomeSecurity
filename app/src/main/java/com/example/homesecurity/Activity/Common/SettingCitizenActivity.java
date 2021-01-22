package com.example.homesecurity.Activity.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homesecurity.Activity.Login.LoginActivity;
import com.example.homesecurity.Model.ModelCitizen;
import com.example.homesecurity.Model.ModelGuard;
import com.example.homesecurity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingCitizenActivity extends AppCompatActivity {
    EditText phone, name, password;
    TextView tvID;
    Button btnLogout, btnCommit;

    Boolean account;

    String CitizenFlat, CitizenPhone, CitizenName, CitizenPasswod, type, ID;

    private DatabaseReference mUserDatabaseCitizen, mUserDatabaseGuard;

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";

    String SP_FLAT = "flat";

    String LoginDetails = "loginDetails";
    String accountType = "accountType";
    String LoggedIN = "loggin";

    String SP_GUARDCODE = "guardcode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_citizen);

        initialize();

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen");
        mUserDatabaseGuard = FirebaseDatabase.getInstance().getReference("Guard");

        setData();

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (account == true) {
                        //Citizen Commit
                        commitCitizen();
                    } else {
                        //Guard commit
                        commitGuard();
                    }
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                startActivity(new Intent(SettingCitizenActivity.this, LoginActivity.class));
            }
        });
    }

    private void commitGuard() {
        CitizenName = name.getText().toString().trim();
        CitizenPhone = phone.getText().toString().trim();
        CitizenPasswod = password.getText().toString().trim();

        ModelGuard modelGuard = new ModelGuard(ID, CitizenName, CitizenPasswod, CitizenPhone);
        mUserDatabaseGuard.child(ID).child("info").setValue(modelGuard);

        //Toast
        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
    }

    private void setData() {
        if (getAccountType().equals("Guard")) {
            //set textview for Guard
            getDataGuard();

            account = false;
            tvID.setText("GUARD ID: -" + ID);
        } else {
            //set TextView for Citizen
            //without family section
            getDataCitizen();

            //with family section
            //getDataCitizenFamily();

            account = true;
            tvID.setText("FLAT ID :-" + CitizenFlat);
        }
    }

//    private void getDataCitizenFamily() {
//       mUserDatabaseCitizen.child(CitizenFlat).child("family").child(CitizenPhone);
//
//       //Name
//        mUserDatabaseCitizen.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void getDataGuard() {
        //get Guard ID
        getGuardID();

        //set data
        //password
        mUserDatabaseGuard.child(ID).child("info").child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CitizenPasswod = (String) snapshot.getValue();
                password.setText(CitizenPasswod);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //name
        mUserDatabaseGuard.child(ID).child("info").child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CitizenName = (String) snapshot.getValue();
                name.setText(CitizenName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //phone
        mUserDatabaseGuard.child(ID).child("info").child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CitizenPhone = (String) snapshot.getValue();
                phone.setText(CitizenPhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getGuardID() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);
        ID = sharedPreferences.getString(SP_GUARDCODE, "");
    }

    private String getAccountType() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(accountType, "");

        return type;
    }

    private void logout() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(accountType, "");
        editor.putBoolean(LoggedIN, false);
        editor.putString(SP_GUARDCODE, "");

        editor.commit();
    }

    private void getFlat() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        CitizenFlat = sharedPreferences.getString(SP_FLAT, "");
    }

    private void initialize() {
        tvID = findViewById(R.id.Tv_SettingCitizenActivity_Flat);
        name = findViewById(R.id.Ed_SettingCitizenActivity_Name);
        phone = findViewById(R.id.Ed_SettingCitizenActivity_Phone);
        password = findViewById(R.id.Ed_SettingCitizenActivity_Password);

        btnCommit = findViewById(R.id.Btn_SettingCitizenActivity_commit);
        btnLogout = findViewById(R.id.Btn_SettingCitizenActivity_Logout);
    }

    private void commitCitizen() {
        //commit
        CitizenName = name.getText().toString().trim();
        CitizenPhone = phone.getText().toString().trim();
        CitizenPasswod = password.getText().toString().trim();

        ModelCitizen modelCitizen = new ModelCitizen(CitizenName, CitizenPasswod, CitizenPhone);

        //set Data
        mUserDatabaseCitizen.child(CitizenFlat).child("info").setValue(modelCitizen);

        //Toast
        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
    }

    private void getDataCitizen() {
        //getFlat
        getFlat();

        //name
        mUserDatabaseCitizen.child(CitizenFlat).child("info").child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CitizenName = (String) snapshot.getValue();
                name.setText(CitizenName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Phone
        mUserDatabaseCitizen.child(CitizenFlat).child("info").child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CitizenPhone = (String) snapshot.getValue();
                phone.setText(CitizenPhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //password
        mUserDatabaseCitizen.child(CitizenFlat).child("info").child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CitizenPasswod = (String) snapshot.getValue();
                password.setText(CitizenPasswod);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Boolean validate() {
        //get Data
        CitizenName = name.getText().toString().trim();
        CitizenPhone = phone.getText().toString().trim();
        CitizenPasswod = password.getText().toString().trim();

        Integer check = CitizenPhone.length();
        if (CitizenPhone.equals("") || CitizenPasswod.equals("") || CitizenName.equals("")) {
            Toast.makeText(this, "please check all the fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!check.equals(10)) {
            Toast.makeText(this, "please check the phone number " + check, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}