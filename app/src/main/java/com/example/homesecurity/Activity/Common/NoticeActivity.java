package com.example.homesecurity.Activity.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homesecurity.Activity.Admin.AdminMainActivity;
import com.example.homesecurity.Activity.Admin.AdminNoticeActivity;
import com.example.homesecurity.Model.ModelNotice;
import com.example.homesecurity.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class NoticeActivity extends AppCompatActivity {
    private EditText edNotice;
    private Button btnDelete, btnSave;

    DatabaseReference mUserDatabaseAdmin;

    String d, time;

    SharedPreferences sharedPreferences;
    String NoticeDetails = "noticeDetails";
    String SP_NOTICE = "notice";
    String SP_STATUS = "noticestatus";
    String SP_UID = "uid";

    private String notice, key;
    private Boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        initialize();

        mUserDatabaseAdmin = FirebaseDatabase.getInstance().getReference("Admin");

        getNotice();

        setData();

        Date date = new Date();
        d = (String) android.text.format.DateFormat.format("dd/MM/yyyy", date);
        time = (String) android.text.format.DateFormat.format("hh:mm:ss", date);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserDatabaseAdmin.child("post").child(key).removeValue();
                Toast.makeText(NoticeActivity.this, "deleted", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice = edNotice.getText().toString().trim();
                if(!notice.isEmpty()){
                    ModelNotice modelNotice = new ModelNotice(key, notice, d, time);
                    mUserDatabaseAdmin.child("post").child(key).setValue(modelNotice);

                    //Toast
                    Toast.makeText(NoticeActivity.this, "done", Toast.LENGTH_SHORT).show();

                    //finishDelete
                    finish();
                    startActivity(new Intent(NoticeActivity.this, AdminMainActivity.class));
                }
                else
                    Toast.makeText(NoticeActivity.this, "please enter the notice", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        edNotice.setText(notice);

        if (status.equals(true)) {
            //from citizen
            edNotice.setEnabled(false);
            btnSave.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
        } else {
            //from admin
            edNotice.setEnabled(true);
            key = sharedPreferences.getString(SP_UID, "");
        }
    }

    private void getNotice() {
        notice = sharedPreferences.getString(SP_NOTICE, "");
        status = sharedPreferences.getBoolean(SP_STATUS, true);
    }

    private void initialize() {
        sharedPreferences = getSharedPreferences(NoticeDetails, Context.MODE_PRIVATE);

        edNotice = findViewById(R.id.Ed_NoticeCommon);

        btnDelete = findViewById(R.id.Btn_NoticeCommon_Delete);
        btnSave = findViewById(R.id.Btn_NoticeCommon_Save);
    }
}