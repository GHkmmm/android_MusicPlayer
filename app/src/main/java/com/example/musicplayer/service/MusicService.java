package com.example.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.fragment.LocalMusicFragment;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐播放功能的服务
 */
public class MusicService extends Service {
    private MediaPlayer player;
    private Timer timer;

    public MusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
    }

    public void addTimer(){
        if(timer == null){
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if(player == null) return;
                    sendContentBroadcast();
                }
            };
            timer.schedule(task, 5, 500);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return new MusicControl();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player == null) return;
        if(player.isLooping()) player.stop();
        player.release(); //释放资源
        player = null;
    }

    public class MusicControl extends Binder{
        //播放音乐
        public void play(String path) {
            try {
                player.reset();
                player.setDataSource(path);
                player.prepare();
                addTimer();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //暂停音乐
        public void pausePlay(){
            player.pause();
        }
        //继续播放音乐
        public void continuePlay(){
            player.start();
        }
        //进度跳转
        public void seekTo(int progress){
            player.seekTo(progress); //设置音乐播放位置
        }
    }

    protected void sendContentBroadcast(){
        Intent intent = new Intent();
        intent.setAction("com.example.musicplayer.service");
        Bundle bundle = new Bundle();
        bundle.putInt("duration", player.getDuration());
        bundle.putInt("currentDuration", player.getCurrentPosition());
        bundle.putBoolean("isPlaying", player.isPlaying());
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }
}
