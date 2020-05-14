package com.example.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.fragment.AlbumFragment;
import com.example.musicplayer.fragment.LyricsFragment;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList;
    private AlbumFragment albumFragment;
    private LyricsFragment lyricsFragment;
    private TextView name, lyrics, circle1, circle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        //查找元素
        name = findViewById(R.id.play_view_name);
        lyrics = findViewById(R.id.play_view_lyrics);
        viewPager = findViewById(R.id.viewPager);
        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);

        albumFragment = new AlbumFragment();
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


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        name.setText(bundle.getString("name"));
        lyrics.setText(bundle.getString("lyrics"));
    }
}
