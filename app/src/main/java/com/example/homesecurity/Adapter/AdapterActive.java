package com.example.homesecurity.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesecurity.R;

public class AdapterActive extends RecyclerView.ViewHolder {

    //GUARD ACTIVE (guest_guard)
    public TextView UID_GuardActive, NameGuest_GuardActive, Datein_GuardActive;
    public Button btnExit_GuardActive, btnCall_GuardActive;

    public AdapterActive(@NonNull View itemView) {
        super(itemView);

        UID_GuardActive = itemView.findViewById(R.id.TvLr_GuestUidGuardActive);
        NameGuest_GuardActive = itemView.findViewById(R.id.TvLr_GuestNameGuardActive);
        Datein_GuardActive = itemView.findViewById(R.id.TvLr_GuestDateEnterGuardActive);

        btnCall_GuardActive = itemView.findViewById(R.id.BtnLr_CitizenNumberActive);
        btnExit_GuardActive = itemView.findViewById(R.id.BtnLr_GuestOutGuardActive);

    }
}
