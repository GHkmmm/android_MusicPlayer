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
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.example.musicplayer.bean.Song;

import java.util.ArrayList;

public class GetMusicUtil {
    ContentResolver resolver;

    public GetMusicUtil(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public ArrayList<Song> getMusic(){
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
                int album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));


                String cover = getAlbumArt(album_id);
                Bitmap bm = BitmapFactory.decodeFile(cover);


//                Bitmap bm = setArtwork(getMediaStoreAlbumCoverUri(album_id));

                song.setId(id);
                song.setName(name);
                song.setSinger(singer);
                song.setAlbum(album);
                song.setUrl(url);
                song.setDuration(duration);
                song.setSize(size);
                song.setAlbum_img(bm);

                System.out.println(name+"-----"+album);

                mList.add(song);

                cursor.moveToNext();
            }
        }else {
            System.out.println("error");
        }
        return mList;
    }

    private String getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        System.out.println("id========"+album_id);
        Cursor cur = resolver.query( Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), null, null, null, null);
        System.out.println("cur-----------"+cur);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0){
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        System.out.println("album --------" + album_art);
        return album_art;
    }

    public static Bitmap setArtwork(String url) {

        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(url);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bMap= BitmapFactory.decodeByteArray(picture,0,picture.length);
        return bMap;
    }

    public static Uri getMediaStoreAlbumCoverUri(int albumId) {
        Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(artworkUri, albumId);
    }
}
