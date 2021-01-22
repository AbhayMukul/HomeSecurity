package com.example.homesecurity.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homesecurity.Activity.Citizen.CitizenMainActivity;
import com.example.homesecurity.Model.ModelCitizen;
import com.example.homesecurity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminFlatActivity extends AppCompatActivity {
    EditText flat, phone, name, password;
    Button btnCheck, btnCommit, btnGoToFlat;

    String CitizenFlat, CitizenPhone, CitizenName, CitizenPasswod;

    private DatabaseReference mUserDatabaseCitizen;

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";

    String SP_FLAT = "flat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_flat);

        initialize();

        btnCommit.setEnabled(false);
        btnGoToFlat.setEnabled(false);

        btnCommit.setEnabled(false);

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen");

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFlat();
                if (CitizenFlat.equals("demo")) {
                    getData();
                    btnCommit.setEnabled(true);
                    btnGoToFlat.setEnabled(true);
                }
                else
                    Toast.makeText(AdminFlatActivity.this, "the demo app does not permit this flat", Toast.LENGTH_SHORT).show();

            }
        });

        btnGoToFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CitizenFlat.equals("demo")) {
                    storeData();
                    startActivity(new Intent(AdminFlatActivity.this, CitizenMainActivity.class));
                } else
                    Toast.makeText(AdminFlatActivity.this, "the demo app does not permit this flat", Toast.LENGTH_SHORT).show();
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CitizenName = name.getText().toString().trim();
                CitizenPhone = phone.getText().toString().trim();
                CitizenPasswod = password.getText().toString().trim();

                if (validate()) {
                    commit();
                }
            }
        });
    }

    private void commit() {
        //commit
        ModelCitizen modelCitizen = new ModelCitizen(CitizenName, CitizenPasswod, CitizenPhone);

        //set Data
        mUserDatabaseCitizen.child(CitizenFlat).child("info").setValue(modelCitizen);

        //Toast
        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
    }

    private void storeData() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SP_FLAT, CitizenFlat);

        editor.commit();
    }

    private void getData() {
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

    private void getFlat() {
        CitizenFlat = flat.getText().toString().trim();
    }

    private void initialize() {
        flat = findViewById(R.id.Ed_AdminFlatActivity_Flat);
        phone = findViewById(R.id.Ed_AdminFlatActivity_Phone);
        name = findViewById(R.id.Ed_AdminFlatActivity_Name);
        password = findViewById(R.id.Ed_AdminFlatActivity_Password);

        btnCheck = findViewById(R.id.Btn_AdminFlatActivity_check);
        btnCommit = findViewById(R.id.Btn_AdminFlatActivity_commit);
        btnGoToFlat = findViewById(R.id.Btn_AdminFlatActivity_FlatMain);
    }
}