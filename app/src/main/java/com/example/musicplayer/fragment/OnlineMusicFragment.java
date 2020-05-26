package com.example.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.ImgChangeToBitMap;
import com.example.musicplayer.ParseJsonUtil;
import com.example.musicplayer.activity.ListsActivity;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.activity.PlayActivity;
import com.example.musicplayer.bean.Album;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.RvAdapter;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.bean.SongList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OnlineMusicFragment extends Fragment {
    private RecyclerView onlineMusicList;
    private List<Album> albums = new ArrayList<>();
    private List<URL> urls = new ArrayList<>();
    private ImageView goPlayListView;
    private ImageView cover, playBtn;
    private TextView name, singer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onlinemusic_fragment, container, false);
        onlineMusicList = view.findViewById(R.id.onlineMusic_list);
        cover = getActivity().findViewById(R.id.goListViewBtn);
        playBtn = getActivity().findViewById(R.id.play_or_pause);
        name = getActivity().findViewById(R.id.song_name);
        singer = getActivity().findViewById(R.id.song_lyrics);
        getData();
        initRv();
        return view;
    }


    private void getData (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                int num = 0;
                for(int i=1;i<26;i++) {
                    num++;
                    if (i == 3) {
                        i = 11;
                    }
                    if (i == 12) {
                        i = 21;
                    }
                    try {
                        System.out.println(i);
                        URL url = new URL("http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=" + i + "&offset=0&size=50");
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
                            Album album = new Album();
                            album = parseJsonUtil.parseJsonObj(response.toString());
                            List<SongList> top = parseJsonUtil.parseJsonArr(response.toString());
                            System.out.println("top:" + top);
                            album.setTop1(top.get(0).getAlbum_title());
                            album.setTop2(top.get(1).getAlbum_title());
                            album.setTop3(top.get(2).getAlbum_title());
                            album.setSongLists(top);
                            System.out.println(album);
                            albums.add(album);
                            msg.what = i;
                        } else {
                            Log.i("HttpURLConnection.GET", "请求失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            initRv();
        }
    };

    private void initRv(){
        RvAdapter adapter = new RvAdapter(albums);
        onlineMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        onlineMusicList.setAdapter(adapter);
        onlineMusicList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new RvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Album album, int position) {
                Toast.makeText(getActivity(), album.getTop1(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ListsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) albums);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Song song = (Song) data.getExtras().getSerializable("song");//得到新Activity 关闭后返回的数据
        ImgChangeToBitMap task = new ImgChangeToBitMap(cover);
        task.execute(song.getImgPath());
        name.setText(song.getName());
        singer.setText(song.getSinger());
        playBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
    }
}
