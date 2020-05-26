package com.example.musicplayer.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.Serializable;

public class Song implements Serializable {
    private String name;
    public String singer;//歌手
    public long size;//歌曲所占空间大小
    public long duration;//歌曲时间长度
    public String url;//歌曲地址
    public String album;//专辑名称
    public Bitmap album_img;
    public String imgPath;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Bitmap getAlbum_img() {
        return album_img;
    }

    public void setAlbum_img(Bitmap album_img) {
        this.album_img = album_img;
    }

    public long id;//歌曲id

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
