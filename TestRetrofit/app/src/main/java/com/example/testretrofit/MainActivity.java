package com.example.testretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    List<Poroda> p;
    private ServerApi serverApi;
    private Spinner spinner;
    private RecyclerView rec;
    private Button history;

    private SQLHelper help;
    private SQLiteDatabase db;

    final static int LIMIT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        history = findViewById(R.id.button4);
        spinner.setPrompt("Poroda");
        rec = findViewById(R.id.recycler);

        help = new SQLHelper(this, FeedEntry.DB_VERS);
        db = help.getWritableDatabase(); // подключение к БД

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApi = retrofit.create(ServerApi.class);

        Call<List<Poroda>> porods = serverApi.getBreeds();

        porods.enqueue(new Callback<List<Poroda>>() {
            @Override
            public void onResponse(Call<List<Poroda>> call, Response<List<Poroda>> response) {
                if (response.isSuccessful()) {
                    Log.d("MyShit ","response "+response.body().size());

                    p = response.body();

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, getPorodsNames());
                    spinner.setAdapter(adapter);

                    Cursor cur = db.query(FeedEntry.TABLE_NAME, null, null, null, null, null, null);
                    cur.moveToFirst();
                    if(cur.getString(cur.getColumnIndex(FeedEntry.TITLE_COLUMN))!="No inf"){
                        spinner.setSelection(adapter.getPosition(cur.getString(cur.getColumnIndex(FeedEntry.TITLE_COLUMN))));
                    }
                } else {
                    Log.d("MyShit ","response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Poroda>> call, Throwable t) {
                Log.d("MyShit","failure " + t);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db.execSQL(FeedEntry.CHANGE_NAME0+getPorodsNames()[position]+FeedEntry.CHANGE_NAME1+"0"+FeedEntry.CHANGE_NAME2);//изменение в таблице
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //________________________
        //Cursor c = db.query(FeedEntry.TABLE_NAME, null, null, null, null, null, null);
        //String s="";
        //while(c.moveToNext()) {
        //    s=s+c.getString(c.getColumnIndex(FeedEntry.TITLE_COLUMN));
        //}
        //t.setText(s);
        //_____________________


        View.OnClickListener bHistoryLike = new View.OnClickListener() {
            Context context = getApplicationContext();
            @Override
            public void onClick(View v) {
                ArrayList<String> urlHistory=getHistory();
                Intent intent = new Intent(context, HistoryActivity.class);
                intent.putExtra("urls", urlHistory);
                startActivity(intent);
            }
        };
        history.setOnClickListener(bHistoryLike);

    }

    private String[] getPorodsNames(){
        String[] str = new String[p.size()];
        for(int i=0; i<p.size(); i++) str[i]=p.get(i).getName();
        return str;
    }

    public void onClickSearch(View view){
        int i = spinner.getSelectedItemPosition();
        Call<List<UrlImg>> urls = serverApi.getImgUrl(p.get(i).getId(), LIMIT);

        urls.enqueue(new Callback<List<UrlImg>>() {
            @Override
            public void onResponse(Call<List<UrlImg>> call, Response<List<UrlImg>> response) {
                if (response.isSuccessful()) {
                    rec.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    MyAdapter myAdapter = new MyAdapter(response.body());
                    rec.setAdapter(myAdapter);
                    rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    });
                }else{
                    Log.d("MyShit ","response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UrlImg>> call, Throwable t) {
                Log.d("MyShit","failure " + t);
            }
        });
    }

    private ArrayList<String> getHistory(){
        ArrayList<String> urlHis = new ArrayList<>();
        help = new SQLHelper(this, FeedEntry.DB_VERS);
        SQLiteDatabase db;
        db = help.getWritableDatabase(); // подключение к БД
        String[] columns = {FeedEntry.TITLE_ID};
        Cursor c = db.query(FeedEntry.TABLE_NAME, null, null, null, null, null, FeedEntry.TITLE_DATE);
        while (c.moveToNext()){
            if(c.getInt(c.getColumnIndex(FeedEntry.TITLE_ID))!=0) {
                if (c.getString(c.getColumnIndex(FeedEntry.TITLE_COLUMN)) != "No inf") {
                    urlHis.add(c.getString(c.getColumnIndex(FeedEntry.TITLE_COLUMN)));
                }
            }
        }
        help.close();
        return urlHis;
    }

    @Override
    public void onDestroy() {
        help.close(); // закрыть подключение
        super.onDestroy();
    }
}
