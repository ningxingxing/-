package com.example.apple.vitamiodemo.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apple.vitamiodemo.MainActivity;
import com.example.apple.vitamiodemo.R;
import com.example.apple.vitamiodemo.adapter.VideoListAdapter;
import com.example.apple.vitamiodemo.data.MediaItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoList extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.tv_nomedia)
    TextView tvNomedia;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private VideoListAdapter videoPagerAdapter;

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
                videoPagerAdapter = new VideoListAdapter(getApplication(),mediaItems,true);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);

        initData();
        listView.setOnItemClickListener(new MyOnItemClickListener());

    }

    private void initData() {

        getDataFromLocal();
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);

            //调起系统所有播放器－－隐式意图
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            startActivity(intent);

            //调用自己写的播放器，显示意图
//            Intent intent = new Intent(getActivity(),SystemVideoPlayer.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            startActivity(intent);

            //3、传递列表数据－对象－序列化
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtra("position",position);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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

                ContentResolver resolver = getContentResolver();
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

                //Handler发消息
                handler.sendEmptyMessage(10);

            }
        }.start();
    }
}
