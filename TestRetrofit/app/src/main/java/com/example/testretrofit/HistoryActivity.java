package com.example.testretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        RecyclerView rec = findViewById(R.id.recycler2);
        Intent intent = getIntent();
        ArrayList<String> urls = intent.getStringArrayListExtra("urls");



        ArrayList<String> urls1 = new ArrayList<>();
        for(int i=0; i<urls.size(); i++){
            if(!(urls.get(i).equals("No inf"))){
                urls1.add(urls.get(i));
            }
        }

        String s="";
        for(int i=0; i<urls1.size();i++){
            s=s+" "+urls1.get(i);
        }

        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        MyHistoryAdapter myHistoryAdapter = new MyHistoryAdapter(urls1);
        rec.setAdapter(myHistoryAdapter);
    }
}
