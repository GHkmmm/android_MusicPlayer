package com.example.musicplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.ImgChangeToBitMap;
import com.example.musicplayer.ParseJsonUtil;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.songListAdapter;
import com.example.musicplayer.bean.Album;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.bean.SongList;
import com.example.musicplayer.fragment.LocalMusicFragment;
import com.example.musicplayer.service.MusicService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListsActivity extends AppCompatActivity {
    List<Album> albums = new ArrayList<>();
    int position;
    ImageView albumImg;
    TextView albumName, updateDate, comment;
    RecyclerView songList;
    List<SongList> songLists;
    Song song;

    private MusicService.MusicControl musicControl; //Binder实例
    private MyServiceConn myServiceConn; //连接实例

    private Intent intent;
    Boolean isUnbind = false;

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

        intent = new Intent(ListsActivity.this, MusicService.class);
        myServiceConn = new MyServiceConn();
        bindService(intent, myServiceConn, BIND_AUTO_CREATE);
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

        slAdapter.setOnItemClickListener(new songListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final SongList songList, int position) {
                Toast.makeText(ListsActivity.this, "正在播放："+songList.getTitle(), Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.song.play&songid="+songList.getSong_id());
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            Log.i("HttpURLConnection.GET", "开始连接");
                            connection.connect();
                            if (connection.getResponseCode() == 200) {
                                Log.i("HttpURLConnection.GET", "请求成功");
                                InputStream in = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                                ParseJsonUtil parseJsonUtil = new ParseJsonUtil();
                                song = parseJsonUtil.parseSongJson(response.toString());
                                musicControl.play(song.getUrl());
                                Intent backIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("song", (Serializable) song);
                                backIntent.putExtras(bundle);
                                ListsActivity.this.setResult(RESULT_OK, backIntent);
                            }else {
                                Log.i("HttpURLConnection.GET", "请求失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        ListsActivity.this.finish();
        super.onDestroy();
        unbindService(myServiceConn); //解绑服务
        stopService(intent); //停止服务
    }
}
