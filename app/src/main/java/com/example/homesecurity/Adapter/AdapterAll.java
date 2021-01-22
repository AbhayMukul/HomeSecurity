package com.example.homesecurity.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesecurity.R;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.jar.Attributes;

public class AdapterAll extends RecyclerView.ViewHolder {
    //All
    public TextView UidAll,NameAll,DateInAllGuard;

    public AdapterAll(@NonNull View itemView) {
        super(itemView);

        UidAll = itemView.findViewById(R.id.TvLr_GuestUidAll);
        NameAll = itemView.findViewById(R.id.TvLr_GuestNameAll);
        DateInAllGuard = itemView.findViewById(R.id.TvLr_GuestDateEnterAllGuard);
    }

}
