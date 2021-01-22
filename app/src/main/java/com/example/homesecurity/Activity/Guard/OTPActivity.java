package com.example.homesecurity.Activity.Guard;

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

import com.example.homesecurity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPActivity extends AppCompatActivity {
    private Button btnVerify;
    private EditText edOTP;

    String verification,OTP;

    SharedPreferences sharedPreferences;
    String GuestDetails = "guestDetails";
    String SP_VERIFICATION = "verification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        initialize();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                OTP = edOTP.getText().toString().trim();
                Toast.makeText(OTPActivity.this, "" + verification, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(OTPActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OTPActivity.this,PhoneGuardActivity.class));
                    btnVerify.setEnabled(false);
                    finish();
                } else {
                    Toast.makeText(OTPActivity.this, "Failed Authentication", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData() {
        sharedPreferences = getSharedPreferences(GuestDetails, Context.MODE_PRIVATE);
        verification = sharedPreferences.getString(SP_VERIFICATION,"");
    }

    private void initialize() {
        btnVerify = findViewById(R.id.Btn_OTPActivity_Verify);
        edOTP = findViewById(R.id.Ed_OTPActivity_OTP);
    }
}