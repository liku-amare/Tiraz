package com.example.tiraz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mezmurRecycler;
    private TextView searchQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String query = "";
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            query = bundle.getString("query");
        }

        searchQuery = findViewById(R.id.search_page_title);
        searchQuery.setText("\"" + query + "\"");

        mezmurRecycler = findViewById(R.id.search_result_recycler);
        populateResults(query);
    }

    private void populateResults(String query){
        mezmurRecycler.setLayoutManager(new LinearLayoutManager(this));
        SearchRecycler searchRecycler = new SearchRecycler(this, query);
        mezmurRecycler.setAdapter(searchRecycler);
    }
}