package com.example.homesecurity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesecurity.Model.ModelCitizenActive;
import com.example.homesecurity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PostAdapter extends FirebaseRecyclerAdapter<ModelCitizenActive, PostAdapter.PastViewHolder> {


    public PostAdapter(@NonNull FirebaseRecyclerOptions<ModelCitizenActive> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull PastViewHolder holder, final int i, @NonNull ModelCitizenActive post) {
        holder.name.setText(post.getName());

    }

    @NonNull
    @Override
    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_resource_guest_citizen_active, parent, false);
        return new PastViewHolder(view);
    }

    public class PastViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone,type;

        public PastViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.TvLr_GuestNameCitizenActive);
//            phone = itemView.findViewById(R.id.Tv_StudetnEmail);
//            type = itemView.findViewById(R.id.Tv_StudentType);
        }
    }
}
