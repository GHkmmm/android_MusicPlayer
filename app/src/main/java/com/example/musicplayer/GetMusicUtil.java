package com.example.musicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicplayer.bean.Song;

import java.util.ArrayList;

public class GetMusicUtil {
    public ArrayList<Song> getMusic(ContentResolver resolver){
        ArrayList<Song> mList = new ArrayList<>();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        System.out.println(cursor.getCount());
        if(cursor.moveToNext()){
            for(int i = 0; i<cursor.getCount(); i++){
                Song song = new Song();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));//id
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));//获取音乐名称
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));//获取歌手名
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));//获取歌手名
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));//获取路径
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));


                song.setId(id);
                song.setName(name);
                song.setSinger(singer);
                song.setAlbum(album);
                song.setUrl(url);
                song.setDuration(duration);
                song.setSize(size);

                System.out.println(name+"-----"+album);

                mList.add(song);

                cursor.moveToNext();
            }
        }else {
            System.out.println("error");
        }
        return mList;
    }
}
