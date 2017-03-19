package com.example.apple.mobileplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.apple.mobileplayer.service.MusicPlayerService;

/**
 * Created by apple on 17/2/15.
 */

public class CacheUtils {

    /**
     * 保存数据
     * @param context
     * @param key
     * @param values
     */
    public static void putString(Context context,String key,String values){

        SharedPreferences sharedPreferences = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,values).commit();

    }

    /**
     * 缓存的数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    /**
     * 保存播放模式
     * @param context
     * @param key
     * @param values
     */
    public static void putPlayMode(Context context,String key,int values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("playMode",Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key,values).commit();
    }

    /**
     * 获取播放模式
     * @param context
     * @param key
     * @return
     */
    public static int getPlayMode(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("playMode",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, MusicPlayerService.REPEAT_NORMAL);
    }

}
