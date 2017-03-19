package com.example.apple.mobileplayer.fragment;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.activity.SystemVideoPlayer;
import com.example.apple.mobileplayer.adapter.VideoPagerAdapter;
import com.example.apple.mobileplayer.base.BaseFragment;
import com.example.apple.mobileplayer.domain.MediaItem;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by apple on 2017/2/6.
 */

public class VideoPager extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.tv_nomedia)
    TextView tvNomedia;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private VideoPagerAdapter videoPagerAdapter;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

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
                videoPagerAdapter = new VideoPagerAdapter(getActivity(),mediaItems,true);
                listView.setAdapter(videoPagerAdapter);

                //文本隐藏
                tvNomedia.setVisibility(View.GONE);

            } else {
                //没有数据

                //文本显示
                tvNomedia.setVisibility(View.VISIBLE);
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
        //友盟统计
        MobclickAgent. startWithConfigure(new MobclickAgent.UMAnalyticsConfig(getActivity(),"58c0bbcd8f4a9d4fc5001ace",""));

        //设置listView的item的点击事件
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


       // if (isInitData == false) {
            Log.e("TAG", "VideoPager");

            initData();
      //  }
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);

           // CrashReport.testJavaCrash();
            //调起系统所有播放器－－隐式意图
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            startActivity(intent);

            //调用自己写的播放器，显示意图
//            Intent intent = new Intent(getActivity(),SystemVideoPlayer.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            startActivity(intent);

            //3、传递列表数据－对象－序列化
            Intent intent = new Intent(getActivity(),SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtra("position",position);
            intent.putExtras(bundle);
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

//                if (getActivity()!=null && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
//
//                }

                if (getActivity()!=null){
                    ContentResolver resolver = getActivity().getContentResolver();
                    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    String[] objs = {
                            MediaStore.Video.Media.DISPLAY_NAME,//视屏文件在sdcard的名称
                            MediaStore.Video.Media.DURATION,//视屏总时长
                            MediaStore.Video.Media.SIZE,//视屏的文件大小
                            MediaStore.Video.Media.DATA,//视屏的绝对地址
                            MediaStore.Video.Media.ARTIST,//歌曲的演唱者
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

                //Handler发消息
                handler.sendEmptyMessage(10);

            }
        }.start();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("VideoPager"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("VideoPager"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }
}
