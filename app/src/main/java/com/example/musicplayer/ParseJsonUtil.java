package com.example.musicplayer;

import com.example.musicplayer.bean.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJsonUtil {
    Album album;

    public String parseJsonObj(String response) throws JSONException {
        album = new Album();
        JSONObject jsonObj = new JSONObject(response);
        String list = jsonObj.getString("song_list");
        JSONObject billboard = new JSONObject(jsonObj.getString("billboard"));
        String albumImg = billboard.getString("pic_s192");
        String errCode = jsonObj.getString("error_code");
        return albumImg;
    }

    public void parseJsonArr(String response, String key) throws JSONException {
        JSONArray jsonArray = new JSONObject(response).getJSONArray(key);
        for (int i = 0; i <jsonArray.length(); i++){
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            String pic = jsonObj.getString("pic_s192");
        }
    }
}
