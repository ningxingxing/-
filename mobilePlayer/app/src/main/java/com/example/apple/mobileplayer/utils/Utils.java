package com.example.apple.mobileplayer.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import static android.net.TrafficStats.getTotalRxBytes;

/**
 * Created by apple on 2017/2/7.
 */

public class Utils {
    private StringBuilder mFormatBuilder;
    private Formatter mformatter;

    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    public Utils() {
        //转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mformatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    /**
     * 把毫秒转换成：1：20：30格式
     * @param timeMs
     * @return
     */
    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours>0){
            return mformatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        }else {
            return mformatter.format("%02d:%02d",minutes,seconds).toString();
        }
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
     * 判断是否是网络资源
     * @param uri
     * @return
     */
    public boolean isNetUri(String uri){
        boolean result = false;

        if (uri!=null){
            if (uri.toLowerCase().startsWith("http")||uri.toLowerCase().startsWith("rtsp")||uri.toLowerCase().startsWith("mms")) {
                result = true;
            }
        }

        return result;
    }

    /**
     * 得到网速
     * @param context
     * @return
     */
    public String showNetSpeed(Context context) {
        String netSpeed = "0kb/s";

        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid)==TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);//转为KB;
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;


        netSpeed = String.valueOf(speed) + " kb/s";

      return netSpeed;
    }



    public boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
    public String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;

            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);

            }
            Log.e("nsc","device_id="+device_id);
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
