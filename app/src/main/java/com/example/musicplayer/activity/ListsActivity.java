package com.example.musicplayer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.ImgChangeToBitMap;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.LocalMusicListAdapter;
import com.example.musicplayer.adapter.songListAdapter;
import com.example.musicplayer.bean.Album;
import com.example.musicplayer.bean.SongList;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ListsActivity extends AppCompatActivity {
    List<Album> albums = new ArrayList<>();
    int position;
    ImageView albumImg;
    TextView albumName, updateDate, comment;
    RecyclerView songList;
    List<SongList> songLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        albums = (List<Album>) bundle.getSerializable("list");
        position = bundle.getInt("position");
        initData();
        setData();
        initAdapter();

    }

    public void initData(){
        albumImg = findViewById(R.id.album_img);
        albumName = findViewById(R.id.album_name);
        updateDate = findViewById(R.id.update_date);
        comment = findViewById(R.id.comment);
        songList = findViewById(R.id.song_list);
        songList = findViewById(R.id.song_list);
    }
    public void setData(){
        albumName.setText(albums.get(position).getName());
        updateDate.setText("最近更新："+albums.get(position).getUpdateDate());
        comment.setText(albums.get(position).getComment());

        songLists = albums.get(position).getSongLists();

        String url = albums.get(position).getAlbumImgPath();

        ImgChangeToBitMap task = new ImgChangeToBitMap(albumImg);
        task.execute(url);
    }
    public void initAdapter(){
        songListAdapter slAdapter = new songListAdapter(songLists);
        songList.setLayoutManager(new LinearLayoutManager(this));
        songList.setAdapter(slAdapter);
    }
}
