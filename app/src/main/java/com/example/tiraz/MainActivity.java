package com.example.tiraz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout tirazOne, tirazTwo, tirazThree, tirazFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tirazOne = findViewById(R.id.tiraz1_home_button);
        tirazTwo = findViewById(R.id.tiraz2_home_button);
        tirazThree = findViewById(R.id.tiraz3_home_button);
        tirazFour = findViewById(R.id.tiraz4_home_button);

//        tirazOne.setBackgroundColor(Color.parseColor("#ebf1f4"));
//        tirazOne.
    }
}