package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.widget.ImageView;

import com.example.musicplayer.bean.Song;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class GetMusicUtil {
    ContentResolver resolver;

    public GetMusicUtil(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public ArrayList<Song> getMusic(){
        ArrayList<Song> mList = new ArrayList<>();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
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
                int album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                Bitmap bm = getArtAlbum(id);

                song.setId(id);
                song.setName(name);
                song.setSinger(singer);
                song.setAlbum(album);
                song.setUrl(url);
                song.setDuration(duration);
                song.setSize(size);
                song.setAlbum_img(bm);

                mList.add(song);

                cursor.moveToNext();
            }
        }else {
            System.out.println("error");
        }
        return mList;
    }

    public Bitmap getArtAlbum(long audioId) {
        String str = "content://media/external/audio/media/" + audioId + "/albumart";
        Uri uri = Uri.parse(str);
        ParcelFileDescriptor pfd = null;
        try {
            pfd = resolver.openFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            return null;
        }
        Bitmap bm;
        if (pfd != null) {
            FileDescriptor fd = pfd.getFileDescriptor();
            bm = BitmapFactory.decodeFileDescriptor(fd);
            return bm;
        }
        return null;
    }

}
