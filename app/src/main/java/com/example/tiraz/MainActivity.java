package com.example.tiraz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout tirazOne, tirazTwo, tirazThree, tirazFour;

        tirazOne = findViewById(R.id.tiraz1_home_button);
        tirazTwo = findViewById(R.id.tiraz2_home_button);
        tirazThree = findViewById(R.id.tiraz3_home_button);
        tirazFour = findViewById(R.id.tiraz4_home_button);

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
}