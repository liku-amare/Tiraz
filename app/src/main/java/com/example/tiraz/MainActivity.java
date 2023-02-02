package com.example.tiraz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout tirazOne, tirazTwo, tirazThree, tirazFour;
        TextView[][] textViews = new TextView[4][2];

        int[][] textViewIds = new int[][]{
                {R.id.tiraz1_year, R.id.tiraz1_hymns},
                {R.id.tiraz2_year, R.id.tiraz2_hymns},
                {R.id.tiraz3_year, R.id.tiraz3_hymns},
                {R.id.tiraz4_year, R.id.tiraz4_hymns}
        };

        for (int i = 0; i < 4; i ++){
            textViews[i][0] = findViewById(textViewIds[i][0]);
            textViews[i][1] = findViewById(textViewIds[i][1]);
        }

        tirazOne = findViewById(R.id.tiraz1_home_button);
        tirazTwo = findViewById(R.id.tiraz2_home_button);
        tirazThree = findViewById(R.id.tiraz3_home_button);
        tirazFour = findViewById(R.id.tiraz4_home_button);

        MezmurRecycler mezmurRecycler = new MezmurRecycler(this, 0);
        String mezmurJSON = mezmurRecycler.getJson("tiraz_lyrics.json");

        String[][] tiraz_details = new String[4][2];
        try{
            JSONArray jsonArray = new JSONArray(mezmurJSON);
            for (int i = 0; i < 4; i ++){
                tiraz_details[i][0] = jsonArray.getJSONObject(i).getString("year");
                tiraz_details[i][1] = jsonArray.getJSONObject(i).getString("hymns_count");
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        setValues(textViews, tiraz_details);


        tirazOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTiraz(1);
            }
        });
        tirazTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTiraz(2);
            }
        });
        tirazThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTiraz(3);
            }
        });
        tirazFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTiraz(4);
            }
        });
    }

    void openTiraz(int id){
        Intent intent = new Intent(getApplicationContext(), TirazActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    void setValues(TextView[][] textViews, String[][] values){
        for (int i = 0; i < 4; i++){
            String year = values[i][0] + " ዓ.ም";
            String hymns = values[i][1] + " መዝሙራት";
            textViews[i][0].setText(year);
            textViews[i][1].setText(hymns);
        }
    }
}