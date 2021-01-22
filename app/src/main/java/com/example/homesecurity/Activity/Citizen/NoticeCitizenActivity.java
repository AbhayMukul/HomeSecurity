package com.example.homesecurity.Activity.Citizen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homesecurity.Activity.Common.NoticeActivity;
import com.example.homesecurity.Adapter.AdapterNotice;
import com.example.homesecurity.Model.ModelNotice;
import com.example.homesecurity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoticeCitizenActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    DatabaseReference mUserDatabaseAdmin;
    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerOptions<ModelNotice> option;
    FirebaseRecyclerAdapter<ModelNotice, AdapterNotice> firebaseRecyclerAdapter;

    SharedPreferences sharedPreferences;
    String NoticeDetails = "noticeDetails";
    String SP_NOTICE ="notice";
    String SP_STATUS = "noticestatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_citizen);

        initialize();

        mUserDatabaseAdmin = FirebaseDatabase.getInstance().getReference("Admin");

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        load();
    }

    private void initialize() {
        recyclerView = findViewById(R.id.recyclerView_CitizenNotice);
    }

    private void load() {
        option = new FirebaseRecyclerOptions.Builder<ModelNotice>().setQuery(mUserDatabaseAdmin.child("post"), ModelNotice.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelNotice, AdapterNotice>(option) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterNotice adapter, int i, @NonNull final ModelNotice model) {
                adapter.tvNotice.setText(model.getNotice());
                adapter.tvNoticeDate.setText(model.getDate());
                adapter.tvNoticeTime.setText(model.getTime());

                adapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //put notice in sharedPrefrence
                        sharedPreferences = getSharedPreferences(NoticeDetails, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SP_NOTICE,model.getNotice());
                        editor.putBoolean(SP_STATUS,true);
                        editor.commit();

                        //start NoticeActivity
                        startActivity(new Intent(NoticeCitizenActivity.this, NoticeActivity.class));
                    }
                });
            }

            @NonNull
            @Override
            public AdapterNotice onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_resource_notice, parent, false);
                return new AdapterNotice(v);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}