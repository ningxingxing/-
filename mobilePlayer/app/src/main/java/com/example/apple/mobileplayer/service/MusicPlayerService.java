package com.example.apple.mobileplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.apple.mobileplayer.IMusicPlayerService;
import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.activity.AudioPlayerActivity;
import com.example.apple.mobileplayer.domain.MediaItem;
import com.example.apple.mobileplayer.utils.CacheUtils;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by apple on 17/2/16.
 */

public class MusicPlayerService extends Service {

    private ArrayList<MediaItem> mediaItems;

    /**
     * 得到当前播放的音频文件
     */
    private MediaItem mediaItem;

    /**
     * 用于播放音频文件
     */
    private MediaPlayer mediaPlayer;

    private int position;

    public static final String OPENAUDIO = "com.nsc.mobileplayer_OPENAUDIO";


    /**
     * 顺序播放
     */
    public static final int REPEAT_NORMAL = 1;

    /**
     * 单曲循环
     */
    public static final int REPEAT_SINGLE = 2;

    /**
     * 全部循环
     */
    public static final int REPEAT_ALL = 3;

    /**
     * 播放模式
     */
    private int playMode = REPEAT_NORMAL;

    @Override
    public void onCreate() {
        super.onCreate();

        playMode = CacheUtils.getPlayMode(this, "playMode");
        //加载音乐列表
        getDataFromLocal();
    }

