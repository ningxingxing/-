package com.example.apple.mobileplayer.activity;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.mobileplayer.IMusicPlayerService;
import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.base.BaseActivity;
import com.example.apple.mobileplayer.domain.MediaItem;
import com.example.apple.mobileplayer.service.MusicPlayerService;
import com.example.apple.mobileplayer.utils.Utils;
import com.example.apple.mobileplayer.view.BaseVisualizerView;
import com.example.apple.mobileplayer.view.LyricUtils;
import com.example.apple.mobileplayer.view.ShowLyricView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by apple on 17/2/16.
 * <p>
 * activity和服务交互的方式 eventBus,Aidl
 */
public class AudioPlayerActivity extends BaseActivity {

    private final String TAG = "AudioPlayerActivity";

    //进度更新
    private final int PROGRESS = 1;

    /**
     * 显示歌词
     */
    private static final int SHOW_LYRIC = 2;

    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_artist)
    TextView tvArtist;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.seekbar_audio)
    SeekBar seekbarAudio;
    @BindView(R.id.btn_audio_playmode)
    Button btnAudioPlaymode;
    @BindView(R.id.btn_audio_pre)
    Button btnAudioPre;
    @BindView(R.id.btn_audio_start_pause)
    Button btnAudioStartPause;
    @BindView(R.id.btn_audio_next)
    Button btnAudioNext;
    @BindView(R.id.btn_lyrc)
    Button btnLyrc;
    @BindView(R.id.showLyrcView)
    ShowLyricView showLyrcView;
    @BindView(R.id.btn_baseVisualizerView)
    BaseVisualizerView btnBaseVisualizerView;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
   // private BaseVisualizerView baseVisualizerView;

    private int position;

    /**
     * true ：从状态栏进入的，不需要重新播放
     * false 从列表进入的
     */
    private boolean notification;

    /**
     * 显示数据广播
     */
    private MyReceiver myReceiver;

    private Utils utils;

    private Visualizer mVisualizer;

    /**
     * 服务的代理类，通过它可以调用服务的方法
     */
    private IMusicPlayerService service;

    private ServiceConnection con = new ServiceConnection() {
        /**
         * 当连接成功的时候回调这个方法
         * @param name
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IMusicPlayerService.Stub.asInterface(iBinder);

            if (service != null) {
                try {

                    //列表
                    if (!notification) {
                        service.openAudio(position);//播放

                    } else {
                        //状态栏，需要重新显示数据
                        showViewData();
                        //  Log.e(TAG, "当前线程" + Thread.currentThread());
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 当断开连接的时候调用该方法
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if (service != null) {
                    service.stop();
                    service = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case SHOW_LYRIC://显示歌词


                    try {
                        //1、得到当前的进度
                        int currentPosition = service.getCurrentPosition();

                        //2、把进度传入Show_LyrcView控件，并计算该高亮哪一句
                        showLyrcView.setShowNextLyric(currentPosition);

                        //3、实时的发消息
                        handler.removeMessages(SHOW_LYRIC);
                        handler.sendEmptyMessage(SHOW_LYRIC);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                    break;

                case PROGRESS:
                    try {
                        //1、得到当前隐藏

                        int currentPosition = service.getCurrentPosition();

                        //2、设置seekBar.setProgress(进度)
                        seekbarAudio.setProgress(currentPosition);

                        //3、时间进度更新
                        tvTime.setText(utils.stringForTime(currentPosition) + "/" + utils.stringForTime(service.getDuration()));

                        //4、每秒更新一次
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audioplayer);
        ButterKnife.bind(this);

        utils = new Utils();
        initData();
        initView();
        getData();
        bindAndStartService();

    }

    /**
     * 初始化数据
     * 注册广播
     */
    private void initData() {
        //注册广播
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPENAUDIO);
        registerReceiver(myReceiver, intentFilter);

        //eventBus注册
        //EventBus.getDefault().register(this);

    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //   showLyric();
            showData(null);

        }
    }

    //订阅方法,不能私有
   // @Subscribe(threadMode = ThreadMode.MAIN, sticky = false, priority = 0)
    public void showData(MediaItem mediaItem) {

        setupVisualizerFxAndUi();
        showViewData();
        checkPlayMode();//校验播放状态
        handler.sendEmptyMessage(SHOW_LYRIC);

        //发消息，开始同步歌词
      //  showLyric();
       // handler.sendEmptyMessage(SHOW_LYRIC);
    }


    private void setupVisualizerFxAndUi() {

        try {
            int audioSessionId = service.getAudioSessionId();

            //mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
            mVisualizer = new Visualizer(audioSessionId);
            //参数必须是两位数
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

            //设置允许波形表示，并且捕获它
            btnBaseVisualizerView.setVisualizer(mVisualizer);
            mVisualizer.setEnabled(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示歌词
     */
    private void showLyric() {

        //解析歌词
        LyricUtils lyricUtils = new LyricUtils();

        try {
            String path = service.getAudioPath();//播放歌曲的路径
            //传递歌词文件
            //mnt/sdcard/audio/kugou/music.mp3
            //mnt/sdcard/audio/kugou/music.krc

            path = path.substring(0, path.lastIndexOf("."));
            File file = new File(path + ".krc");

          //  getLyricPath(path+"");

            if (!file.exists()) {
                file = new File(path + ".lrc");
            }
            if (!file.exists()) {
                file = new File(path + ".txt");
            }
            lyricUtils.readLyricUtils(file);//解析歌词

           //
             showLyrcView.setLyrics(lyricUtils.getLyrics());


        } catch (RemoteException e) {
            e.printStackTrace();
        }


        if (lyricUtils.isExistsLyric()) {
            handler.sendEmptyMessage(SHOW_LYRIC);
        }

    }

    /**
     * 显示数据
     */

    public void showViewData() {
        try {
            tvArtist.setText(service.getArtist());
            tvName.setText(service.getName());

            //设置进度条最大值
            seekbarAudio.setMax(service.getDuration());

            //发消息
            handler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定服务
     */
    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.nsc.mobileplayer_OPENAUDIO");
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        startService(intent);//不至于启动多个服务
    }

    private void initView() {
        showLyrcView.setVisibility(View.GONE);
        //baseVisualizerView = (BaseVisualizerView) findViewById(R.id.baseVisualizerView);

        //图片动画效果
        ivIcon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivIcon.getBackground();
        animationDrawable.start();

        /**
         * 设置视频拖动
         */
        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (fromUser) {
                //拖动进度
                try {
                    service.seekTO(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @OnClick({R.id.btn_audio_playmode, R.id.btn_audio_pre, R.id.btn_audio_start_pause, R.id.btn_audio_next, R.id.btn_lyrc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_audio_playmode:

                setPlayMode();

                break;
            case R.id.btn_audio_pre:
                if (service != null) {
                    try {
                        service.pre();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btn_audio_start_pause:
                if (service != null) {
                    try {
                        if (service.isPlaying()) {

                            //暂停
                            service.pause();
                            //按钮播放
                            btnAudioStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector2);

                        } else {
                            //播放
                            service.start();
                            //按钮－暂停
                            btnAudioStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);

                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btn_audio_next:
                if (service != null) {
                    try {
                        service.next();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btn_lyrc:

                break;
        }
    }

    /**
     * 设置播放模式
     */
    private void setPlayMode() {
        try {
            int playMode = service.getPlayMode();

            if (playMode == MusicPlayerService.REPEAT_NORMAL) {
                playMode = MusicPlayerService.REPEAT_SINGLE;
            } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
                playMode = MusicPlayerService.REPEAT_ALL;
            } else if (playMode == MusicPlayerService.REPEAT_ALL) {
                playMode = MusicPlayerService.REPEAT_NORMAL;
            } else {
                playMode = MusicPlayerService.REPEAT_NORMAL;
            }

            //保存
            service.setPlayMode(playMode);


            //设置图片
            showPlayMode();


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验播放模式 ，切换图片
     */
    private void checkPlayMode() {
        try {
            int playMode = service.getPlayMode();

            if (playMode == MusicPlayerService.REPEAT_NORMAL) {
                // btnAudioPlaymode.setBackgroundResource(R.drawable.);
                //  Toast.makeText(getApplication(),"顺序播放",Toast.LENGTH_SHORT).show();
            } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
                // Toast.makeText(getApplication(),"单曲循环",Toast.LENGTH_SHORT).show();
            } else if (playMode == MusicPlayerService.REPEAT_ALL) {
                //  Toast.makeText(getApplication(),"全部循环",Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(getApplication(),"顺序播放",Toast.LENGTH_SHORT).show();
            }

            //校验播放和暂停的按钮
            if (service.isPlaying()) {//btn_video_pause_selector
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
            } else {
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector2);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    /**
     * 播放模式切换的时候提示
     */
    private void showPlayMode() {
        try {
            int playMode = service.getPlayMode();

            if (playMode == MusicPlayerService.REPEAT_NORMAL) {
                // btnAudioPlaymode.setBackgroundResource(R.drawable.);
                Toast.makeText(getApplication(), "顺序播放", Toast.LENGTH_SHORT).show();
            } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
                Toast.makeText(getApplication(), "单曲循环", Toast.LENGTH_SHORT).show();
            } else if (playMode == MusicPlayerService.REPEAT_ALL) {
                Toast.makeText(getApplication(), "全部循环", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "顺序播放", Toast.LENGTH_SHORT).show();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取传递过来的数据
     */
    public void getData() {

        notification = getIntent().getBooleanExtra("Notification", false);

        if (!notification) {//列表进入
            position = getIntent().getIntExtra("position", 0);
        }
        //状态栏进入，不操作


    }

    @Override
    protected void onDestroy() {

        //移除所有消息
        handler.removeCallbacksAndMessages(null);

        //取消注册广播
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
            myReceiver = null;//会优先回收
        }

        //解绑服务
        if (con != null) {
            unbindService(con);
            con = null;
        }

        //2、eventBus取消注册
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mVisualizer != null) {
            mVisualizer.release();
        }
    }
}
