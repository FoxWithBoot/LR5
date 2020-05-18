package com.example.testretrofit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyHistoryViewHolder> {
    private ArrayList<String> urls;

    public MyHistoryAdapter(ArrayList<String> urls){
        this.urls=urls;
    }

    public class MyHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView img2;
        public MyHistoryViewHolder(View view) {
            super(view);
            img2 = view.findViewById(R.id.imageView2);
        }
    }

    @NonNull
    @Override
    public MyHistoryAdapter.MyHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_history_row, parent, false);
        return new MyHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHistoryViewHolder holder, int position) {
        Picasso.get().load(urls.get(position)).into(holder.img2);
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }
}
