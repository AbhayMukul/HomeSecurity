package com.example.homesecurity.Activity.Guard;

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

import com.example.homesecurity.Activity.Common.GuestDescriptionActivity;
import com.example.homesecurity.Adapter.AdapterActive;
import com.example.homesecurity.Adapter.AdapterAll;
import com.example.homesecurity.Model.ModelGuestActive;
import com.example.homesecurity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class GuardAllActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button btnCalender;

    RecyclerView recyclerView;

    DatabaseReference mUserDatabaseGuest;

    FirebaseRecyclerOptions<ModelGuestActive> option;
    FirebaseRecyclerAdapter<ModelGuestActive, AdapterAll> firebaseRecyclerAdapter;

    String dateSearch;

    SharedPreferences sharedPreferences;
    String GuestDetails = "guestDetails";

    String SP_KEY = "key";
    String SP_NAME = "name";
    String SP_PHONE = "phone";
    String SP_FLAT = "flat";
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
        setContentView(R.layout.activity_guard_all);

        initialize();

        mUserDatabaseGuest = FirebaseDatabase.getInstance().getReference("guest").child("Active");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //show views
        option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("guest").child("All"), ModelGuestActive.class).build();
        load();
        //end

        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepickerDialogue();
            }
        });
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

                        editor.putString(SP_NAME,model.getName());
                        editor.putString(SP_KEY,model.getKeyUID());
                        editor.putString(SP_WORK,model.getWork());
                        editor.putString(SP_PHONE,model.getPhone());
                        editor.putString(SP_FLAT,model.getFlat());
                        editor.putString(SP_DateInGuard,model.getDateInGuard());

                        if (model.getDateOutGuard().equals("")) {
                            editor.putString(SP_DateOutGuard, "not out of society");
                        } else
                            editor.putString(SP_DateOutGuard, model.getDateOutGuard());

                        if (model.getDateOutCitizen().equals("")) {
                            editor.putString(SP_DateOutCitizen, "not out of flat");
                        } else
                            editor.putString(SP_DateOutCitizen, model.getDateOutCitizen());

                        if (model.getGuardIn().equals(null)) {
                            editor.putString(SP_GuardIn, "");
                        } else
                            editor.putString(SP_GuardIn, model.getGuardIn());

                        if (model.getGuardOut().equals(null) || model.getGuardOut().equals("")) {
                            editor.putString(SP_GuardOut, "not exited");
                        } else
                            editor.putString(SP_GuardOut, model.getGuardOut());

                        editor.commit();

                        startActivity(new Intent(GuardAllActivity.this, GuestDescriptionActivity.class));
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
        recyclerView = findViewById(R.id.recyclerView_GuestGuardAll);
        btnCalender = findViewById(R.id.Btn_GuardAllGuest_SearchDate);
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

        option = new FirebaseRecyclerOptions.Builder<ModelGuestActive>().setQuery(FirebaseDatabase.getInstance().getReference().child("guest").child("All").orderByChild("dateInGuard").startAt(dateSearch).endAt(dateSearch + "/uf8ff"), ModelGuestActive.class).build();
        load();
    }
}