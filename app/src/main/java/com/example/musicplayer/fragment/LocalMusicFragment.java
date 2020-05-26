package com.example.musicplayer.fragment;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.GetMusicUtil;
import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.adapter.LocalMusicListAdapter;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.service.MusicService;

import java.util.ArrayList;

public class LocalMusicFragment extends Fragment {
    private ImageView cover, playBtn;
    private TextView name, singer;
    private ArrayList<Song> songList;
    private GetMusicUtil util;
    private RecyclerView localMusicList;

    private MusicService.MusicControl musicControl; //Binder实例
    private MyServiceConn myServiceConn; //连接实例

    private Intent intent;

    private boolean isUnbind = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.localmusic_fragment, container, false);
        localMusicList = view.findViewById(R.id.localMusic_list);
        cover = getActivity().findViewById(R.id.goListViewBtn);
        playBtn = getActivity().findViewById(R.id.play_or_pause);
        name = getActivity().findViewById(R.id.song_name);
        singer = getActivity().findViewById(R.id.song_lyrics);
        System.out.println("name is ======="+name);
        initView();
        setData();
        initRv();
        return view;
    }

    private void initView(){
        intent = new Intent(getActivity(), MusicService.class);
        myServiceConn = new MyServiceConn();
        getActivity().bindService(intent, myServiceConn, Context.BIND_AUTO_CREATE);
//        bindService(intent, MyServiceConn, BIND_AUTO_CREATE);
    }

    private void setData(){
        ContentResolver resolver = getActivity().getContentResolver();
        util = new GetMusicUtil(resolver);
        songList= util.getMusic();
    }

    private void initRv(){
        LocalMusicListAdapter lmAdapter = new LocalMusicListAdapter(songList);
        localMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        localMusicList.setAdapter(lmAdapter);
        lmAdapter.setOnItemClickListener(new LocalMusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Song song, int position) {
                int REQUEST_EXTERNAL_STORAGE = 1;
                String[] PERMISSIONS_STORAGE = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }

                Toast.makeText(getActivity(), "正在播放："+song.getName(), Toast.LENGTH_SHORT).show();
                name.setText(song.getName());
                singer.setText(song.getSinger());
                playBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
                musicControl.play(song.getUrl());
            }
        });
    }

//    public static Handler handler = new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//        }
//    };

    class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

//    private void myUnbind(boolean isUnbind){
//        if(!isUnbind){
//            isUnbind = true;
//            musicControl.pausePlay(); //暂停播放
//            unbindService(myServiceConn); //解绑服务
//            stopService(intent); //停止服务
//        }
//    }
}
