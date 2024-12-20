package com.example.tiraz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MezmurPlayer extends AppCompatActivity {

    private TextView mezmurTitle, mezmurLyrics, currentTime, totalTime;
    private ImageButton playButton, nextButton, prevButton;
    private SeekBar seekBar;

    private ProgressBar progressBar;

    ArrayList<AudioModel> mezmurList;

    AudioModel currentMezmur;

    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mezmur_player);

        mezmurList = (ArrayList<AudioModel>)getIntent().getSerializableExtra("LIST");

        mezmurTitle = findViewById(R.id.mezmur_player_title);
        mezmurLyrics = findViewById(R.id.mezmur_player_lyrics);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        playButton = findViewById(R.id.mezmur_player_play);
        nextButton = findViewById(R.id.mezmur_player_next);
        prevButton = findViewById(R.id.mezmur_player_previous);
        progressBar = findViewById(R.id.mezmur_player_loading);
        handler = new Handler();

        setResourcesWithMusic();
    }

    void setResourcesWithMusic(){
        Log.d("Current Index Value: ", String.valueOf(MyMediaPlayer.currentIndex));
        currentMezmur = mezmurList.get(MyMediaPlayer.currentIndex);
        mezmurTitle.setText(currentMezmur.getTitle());
        mezmurLyrics.setText(currentMezmur.getLyrics());

        playButton.setOnClickListener(v-> pausePlay());
        nextButton.setOnClickListener(v-> playNextSong());
        prevButton.setOnClickListener(v-> playPreviousSong());

        if (currentMezmur.audio_id == -1){
            progressBar.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "የድምፅ ቅጅ አልተገኘም።", Toast.LENGTH_SHORT).show();
            playButton.setImageResource(R.drawable.baseline_not_found_24);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
//        playButton.setVisibility(View.VISIBLE);


        // Get a non-default Storage bucket
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://tiraz2023.appspot.com/");

        // Example: gs://tiraz2023.appspot.com/tiraz4/4001.mp3
        String fileName = "tiraz" + currentMezmur.tirazNo + "/" + currentMezmur.audio_id + ".mp3";

        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(fileName);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("URL: ", uri.toString());
                playMusic(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO: set on failure task
            }
        });
    }
    private void playMusic(Uri uri){
        mediaPlayer.reset();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(uri.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(mediaPlayer1 -> {
            progressBar.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            playButton.setImageResource(R.drawable.baseline_play_circle_24);
            totalTime.setText(convertToMMSS(String.valueOf(mediaPlayer.getDuration())));
//            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            updateSeekBar();
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                double ratio = percent / 100.0;
                int bufferingLevel = (int)(mp.getDuration() * ratio);
                seekBar.setSecondaryProgress(bufferingLevel);
            }
        });

    }

    public void updateSeekBar(){
        int curPos = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(curPos);
        currentTime.setText(convertToMMSS(String.valueOf(curPos)));

        runnable = new Runnable() {
            @Override
            public void run() {
                updateSeekBar();
            }
        };
        handler.postDelayed(runnable, 100);
    }

    private void playNextSong(){
        Log.d("Next Index: ", "" + MyMediaPlayer.currentIndex);
        Log.d("Next Mezmur Size: ", "" + mezmurList.size());

        if(MyMediaPlayer.currentIndex == mezmurList.size()-1)
            return;
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void playPreviousSong(){
        Log.d("Previous Index: ", "" + MyMediaPlayer.currentIndex);
        Log.d("Previous Mezmur Size: ", "" + mezmurList.size());

        if(MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if (currentMezmur.audio_id == -1)
            Toast.makeText(this, "የድምፅ ቅጅ አልተገኘም።", Toast.LENGTH_SHORT).show();
        else if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.baseline_play_circle_24);
        }
        else {
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.baseline_pause_circle_24);
        }
    }

    public static String convertToMMSS(String duration){
        long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}