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
//    File file = new File(Environment.getExternalStorageDirectory() + "/Download/", "周杰伦+-+黑色幽默.mp3");
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
                    int duration = player.getDuration();
                    int currentDuration = player.getCurrentPosition();
//                    Message msg = LocalMusicFragment.handler.obtainMessage();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("duration", duration);
//                    bundle.putInt("currentDuration", currentDuration);
//                    msg.setData(bundle);
//
//                    LocalMusicFragment.handler.sendMessage(msg);
                    sendContentBroadcast(duration, currentDuration);
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

        public void play(String path) {
            System.out.println(path);
            try {
                player.reset();
//                player = MediaPlayer.create(getApplicationContext(), R.raw.test);
                player.setDataSource(path);
                player.prepare();

                addTimer();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void playOnlineMusic(String url) {
            try{
                player.reset();
                player.setDataSource(url);
                player.prepare();
                addTimer();
                player.start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void pausePlay(){
            player.pause();
        }

        public void continuePlay(){
            player.start();
        }

        public void seekTo(int progress){
            player.seekTo(progress); //设置音乐播放位置
        }
    }

    protected void sendContentBroadcast(int duration, int currentDuration){
        Intent intent = new Intent();
        intent.setAction("com.example.musicplayer.service");
        Bundle bundle = new Bundle();
        bundle.putInt("duration", duration);
        bundle.putInt("currentDuration", currentDuration);
        bundle.putBoolean("isPlaying", player.isPlaying());
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }
}
