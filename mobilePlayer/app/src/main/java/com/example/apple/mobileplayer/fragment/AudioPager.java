package com.example.apple.mobileplayer.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.activity.AudioPlayerActivity;
import com.example.apple.mobileplayer.adapter.VideoPagerAdapter;
import com.example.apple.mobileplayer.base.BaseFragment;
import com.example.apple.mobileplayer.domain.MediaItem;
import com.example.apple.mobileplayer.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by apple on 2017/2/6.
 */

public class AudioPager extends BaseFragment {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.tv_nomedia)
    TextView tvNomedia;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private VideoPagerAdapter videoPagerAdapter;

    private Utils utils;

    /**
     * 装数据集合
     */
    private ArrayList<MediaItem> mediaItems;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mediaItems != null && mediaItems.size() > 0) {
                //有数据

                //设置适配器
                videoPagerAdapter = new VideoPagerAdapter(getActivity(),mediaItems,false);
                listView.setAdapter(videoPagerAdapter);

                //文本隐藏
                tvNomedia.setVisibility(View.GONE);

            } else {
                //没有数据

                //文本显示
                tvNomedia.setVisibility(View.VISIBLE);
                tvNomedia.setText("没有发现音频");
            }
            //隐藏progress
            pbLoading.setVisibility(View.GONE);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.video_pager, null);

        ButterKnife.bind(this, view);
        utils = new Utils();
        MobclickAgent.setDebugMode( true );//使用集成测试服务（推荐）

        //设置listView的item的点击事件
        listView.setOnItemClickListener(new AudioPager.MyOnItemClickListener());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // if (isInitData == false) {
        Log.e("TAG", "VideoPager");
        utils.getDeviceInfo(getActivity());
        initData();
        //  }
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //3、传递列表数据－对象－序列化
            Intent intent = new Intent(getActivity(),AudioPlayerActivity.class);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        //加载本地视频数据
        getDataFromLocal();
        Log.e("TAG", "VideoPager1111");
    }

    /**
     * 从本地的sdcard得到数据
     * <p>
     * 1、遍历sdcard ，后缀名
     * <p>
     * 2、去数据库中获取，内容提供者
     * <p>
     * 3、如果是6.0系统需要动态读取sdcand权限
     */
    public void getDataFromLocal() {

        new Thread() {
            @Override
            public void run() {
                super.run();

                mediaItems = new ArrayList<>();

                ContentResolver resolver = getActivity().getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//视屏文件在sdcard的名称
                        MediaStore.Audio.Media.DURATION,//视屏总时长
                        MediaStore.Audio.Media.SIZE,//视屏的文件大小
                        MediaStore.Audio.Media.DATA,//视屏的绝对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者
                       // MediaStore.Audio.Media.
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

                //Handler发消息
                handler.sendEmptyMessage(10);

            }
        }.start();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AudioPager"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AudioPager"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }

}
