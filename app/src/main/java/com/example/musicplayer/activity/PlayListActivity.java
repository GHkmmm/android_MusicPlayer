package com.example.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;

public class PlayListActivity extends AppCompatActivity {
    TextView name, lyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_list);

        name = findViewById(R.id.current_song_name);
        lyrics = findViewById(R.id.current_song_lyrics);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        name.setText(bundle.getString("name"));
        lyrics.setText(bundle.getString("lyrics"));

    }

}
