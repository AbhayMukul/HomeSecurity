package com.example.homesecurity.Activity.Guard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homesecurity.Activity.Common.SettingCitizenActivity;
import com.example.homesecurity.Adapter.AdapterActive;
import com.example.homesecurity.Model.ModelGuestActive;
import com.example.homesecurity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button btnUploadGuest, btnAllGuestGuard, btnSettingGuard, btnSearchName;
    private RecyclerView recyclerView;
    private EditText edName;

    LinearLayoutManager linearLayoutManager;

    SharedPreferences sharedPreferences;
    String LoginDetails = "loginDetails";
    String SP_GUARDCODE = "guardcode";
    String code;

    DatabaseReference mUserDatabaseGuest, mUserDatabaseCitizen;

    private String d, name;
    private Boolean status;

    FirebaseRecyclerOptions<ModelGuestActive> option;
    FirebaseRecyclerAdapter<ModelGuestActive, AdapterActive> firebaseRecyclerAdapter;
    String time;

    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        getGuardID();

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen");

        Date date = new Date();
        d = (String) android.text.format.DateFormat.format("dd/MM/yyyy", date);
        time = (String) android.text.format.DateFormat.format("kk:mm:ss", date);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //show views
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("guest").child("Active"), ModelGuestActive.class).build();
        load();
        //end

        btnUploadGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUploadGuest();
            }
        });

        btnAllGuestGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GuardAllActivity.class));
            }
        });

        btnSettingGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingCitizenActivity.class));
            }
        });

        btnSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edName.getText().toString().trim();
                if (name.equals("All")) {
                    option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("guest").child("Active"), ModelGuestActive.class).build();
                    load();
                } else {
                    option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("guest").child("Active").orderByChild("name").startAt(name).endAt(name + "/uf8ff"), ModelGuestActive.class).build();
                    load();
                }
            }
        });

    }

    private void load() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelGuestActive, AdapterActive>(option) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull final AdapterActive adapter, int i, @NonNull final ModelGuestActive model) {

                adapter.UID_GuardActive.setText(model.getKeyUID());
                adapter.NameGuest_GuardActive.setText(model.getName());
                adapter.Datein_GuardActive.setText("Entry :-" + model.getDateInGuard());

                status = model.getExitCitizen();

                if (status.equals(false)) {
                    adapter.btnExit_GuardActive.setEnabled(false);
                }

                adapter.btnExit_GuardActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mUserDatabaseGuest = FirebaseDatabase.getInstance().getReference("guest");
                        //set exit by guard allowed
                        mUserDatabaseGuest.child("All").child(model.getKeyUID()).child("exitGuard").setValue(true);
                        //set date exited
                        mUserDatabaseGuest.child("All").child(model.getKeyUID()).child("dateOutGuard").setValue(d);
                        //set Time of exit
                        mUserDatabaseGuest.child("All").child(model.getKeyUID()).child("timeOutGuard").setValue(time);
                        //set guard ID for exit
                        mUserDatabaseGuest.child("All").child(model.getKeyUID()).child("guardOut").setValue(code);

                        //remove data entry from active
                        mUserDatabaseGuest.child("Active").child(model.getKeyUID()).removeValue();
                    }
                });

                adapter.btnCall_GuardActive.setText("CALL FLAT " + model.getFlat());

                adapter.btnCall_GuardActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mUserDatabaseCitizen.child(model.getFlat()).child("info").child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String CitizenPhone = (String) snapshot.getValue();
                                if (CitizenPhone.trim().length() > 0) {
                                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                                    } else {
                                        String dial = "tel:" + CitizenPhone;
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });

            }

            @Override
            public AdapterActive onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_resource_guest_guard, parent, false);
                return new AdapterActive(v);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission ACCEPTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialize() {
        btnUploadGuest = findViewById(R.id.Btn_UploadGuest);
        btnAllGuestGuard = findViewById(R.id.Btn_AllGuestGuard);
        btnSettingGuard = findViewById(R.id.Btn_SettingGuard);
        btnSearchName = findViewById(R.id.Btn_GuardMain_Search);

        edName = findViewById(R.id.Ed_GuardMain_SearchName);

        recyclerView = findViewById(R.id.recyclerView_GuestGuard);
    }

    public void openUploadGuest() {
        startActivity(new Intent(MainActivity.this, UploadGuestActivity.class));
    }

    private void getGuardID() {
        sharedPreferences = getSharedPreferences(LoginDetails, Context.MODE_PRIVATE);

        code = sharedPreferences.getString(SP_GUARDCODE,"");
    }
}