package com.example.homesecurity.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesecurity.R;

public class AdapterCitizenActive extends RecyclerView.ViewHolder{

    //GUARD ACTIVE (guest_guard)
    public TextView UID_Citizenctive, NameGuest_Citizenctive, Datein_CitizenActive,Description_CitizenActive;
    public Button btnExit_CitizenActive, btnCall_CitizenActive;

    public AdapterCitizenActive(@NonNull View itemView) {
        super(itemView);

        UID_Citizenctive = itemView.findViewById(R.id.TvLr_GuestUidCitizemActive);
        NameGuest_Citizenctive = itemView.findViewById(R.id.TvLr_GuestNameCitizenActive);
        Datein_CitizenActive = itemView.findViewById(R.id.TvLr_GuestDateEnterCitizenActive);
        Description_CitizenActive = itemView.findViewById(R.id.TvLr_GuestDescriptionCitizenActive);

        btnCall_CitizenActive = itemView.findViewById(R.id.BtnLr_GuestNumberActive);
        btnExit_CitizenActive = itemView.findViewById(R.id.BtnLr_GuestOutGuardActiveExit);

    }
}
