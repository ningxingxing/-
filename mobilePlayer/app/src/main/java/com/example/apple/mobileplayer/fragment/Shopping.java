package com.example.apple.mobileplayer.fragment;

import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.adapter.ShoppingAdapter;
import com.example.apple.mobileplayer.base.BaseFragment;
import com.example.apple.mobileplayer.domain.ShoppingItem;
import com.example.apple.mobileplayer.domain.ShoppingItemData;
import com.example.apple.mobileplayer.utils.CacheUtils;
import com.example.apple.mobileplayer.utils.Contants;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.apple.mobileplayer.utils.Contants.SHOPPING_URL;

/**
 * Created by apple on 17/3/16.
 */

public class Shopping extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private List<ShoppingItemData> datas;
    /**
     * 每页数据
     */
    private int pageSize = 10;

    /**
     * 当前页
     */
    private int curPage = 1;

    /**
     * 总页数
     */
    private int totalPage = 1;


    private String url;

    private final String TAG = "Shopping";

    private ShoppingAdapter adapter;

    //默认状态
    private static final int STATE_NORMAL = 1;
    //下拉刷新
    private static final int STATE_REFLSH = 2;
    //上拉加载更多
    private static final int STATE_MORE = 3;

    private int state = STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.shopping, null);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRefresh();

        setRequestParams();
        getDataFromNat();

    }

    private void initRefresh() {

        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            /**
             * 下拉刷新
             * @param materialRefreshLayout
             */
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {


                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        state = STATE_REFLSH;
                        curPage = 1;
                        url = SHOPPING_URL + pageSize + "&curPage=" + curPage;
                        getDataFromNat();
                        refresh.finishRefresh();

                    }
                }, 1000);
            }

            @Override
            public void onfinish() {
                super.onfinish();
            }

            /**
             * 加载更多
             * @param materialRefreshLayout
             */
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (curPage<totalPage){
                            state = STATE_MORE;
                            curPage++;
                            url = SHOPPING_URL + pageSize + "&curPage=" + curPage;
                            getDataFromNat();
                        }else {
                            Toast.makeText(getActivity(),"没有更多数据",Toast.LENGTH_SHORT).show();
                            refresh.finishRefreshLoadMore();
                        }

                    }
                }, 1000);


            }
        });
    }

    private void getDataFromNat() {

        String json =  CacheUtils.getString(getActivity(), Contants.SHOPPING_URL);
        if (!TextUtils.isEmpty(json)){
            processData(json);
        }
        //使用okhttp请求网络
        OkHttpUtils.get()
                .url(url)
                .id(100)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Toast.makeText(getActivity(),"请求失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("nsc","response="+response);

                        //缓存数据
                        CacheUtils.putString(getActivity(),url,response);
                        processData(response);


                    }
                });

    }

    /**
     * 解析数据和显示数据
     * @param json
     */
    private void processData(String json) {

        ShoppingItem item = parsedJson(json);
        datas = item.getList();
        curPage = item.getCurrentPage();
        //totalPage = item.getTotalPage();
        totalPage = 2;
        Log.e(TAG,"curPage="+curPage+" totalPage="+item.getTotalPage());

        showData();
        //设置适配器

        pbLoading.setVisibility(View.GONE);
    }

    /**
     *
     */
    private void showData() {

        switch (state){
            case STATE_NORMAL:
                //显示数据
                adapter = new ShoppingAdapter(getActivity(),datas);
                recyclerview.setAdapter(adapter);
                //布局管理器
                recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                break;

            case STATE_REFLSH://下拉刷新

                //1、清空数据
                datas.clear();
                adapter.notifyItemRangeChanged(0,datas.size());

                //2、添加数据
                adapter.addData(0,datas);
                //3、状态还原
                state = STATE_NORMAL;

                break;


            case STATE_MORE://上拉加载

                //1、把新数据添加到原来的数据的末尾 ＝刷新
                adapter.addData(adapter.getDataCount(),datas);

                //2、把状态还原
                refresh.finishRefreshLoadMore();

                break;
        }



    }

    /**
     * 解析数据
     * @param json
     * @return
     */
    private ShoppingItem parsedJson(String json) {
        return new Gson().fromJson(json,ShoppingItem.class);
    }

    /**
     * 请求网络
     */
    private void setRequestParams() {

        state = STATE_NORMAL;
        curPage = 1;

         url = SHOPPING_URL + pageSize + "&curPage=" + curPage;


    }


}
