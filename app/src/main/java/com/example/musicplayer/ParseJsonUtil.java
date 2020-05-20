package com.example.musicplayer;

import com.example.musicplayer.bean.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ParseJsonUtil {
    Album album;
    List<String> top;

    public String parseJsonObj(String response) throws JSONException {
        album = new Album();
        JSONObject jsonObj = new JSONObject(response);
        String list = jsonObj.getString("song_list");
        JSONObject billboard = new JSONObject(jsonObj.getString("billboard"));
        String albumImg = billboard.getString("pic_s192");
        String errCode = jsonObj.getString("error_code");
        return albumImg;
    }

    public List<String> parseJsonArr(String response) throws JSONException {
        top = new ArrayList<>();
        JSONArray jsonArray = new JSONObject(response).getJSONArray("song_list");
        for (int i = 0; i <3; i++){
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            String name = jsonObj.getString("title");
            top.add(name);
        }
        return top;
    }
}
