package com.example.homesecurity.Activity.Login.Citizen.LoginPhone;

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

import com.example.homesecurity.Activity.Login.Citizen.LoginCitizenFlatActivity;
import com.example.homesecurity.Activity.Login.Citizen.OTPCitizenLoginActivity;
import com.example.homesecurity.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {
    private EditText edPhone;
    private Button btnNext;

    private String phone, flat,verification,admin;

    private DatabaseReference mUserDatabaseNumber;
    PhoneAuthProvider.ForceResendingToken Token;

    String CitizenLoginDetails = "citizenLoginDetails";
    String SP_Name = "name";
    String SP_Password = "password";
    String SP_Phone = "phone";

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";
    String SP_OTPType = "OTPType";
    String SP_PhoneFamily = "phone";
    String SP_VERIFICATION = "verification";
    String SP_FLAT = "flat";
    String SP_Admin = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        initialize();

        mUserDatabaseNumber = FirebaseDatabase.getInstance().getReference("Master");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //data from editText
                getDataEditText();

                if(phone.equals("ce8a7f9814")){
                    startActivity(new Intent(LoginPhoneActivity.this,LoginCitizenFlatActivity.class));
                }
                
                //check flat if exist with number
                getNumberFLat();
                getAdmin();

                if(flat == null){
                    //number not registered to flat
                    Toast.makeText(LoginPhoneActivity.this, "number not registered to a given flat", Toast.LENGTH_SHORT).show();
                }
                else {
                    //number registerd to a given flat
                    setSP_Phone();
                    btnNext.setEnabled(false);
                    phone = "+91" + phone;
                    requestOTP(phone);
                }
            }
        });

    }

    private void setSP_Phone() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SP_PhoneFamily,phone);

        editor.commit();
    }

    private void getNumberFLat() {
        mUserDatabaseNumber.child(phone).child("flat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flat = (String) snapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAdmin() {
        mUserDatabaseNumber.child(phone).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                admin = (String) snapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataEditText() {
        phone = edPhone.getText().toString().trim();
    }

    private void initialize() {
        edPhone = findViewById(R.id.Ed_LoginPhoneActivity_Phone);

        btnNext = findViewById(R.id.Btn_LoginPhoneActivity_Next);
    }

    private void requestOTP(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 10L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification = s;

                Token = forceResendingToken;

                //Toast.makeText(LoginCitizenFlatActivity.this, "" + verification, Toast.LENGTH_SHORT).show();

                putData();

                startActivity(new Intent(LoginPhoneActivity.this, OTPCitizenLoginActivity.class));

            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        });
    }

    private void putData() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SP_OTPType,"");
        editor.putString(SP_FLAT, flat);
        editor.putString(SP_Admin,admin);
        editor.putString(SP_VERIFICATION, verification);

        editor.commit();
    }
}