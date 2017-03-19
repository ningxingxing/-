package com.example.apple.mobileplayer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by apple on 2017/2/6.
 *
 * 知识回顾

 1、启动、延时两秒进入主页面

 2、快速进入－－》点击进入（单例模式）

 3、实现主页面布局，

 4、自定义RadioButton -> style

 5、标题栏

 6、自定义类继承布局

 7、写一个公共的页面

 8、实现各个页面

 9、把页面初始化

 10、设置radioGroup的坚挺

 11、根据监听切换不同页面

 12、软件架构

 13、软件分析

 14、读取本地视屏，在6.0需要动态获取权限

 15、简单播放器

 16、把所有播放器掉起来

 17、mediaPlayer,和底层交互c活着c++

 18、videoView,封装MediaPlayer继承sur faceView

 19、视屏的播放完成的监听，播放出错、准备好

 20、自定义videoview视频全屏和默认

 21、手势识别器、双击单击、长按，4秒自动隐藏控制面板

 22、控制面板

 23、屏幕横竖屏切换生命周期的屏蔽

 24、activity生命周期

 25、activity和service交互aidl

 26、EventBus

 27、声音的调节

 28、亮度调节

 29、滑动屏幕改变声音和亮度

 30、声网直播

 31、两种实现播放器的方式

 32、视屏进度更新

 33、万能播放器和系统播放器切换

 34、万能解码框架：ffmpeg、VLC、vitmiao

 35、vitmiao集成

 36、网络视频的缓冲处理

 37、监听卡、认为监听卡

 38、拖动卡

 39、显示网络速度

 40、加载视频等待效果

 42、把本地电脑共享wifi热点、让手机链接播放电脑tomcat资源

 43、读取本地音乐

 44、播放音乐－－service

 45、activity和服务交互－－aide

 46、帧动画

 47、播放器歌曲

 48、上一曲、下一曲

 49、设置播放模式

 50、通过广播获取当前播放歌曲的时间和名称

 51、播放歌曲的进度更新

 52、歌词显示控件showLyricView,继承TextView

 53、设置假歌词

 54、绘制歌词

 55、解析歌词－－－一行一行读取排序、计算每句高亮显示时间

 56、歌词同步

 57、解决歌词乱码

 58、网络视频－－xUtils3请求网络

 59、使用系统api 解析json数据

 60、下拉上拉刷新xListView

 61、分类行的listview

 62、jcvideoPlayer-lib播放视频

 63、使用glide加载图片

 64、使用科大讯飞语音输入

 65、Gson解析和gsonFormat

 66、标题栏

 67、软件退出

 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
