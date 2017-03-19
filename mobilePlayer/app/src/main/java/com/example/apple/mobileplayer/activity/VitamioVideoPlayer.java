package com.example.apple.mobileplayer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.domain.MediaItem;
import com.example.apple.mobileplayer.utils.Utils;
import com.example.apple.mobileplayer.view.VitamioVideoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;


/**
 * Created by apple on 2017/2/7.
 * <p>
 * 1、MediaPlayer 负责和底层打交道，底层解码，封装了很多方法start pause stop
 * <p>
 * 2、可以播放本地和网络视频
 * <p>
 * 3、支持格式 mp4 3gp m3u8
 * <p>
 * 2、videoView 用于显示视频，继承SurfaceView  实现MediaPlayerControl接口
 * <p>
 * 封装了mediaPlayer
 * <p>
 * 3、SurfaceView
 * <p>
 * <p>
 * seekbar拖动
 * <p>
 * 2、设置seekbar状态更新
 * <p>
 * 注册广播
 * <p>
 * 6、手势识别
 * <p>
 * 7、定义  实例化
 * <p>
 * 8、调声音 实例化AudioManager 当前音量 最大音量
 * <p>
 * 9、添加意图，被其他软件能调用播放器
 * <p>
 * 10、设置监听播放网络视频卡
 * <p>
 * 11、校验播放进度判断是否监听卡  当前播放进度－上一次播放进度
 * <p>
 * 12、vitamio的集成
 */
public class VitamioVideoPlayer extends Activity {


