package com.example.onlineradioapp;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button b_play;
    String stream = "http://stream.radioreklama.bg:80/radio1rock128";
    MediaPlayer mediaPlayer;
    boolean prepared = false;
    boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b_play = findViewById(R.id.b_play);
        b_play.setEnabled(false);
        b_play.setText("LOADING");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerTask().execute(stream);
        b_play.setOnClickListener((view) -> {
            if (started){
                started = false;
                mediaPlayer.pause();
                b_play.setText("PLAY");
            }else{
                started = true;
                mediaPlayer.start();
                b_play.setText("PAUSE");
            }
        });
    }

    class PlayerTask extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mediaPlayer.start();
            b_play.setEnabled(true);
            b_play.setText("PLAY");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (started){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (started){
            mediaPlayer.release();
        }
    }
}