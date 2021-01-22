package com.example.homesecurity.Activity.Login.Citizen;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CitizenLoginNewActivity extends AppCompatActivity {
    private EditText edNameCitizenNew, edPhoneCitizenNew, edPasswordCitizenNew;
    private Button btnUpload;
    private String name, phone, password;

    private DatabaseReference mUserDatabaseCitizen;

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";

    String LoginDetails = "loginDetails";
    String accountType = "accountType";
    String LoggedIN = "loggin";

    String SP_FLAT = "flat";

    String flat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_login_new);

        initialize();

        getFlat();

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();

                if(validate()){
                    ModelCitizen modelCitizen = new ModelCitizen(name, password, phone);

                    //set Data
                    mUserDatabaseCitizen.child(flat).child("info").setValue(modelCitizen);

                    //set Booleen
                    mUserDatabaseCitizen.child(flat).child("AccountActive").setValue(true);

                    //set logging in Shared prefrence
                    setLoginDetails();

                    btnUpload.setEnabled(false);

                    openCitizenMain();
                }
            }
        });
    }

    private boolean validate() {
        Integer check = phone.length();
        if (name.equals("") || phone.equals("") || password.equals("")) {
            Toast.makeText(this, "please check all the fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!check.equals(10)){
            Toast.makeText(this, "please check the phone number " + check, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setLoginDetails() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(accountType,"Citizen");
        editor.putBoolean(LoggedIN,true);
        editor.putString(SP_FLAT,flat);

        editor.commit();
    }

    private void getFlat() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        flat = sharedPreferences.getString(SP_FLAT, "");
    }

    private void getData() {
        name = edNameCitizenNew.getText().toString().trim();
        phone = edPhoneCitizenNew.getText().toString().trim();
        password = edPasswordCitizenNew.getText().toString().trim();
    }

    private void initialize() {
        edNameCitizenNew = findViewById(R.id.Ed_FlatNameLoginNew);
        edPhoneCitizenNew = findViewById(R.id.Ed_FlatPhoneLoginNew);
        edPasswordCitizenNew = findViewById(R.id.Ed_FlatPasswordLoginNew);

        btnUpload = findViewById(R.id.Btn_CitizenFlatNextNew);
    }

    private void openCitizenMain() {
        startActivity(new Intent(CitizenLoginNewActivity.this, CitizenMainActivity.class));
    }
}