    /**
     * 网络速度
     */
    private static final int SHOW_SPEED = 4;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_battery)
    ImageView ivBattery;
    @BindView(R.id.tv_system_time)
    TextView tvSystemTime;
    @BindView(R.id.btn_voice)
    Button btnVoice;
    @BindView(R.id.seekbar_voice)
    SeekBar seekbarVoice;
    @BindView(R.id.btn_switch_player)
    Button btnSwitchPlayer;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.seekbar_video)
    SeekBar seekbarVideo;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.btn_exit)
    Button btnExit;
    @BindView(R.id.btn_video_pre)
    Button btnVideoPre;
    @BindView(R.id.btn_video_start_pause)
    Button btnVideoStartPause;
    @BindView(R.id.btn_video_next)
    Button btnVideoNext;
    @BindView(R.id.btn_video_siwch_screen)
    Button btnVideoSiwchScreen;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_buffer_netSpeed)
    TextView tvBufferNetSpeed;
    @BindView(R.id.tv_loading_netSpeed)
    TextView tvLoadingNetSpeed;
    @BindView(R.id.vitamio_videoView)
    VitamioVideoView vitamioVideoView;

    private RelativeLayout media_controller;
    private TextView tv_buffer_netSpeed;
    private LinearLayout ll_buffer;
    private LinearLayout ll_loading;
    private TextView tv_loading_netSpeed;

    private Uri uri;

    private final String TAG = "SystemVideoPlayer";

    //视频进度的更新
    private static final int PROGRESS = 1;

    private static final int HIDE_MEDIA_CONTROLLER = 2;

    private Utils utils;

    /**
     * 传进来的视频列表
     */
    private ArrayList<MediaItem> mediaItems;

    /**
     * 列表中播放的位置
     */
    private int position;

    /**
     * 监听电量变化广播
     */
    private MyReceiver myReceiver;

    /**
     * 定义手势识别器
     */
    private GestureDetector detector;

    /**
     * 是否显示控制面板
     */
    private boolean isShowMediaController = false;

    /**
     * 是否全屏
     */
    private boolean isFullScreen = false;

    /**
     * 全屏
     */
    private static final int FULL_SCREEN = 3;
    /**
     * 默认屏幕
     */
    private static final int DEFAULT_FULL_SCREEN = 4;

    /**
     * 屏幕宽高
     */
    private int screenWidth = 0;
    private int screenHeight = 0;

    /**
     * 视频真实宽高
     */
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    /**
     * 调节声音
     */
    private AudioManager am;
    /**
     * 当前声音
     */
    private int currentVolue;

    /**
     * 最大音量  0～15
     */
    private int maxVoice;

    /**
     * 是否是静音
     */
    private boolean isMute = false;

    private float startY;

    //屏幕的高
    private float touchRang;

    private int mVol;

    /**
     * 是否是网络uri
     */
    private boolean isNetUri;

    //是否系统
    private boolean isUseSystem = true;

    /**
     * 上一次播放进度
     */
    private int preCurrentPosition;

    //广播中处理播放进度时间显示等
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case PROGRESS:
                    //得到当前的视频的播放进程
                    int currentPosition = (int) vitamioVideoView.getCurrentPosition();

                    //seekbar.setProgress(当前进度)
                    seekbarVideo.setProgress(currentPosition);

                    //更新播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    //设置系统时间
                    tvSystemTime.setText(utils.getSystemTime());

                    //缓存进度的更新
                    if (isNetUri) {
                        //网络地址
                        int buffer = vitamioVideoView.getBufferPercentage();//0~100
                        int totalBuffer = buffer * seekbarVideo.getMax();
                        int secondaryProgress = totalBuffer / 100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    } else {
                        //本地地址
                        seekbarVideo.setSecondaryProgress(0);
                    }

                    //监听卡
                    if (!isUseSystem && vitamioVideoView.isPlaying()) {
                        if (vitamioVideoView.isPlaying()) {
                            int buffer = currentPosition - preCurrentPosition;
                            if (buffer < 500) {
                                //视频卡了
                                ll_buffer.setVisibility(View.VISIBLE);

                            } else {
                                //视频不卡了
                                ll_buffer.setVisibility(View.GONE);
                            }
                        } else {
                            ll_buffer.setVisibility(View.GONE);
                        }

                    }
                    preCurrentPosition = currentPosition;


                    //每秒更新一次
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;

                case HIDE_MEDIA_CONTROLLER://隐藏控制面板

                    hideMediaController();

                    break;

                case SHOW_SPEED://显示网速
                    //得到网速

                    String netSpeed = utils.showNetSpeed(getApplication());

                    tv_loading_netSpeed.setText("加载中" + netSpeed);
                    tv_buffer_netSpeed.setText("缓存中" + netSpeed);
                    //两秒一次
                    handler.removeMessages(SHOW_SPEED);
                    handler.sendEmptyMessageDelayed(SHOW_SPEED, 2000);

                    break;

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//初始化父类

        Vitamio.isInitialized(this);

        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activity_vitamio_video_player);
        ButterKnife.bind(this);
        Log.e(TAG, "VitamioVideoPlayer－－－－onCreate");

        initView();
        getData();
        initData();
        setData();

        //准备好的监听
        vitamioVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //加载页面消掉
                ll_loading.setVisibility(View.GONE);

                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();

                vitamioVideoView.start();//开始播放
                // mp.getDuration();
                int duration = (int) vitamioVideoView.getDuration();//得到总时长
                seekbarVideo.setMax(duration);
                tvDuration.setText(utils.stringForTime(duration));//显示总时长

                hideMediaController();//隐藏控制面板

                //发消息
                handler.sendEmptyMessage(PROGRESS);

                //设置视频宽高
                setVideoType(DEFAULT_FULL_SCREEN);

                // updateVoice(currentVolue,isMute);

//            mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
//                @Override
//                public void onSeekComplete(MediaPlayer mp) {
//                    Toast.makeText(getApplication(),"拖动完成",Toast.LENGTH_SHORT).show();
//                }
//            });


            }
        });

        //播放出错监听
        vitamioVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                Toast.makeText(getApplication(), "播放出错！！！", Toast.LENGTH_SHORT).show();
                //1、播放的视频格式不支持－－跳转到万能播放器继续播放
                showErrorDialog();

                //2、播放网络视频的时候，网络中断 1、如果网络确实断了，可以提示用户网络断了 2、网络断断续续 3、重新播放


                //3、播放的时候本地视频有空白 －－下载完成

                return true;
            }
        });


        //播放完成监听
        vitamioVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Toast.makeText(getApplication(), "播放完成=" + uri, Toast.LENGTH_SHORT).show();
                playNextVideo();
            }
        });


        //设置控制面板
        // videoView.setMediaController(new MediaController(this));


        //视频拖动监听
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 当手指滑动的时候，引起seekBar进度变化
             * @param seekBar
             * @param progress
             * @param fromUser 如果是用户引起的是true 不是用户引起的是false
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    vitamioVideoView.seekTo(progress);
                }
            }

            /**
             * 当手指触碰的时候回调这个方法
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                handler.removeMessages(HIDE_MEDIA_CONTROLLER);

            }

            /**
             * 当手指离开的时候
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
            }
        });

        /**
         * 声音进度条
         */
        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    //更新音量
                    if (progress > 0) {
                        isMute = false;
                    } else {
                        isMute = true;
                    }
                    updateVoice(progress, isMute);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (isUseSystem) {
            //监听视频播放卡,系统api
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                vitamioVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START://视频卡顿了，拖动卡

                                //Toast.makeText(getApplication(),"卡了",Toast.LENGTH_SHORT).show();

                                ll_buffer.setVisibility(View.VISIBLE);

                                break;

                            case MediaPlayer.MEDIA_INFO_BUFFERING_END://视频卡结束了，拖动卡结束
                                ll_buffer.setVisibility(View.GONE);
                                // Toast.makeText(getApplication(),"卡结束了",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
            }
        }

    }

    /**
     * 播放出错弹的提示dialog
     */
    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("提示");
        builder.setMessage("抱歉，无法播放该视频");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    /**
     * 设置音量大小
     *
     * @param progress 进度条值
     * @param isMute   是否静音
     */
    private void updateVoice(int progress, boolean isMute) {
        if (isMute) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);//1调系统
            seekbarVoice.setProgress(0);

        } else {

            am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);//1调系统
            seekbarVoice.setProgress(progress);
            currentVolue = progress;
        }
    }

    /**
     * //准备好的监听
     */
