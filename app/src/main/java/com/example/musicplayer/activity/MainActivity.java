package com.example.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.GetMusicUtil;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.fragment.LocalMusicFragment;
import com.example.musicplayer.fragment.OnlineMusicFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView goPlayListView;
    private ImageView goPlayView;
    private TextView localMusic, onlineMusic, songName, songLyrics;
    private ViewPager homeViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList;
    private LocalMusicFragment localMusicFragment;
    private OnlineMusicFragment onlineMusicFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localMusic = findViewById(R.id.goLocalMusic);
        onlineMusic = findViewById(R.id.goOnlineMusic);
        homeViewPager = findViewById(R.id.home_viewPager);
        goPlayListView = findViewById(R.id.goListBtn);
        goPlayView = findViewById(R.id.goListViewBtn);
        songName = findViewById(R.id.song_name);
        songLyrics = findViewById(R.id.song_lyrics);

        localMusicFragment = new LocalMusicFragment();
        onlineMusicFragment = new OnlineMusicFragment();

        fragmentList = new ArrayList<>();
        fragmentList.add(localMusicFragment);
        fragmentList.add(onlineMusicFragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager, fragmentList);

        homeViewPager.setAdapter(viewPagerAdapter);

        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                homeViewPager.setCurrentItem(position);
                switch (position){
                    case 0:
                        localMusic.setTextColor(getResources().getColor(R.color.textColor));
                        onlineMusic.setTextColor(getResources().getColor(R.color.textLightColor));
                        break;
                    case 1:
                        localMusic.setTextColor(getResources().getColor(R.color.textLightColor));
                        onlineMusic.setTextColor(getResources().getColor(R.color.textColor));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
