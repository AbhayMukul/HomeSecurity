package com.example.homesecurity.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homesecurity.Activity.Guard.MainActivity;
import com.example.homesecurity.Model.ModelGuard;
import com.example.homesecurity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminGuardActivity extends AppCompatActivity {
    Button btnCheck,btnCommit,btnGuardMain;
    EditText edID,edName,edPhone,edPassword;

    String ID,password,name,phone;

    DatabaseReference mUserDatabaseGuard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_guard);

        initialize();

        mUserDatabaseGuard = FirebaseDatabase.getInstance().getReference("Guard");

        btnCommit.setEnabled(false);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGuardID();
                mUserDatabaseGuard.child(ID).child("info").child("id").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ID = (String) snapshot.getValue();
                        if(ID == null){
                            //setData();
                            Toast.makeText(AdminGuardActivity.this, "new Guard with ID " + ID + " to be created", Toast.LENGTH_LONG).show();
                        }
                        else {
                           setData();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if(validate()){
                    ModelGuard modelGuard = new ModelGuard(ID,name,password,phone);
                    mUserDatabaseGuard.child(ID).child("info").setValue(modelGuard);
                    Toast.makeText(AdminGuardActivity.this, "done", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGuardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminGuardActivity.this, MainActivity.class));
            }
        });

    }

    private void setData() {
        //password
        mUserDatabaseGuard.child(ID).child("info").child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                password = (String) snapshot.getValue();
                edPassword.setText(password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //name
        mUserDatabaseGuard.child(ID).child("info").child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = (String) snapshot.getValue();
                edName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //phone
        mUserDatabaseGuard.child(ID).child("info").child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phone= (String) snapshot.getValue();
                edPhone.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getData() {
        name = edName.getText().toString().trim();
        password = edPassword.getText().toString().trim();
        phone = edPhone.getText().toString().trim();
    }

    private void getGuardID() {
        ID = edID.getText().toString().trim();
    }

    private void initialize() {
        btnCheck = findViewById(R.id.Btn_AdminGuardActivity_check);
        btnCommit = findViewById(R.id.Btn_AdminGuardActivity_commit);
        btnGuardMain = findViewById(R.id.Btn_AdminGuardActivity_GuardMain);

        edID = findViewById(R.id.Ed_AdminGuardActivity_ID);
        edName = findViewById(R.id.Ed_AdminGuardActivity_Name);
        edPassword = findViewById(R.id.Ed_AdminGuardActivity_Password);
        edPhone = findViewById(R.id.Ed_AdminGuardActivity_Phone);
    }

    private Boolean validate() {
        Integer check = phone.length();
        if (phone.equals("") || name.equals("") || password.equals("")) {
            Toast.makeText(this, "please check all the fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!check.equals(10)){
            Toast.makeText(this, "please check the phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}