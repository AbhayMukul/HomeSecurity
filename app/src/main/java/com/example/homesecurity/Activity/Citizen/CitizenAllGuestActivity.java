package com.example.homesecurity.Activity.Citizen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.homesecurity.Activity.Common.GuestDescriptionActivity;
import com.example.homesecurity.Activity.Guard.GuardAllActivity;
import com.example.homesecurity.Adapter.AdapterAll;
import com.example.homesecurity.Adapter.AdapterCitizenActive;
import com.example.homesecurity.Model.ModelGuestActive;
import com.example.homesecurity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class CitizenAllGuestActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    RecyclerView recyclerView;
    Button btnCalenderDate;

    FirebaseRecyclerOptions<ModelGuestActive> option;
    FirebaseRecyclerAdapter<ModelGuestActive, AdapterAll> firebaseRecyclerAdapter;

    SharedPreferences sharedPreferences;
    String CitizenDetails = "citizenDetails";

    String SP_FLAT = "flat";
    String flat;

    String dateSearch;

    String GuestDetails = "guestDetails";

    String SP_KEY = "key";
    String SP_NAME = "name";
    String SP_PHONE = "phone";
    String SP_FLAT_GUEST = "flat";
    String SP_WORK = "work";
    String SP_DateOutCitizen = "dateOutCitizen";
    String SP_TimeOutCitizen = "timeOutCitizen";
    String SP_DateInGuard = "dateInGuard";
    String SP_TimeInGuard = "timeInGuard";
    String SP_TimeOutGuard = "timeOutGuard";
    String SP_DateOutGuard = "dateOutGuard";
    String SP_GuardIn = "guardIn";
    String SP_GuardOut = "guardOut";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_all_guest);

        initialize();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getData();

        Toast.makeText(this, "flat :-" + flat, Toast.LENGTH_SHORT).show();

        btnCalenderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepickerDialogue();
            }
        });

        option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("citizen").child(flat).child("Guest").child("All"), ModelGuestActive.class).build();
        load();
    }

    private void showDatepickerDialogue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void load() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelGuestActive, AdapterAll>(option) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull AdapterAll adapter, int i, @NonNull final ModelGuestActive model) {
                adapter.UidAll.setText(model.getKeyUID());
                adapter.NameAll.setText(model.getName());

                adapter.DateInAllGuard.setText("enter :-" + model.getDateInGuard());

                adapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharedPreferences = getSharedPreferences(GuestDetails, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(SP_NAME, model.getName());
                        editor.putString(SP_KEY, model.getKeyUID());
                        editor.putString(SP_WORK, model.getWork());
                        editor.putString(SP_PHONE, model.getPhone());
                        editor.putString(SP_FLAT_GUEST, model.getFlat());
                        editor.putString(SP_DateInGuard, model.getDateInGuard());

                        if (model.getDateOutGuard().equals("")) {
                            editor.putString(SP_DateOutGuard, "not out of society");
                        } else
                            editor.putString(SP_DateOutGuard, model.getDateOutGuard());

                        if (model.getDateOutCitizen().equals("")) {
                            editor.putString(SP_DateOutCitizen, "not out of flat");
                        } else
                            editor.putString(SP_DateOutCitizen, model.getDateOutCitizen());

                        editor.putString(SP_GuardIn, model.getGuardIn());

                        if (model.getGuardOut().equals(null) || model.getGuardOut().equals("")) {
                            editor.putString(SP_GuardOut, "not exited");
                        } else
                            editor.putString(SP_GuardOut, model.getGuardOut());

                        editor.commit();

                        startActivity(new Intent(CitizenAllGuestActivity.this, GuestDescriptionActivity.class));
                    }
                });
            }

            @Override
            public AdapterAll onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_resource_guest_guard_all, parent, false);
                return new AdapterAll(v);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void initialize() {
        recyclerView = findViewById(R.id.recyclerView_GuestCitizenAll);
        btnCalenderDate = findViewById(R.id.Btn_CitizenAllGuest_SearchDate);
    }

    private void getData() {
        sharedPreferences = getSharedPreferences(CitizenDetails, Context.MODE_PRIVATE);
        flat = sharedPreferences.getString(SP_FLAT, "");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

        //month starts from 0;
        month = month + 1;
        if (date < 10) {
            dateSearch = "0" + date + "/";
        } else
            dateSearch = date + "/";

        if (month < 10) {
            dateSearch = dateSearch + "0" + month + "/";
        } else
            dateSearch = dateSearch + month + "/";

        dateSearch = dateSearch + year;

        option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("citizen").child(flat).child("Guest").child("All").orderByChild("dateInGuard").startAt(dateSearch).endAt(dateSearch + "/uf8ff"), ModelGuestActive.class).build();
        load();

    }
}