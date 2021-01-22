package com.example.homesecurity.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesecurity.R;

public class AdapterNotice extends RecyclerView.ViewHolder {
    public TextView tvNotice,tvNoticeTime,tvNoticeDate;

    public AdapterNotice(@NonNull View itemView) {
        super(itemView);
        tvNotice = itemView.findViewById(R.id.Tv_Notice);
        tvNoticeDate = itemView.findViewById(R.id.Tv_NoticeDate);
        tvNoticeTime = itemView.findViewById(R.id.Tv_NoticeTime);
    }
}