    public void getDataFromLocal() {

        new Thread() {
            @Override
            public void run() {
                super.run();

                mediaItems = new ArrayList<>();

                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//视屏文件在sdcard的名称
                        MediaStore.Audio.Media.DURATION,//视屏总时长
                        MediaStore.Audio.Media.SIZE,//视屏的文件大小
                        MediaStore.Audio.Media.DATA,//视屏的绝对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        MediaItem mediaItem = new MediaItem();

                        String name = cursor.getString(0);//视频名称
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);//视频的时长
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);//视频大小
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);//视频播放地址
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);//艺术家
                        mediaItem.setArtist(artist);

                        mediaItems.add(mediaItem);//可以写在上面
                    }

                    cursor.close();
                }

            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;//返回stub
    }


    private IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {

        MusicPlayerService service = MusicPlayerService.this;

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void setPlayMode(int playMode) throws RemoteException {
            service.setPlayMode(playMode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void seekTO(int position) throws RemoteException {
            mediaPlayer.seekTo(position);
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return mediaPlayer.getAudioSessionId();
        }

    };

    /**
     * 根据位置打开对应的音频文件,并且播放
     *
     * @param position
     */
    private void openAudio(int position) {

        this.position = position;

        if (mediaItems != null && mediaItems.size() > 0) {

            mediaItem = mediaItems.get(position);

            if (mediaPlayer != null) {//切换视频时先释放
                // mediaPlayer.release();
                mediaPlayer.reset();
            }

            try {
                //设置监听
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());//播放准备
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());//播放成功
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());//播放出错
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();

                if (playMode == MusicPlayerService.REPEAT_SINGLE) {
                    //单曲循环播放，不会触发播放完成的回调
                    mediaPlayer.setLooping(true);
                } else {
                    //非单曲循环
                    mediaPlayer.setLooping(false);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(getApplication(), "还没数据", Toast.LENGTH_SHORT).show();

        }

    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            //通知activity来获取信息－－－广播
            notifyChange(OPENAUDIO);

            //发送消息过去
           // EventBus.getDefault().post(mediaItem);

            start();
        }
    }

    /**
     * 根据动作发广播
     *
     * @param action
     */
    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);

    }

    private class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    private class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(getApplication(), "播放出错", Toast.LENGTH_SHORT).show();
            return true;//出错播放下一个
        }
    }

    private NotificationManager manager;


    /**
     * 播放音乐
     */
    private void start() {
        mediaPlayer.start();

        /**
         * 当播放歌曲的时候，在状态栏显示正在播放，点击的时候，进入播放页面
         */
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("Notification", true);//标识来之状态栏
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("音乐")
                    .setContentText("正在播放" + getName())
                    .setContentIntent(pendingIntent)
                    .build();
        }
        manager.notify(1, notification);
    }

    /**
     * 停止播放
     */
    private void stop() {
        mediaPlayer.stop();
    }

    /**
     * 得到当前播放进度
     *
     * @return
     */
    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }


    /**
     * 得到当前音频的总时长
     *
     * @return
     */
    private int getDuration() {
        return mediaPlayer.getDuration();
    }

    /**
     * 得到艺术家
     *
     * @return
     */
    private String getArtist() {
        return mediaItem.getArtist();
    }

    /**
     * 得到歌曲名字
     *
     * @return
     */
    private String getName() {
        return mediaItem.getName();
    }

    /**
     * 得到歌曲播放路径
     *
     * @return
     */
    private String getAudioPath() {
        return mediaItem.getData();
    }

    /**
     * 下一个视频
     */
    private void next() {

        //1、根据当前的播放模式，设置下一个的位置
        setNextPosition();

        //2、根据当前的播放模式和下标位置去播放音频
        openNextAudio();

    }

    /**
     * 根据当前的播放模式和下标位置去播放音频
     */
    private void openNextAudio() {

        int playMode = getPlayMode();

        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            if (position < mediaItems.size()) {
                //正常范围
                openAudio(position);
            } else {
                position = mediaItems.size() - 1;
            }

        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            openAudio(position);

        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            openAudio(position);

        } else {
            if (position < mediaItems.size()) {
                //正常范围
                openAudio(position);
            } else {
                position = mediaItems.size() - 1;
            }
        }

    }

    /**
     * 1、根据当前的播放模式，设置下一个的位置
     */
    private void setNextPosition() {
        int playMode = getPlayMode();

        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            position++;

        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            position++;
            if (position >= mediaItems.size()) {
                position = 0;
            }

        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            position++;
            if (position >= mediaItems.size()) {
                position = 0;
            }

        } else {
            position++;
        }
    }

    /**
     * 上一个视频
     */
    private void pre() {
        //1、根据当前的播放模式，设置下一个的位置
        setPrePosition();

        //2、根据当前的播放模式和下标位置去播放音频
        openPreAudio();
    }

    private void openPreAudio() {

        int playMode = getPlayMode();

        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            if (position >= 0) {
                //正常范围
                openAudio(position);
            } else {
                position = 0;
            }

        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            openAudio(position);

        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            openAudio(position);

        } else {
            if (position >= 0) {
                //正常范围
                openAudio(position);
            } else {
                position = 0;
            }
        }

    }

    private void setPrePosition() {
        int playMode = getPlayMode();

        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            position--;

        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            position--;
            if (position < 0) {
                position = mediaItems.size() - 1;
            }

        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            position--;
            if (position < 0) {
                position = mediaItems.size() - 1;
            }

        } else {
            position--;
        }
    }

    /**
     * 设置播放模式
     *
     * @param playMode
     */
    private void setPlayMode(int playMode) {
        this.playMode = playMode;
        CacheUtils.putPlayMode(this, "playMode", playMode);//保存设置播放模式

        if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            //单曲循环播放，不会触发播放完成的回调
            mediaPlayer.setLooping(true);
        } else {
            //非单曲循环
            mediaPlayer.setLooping(false);
        }
    }

    /**
     * 得到播放模式
     *
     * @param
     */
    private int getPlayMode() {
        return playMode;
    }


    /**
     * 是否在播放音频
     *
     * @return
     */
    private boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * 暂停播放
     */
    private void pause() {
        mediaPlayer.pause();
        manager.cancel(1);
    }

    /**
     * 拖动音频
     */
    private void seekTO(int position) {

    }

}









