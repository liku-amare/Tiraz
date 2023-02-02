package com.example.tiraz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MezmurPlayer extends AppCompatActivity {

    private TextView mezmurTitle, mezmurLyrics;
    private ImageButton playButton, nextButton, prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mezmur_player);

        String mezmurJson = getIntent().getStringExtra("mezmur");


        mezmurTitle = findViewById(R.id.mezmur_player_title);
        mezmurLyrics = findViewById(R.id.mezmur_player_lyrics);
//        playButton = findViewById(R.id.mezmur_player_play);
//        nextButton = findViewById(R.id.mezmur_player_next);
//        prevButton = findViewById(R.id.mezmur_player_previous);

        JSONObject mezmur;
        String title, lyrics;
        int audio_id;
        try {
            mezmur = new JSONObject(mezmurJson);
            title = mezmur.getString("mezmur_title");
            lyrics = mezmur.getString("lyrics");
            audio_id = mezmur.getInt("audio_id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        mezmurTitle.setText(title);
        mezmurLyrics.setText(lyrics);

//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }
}