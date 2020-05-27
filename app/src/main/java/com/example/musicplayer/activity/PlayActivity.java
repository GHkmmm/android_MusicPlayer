package com.example.musicplayer.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.bean.Album;
import com.example.musicplayer.fragment.AlbumFragment;
import com.example.musicplayer.fragment.LyricsFragment;
import com.example.musicplayer.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList;
    private AlbumFragment albumFragment;
    private LyricsFragment lyricsFragment;
    private TextView name, lyrics, circle1, circle2;
    private static TextView songCurrentDuration, songDuration;
    private static SeekBar sb;
    private ImageView playOrPauseBtn, playCover;
    Bitmap img;
    private MusicService.MusicControl service;
    private ServiceConnection conn;
    private ContentReceiver mReceiver;
    Boolean isPlaying = false;
    Intent serviceIntent;
    Boolean isUnbind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        initView();

        albumFragment = new AlbumFragment(img);
        lyricsFragment = new LyricsFragment();

        fragmentList = new ArrayList<>();
        fragmentList.add(albumFragment);
        fragmentList.add(lyricsFragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager, fragmentList);

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
                switch (position){
                    case 0:
                        circle1.setBackground(getResources().getDrawable(R.drawable.circle_shape_active));
                        circle2.setBackground(getResources().getDrawable(R.drawable.circle_shape));
                        break;
                    case 1:
                        circle1.setBackground(getResources().getDrawable(R.drawable.circle_shape));
                        circle2.setBackground(getResources().getDrawable(R.drawable.circle_shape_active));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        playCover = (getSupportFragmentManager().findFragmentByTag("android:switcher"+R.id.viewPager+"0")).getView().findViewById(R.id.play_cover);

//        System.out.println("playCover is ======"+ getSupportFragmentManager().findFragmentByTag("android:switcher"+R.id.viewPager+"0"));


//        playCover.setImageBitmap(img);

        doRegisterReceiver();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            //停止滑动时的处理
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                service.seekTo(progress);
            }
        });
        playOrPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    service.pausePlay();
                    playOrPauseBtn.setImageResource(R.drawable.ic_play_btn_play);
                }else {
                    service.continuePlay();
                    playOrPauseBtn.setImageResource(R.drawable.ic_play_btn_pause);
                }
            }
        });
    }

    public void initView(){
        //查找元素
        name = findViewById(R.id.play_view_name);
        lyrics = findViewById(R.id.play_view_lyrics);
        viewPager = findViewById(R.id.viewPager);
        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);
        sb = findViewById(R.id.seekBar);
        playOrPauseBtn = findViewById(R.id.play_play_or_pause);
        songCurrentDuration = findViewById(R.id.current_duration);
        songDuration = findViewById(R.id.duration);

        serviceIntent = new Intent(PlayActivity.this, MusicService.class);
        conn = new MyServiceConn();
        bindService(new Intent(PlayActivity.this, MusicService.class), conn, BIND_AUTO_CREATE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        byte buff[] = intent.getByteArrayExtra("img");
        img = BitmapFactory.decodeByteArray(buff, 0, buff.length);

        name.setText(bundle.getString("name"));
        lyrics.setText(bundle.getString("lyrics"));

        System.out.println("img is ======="+img);
    }

    private void doRegisterReceiver() {
        mReceiver=new ContentReceiver();
        IntentFilter filter = new IntentFilter(
                "com.example.musicplayer.service");
        registerReceiver(mReceiver, filter);
    }

    public final class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = (MusicService.MusicControl) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public class ContentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int currentDuration = intent.getExtras().getInt("currentDuration");
            int duration = intent.getExtras().getInt("duration");
            isPlaying = intent.getExtras().getBoolean("isPlaying");

            sb.setMax(duration); //设置总长度
            sb.setProgress(currentDuration); //设置当前进度值

            int min = duration / 1000 / 60;
            int sec = duration / 1000 % 60;

            String strMin = "";
            String strSec = "";
            if(min<10){
                strMin = "0"+min;
            }else {
                strMin = min+"";
            }

            if(sec<10){
                strSec = "0"+sec;
            }else {
                strSec = sec+"";
            }

            //设置进度条
            songDuration.setText(strMin + ":" + strSec);

            min = currentDuration / 1000 / 60;
            sec = currentDuration / 1000 % 60;

            if(min<10){
                strMin = "0"+min;
            }else {
                strMin = min+"";
            }

            if(sec<10){
                strSec = "0"+sec;
            }else {
                strSec = sec+"";
            }

            songCurrentDuration.setText(strMin + ":" + strSec);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        myUnbind();
        super.onDestroy();
    }

    private void myUnbind(){
        if(!isUnbind){
            isUnbind = true;
            unbindService(conn); //解绑服务
            stopService(serviceIntent); //停止服务
        }
    }
}
