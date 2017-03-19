// IMusicPlayerService.aidl
package com.example.apple.mobileplayer;

// Declare any non-default types here with import statements

interface IMusicPlayerService {
    /**
        * 根据位置打开对应的音频文件
        * @param position
        */
        void openAudio(int position);

       /**
        * 播放音乐
        */
        void start();

       /**
        * 停止播放
        */
        void stop();

       /**
        * 得到当前播放进度
        * @return
        */
        int getCurrentPosition();


       /**
        * 得到当前音频的总时长
        * @return
        */
        int getDuration();

       /**
        * 得到艺术家
        * @return
        */
        String getArtist();

       /**
        * 得到歌曲名字
        * @return
        */
        String getName();

       /**
        * 得到歌曲播放路径
        * @return
        */
        String getAudioPath();

       /**
        * 下一个视频
        */
        void next();

       /**
        * 上一个视频
        */
        void pre();

       /**
        * 设置播放模式
        * @param playMode
        */
        void setPlayMode(int playMode);

       /**
        * 得到播放模式
        * @param playMode
        */
        int getPlayMode();

        /*是否正在播放*/
        boolean isPlaying();

        /*
        暂停播放
        */
         void pause();

         /**
          * 拖动音频
          */
         void seekTO(int position);

         int getAudioSessionId();

}
