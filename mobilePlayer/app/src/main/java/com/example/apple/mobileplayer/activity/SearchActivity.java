package com.example.apple.mobileplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.adapter.SearchAdapter;
import com.example.apple.mobileplayer.base.BaseActivity;
import com.example.apple.mobileplayer.utils.Contants;
import com.example.apple.mobileplayer.utils.JsonParser;
import com.example.apple.mobileplayer.domain.SearchBean;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by apple on 17/2/19.
 */
public class SearchActivity extends BaseActivity {
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_noData)
    TextView tvNoData;

    private String url;

    private final String TAG = "SearchActivity";
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private List<SearchBean.ItemsBean> items;

    private SearchAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        //initView();
    }


    @OnClick({R.id.iv_voice, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_voice://语音输入
                showDialog();
                Toast.makeText(getApplication(), "语音输入", Toast.LENGTH_SHORT).show();


                break;

            case R.id.tv_search://搜索
              //  speechText();
                Toast.makeText(getApplication(), "搜索", Toast.LENGTH_SHORT).show();

                 searchText();
                break;
        }
    }

    private void searchText() {

        String text = etInput.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {

            if (items!=null && items.size()>0){
                items.clear();
            }

            try {

                text = URLEncoder.encode(text, "UTF-8");

                url = Contants.SEARCH_URL + text;

                getDataFromNet();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取网络数据
     */
    private void getDataFromNet() {
        progressBar.setVisibility(View.VISIBLE);

        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                processData(result);//解析
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void processData(String result) {
        SearchBean searchBean = parsedJson(result);
        items = searchBean.getItems();

        if (items != null && items.size()>0) {
            //设置适配器
            adapter = new SearchAdapter(this, items);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);

        }else {
            tvNoData.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);

    }

    /**
     * 解析json数据
     *
     * @param result
     * @return
     */
    private SearchBean parsedJson(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, SearchBean.class);
    }

    private void speechText() {
        //1.创建 SpeechSynthesizer 对象, 第二个参数:本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置,详见《MSC Reference Manual》SpeechSynthesizer 类
        // 设置发音人(更多在线发音人,用户可参见 附录13.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        // 设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");
        // 设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        // 设置音量,范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置云端
        // 设置合成音频保存位置(可自定义保存位置),保存在“./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限 //仅支持保存为 pcm 和 wav 格式,如果不需要保存合成音频,注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成
        mTts.startSpeaking(etInput.getText().toString(), mSynListener);

    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口,没有错误时,error为null
        public void onCompleted(SpeechError error) {
            Toast.makeText(getApplication(), "完成了", Toast.LENGTH_SHORT).show();

        }
        //缓冲进度回调 //percent为缓冲进度0~100,beginPos为缓冲音频在文本中开始位置,endPos表示缓冲音频在
        // 文本中结束位置,info为附加信息。

        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
            Toast.makeText(getApplication(), "开始了！", Toast.LENGTH_SHORT).show();
        }

        //暂停播放
        public void onSpeakPaused() {
        }
        //播放进度回调
        // percent为播放进度0~100,beginPos为播放音频在文本中开始位置,endPos表示播放音频在文本中结束位置.

        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        } //恢复播放回调接口

        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

    /**
     * 弹出录音
     */
    private void showDialog() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //若要将UI控件用于语义理解,必须添加以下参数设置,设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener()); //4.显示dialog,接收语音输入
        mDialog.show();
    }

    class MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(getApplication(), "初始化失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {
        /**
         * @param results
         * @param b       是否说话结束
         */
        @Override
        public void onResult(RecognizerResult results, boolean b) {
            Log.e(TAG, "result=" + results.getResultString());
            String text = JsonParser.parseIatResult(results.getResultString());
            Log.e(TAG, "text=" + text);
            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();//拼成一句
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }

            etInput.setText(resultBuffer.toString());
            etInput.setSelection(etInput.length());

        }

        /**
         * 出错了
         *
         * @param speechError
         */
        @Override
        public void onError(SpeechError speechError) {
            String error = speechError.getErrorDescription();
            Log.e(TAG, "error=" + error);
        }
    }
}
