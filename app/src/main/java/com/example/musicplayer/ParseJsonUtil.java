package com.example.musicplayer;

import com.example.musicplayer.bean.Album;
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

    public List<SongList> parseJsonArr(String response) throws JSONException {
        songListArray = new ArrayList<>();
        JSONArray jsonArray = new JSONObject(response).getJSONArray("song_list");
        for (int i = 0; i < jsonArray.length(); i++){
            SongList songList = new SongList();
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            String title = jsonObj.getString("title");
            String author = jsonObj.getString("author");
            String album_title = jsonObj.getString("album_title");

            songList.setTitle(title);
            songList.setAuthor(author);
            songList.setAlbum_title(album_title);

            songListArray.add(songList);
        }
        return songListArray;
    }
}
