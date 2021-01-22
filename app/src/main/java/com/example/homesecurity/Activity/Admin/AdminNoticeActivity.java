package com.example.homesecurity.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homesecurity.Model.ModelNotice;
import com.example.homesecurity.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AdminNoticeActivity extends AppCompatActivity {
    EditText edNotice;
    String Notice;
    Button btnPost;

    String d, time, key;
    Boolean noticeStatus;

    DatabaseReference mUserDatabaseAdmin;

    SharedPreferences sharedPreferences;
    String NoticeDetails = "noticeDetails";
    String SP_NOTICE = "notice";
    String SP_NOTICE_STATUS = "noticestatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice);

        initialize();

        mUserDatabaseAdmin = FirebaseDatabase.getInstance().getReference("Admin");

        Date date = new Date();
        d = (String) android.text.format.DateFormat.format("dd/MM/yyyy", date);
        time = (String) android.text.format.DateFormat.format("hh:mm:ss", date);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    getData();
                    key = mUserDatabaseAdmin.push().getKey();
                    ModelNotice modelNotice = new ModelNotice(key, Notice, d, time);
                    //upload
                    mUserDatabaseAdmin.child("post").child(key).setValue(modelNotice);

                    //Toast
                    Toast.makeText(AdminNoticeActivity.this, "done", Toast.LENGTH_SHORT).show();

                    //delete data in SharedPrefrence
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SP_NOTICE_STATUS, false);
                    editor.commit();

                    //go back
                    finish();
                    startActivity(new Intent(AdminNoticeActivity.this, AdminMainActivity.class));
                } else
                    Toast.makeText(AdminNoticeActivity.this, "please fill everything", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        if(!Notice.equals("")) {
            return true;
        }
        return false;
    }

    private void getData() {
        Notice = edNotice.getText().toString().trim();
    }

    private void initialize() {
        sharedPreferences = getSharedPreferences(NoticeDetails, Context.MODE_PRIVATE);
        btnPost = findViewById(R.id.Btn_AdminNoticeActivity_Post);
        edNotice = findViewById(R.id.Ed_NoticeFormAdmin);
    }
}