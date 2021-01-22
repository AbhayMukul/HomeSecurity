package com.example.homesecurity.Activity.Citizen;

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

import com.example.homesecurity.Activity.Guard.PhoneGuardActivity;
import com.example.homesecurity.Activity.Login.Citizen.LoginCitizenFlatActivity;
import com.example.homesecurity.Activity.Login.Citizen.OTPCitizenLoginActivity;
import com.example.homesecurity.Model.ModelFamily;
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

public class NewFamilyActivity extends AppCompatActivity {
    private EditText edPhone, edName,edPassword;
    private Button btnDone;

    private Boolean check;
    String Phone, Name, flat, key, verification, validate,passwordFamilyMember;

    private DatabaseReference mUserDatabaseCitizen;
    private DatabaseReference mUserDatabaseNumber;

    PhoneAuthProvider.ForceResendingToken Token;

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";
    String SP_Password = "password";
    String SP_OTPType = "OTPType";
    String SP_Edit = "edit";
    String SP_Phone = "phone";
    String SP_Name = "name";
    String SP_Key = "key";
    String SP_FLAT = "flat";
    String SP_VERIFICATION = "verification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_family);

        initilize();

        getSharedPreferenceData();

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen");
        mUserDatabaseNumber = FirebaseDatabase.getInstance().getReference("FlatAdminNumber");

        edName.setText(Name);
        edPhone.setText(Phone);
        edPassword.setText(passwordFamilyMember);

        if (key.equals("")) {
            key = mUserDatabaseCitizen.push().getKey();
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                if (Name.equals("") || Phone.equals("") || passwordFamilyMember.equals("") || Phone.length() < 10) {
                    Toast.makeText(NewFamilyActivity.this, "please check the inputs", Toast.LENGTH_SHORT).show();
                } else {
                    btnDone.setEnabled(false);
                    setSharedPrefrence();
                    validateNumber();
                    if (validateNumber()) {
                        setSP_Number();
                        Phone = "+91" + Phone;
                        requestOTP(Phone);
                    } else
                        Toast.makeText(NewFamilyActivity.this, "Account with that number Exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setSP_Number() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SP_Phone, Phone);

        editor.commit();
    }

    private boolean validateNumber() {
        mUserDatabaseNumber.child(Phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                validate = (String) snapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (validate == null) {
            return true;
        }
        return false;
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

                Toast.makeText(NewFamilyActivity.this, "" + verification, Toast.LENGTH_SHORT).show();

                setSharedPrefrence();

                startActivity(new Intent(NewFamilyActivity.this, OTPCitizenLoginActivity.class));

                finish();
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        });
    }


    private void getSharedPreferenceData() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);

        passwordFamilyMember = sharedPreferences.getString(SP_Password,"");
        flat = sharedPreferences.getString(SP_FLAT, "");
        key = sharedPreferences.getString(SP_Key, "");
        Name = sharedPreferences.getString(SP_Name, "");
        Phone = sharedPreferences.getString(SP_Phone, "");
        check = sharedPreferences.getBoolean(SP_Edit, true);
    }

    private void getData() {
        passwordFamilyMember = edPassword.getText().toString().trim();
        Phone = edPhone.getText().toString().trim();
        Name = edName.getText().toString().trim();
    }

    private void initilize() {
        edName = findViewById(R.id.Ed_NewFamilyActivity_Name);
        edPhone = findViewById(R.id.Ed_NewFamilyActivity_Phone);
        edPassword = findViewById(R.id.Ed_NewFamilyActivity_Password);

        btnDone = findViewById(R.id.Btn_NewFamilyActivity_Done);
    }

    private void setSharedPrefrence() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SP_Password,passwordFamilyMember);
        editor.putString(SP_OTPType, "Family");
        editor.putString(SP_Name, Name);
        editor.putString(SP_Key, key);
        editor.putString(SP_VERIFICATION, verification);
        editor.putBoolean(SP_Edit, false);

        editor.commit();
    }


}