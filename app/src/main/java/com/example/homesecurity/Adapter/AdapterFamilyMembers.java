package com.example.homesecurity.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesecurity.R;

public class AdapterFamilyMembers extends RecyclerView.ViewHolder {
    public TextView tvName;
    public TextView tvPhone;
    public TextView tvAdmin;

    public Button btnEdit;
    public Button btnRemove;
    public AdapterFamilyMembers(@NonNull View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.TvLr_LayoutResourceFamilyMember_Name);
        tvPhone = itemView.findViewById(R.id.TvLr_LayoutResourceFamilyMember_Phone);
        tvAdmin = itemView.findViewById(R.id.TvLr_LayoutResourceFamilyMember_Admin);

        btnEdit = itemView.findViewById(R.id.BtnLr_LayoutResourceFamilyMember_Edit);
        btnRemove = itemView.findViewById(R.id.BtnLr_LayoutResourceFamilyMember_Remove);
    }
}
