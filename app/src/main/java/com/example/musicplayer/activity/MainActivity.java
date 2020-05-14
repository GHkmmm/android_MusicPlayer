package com.example.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.R;

public class MainActivity extends AppCompatActivity {
    private ImageView goPlayListView;
    private ImageView goPlayView;
    private TextView songName, songLyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goPlayListView = findViewById(R.id.goListBtn);
        goPlayView = findViewById(R.id.goListViewBtn);
        songName = findViewById(R.id.song_name);
        songLyrics = findViewById(R.id.song_lyrics);

        goPlayListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", songName.getText().toString());
                bundle.putString("lyrics", songLyrics.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        goPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("click go");
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", songName.getText().toString());
                bundle.putString("lyrics", songLyrics.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
