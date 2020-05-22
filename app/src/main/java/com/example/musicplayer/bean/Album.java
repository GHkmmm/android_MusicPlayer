package com.example.musicplayer.bean;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {
    String albumImgPath;
    String name;
    String updateDate;
    String comment;
    String top1;
    String top2;
    String top3;
    List<SongList> songLists;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public List<SongList> getSongLists() {
        return songLists;
    }

    public void setSongLists(List<SongList> songLists) {
        this.songLists = songLists;
    }

    public String getAlbumImgPath() {
        return albumImgPath;
    }

    public void setAlbumImgPath(String albumImgPath) {
        this.albumImgPath = albumImgPath;
    }

    public String getTop1() {
        return top1;
    }

    public void setTop1(String top1) {
        this.top1 = top1;
    }

    public String getTop2() {
        return top2;
    }

    public void setTop2(String top2) {
        this.top2 = top2;
    }

    public String getTop3() {
        return top3;
    }

    public void setTop3(String top3) {
        this.top3 = top3;
    }
}