//    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
//
//        //当底层解码准备好的时候
//        @Override
//        public void onPrepared(MediaPlayer mp) {
//
//            mVideoWidth = mp.getVideoWidth();
//            mVideoHeight = mp.getVideoHeight();
//
//            videoView.start();//开始播放
//            // mp.getDuration();
//            int duration = (int) videoView.getDuration();//得到总时长
//            seekbarVideo.setMax(duration);
//            tvDuration.setText(utils.stringForTime(duration));//显示总时长
//
//            hideMediaController();//隐藏控制面板
//
//            //发消息
//            handler.sendEmptyMessage(PROGRESS);
//
//            //设置视频宽高
//            setVideoType(DEFAULT_FULL_SCREEN);
//
//            // updateVoice(currentVolue,isMute);
//
////            mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
////                @Override
////                public void onSeekComplete(MediaPlayer mp) {
////                    Toast.makeText(getApplication(),"拖动完成",Toast.LENGTH_SHORT).show();
////                }
////            });
//
//            //加载页面消掉
//            ll_loading.setVisibility(View.GONE);
//
//        }
//    }

    /**
     * 初始化ui
     */
    private void initView() {


        media_controller = (RelativeLayout) findViewById(R.id.media_controller);
        tv_buffer_netSpeed = (TextView) findViewById(R.id.tv_buffer_netSpeed);
        ll_buffer = (LinearLayout) findViewById(R.id.ll_buffer);

        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        ll_loading.setVisibility(View.GONE);
        tv_loading_netSpeed = (TextView) findViewById(R.id.tv_loading_netSpeed);

        //开始更新网络速度
        handler.sendEmptyMessage(SHOW_SPEED);
    }

    /**
     * 设置数据
     */
    private void setData() {

        if (mediaItems != null && mediaItems.size() > 0) {

            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());//显示视频的名字
            vitamioVideoView.setVideoPath(mediaItem.getData());//设置地址

            isNetUri = utils.isNetUri(mediaItem.getData());

        } else if (uri != null) {
            tvName.setText(uri.toString());//设置视频的名称
            isNetUri = utils.isNetUri(uri.toString());
            vitamioVideoView.setVideoURI(uri);//设置播放地址

        } else {
            Toast.makeText(VitamioVideoPlayer.this, "没有数据传递过来", Toast.LENGTH_SHORT).show();
        }

        setButtonState();

    }

    /**
     * 获取数据
     */
    private void getData() {
        //得到播放地址，视频播放器会被调起并且播放
        uri = getIntent().getData();//文件夹，图片浏览器，qq空间

        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");

        position = getIntent().getIntExtra("position", 0);

        if (uri != null) {

            vitamioVideoView.setVideoURI(uri);
        }
    }

    /**
     * 初始化数据方法
     */
    private void initData() {

        utils = new Utils();

        //注册电量广播
        myReceiver = new MyReceiver();
        IntentFilter intentFiler = new IntentFilter();
        //当电量变化的时候发送广播
        intentFiler.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver, intentFiler);

        /**
         * 2、实例化手势识别器，并且重写双击，点击和长按
         */
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 长按
             * @param e
             */
            @Override
            public void onLongPress(MotionEvent e) {

                //  Toast.makeText(getApplication(),"onLongPress",Toast.LENGTH_SHORT).show();

                startAndPause();
                super.onLongPress(e);
            }

            /**
             * 双击
             * @param e
             * @return
             */
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                setFullScreenAndDefault();

                // vitamioVideoView.setVideoSize(500, 500);
                return super.onDoubleTap(e);
            }

            /**
             * 单击
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                if (isShowMediaController) {
                    hideMediaController();//隐藏控制面板

                    //把隐藏消息移除
                    handler.removeMessages(HIDE_MEDIA_CONTROLLER);

                } else {
                    showMediaController();//显示控制面板
                    //发消息隐藏
                    handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
                }

                //Toast.makeText(getApplication(),"onSingleTapConfirmed",Toast.LENGTH_SHORT).show();
                return super.onSingleTapConfirmed(e);
            }
        });

//        //得到屏幕宽高
//        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//        screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕宽高像素
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        //得到音量,设置音量
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolue = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //最大音量
        seekbarVoice.setMax(maxVoice);
        //设置音量
        seekbarVoice.setProgress(currentVolue);
    }

    /**
     * 全屏与默认切换
     */
    private void setFullScreenAndDefault() {

        if (isFullScreen) {
            //默认
            setVideoType(DEFAULT_FULL_SCREEN);
        } else {
            //全屏
            setVideoType(FULL_SCREEN);
        }

    }

    private void setVideoType(int defaultFullScreen) {

        switch (defaultFullScreen) {

            case FULL_SCREEN://全屏
                //1、设置视频画面大小 屏幕大小
                vitamioVideoView.setVideoSize(screenWidth, screenHeight);

                //2、设置按钮状态
                btnVideoSiwchScreen.setBackgroundResource(R.drawable.btn_video_siwch_screen_default_selector);

                //3、全屏
                isFullScreen = true;

                break;

            case DEFAULT_FULL_SCREEN://默认

                //1、设置视频画面大小

                //屏幕宽高
                int height = screenHeight;
                int width = screenWidth;

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }

                vitamioVideoView.setVideoSize(width, height);

                //2、设置按钮状态
                btnVideoSiwchScreen.setBackgroundResource(R.drawable.btn_video_siwch_screen_selector);
                isFullScreen = false;
                break;
        }
    }

    /**
     * 电量变化广播
     */
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);//电量 0～100
            setBattery(level);

        }
    }

    /**
     * 电量图标显示
     *
     * @param level 电量
     */
    public void setBattery(int level) {
        if (level <= 0) {//电量为0
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    @OnClick({R.id.btn_voice, R.id.btn_switch_player, R.id.btn_exit, R.id.btn_video_pre, R.id.btn_video_start_pause, R.id.btn_video_next, R.id.btn_video_siwch_screen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_voice:

                isMute = !isMute;
                updateVoice(currentVolue, isMute);

                break;
            case R.id.btn_switch_player:
                showSiwchPlayerDialog();
                break;

            case R.id.btn_exit:
                finish();

                break;

            case R.id.btn_video_pre:
                playPreVideo();

                break;

            case R.id.btn_video_start_pause://播放暂停按钮

                Log.e(TAG, "播放状态：" + vitamioVideoView.isPlaying());

                startAndPause();

                break;

            case R.id.btn_video_next:
                playNextVideo();

                break;
            case R.id.btn_video_siwch_screen:

                setFullScreenAndDefault();//设置全屏与默认

                break;
        }
        //防止点击的时候移除了
        handler.removeMessages(HIDE_MEDIA_CONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
    }

    private void showSiwchPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("万能播放器提醒");
        builder.setMessage("当您播放一个视频，有花屏时，可以尝试切换到系统播放器");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSystemPlayer();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 把数据原来的样子传入VitamioVideoPlayer播放器
     * <p>
     * 关闭播放器
     */
    private void startSystemPlayer() {

        if (vitamioVideoView != null) {//停止释放播放器
            vitamioVideoView.stopPlayback();
        }

        Intent intent = new Intent(this, SystemVideoPlayer.class);

        if (mediaItems != null && mediaItems.size() > 0) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);

        } else if (uri != null) {//地址的时候
            intent.setData(uri);
        }
        startActivity(intent);

        finish();//关闭页面
    }

    private void startAndPause() {

        if (vitamioVideoView.isPlaying()) {
            //视频播放－设置暂停
            vitamioVideoView.pause();
            //按钮状态设置播放
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector2);

        } else {
            //视频播放
            vitamioVideoView.start();
            //按钮暂停
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    /**
     * 播放上一个视频
     */
    private void playPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //播放下一个
            position--;
            if (position >= 0) {

                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
                vitamioVideoView.setVideoPath(mediaItem.getData());

                //设置按钮状态
                setButtonState();

            }

        } else if (uri != null) {
            //上一个和下一个按钮设置不可操作 //设置按钮状态
            setButtonState();

        }
    }

    /**
     * 播放下一个
     */
    private void playNextVideo() {

        if (mediaItems != null && mediaItems.size() > 0) {
            //播放下一个
            position++;
            if (position < mediaItems.size()) {

                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());//设置视频名称
                isNetUri = utils.isNetUri(mediaItem.getData());//判断是否是网络视频
                vitamioVideoView.setVideoPath(mediaItem.getData());

                //设置按钮状态
                setButtonState();

            }

        } else if (uri != null) {
            //上一个和下一个按钮设置不可操作

            //设置按钮状态
            setButtonState();

        }

    }

    private void setButtonState() {
        if (mediaItems != null && mediaItems.size() > 0) {

            if (mediaItems.size() == 1) {
                //两个按钮设置灰色
                setEnableFalse();
            } else if (mediaItems.size() == 2) {

                if (position == 0) {
                    //两个按钮设置灰色
                    btnVideoPre.setBackgroundResource(R.drawable.video_pre_gray);
                    btnVideoPre.setEnabled(false);

                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);

                } else if (position == 1) {
                    //下一个按钮不可操作
                    btnVideoNext.setBackgroundResource(R.drawable.video_next_btn_bg);
                    btnVideoNext.setEnabled(false);

                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);

                }

            } else {

                if (position == 0) {
                    //两个按钮设置灰色
                    btnVideoPre.setBackgroundResource(R.drawable.video_pre_gray);
                    btnVideoPre.setEnabled(false);

                } else if (position == mediaItems.size() - 1) {
                    //下一个按钮不可操作
                    btnVideoNext.setBackgroundResource(R.drawable.video_next_btn_bg);
                    btnVideoNext.setEnabled(false);
                } else {
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);

                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);
                }
            }

        } else if (uri != null) {
            //两个按钮设置灰色
            setEnableFalse();
        }
    }

    /**
     * //两个按钮设置灰色
     */
    private void setEnableFalse() {
        btnVideoPre.setBackgroundResource(R.drawable.video_pre_gray);
        btnVideoPre.setEnabled(false);
        btnVideoNext.setBackgroundResource(R.drawable.video_next_btn_bg);
        btnVideoNext.setEnabled(false);
    }


    @Override
    protected void onStart() {
        Log.e(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        //移除所有消息
        handler.removeCallbacksAndMessages(null);

        //释放资源的时候，先释放子类，再释放父类
        Log.e(TAG, "onDestroy");
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //3、把事件传递给手势识别器
        detector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下
                //1、按下去
                startY = event.getY();
                mVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//按下获取音量
                touchRang = Math.min(screenHeight, screenWidth);//获取屏幕最小值即高度
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);

                break;

            case MotionEvent.ACTION_MOVE:

                //移动的纪录相关值
                float endY = event.getY();
                float distanceY = endY - startY;

                //改变的音量 ＝ （滑动屏幕的距离：总距离）＊ 音量最大值
                float delta = (distanceY / touchRang) * maxVoice;

                //最终的音量 ＝ 原来的 ＋ 改变的
                int voice = (int) Math.min(Math.max((mVol + delta), 0), maxVoice);

                if (delta != 0) {
                    isMute = false;//设置非静音
                    updateVoice(voice, false);
                }

                break;

            case MotionEvent.ACTION_UP:

                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
                break;
        }

        return super.onTouchEvent(event);
    }


    /**
     * 显示控制面板
     */
    private void showMediaController() {
        media_controller.setVisibility(View.VISIBLE);
        isShowMediaController = true;
    }

    /**
     * 隐藏控制面板
     */
    private void hideMediaController() {
        media_controller.setVisibility(View.GONE);
        isShowMediaController = false;
    }

    /**
     * 物理按键控制
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

            currentVolue--;
            updateVoice(currentVolue, false);
            handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            currentVolue++;
            updateVoice(currentVolue, false);
            handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
