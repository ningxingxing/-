package com.example.apple.mobileplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.adapter.NetAudioPagerAdapter;
import com.example.apple.mobileplayer.base.BaseFragment;
import com.example.apple.mobileplayer.domain.NetAudioPagerData;
import com.example.apple.mobileplayer.utils.CacheUtils;
import com.example.apple.mobileplayer.utils.Contants;
import com.example.apple.mobileplayer.view.XListView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by apple on 2017/2/6.
 */

public class NetAudioPager extends BaseFragment {


    private final String TAG = "NetAudioPager";
    @BindView(R.id.listView)
    XListView listView;
    @BindView(R.id.net_netnomedia)
    TextView netNetnomedia;
    @BindView(R.id.net_pb_loading)
    ProgressBar netPbLoading;

    /**
     * 页面数据
     */
    private List<NetAudioPagerData.ListBean> datas;

    private NetAudioPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.netaudio_pager, null);

        initView(view);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

    }

    private void initView(View view) {

        x.view().inject(this, view);

    }

    @Override
    public void initData() {
        getDataFromNet();

        String saveJson = CacheUtils.getString(getActivity(), Contants.ALL_RES_URL);

        if (!TextUtils.isEmpty(saveJson)) {
            //解析数据
            precessData(saveJson);//解析数据

        } else {

        }

    }

    /**
     * 获取网络数据
     */
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Contants.ALL_RES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //保存数据
                CacheUtils.putString(getActivity(), Contants.ALL_RES_URL, result);
                Log.e(TAG, "请求数据成功＝＝" + result);
                precessData(result);//解析数据
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "请求数据成功＝＝" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled＝＝" + cex.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 解析json数据和显示数据
     * 解析数据：GsonFormat
     *
     * @param result
     */
    private void precessData(String result) {

        //解析数据
        NetAudioPagerData data = parsedJson(result);
        // Log.e(TAG,"data ="+data.getList());
        datas = data.getList();

        if (datas != null && datas.size() > 0) {
            //有数据
            netNetnomedia.setVisibility(View.GONE);
            //设置适配器
            adapter = new NetAudioPagerAdapter(getActivity(), datas);
            listView.setAdapter(adapter);

        } else {
            //没有数据
            netNetnomedia.setText("没有数据");
            netNetnomedia.setVisibility(View.VISIBLE);
        }

        netPbLoading.setVisibility(View.GONE);
    }

    /**
     * gson解析数据
     *
     * @param result
     * @return
     */
    private NetAudioPagerData parsedJson(String result) {
        return new Gson().fromJson(result, NetAudioPagerData.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NetAudioPager"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NetAudioPager"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }
}
