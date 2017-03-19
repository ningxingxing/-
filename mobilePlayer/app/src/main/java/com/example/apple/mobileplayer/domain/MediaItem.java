package com.example.apple.mobileplayer.domain;

import java.io.Serializable;

/**
 * Created by apple on 2017/2/6.
 * 代表一个视频和音频
 */

public class MediaItem implements Serializable{
    private String name;
    private long duration;
    private long size;
    private String data;
    private String artist;
    private String desc;//视频数据
    private String imageUrl;

    public MediaItem() {
    }

    public MediaItem(String name, long duration, long size, String data, String artist, String desc, String imageUrl) {
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.data = data;
        this.artist = artist;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                ", desc='" + desc + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
