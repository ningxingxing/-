package com.example.apple.mobileplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.activity.SystemVideoPlayer;
import com.example.apple.mobileplayer.activity.VitamioVideoPlayer;
import com.example.apple.mobileplayer.adapter.NetVideoPagerAdapter;
import com.example.apple.mobileplayer.adapter.VideoPagerAdapter;
import com.example.apple.mobileplayer.base.BaseFragment;
import com.example.apple.mobileplayer.domain.MediaItem;
import com.example.apple.mobileplayer.domain.NetAudioPagerData;
import com.example.apple.mobileplayer.utils.CacheUtils;
import com.example.apple.mobileplayer.utils.Contants;
import com.example.apple.mobileplayer.view.XListView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by apple on 2017/2/6.
 */

public class NetVideoPager extends BaseFragment {
   // private boolean isInitData = false;

    @ViewInject(R.id.listView)
    private XListView mListView;

    @ViewInject(R.id.tv_netNomedia)
    private TextView tv_netNomedia;

    @ViewInject(R.id.pb_loading)
    private ProgressBar mProgressBar;

    /**
     * 装数据集合
     */
    private ArrayList<MediaItem> mediaItems;

    private NetVideoPagerAdapter adapter;

    /**
     * 是否已经加载更多
     */
    private boolean isLoadMore = false;
    private CacheUtils cacheuils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = View.inflate(getActivity(), R.layout.netvideo_pager, null);

        //第一个参数是：NetVideoPager.this,第二个参数：布局
        x.view().inject(NetVideoPager.this, view);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), SystemVideoPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videolist",mediaItems);
                intent.putExtra("position",(position-1));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(new MyIXListViewListener());
        return view;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cacheuils = new CacheUtils();
        initData();

    }

    /**
     * 获取网络数据 x.Util
     */
    public void getData() {
        //联网、视频内容
        RequestParams params = new RequestParams(Contants.NET_URL);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                //缓存数据
                cacheuils.putString(getActivity(),Contants.NET_URL,result);

                Log.e("TAG", "onSuccess＝" + result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "onError=" + ex.getMessage());
                tv_netNomedia.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                showData();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled");

            }

            @Override
            public void onFinished() {
                Log.e("TAG", "onFinished");
            }
        });
    }


    class MyIXListViewListener implements XListView.IXListViewListener{

        @Override
        public void onRefresh() {
            getData();
        }

        @Override
        public void onLoadMore() {
            getMoreDataFromNet();

        }
    }

    /**
     * 加载更多数据
     */
    private void getMoreDataFromNet() {
        //联网、视频内容
        RequestParams params = new RequestParams(Contants.NET_URL);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                isLoadMore = true;
                Log.e("TAG", "onSuccess＝" + result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "onError=" + ex.getMessage());
                isLoadMore = false;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled");
                isLoadMore = false;
            }

            @Override
            public void onFinished() {
                Log.e("TAG", "onFinished");
                isLoadMore = false;
            }
        });
    }



    /**
     * 初始化数据
     */
    public void initData() {

        //没有网的时候显示缓存数据
        String saveJson = CacheUtils.getString(getActivity(),Contants.NET_URL);

        if (!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        getData();

    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("更新时间"+getSystemTime());
    }

    /**
     *  得到当前系统时间
     * @return
     */

    public String getSystemTime() {

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(new Date());
    }

    /**
     *
     * @param json
     */
    private void processData(String json) {

        //不是加载更多
        if (isLoadMore==false){
            mediaItems = parseJson(json);

            showData();

        }else {//加载更多

            isLoadMore = false;
            //要把更多数据添加到原来的集合中
          //  moreItems = parseJson(json);

            mediaItems.addAll(parseJson(json));

            //刷新适配器
            adapter.notifyDataSetChanged();

            onLoad();

        }


    }

    /**
     * 显示数据
     */
    private void showData() {
        //设置适配器
        if (mediaItems != null && mediaItems.size() > 0) {
            //有数据

            //设置适配器
            adapter = new NetVideoPagerAdapter(getActivity(),mediaItems);
            mListView.setAdapter(adapter);
            onLoad();//加载更多
            //文本隐藏
            tv_netNomedia.setVisibility(View.GONE);

        } else {
            //没有数据

            //文本显示
            tv_netNomedia.setVisibility(View.VISIBLE);
        }
        //隐藏progress
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 解析json数据
     * 1、用系统接口解析json数据
     * 2、使用第三方解析工具（GSON,fastjson)
     *
     * @param json
     * @return
     */
    private ArrayList<MediaItem> parseJson(String json) {

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");//key存在也不会蹦
            if (jsonArray != null && jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);

                    if (jsonObjectItem != null) {

                        MediaItem mediaItem = new MediaItem();

                        String movieName = jsonObjectItem.optString("movieName");//name
                        mediaItem.setName(movieName);

                        String videoTitle = jsonObjectItem.optString("videoTitle");//title
                        mediaItem.setDesc(videoTitle);

                        String coverImg = jsonObjectItem.optString("coverImg");//img
                        mediaItem.setImageUrl(coverImg);

                        String hightUrl = jsonObjectItem.optString("hightUrl");
                        mediaItem.setData(hightUrl);

                        //添加到集合中去
                        mediaItems.add(mediaItem);
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return mediaItems;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NetVideoPager"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NetVideoPager"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }
}
