package com.example.apple.mobileplayer.activity;

import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.fragment.AudioPager;
import com.example.apple.mobileplayer.fragment.NetAudioPager;
import com.example.apple.mobileplayer.fragment.NetVideoPager;
import com.example.apple.mobileplayer.fragment.Shopping;
import com.example.apple.mobileplayer.fragment.VideoPager;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity {


    @BindView(R.id.fl_main_content)
    FrameLayout flMainContent;
    @BindView(R.id.rb_video)
    RadioButton rbVideo;
    @BindView(R.id.rb_audio)
    RadioButton rbAudio;
    @BindView(R.id.rb_net_video)
    RadioButton rbNetVideo;
    @BindView(R.id.rb_net_audio)
    RadioButton rbNetAudio;
    @BindView(R.id.rg_button_tag)
    RadioGroup rgButtonTag;
    @BindView(R.id.rl_game)
    RelativeLayout rlGame;
    @BindView(R.id.iv_record)
    ImageView ivRecord;
    @BindView(R.id.rb_shopping)
    RadioButton rbShopping;

    private Fragment videoPager;
    private Fragment audioPager;
    private Fragment netAudioPager;
    private Fragment newVideoPager;
    private Fragment shopping;

    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        initView();
        initListener();

    }

    private void initView() {

        videoPager = new VideoPager();
        audioPager = new AudioPager();

        netAudioPager = new NetAudioPager();
        newVideoPager = new NetVideoPager();

        shopping = new Shopping();
    }

    private void initListener() {

        rgButtonTag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Fragment fragment = null;

                switch (checkedId) {

                    case R.id.rb_video:
                        position = 0;
                        fragment = videoPager;
                        break;

                    case R.id.rb_audio:
                        position = 1;
                        fragment = audioPager;
                        break;

                    case R.id.rb_net_video:
                        position = 2;
                        fragment = newVideoPager;
                        break;

                    case R.id.rb_net_audio:
                        position = 3;
                        fragment = netAudioPager;
                        break;

                    case R.id.rb_shopping:
                        position = 4;
                        fragment = shopping;

                        break;
                }
                switchFragment(fragment);
            }
        });
        rgButtonTag.check(R.id.rb_video);//初始化默认选中
    }


    private void switchFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main_content, fragment).commit();

    }

    /**
     * 是否已经退出
     */
    private boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (position != 0) {
                position = 0;
                rgButtonTag.check(R.id.rb_video);//初始化默认选中
                return true;
            } else if (!isExit) {
                isExit = true;
                Toast.makeText(getApplication(), "再按一次退出", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);

                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}




















