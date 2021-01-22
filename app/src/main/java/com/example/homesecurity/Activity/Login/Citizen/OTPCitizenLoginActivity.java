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
import com.example.homesecurity.Activity.Guard.OTPActivity;
import com.example.homesecurity.Activity.Guard.PhoneGuardActivity;
import com.example.homesecurity.Model.ModelAccountFlat;
import com.example.homesecurity.Model.ModelFamily;
import com.example.homesecurity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OTPCitizenLoginActivity extends AppCompatActivity {
    private EditText edOTP;
    private Button btnNext;

    String verification, OTP, flat, type,key,Name,Phone,password;

    private DatabaseReference mUserDatabaseCitizen;
    private DatabaseReference mUserDatabaseNumber;

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";
    String SP_Password = "password";
    String SP_VERIFICATION = "verification";
    String SP_OTPType = "OTPType";
    String SP_Edit = "edit";
    String SP_Phone = "phone";
    String SP_Name = "name";
    String SP_Key = "key";
    String SP_FLAT = "flat";

    //login details
    String LoginDetails = "loginDetails";
    String accountType = "accountType";
    String LoggedIN = "loggin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_citizen_login);

        initialize();

        getData();

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen");
        mUserDatabaseNumber = FirebaseDatabase.getInstance().getReference("Master");

        getSharedPreferenceData();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                OTP = edOTP.getText().toString().trim();
                Toast.makeText(OTPCitizenLoginActivity.this, "" + verification, Toast.LENGTH_SHORT).show();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification, OTP);
                VerifyAuth(credential);
            }
        });
    }

    private void VerifyAuth(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(OTPCitizenLoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    btnNext.setEnabled(false);

                    //OTP for new family check
                    if (type.equals("Family")) {
                        uploadData();
                        finish();
                    }

                    //OTP for login
                    else {
                        startActivity(new Intent(OTPCitizenLoginActivity.this, CitizenMainActivity.class));
                        setLoginDetails();
                    }


                    finish();
                } else {
                    Toast.makeText(OTPCitizenLoginActivity.this, "Failed Authentication", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        ModelFamily modelFamily = new ModelFamily(key, Name, Phone,password,"");
        ModelAccountFlat modelAccountFlat = new ModelAccountFlat(flat,"",password);

        //set data in family
        mUserDatabaseCitizen.child(flat).child("family").child(Phone).setValue(modelFamily);

        //set Data in NumberList
       mUserDatabaseNumber.child(Phone).setValue(modelAccountFlat);
    }

    private void setLoginDetails() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(accountType, "Citizen");
        editor.putBoolean(LoggedIN, true);
        editor.putString(SP_FLAT, flat);

        editor.commit();
    }

    private void getData() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        verification = sharedPreferences.getString(SP_VERIFICATION, "");
        type = sharedPreferences.getString(SP_OTPType, "");
        flat = sharedPreferences.getString(SP_FLAT, "");
    }

    private void initialize() {
        edOTP = findViewById(R.id.Ed_OTPCitizenLoginActivity_OTP);
        btnNext = findViewById(R.id.Btn_OTPCitizenLoginActivity_Verify);
    }

    private void getSharedPreferenceData() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);

        password = sharedPreferences.getString(SP_Password,"");
        flat = sharedPreferences.getString(SP_FLAT, "");
        key = sharedPreferences.getString(SP_Key, "");
        Name = sharedPreferences.getString(SP_Name, "");
        Phone = sharedPreferences.getString(SP_Phone, "");
    }
}