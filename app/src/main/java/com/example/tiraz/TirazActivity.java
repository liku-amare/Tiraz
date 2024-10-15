package com.example.tiraz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TirazActivity extends AppCompatActivity {

    private RecyclerView mezmurRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiraz);

        int tiraz_id = 0;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            tiraz_id = (int) bundle.getInt("id");
        }

        String[] tirazTitles = new String[]{
                "አንድ", "ሁለት", "ሦስት", "አራት", "አምስት"
        };
        TextView tirazTitle = (TextView) findViewById(R.id.tiraz_page_title);
        mezmurRecycler = (RecyclerView) findViewById(R.id.mezmur_recycler);

        String finalTitle = "ጥራዝ " + tirazTitles[tiraz_id - 1];
        tirazTitle.setText(finalTitle);

        initRecycler(tiraz_id);
    }

    private void initRecycler(int tiraz_id) {
        mezmurRecycler.setLayoutManager(new LinearLayoutManager(this));
        MezmurRecycler mezmurRecyclerAdapter = new MezmurRecycler(this, tiraz_id);
        mezmurRecycler.setAdapter(mezmurRecyclerAdapter);
    }
}