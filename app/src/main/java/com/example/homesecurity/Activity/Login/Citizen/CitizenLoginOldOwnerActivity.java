package com.example.homesecurity.Activity.Login.Citizen;

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
import com.example.homesecurity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CitizenLoginOldOwnerActivity extends AppCompatActivity {
    EditText edPassword;
    Button btnLogin;
    public String flat, password;

    DatabaseReference mUserDatabaseCitizen;

    String LoginDetails = "loginDetails";
    String accountType = "accountType";
    String LoggedIN = "loggin";

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";

    String SP_FLAT = "flat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_login_old_owner);

        initialize();

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();

                getFlat();

                mUserDatabaseCitizen.child(flat).child("info").child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String passwordAccount = (String) snapshot.getValue();
                        if (passwordAccount.equals(password)) {
                           //set login Details
                            setLoginDetails();

                            btnLogin.setEnabled(false);

                            //allow entry
                            openCitizenMain();
                        } else {
                            //old user
                            Toast.makeText(CitizenLoginOldOwnerActivity.this, "the password entered for the given flat is wrong", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void setLoginDetails() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(accountType,"Citizen");
        editor.putBoolean(LoggedIN,true);
        editor.putString(SP_FLAT,flat);

        editor.commit();
    }

    private void getData() {
        password = edPassword.getText().toString().trim();
    }

    private void getFlat() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        flat = sharedPreferences.getString(SP_FLAT, "");
    }

    private void initialize() {
        edPassword = findViewById(R.id.Ed_FlatPasswordLoginOld);
        btnLogin = findViewById(R.id.Btn_CitizenFlatNextOld);
    }

    private void openCitizenMain() {
        startActivity(new Intent(CitizenLoginOldOwnerActivity.this, CitizenMainActivity.class));
    }
}