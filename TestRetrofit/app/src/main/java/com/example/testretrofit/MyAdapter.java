package com.example.testretrofit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    List<UrlImg> urls;
    int realPossition;
    Context context;

    private static final String SUB_ID = "User-1234";

    public MyAdapter(List<UrlImg> urls){
        this.urls=urls;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button like;
        Button dislike;
        LinearLayout l;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            l = view.findViewById(R.id.layout);
            like = view.findViewById(R.id.button2);
            dislike = view.findViewById(R.id.button3);
            img = view.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        realPossition=holder.getAdapterPosition()%urls.size();
        Picasso.get().load(urls.get(realPossition).getUrl()).fit().centerCrop().into(holder.img);
        View.OnClickListener bLike = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUrlInTable(urls.get(realPossition).getUrl());
                setGolos(1);
            }
        };
        holder.like.setOnClickListener(bLike);

        View.OnClickListener bDislike = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGolos(0);
            }
        };
        holder.dislike.setOnClickListener(bDislike);

    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    private void setGolos(int v){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi serverApi = retrofit.create(ServerApi.class);
        Call<Golos> golos = serverApi.setGolos(new Golos(urls.get(realPossition).getId(), SUB_ID, v));
        golos.enqueue(new Callback<Golos>() {
            @Override
            public void onResponse(Call<Golos> call, Response<Golos> response) {
                if (response.isSuccessful()) {
                    Log.d("MyLog ","response ");
                } else {
                    Log.d("MyLog ","response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Golos> call, Throwable t) {
                Log.d("MyLog ","failure " + t);
            }
        });
    }

    private void setUrlInTable(String url) {
        boolean b = true;
        SQLHelper help = new SQLHelper(context, FeedEntry.DB_VERS);
        SQLiteDatabase db;
        db = help.getWritableDatabase(); // подключение к БД
        String[] columns = {FeedEntry.TITLE_ID};
        Cursor c = db.query(FeedEntry.TABLE_NAME, null, null, null, null, null, FeedEntry.TITLE_DATE);
        while (c.moveToNext()){
            if(c.getInt(c.getColumnIndex(FeedEntry.TITLE_ID))!=0){
                if(c.getString(c.getColumnIndex(FeedEntry.TITLE_COLUMN)).equals("No inf")){
                    b=false;
                    int p = c.getInt(c.getColumnIndex(FeedEntry.TITLE_ID));
                    db.execSQL(FeedEntry.CHANGE_NAME0+url+FeedEntry.CHANGE_NAME1+Integer.toString(p)+FeedEntry.CHANGE_NAME2);//изменение в таблице
                    break;
                }
            }
        }
        if(b){
            columns = new String[] {FeedEntry.TITLE_ID};
            c = db.query(FeedEntry.TABLE_NAME, columns, null, null, null, null, FeedEntry.TITLE_DATE);
            int i=0;
            while(c.moveToNext()){
                if(i==1){
                    db.execSQL(FeedEntry.CHANGE_NAME0+url+FeedEntry.CHANGE_NAME1+Integer.toString(c.getInt(c.getColumnIndex(FeedEntry.TITLE_ID)))+FeedEntry.CHANGE_NAME2);//изменение в таблице
                    db.execSQL(FeedEntry.CHANGE_DATE0+Integer.toString(c.getInt(c.getColumnIndex(FeedEntry.TITLE_ID)))+FeedEntry.CHANGE_DATE1);
                    break;
                }
                i++;
            }
        }
        help.close();
    }
}
