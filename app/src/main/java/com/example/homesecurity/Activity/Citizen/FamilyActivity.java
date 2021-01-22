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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homesecurity.Adapter.AdapterAll;
import com.example.homesecurity.Adapter.AdapterFamilyMembers;
import com.example.homesecurity.Model.ModelFamily;
import com.example.homesecurity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FamilyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnNewFamilyMember;
    private TextView tvFlatID;

    LinearLayoutManager linearLayoutManager;

    private String flat, name, phone, key,admin;

    DatabaseReference mUserDatabaseCitizen;
    private DatabaseReference mUserDatabaseNumber;

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";
    String SP_Edit = "edit";
    String SP_Password = "password";
    String SP_Phone = "phone";
    String SP_Name = "name";
    String SP_Key = "key";
    String SP_FLAT = "flat";
    String SP_Admin = "admin";

    FirebaseRecyclerOptions<ModelFamily> option;
    FirebaseRecyclerAdapter<ModelFamily, AdapterFamilyMembers> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        initialize();

        getData();

        if(admin.equals("")){
            btnNewFamilyMember.setEnabled(false);
        }

        tvFlatID.setText("FLAT :- " + flat);

        btnNewFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPrefrenceNew();
                startActivity(new Intent(FamilyActivity.this, NewFamilyActivity.class));
            }
        });

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen");
        mUserDatabaseNumber = FirebaseDatabase.getInstance().getReference("Master");

        Toast.makeText(this, "flat :-" + flat, Toast.LENGTH_SHORT).show();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        load();

        setSharedPrefrenceBoolean();

    }

    private void load() {
        option = new FirebaseRecyclerOptions.Builder<ModelFamily>().setQuery(mUserDatabaseCitizen.child(flat).child("family"), ModelFamily.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelFamily, AdapterFamilyMembers>(option) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterFamilyMembers adapter, int i, @NonNull final ModelFamily model) {
                adapter.tvName.setText(model.getName());
                adapter.tvPhone.setText(model.getPhone());

                if(admin.equals("")){
                    adapter.btnEdit.setEnabled(false);
                    adapter.btnRemove.setEnabled(false);
                    adapter.tvAdmin.setEnabled(false);
                }

                if(model.getAdmin().equals("Admin")){
                    adapter.tvAdmin.setText("Change to normal member?");
                }

                adapter.tvAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.getAdmin().equals("Admin")){
                            //make normal member
                            mUserDatabaseCitizen.child(flat).child("family").child(model.getPhone()).child("admin").setValue("");
                            mUserDatabaseNumber.child(model.getPhone()).child("admin").setValue("");
                        }
                        else{
                            mUserDatabaseNumber.child(model.getPhone()).child("admin").setValue("Admin");
                            mUserDatabaseCitizen.child(flat).child("family").child(model.getPhone()).child("admin").setValue("Admin");
                        }
                    }
                });

                //Button setOnClickListener
                adapter.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSharedPrefrenceEdit(model.getName(), model.getPhone(), model.getKey(),model.getPassword());
                        startActivity(new Intent(FamilyActivity.this, NewFamilyActivity.class));
                        Toast.makeText(FamilyActivity.this, "+" + model.getPhone(), Toast.LENGTH_SHORT).show();
                    }
                });

                adapter.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //remove from family
                        mUserDatabaseCitizen.child(flat).child("family").child(model.getPhone()).removeValue();

                        //remove from master
                        mUserDatabaseNumber.child(model.getPhone()).removeValue();
                    }
                });
            }

            @NonNull
            @Override
            public AdapterFamilyMembers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_resource_family_member, parent, false);
                return new AdapterFamilyMembers(v);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void setSharedPrefrenceEdit(String SPName, String SPPhone, String SPKey,String password) {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SP_Password,password);
        editor.putBoolean(SP_Edit, true);
        editor.putString(SP_Name, SPName);
        editor.putString(SP_Phone, SPPhone);
        editor.putString(SP_Key, SPKey);

        editor.commit();
    }

    private void setSharedPrefrenceNew() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SP_Password,"");
        editor.putBoolean(SP_Edit, false);
        editor.putString(SP_Name, "");
        editor.putString(SP_Phone, "");
        editor.putString(SP_Key, "");

        editor.commit();
    }

    private void setSharedPrefrenceBoolean() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SP_Edit, false);

        editor.commit();
    }


    private void initialize() {
        recyclerView = findViewById(R.id.Rv_FamilyActivivty);
        tvFlatID = findViewById(R.id.Tv_FamilyActivity_FlatID);
        btnNewFamilyMember = findViewById(R.id.Btn_FamilyActivity_NewFamily);
    }

    private void getData() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        flat = sharedPreferences.getString(SP_FLAT, "");
        admin = sharedPreferences.getString(SP_Admin,"");
    }
}