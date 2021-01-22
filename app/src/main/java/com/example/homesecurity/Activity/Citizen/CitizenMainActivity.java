package com.example.homesecurity.Activity.Citizen;

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
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homesecurity.Activity.Common.SettingCitizenActivity;
import com.example.homesecurity.Activity.Guard.MainActivity;
import com.example.homesecurity.Adapter.AdapterCitizenActive;
import com.example.homesecurity.Model.ModelGuestActive;
import com.example.homesecurity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CitizenMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    Button btnAllGuestCitizen, btnNotice, btnSetting, btnSearch,btnFamily;
    EditText edNameSearch;

    String d, flat, time, nameSearch;

    DatabaseReference mUserDatabaseCitizen;
    DatabaseReference mUserDatabaseGuest;

    FirebaseRecyclerOptions<ModelGuestActive> option;
    FirebaseRecyclerAdapter<ModelGuestActive, AdapterCitizenActive> firebaseRecyclerAdapter;

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";

    String SP_FLAT = "flat";

    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_main);

        initialize();

        getData();

        mUserDatabaseGuest = FirebaseDatabase.getInstance().getReference("guest");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("citizen").child(flat).child("Guest").child("Active"), ModelGuestActive.class).build();

        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference("citizen").child(flat).child("Guest").child("Active");

        Date date = new Date();
        d = (String) android.text.format.DateFormat.format("dd/MM/yyyy", date);
        time = (String) android.text.format.DateFormat.format("kk:mm:ss", date);

        btnAllGuestCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CitizenMainActivity.this, CitizenAllGuestActivity.class));
            }
        });

        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CitizenMainActivity.this, NoticeCitizenActivity.class));
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CitizenMainActivity.this, SettingCitizenActivity.class));
            }
        });

        load();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameSearch = edNameSearch.getText().toString().trim();
                if (nameSearch.equals("All")) {
                    option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("citizen").child(flat).child("Guest").child("Active"), ModelGuestActive.class).build();

                } else
                    option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("citizen").child(flat).child("Guest").child("Active").orderByChild("name").startAt(nameSearch).endAt(nameSearch + "/uf8ff"), ModelGuestActive.class).build();
                load();
            }
        });

        btnFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CitizenMainActivity.this,FamilyActivity.class));
            }
        });
    }

    private void load() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelGuestActive, AdapterCitizenActive>(option) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull AdapterCitizenActive adapter, int i, @NonNull final ModelGuestActive model) {
                adapter.NameGuest_Citizenctive.setText(model.getName());
                adapter.UID_Citizenctive.setText(model.getKeyUID());
                adapter.Description_CitizenActive.setText(model.getWork());
                adapter.Datein_CitizenActive.setText(model.getDateInGuard());

                adapter.btnCall_CitizenActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String CitizenPhone = model.getPhone();
                        if (CitizenPhone.trim().length() > 0) {
                            if (ContextCompat.checkSelfPermission(CitizenMainActivity.this,
                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(CitizenMainActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                            } else {
                                String dial = "tel:" + CitizenPhone;
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                            }
                        } else {
                            Toast.makeText(CitizenMainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                adapter.btnExit_CitizenActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //change DateoutCitizen in Citizen All and Guest All
                        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference().child("citizen").child(flat).child("Guest").child("All").child(model.getKeyUID()).child("dateOutCitizen");
                        mUserDatabaseCitizen.setValue(d);
                        mUserDatabaseGuest.child("All").child(model.getKeyUID()).child("dateOutCitizen").setValue(d);

                        //change Timeout in Citizen All and Guest All
                        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference().child("citizen").child(flat).child("Guest").child("All").child(model.getKeyUID()).child("timeOutCitizen");
                        mUserDatabaseCitizen.setValue(time);
                        mUserDatabaseGuest.child("All").child(model.getKeyUID()).child("timeOutCitizen").setValue(time);

                        //change boolleen to allow guest out to true
                        mUserDatabaseGuest.child("All").child(model.getKeyUID()).child("exitCitizen").setValue(true);
                        mUserDatabaseGuest.child("Active").child(model.getKeyUID()).child("exitCitizen").setValue(true);

                        //remove View
                        mUserDatabaseCitizen = FirebaseDatabase.getInstance().getReference().child("citizen").child(flat).child("Guest").child("Active").child(model.getKeyUID());
                        mUserDatabaseCitizen.removeValue();
                    }
                });

                adapter.btnCall_CitizenActive.setText(model.getPhone());
            }

            @Override
            public AdapterCitizenActive onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_resource_guest_citizen_active, parent, false);
                return new AdapterCitizenActive(v);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void getData() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        flat = sharedPreferences.getString(SP_FLAT, "");
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
        recyclerView = findViewById(R.id.recyclerView_GuestCitizen);
        btnFamily = findViewById(R.id.Btn_FamilyCitizen);
        btnAllGuestCitizen = findViewById(R.id.Btn_AllGuestCitizen);
        btnNotice = findViewById(R.id.Btn_NoticeCitizen);
        btnSetting = findViewById(R.id.Btn_SettingsCitizen);
        btnSearch = findViewById(R.id.Btn_CitizenMain_Search);
        edNameSearch = findViewById(R.id.Ed_CitizenMain_SearchName);
    }
}