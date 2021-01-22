package com.example.homesecurity.Activity.Login.Guard;

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

import com.example.homesecurity.Activity.Guard.MainActivity;
import com.example.homesecurity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GuardLoginActivity extends AppCompatActivity {
    EditText editTextID,editTextPassword;
    Button button;
    public String code,password;

    DatabaseReference mUserDatabaseGuard;

    SharedPreferences sharedPreferences;

    String LoginDetails = "loginDetails";
    String accountType = "accountType";
    String LoggedIN = "loggin";

    String SP_GUARDCODE = "guardcode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_login);

        initialize();

        mUserDatabaseGuard = FirebaseDatabase.getInstance().getReference("Guard");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if(validate()){
                    mUserDatabaseGuard.child(code).child("info").child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String checkPassword = (String) snapshot.getValue();
                            if(password.equals(checkPassword)){
                                //store Login Details
                                setLoginDetails();

                                button.setEnabled(false);

                                //open Main
                                openGuardMain();
                            }
                            else {
                                Toast.makeText(GuardLoginActivity.this, "WRONG PASSWORD ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private boolean validate() {
        if(code.equals("") || password.equals("")){
            Toast.makeText(this, "please fill all the details", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setLoginDetails() {
            sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(accountType,"Guard");
            editor.putBoolean(LoggedIN,true);
            editor.putString(SP_GUARDCODE,code);

            editor.commit();
    }

    private void openGuardMain() {
        startActivity(new Intent(GuardLoginActivity.this,MainActivity.class));
    }

    private void getData() {
        code = editTextID.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
    }

    private void initialize() {
        editTextID = findViewById(R.id.Ed_CodeGuard);
        editTextPassword = findViewById(R.id.Ed_PasswordGuard);
        button = findViewById(R.id.Btn_GuardNext);
    }
}