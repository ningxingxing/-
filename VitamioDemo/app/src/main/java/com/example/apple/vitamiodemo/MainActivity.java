package com.example.apple.vitamiodemo;

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
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.vitamiodemo.data.MediaItem;
import com.example.apple.vitamiodemo.utils.Utils;
import com.example.apple.vitamiodemo.view.MyVideoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

public class MainActivity extends Activity {

    private final String TAG = "MainActivity";

    //视频进度的更新
    private static final int PROGRESS = 1;
    /**
     * 控制面板显隐
     */
    private static final int HIDE_MEDIA_CONTROLLER = 2;

    /**
     * 全屏
     */
    private static final int FULL_SCREEN = 3;
    /**
     * 默认屏幕
     */
    private static final int DEFAULT_FULL_SCREEN = 5;

    /**
     * 网络速度
     */
    private static final int SHOW_SPEED = 4;

    @BindView(R.id.mVideoView)
    MyVideoView mVideoView;
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

    private LinearLayout ll_loading;
    private RelativeLayout media_controller;
    private LinearLayout ll_buffer;
    /**
     * 视频真实宽高
     */
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    /**
     * 屏幕宽高
     */
    private int screenWidth = 0;
    private int screenHeight = 0;

    /**
     * 是否全屏
     */
    private boolean isFullScreen = false;

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
    private float startX;

    //屏幕的高
    private float touchRang;

    private int mVol;

    /**
     * 定义手势识别器
     */
    private GestureDetector detector;

    /**
     * 是否显示控制面板
     */
    private boolean isShowMediaController = false;

    /**
     * 传进来的视频列表
     */
    private ArrayList<MediaItem> mediaItems;

    private String path = "http://data.vod.itc.cn/?rb=1&prot=1&key=jbZhEJhlqlUN-Wj_HEI8BjaVqKNFvDrn&prod=flash&pt=1&new=/86/168/RUXzy059JIvGRrrXQvOkNE.mp4";


    private Uri uri;

    /**
     * 是否是网络uri
     */
    private boolean isNetUri;

    /**
     * 列表中播放的位置
     */
    private int position;

    private Utils utils;

    //是否系统
    private boolean isUseSystem = true;

    /**
     * 上一次播放进度
     */
    private int preCurrentPosition;

    /**
     * 监听电量变化广播
     */
    private MyReceiver myReceiver;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PROGRESS:
                    //得到当前的视频的播放进程
                    int currentPosition = (int) mVideoView.getCurrentPosition();

                    //seekbar.setProgress(当前进度)
                    seekbarVideo.setProgress(currentPosition);

                    //更新播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    //设置系统时间
                    tvSystemTime.setText(utils.getSystemTime());
                    seekbarVideo.setSecondaryProgress(0);
                    //缓存进度的更新
                    if (isNetUri) {
                        //网络地址
                        int buffer = mVideoView.getBufferPercentage();//0~100
                        int totalBuffer = buffer * seekbarVideo.getMax();
                        int secondaryProgress = totalBuffer / 100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    } else {
                        //本地地址
                        seekbarVideo.setSecondaryProgress(0);
                    }

