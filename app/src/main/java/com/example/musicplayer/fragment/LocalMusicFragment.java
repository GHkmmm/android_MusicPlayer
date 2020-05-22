package com.example.musicplayer.fragment;

import android.content.ContentResolver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.GetMusicUtil;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.LocalMusicListAdapter;
import com.example.musicplayer.adapter.RvAdapter;
import com.example.musicplayer.bean.Song;

import java.util.ArrayList;

public class LocalMusicFragment extends Fragment {
    private ArrayList<Song> songList;
    private GetMusicUtil util;
    private RecyclerView localMusicList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.localmusic_fragment, container, false);
        localMusicList = view.findViewById(R.id.localMusic_list);
        setData();
        initRv();
        return view;
    }

    private void setData(){
        ContentResolver resolver = getActivity().getContentResolver();
        util = new GetMusicUtil(resolver);
        songList= util.getMusic();
        System.out.println("songList-----"+songList);
    }

    private void initRv(){
        LocalMusicListAdapter lmAdapter = new LocalMusicListAdapter(songList);
        localMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        localMusicList.setAdapter(lmAdapter);
    }
}
