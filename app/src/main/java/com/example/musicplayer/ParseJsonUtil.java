package com.example.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.musicplayer.bean.Album;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.bean.SongList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ParseJsonUtil {
    Album album;
    List<SongList> songListArray;

    public Album parseJsonObj(String response) throws JSONException {
        album = new Album();
        JSONObject jsonObj = new JSONObject(response);
        String list = jsonObj.getString("song_list");
        JSONObject billboard = new JSONObject(jsonObj.getString("billboard"));
        String albumImg = billboard.getString("pic_s192");
        String name = billboard.getString("name");
        String updateDate = billboard.getString("update_date");
        String comment = billboard.getString("comment");
        String errCode = jsonObj.getString("error_code");

        album.setAlbumImgPath(albumImg);
        album.setName(name);
        album.setUpdateDate(updateDate);
        album.setComment(comment);

        return album;
    }

    public Song parseSongJson(String response) throws JSONException{
        Song song = new Song();
        JSONObject jsonObject = new JSONObject(response);
        JSONObject bitrate = new JSONObject(jsonObject.getString("bitrate"));
        JSONObject songInfo = new JSONObject(jsonObject.getString("songinfo"));

        String url = bitrate.getString("show_link");
        String name = songInfo.getString("title");
        String singer = songInfo.getString("author");
        String duration = bitrate.getString("file_duration");
        String imgUrl = songInfo.getString("pic_radio");

        song.setUrl(url);
        song.setName(name);
        song.setSinger(singer);
        song.setDuration((Long.parseLong(duration)));
        song.setImgPath(imgUrl);

        return song;
    }

    public List<SongList> parseJsonArr(String response) throws JSONException {
        songListArray = new ArrayList<>();
        JSONArray jsonArray = new JSONObject(response).getJSONArray("song_list");
        for (int i = 0; i < jsonArray.length(); i++){
            SongList songList = new SongList();
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            String title = jsonObj.getString("title");
            String author = jsonObj.getString("author");
            String album_title = jsonObj.getString("album_title");
            String pic = jsonObj.getString("pic_radio");
            String id = jsonObj.getString("song_id");

            songList.setTitle(title);
            songList.setAuthor(author);
            songList.setAlbum_title(album_title);
            songList.setPic_radio(pic);
            songList.setSong_id(id);

            songListArray.add(songList);
        }
        return songListArray;
    }
}