                    //监听卡
                    if (!isUseSystem && mVideoView.isPlaying()) {
                        if (mVideoView.isPlaying()) {
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

                case SHOW_SPEED://显示网速
                    //得到网速

                    String netSpeed = utils.showNetSpeed(getApplication());

                    tvLoadingNetSpeed.setText("加载中" + netSpeed);
                    tvLoadingNetSpeed.setText("缓存中" + netSpeed);
                    //两秒一次
                    handler.removeMessages(SHOW_SPEED);
                    handler.sendEmptyMessageDelayed(SHOW_SPEED, 2000);

                    break;

                case HIDE_MEDIA_CONTROLLER://隐藏控制面板

                    hideMediaController();

                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);//vitamio播放器初始化

//        if (!LibsChecker.checkVitamioLibs(this))
//            return;
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initView();
        initData();
        getData();
        setData();


        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                //加载页面消掉
                ll_loading.setVisibility(View.GONE);
                // optional need Vitamio 4.0
                //   mediaPlayer.setPlaybackSpeed(1.0f);
                mVideoWidth = mediaPlayer.getVideoWidth();
                mVideoHeight = mediaPlayer.getVideoHeight();

                mVideoView.start();//开始播放

                long duration = mVideoView.getDuration();//得到总时长
                seekbarVideo.setMax((int) duration);//设置最大值
                tvDuration.setText(utils.stringForTime((int) duration));
                //设置视频宽高
                setVideoType(DEFAULT_FULL_SCREEN);

                //发消息
                handler.sendEmptyMessage(PROGRESS);
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                showErrorDialog();
                Toast.makeText(getApplication(), "播放出错！！！", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextVideo();
            }
        });

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
                    mVideoView.seekTo(progress);
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
                Log.e(TAG, "setOnSeekBarChangeListener progress=" + progress + "fromUser=" + fromUser);
                if (fromUser) {
                    //更新音量
                    if (progress > 0) {
                        isMute = false;
                    } else {
                        isMute = true;
                    }
                    Log.e(TAG, "updateVoice(progress, isMute);");
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
                mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
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

    @OnClick({R.id.btn_voice, R.id.btn_switch_player, R.id.btn_exit, R.id.btn_video_pre, R.id.btn_video_start_pause, R.id.btn_video_next, R.id.btn_video_siwch_screen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_voice:
                isMute = !isMute;
                updateVoice(currentVolue, isMute);
                break;

            case R.id.btn_switch_player:


                break;
            case R.id.btn_exit:
                finish();
                break;

            case R.id.btn_video_pre:
                playPreVideo();
                break;

            case R.id.btn_video_start_pause:
                startAndPause();
                break;

            case R.id.btn_video_next:
                playNextVideo();
                break;

            case R.id.btn_video_siwch_screen:
                setFullScreenAndDefault();//设置全屏与默认
                break;
        }
    }


    /**
     * 初始化view
     */
    private void initView() {

        utils = new Utils();

        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        ll_loading.setVisibility(View.GONE);

        media_controller = (RelativeLayout) findViewById(R.id.media_controller);

        ll_buffer = (LinearLayout) findViewById(R.id.ll_buffer);
        ll_buffer.setVisibility(View.GONE);
        //开始更新网络速度
        handler.sendEmptyMessage(SHOW_SPEED);
    }

    /**
     * 初始化数据
     */
    private void initData() {

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
                Log.e(TAG, "onLongPress");
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
                Log.e(TAG, "onDoubleTap");
                setFullScreenAndDefault();

                // videoView.setVideoSize(500, 500);
                return super.onDoubleTap(e);
            }

            /**
             * 单击
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.e(TAG, "onSingleTapConfirmed");
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
        Log.e(TAG, "maxVoice===" + maxVoice + " initData currentVolue＝" + currentVolue);
        //设置音量
        seekbarVoice.setProgress(currentVolue);
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

            mVideoView.setVideoURI(uri);//设置播放地址，开始播放
        }
    }

    /**
     * 设置数据
     */
    private void setData() {

        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());//显示视频的名字
            mVideoView.setVideoPath(mediaItem.getData());//设置地址

            isNetUri = utils.isNetUri(mediaItem.getData());
        } else if (uri != null) {
            tvName.setText(uri.toString());//设置视频的名称
            isNetUri = utils.isNetUri(uri.toString());
            mVideoView.setVideoURI(uri);//设置播放地址，以Uri的方式设置VideoView播放的视频源，可以是网络Uri或本地Uri。
        } else {
            Toast.makeText(this, "没有数据传递过来", Toast.LENGTH_SHORT).show();
        }
        setButtonState();
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
                mVideoView.setVideoPath(mediaItem.getData());

                //设置按钮状态
                setButtonState();

            }

        } else if (uri != null) {
            //上一个和下一个按钮设置不可操作 //设置按钮状态
            setButtonState();

        }
    }

    /**
     * 开始播放和暂停播放
     */
    private void startAndPause() {

        if (mVideoView.isPlaying()) {
            //视频播放－设置暂停
            mVideoView.pause();
            //按钮状态设置播放
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector2);
            Log.e(TAG,"pause");

        } else {
            //视频播放
            mVideoView.start();
            //按钮暂停
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
            Log.e(TAG,"start");
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
                mVideoView.setVideoPath(mediaItem.getData());

                //设置按钮状态
                setButtonState();

            }

        } else if (uri != null) {
            //上一个和下一个按钮设置不可操作

            //设置按钮状态
            setButtonState();

        }

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
                mVideoView.setVideoSize(screenWidth, screenHeight);

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

                mVideoView.setVideoSize(width, height);

                //2、设置按钮状态
                btnVideoSiwchScreen.setBackgroundResource(R.drawable.btn_video_siwch_screen_selector);
                isFullScreen = false;
                break;
        }
    }

    /**
     * 设置按钮状态
     */
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //移除所有消息
        handler.removeCallbacksAndMessages(null);

        if (myReceiver != null) {

            unregisterReceiver(myReceiver);
            myReceiver = null;

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //3、把事件传递给手势识别器
        detector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下
                //1、按下去
                startY = event.getY();
                startX = event.getX();
                mVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//按下获取音量
                touchRang = Math.min(screenHeight, screenWidth);//获取屏幕最小值即高度
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);

                break;

            case MotionEvent.ACTION_MOVE:

                //移动的纪录相关值
                float endY = event.getY();
                float endX = event.getX();
                float distanceY = endY - startY;

                if (endX<screenWidth/2){
                    //左边屏幕－－调节亮度
                    final double FLING_MIN_DISTANCE = 0.5;
                    final double FLING_MIN_VELOCITY = 0.5;
                    if (startY - endY > FLING_MIN_DISTANCE
                            && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        Log.e(TAG, "up");
                        setBrightness(20);
                    }
                    if (startY - endY < FLING_MIN_DISTANCE
                            && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        Log.e(TAG, "down");
                        setBrightness(-20);
                    }

                }else {
                    //右边－－调节声音

                    Log.e(TAG, "MotionEvent ACTION_MOVE distanceY=" + distanceY);
                    //改变的音量 ＝ （滑动屏幕的距离：总距离）＊ 音量最大值
                    float delta = (distanceY / touchRang) * maxVoice;

                    //最终的音量 ＝ 原来的 ＋ 改变的
                    int voice = (int) Math.min(Math.max((mVol + delta), 0), maxVoice);

                    //加个距离判断防止误触
                    if ((delta != 0 && distanceY > 5) || (delta != 0 && distanceY < -5)) {
                        isMute = false;//设置非静音
                        updateVoice(voice, false);
                        //Log.e(TAG,"MotionEvent voice"+voice);
                    }

                }


                break;

            case MotionEvent.ACTION_UP:

                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
                break;
        }

        return super.onTouchEvent(event);
    }

    /*
    *
    * 设置屏幕亮度 lp = 0 全暗 ，lp= -1,根据系统设置， lp = 1; 最亮
    */
    private Vibrator vibrator;
    public void setBrightness(float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // if (lp.screenBrightness <= 0.1) {
        // return;
        // }
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, -1);
        } else if (lp.screenBrightness < 0.2) {
            lp.screenBrightness = (float) 0.2;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, -1);
        }
        Log.e(TAG, "lp.screenBrightness= " + lp.screenBrightness);
        getWindow().setAttributes(lp);
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
        Log.e(TAG, "onKeyDown currentVolue = " + currentVolue);
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
        Log.e(TAG, "updateVoice currentVolue=" + currentVolue);
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
}
