package com.example.apple.mobileplayer.view;

/**
 * Created by apple on 17/2/17.
 * 歌词类
 */
public class Lyric {

    /**
     * 歌词内容
     */
    private String content;

    /**
     * 时间戳
     */
    private long timePoint;

    /**
     * 高亮显示的时间
     */
    private long sleepTime;

    public Lyric() {
    }

    public Lyric(String content, long timePoint, long sleepTime) {
        this.content = content;
        this.timePoint = timePoint;
        this.sleepTime = sleepTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(long timePoint) {
        this.timePoint = timePoint;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "content='" + content + '\'' +
                ", timePoint=" + timePoint +
                ", sleepTime=" + sleepTime +
                '}';
    }
}
