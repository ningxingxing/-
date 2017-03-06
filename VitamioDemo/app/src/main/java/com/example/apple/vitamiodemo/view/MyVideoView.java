package com.example.apple.vitamiodemo.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.vov.vitamio.widget.VideoView;

/**
 * Created by apple on 17/3/6.
 */

public class MyVideoView extends VideoView{


    /**
     * 在代码中创建的时候一般用这个方法
     * @param context
     */
    public MyVideoView(Context context) {
        super(context,null);
    }

    /**
     * 当这个类在布局文件的时候，系统通过该构造方法实例化该类
     * @param context
     * @param attrs
     */
    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    /**
     * 当需要设置样式的时候调用该方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置视频宽高
     * @param videoWidth
     * @param videoHeight
     */
    public void setVideoSize(int videoWidth,int videoHeight){
        ViewGroup.LayoutParams params = getLayoutParams();

        params.width = videoWidth;
        params.height = videoHeight;

        setLayoutParams(params);
        //requestLayout();
    }
}
