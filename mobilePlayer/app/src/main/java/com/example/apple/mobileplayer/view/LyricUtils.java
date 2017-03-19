package com.example.apple.mobileplayer.view;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by apple on 17/2/17.
 * <p>
 * 解析歌词工具类
 */

public class LyricUtils {

    private ArrayList<Lyric> lyrics;

    /**
     * 得到解析好的歌词
     * @return
     */
    public ArrayList<Lyric> getLyrics() {
        return lyrics;
    }

    /**
     * 是否存在歌词
     */
    private boolean isExistsLyric = false;

    /**
     * 是否存在歌词
     * @return
     */
    public boolean isExistsLyric() {
        return isExistsLyric;
    }

    /**
     * 读取歌词文件
     *
     * @param file ／mnt/scard/kugou/lyrics/李宇春－和你一样.krc
     */
    public void readLyricUtils(File file) {

        if (file == null || !file.exists()) {
            //歌词文件不存在
            lyrics = null;

            isExistsLyric = false;


        } else {
            //歌词文件存在

            //1、解析歌词，一行的读取－解析
            lyrics = new ArrayList<>();
            isExistsLyric = true;
            BufferedReader reader = null;
            try {

                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), getCharset(file)));
               // reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ));
                String line = reader.readLine();
                Log.e("nsc","line1==="+line);
                while (line!=null) {
                    Log.e("nsc","line==="+line);
                    line = parsedLyric(line);//
                    //reader.readLine();//读取一行
                }

                reader.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


            //2、排序,根据时间排序
            Collections.sort(lyrics, new Comparator<Lyric>() {
                @Override
                public int compare(Lyric o1, Lyric o2) {

                    if (o1.getTimePoint()<o2.getTimePoint()){
                        return  -1;
                    }else if (o1.getTimePoint()>o2.getTimePoint()){
                        return 1;
                    }else {
                        return 0;
                    }
                }
            });


            //3、计算每句高亮显示时间
            for (int i = 0;i<lyrics.size();i++){

                Lyric oneLyric = lyrics.get(i);//第一句时间
                if (i+1<lyrics.size()){
                    Lyric twoLyric = lyrics.get(i+1);//第二句时间
                    oneLyric.setSleepTime(twoLyric.getTimePoint()-oneLyric.getTimePoint());//显示时间
                }
            }

        }
    }

    /**
     * 判断文件编码
     * @param file
     * @return
     */
    public static String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1]
                    == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1]
                    == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    //单独出现BF以下的，也算是GBK
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)// 双字节 (0xC0 - 0xDF)
                            // (0x80 -
                            // 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                        // 也有可能出错，但是几率较小
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
                System.out.println(loc + " " + Integer.toHexString(read));
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    /**
     * 解析一句歌词
     *
     * @param line［00:00:00］ ［00:00:00］［00:11:00］发生地
     * @return
     */
    private String parsedLyric(String line) {

        //第一次出现［的位置
        int pos1 = line.indexOf("[");//0 如果没有返回－1

        int pos2 = line.indexOf("]");//9,如果没有返回－1

        if (pos1 == 0 && pos2 != -1) {//肯定是有一句歌词

            /**
             * 装时间
             */
            long[] times = new long[getCountTag(line)];

            String strTime = line.substring(pos1 + 1, pos2);//00:00.00
            times[0] = strTime2LongTime(strTime);

            String content = line;
            int i = 1;
            while (pos1 == 0 && pos2 != -1) {
                //得到内容
                content = content.substring(pos2+1);//后面中括号开始切 [00:00.00][00:11.00]呵呵-->[00:11.00]呵呵--

                pos1 = content.indexOf("[");//0   -1
                pos2 = content.indexOf("]");//9   -1

                if (pos2!=-1) {

                    strTime = content.substring(pos1+1,pos2);//00:00.00-->00:11.00
                    times[i] = strTime2LongTime(strTime);//00:00.00转成毫秒

                    //times[i]=-1 说明已经切完了了，直接返回
                    if (times[i]==-1){
                        return "";
                    }

                    i++;
                }

            }

            Lyric lyric = new Lyric();
            //把时间数组和文本关联起来，并且加入到集合中
            for (int j = 0;j<times.length;j++){

                if (times[j] !=0){//有时间戳

                    lyric.setContent(content);//保存内容
                    lyric.setTimePoint(times[j]);//保存时间
                    //添加到集合中
                    lyrics.add(lyric);
                    lyric = new Lyric();

                }

            }

            return content;//呵呵

        }

        return "";
    }

    /**
     * 把string类型转成long
     *
     * @param strTime//00:00:00
     * @return
     */
    private long strTime2LongTime(String strTime) {

        long result = -1;

        try {
            //1、先切割00 00.00
            String[] s1 = strTime.split(":");

            //2、把00.00按照，切成00和 00
            String[] s2 = s1[1].split("\\.");

            //1、分
            long min = Long.parseLong(s1[0]);

            //2、秒
            long second = Long.parseLong(s2[0]);

            //3、毫秒
            long mil = Long.parseLong(s2[1]);

            result = min * 60 * 1000 + second * 1000 + mil;
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        }


        return result;
    }

    /**
     * 判断由多少句歌词
     *
     * @param line [00:00.00][00:00.00][00:11.00]呵呵
     * @return
     */
    private int getCountTag(String line) {
        int result = -1;

        String[] left = line.split("\\[");
        String[] right = line.split("\\]");

        if (left.length == 0 && right.length == 0) {
            result = 1;
        } else if (left.length > right.length) {
            result = left.length;
        } else {
            result = right.length;
        }

        return result;
    }

}
