package com.example.apple.mobileplayer.utils;

import android.content.Context;

/**
 * Created by apple on 17/2/17.
 */

public class DensityUtil {

    /**
     * 根据手机的分辨率从dip的单位转成px
     *
     */
    public static int dip2px(Context context,float dpValue){

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue*scale+0.5f);
    }

    /**
     * 根据手机的分辨率从px转dp
     *
     */
    public static int px2dip(Context context,float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue/scale+0.5f);
    }
}
