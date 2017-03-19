package com.example.apple.mobileplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.example.apple.mobileplayer.utils.DensityUtil;

import java.util.ArrayList;

/**
 * Created by apple on 17/2/17.
 */

public class ShowLyricView extends TextView {
    private final String TAG = "ShowLyricView";

    private Paint paint;

    private Paint whitePaint;


    private int width;
    private int height;

    /**
     * 歌词列表中的索引，第几句歌词
     */
    private int index;

    /**
     * 每行歌词的高
     */
    private float textHeight;


    /**
     * 当前播放进度
     */
    private float currentPosition;


    /**
     * 高亮显示的时间或者休眠时间
     */
    private float sleepTime;

    /**
     * 时间戳，什么时候到高亮那句歌词
     */
    private float tempPoint;

    /**
     * 歌词列表
     */
    private ArrayList<Lyric> lyrics;

    /**
     * 设置歌词列表
     *
     * @param lyrics
     */
    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
    }

    public ShowLyricView(Context context) {
        this(context, null);
    }

    public ShowLyricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowLyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView(Context context) {
        textHeight = DensityUtil.dip2px(context, 18);//对应的像素
        Log.e(TAG, "textHeight=" + textHeight);

        //创建画笔
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(20);
        paint.setAntiAlias(true);
        //设置居中
        paint.setTextAlign(Paint.Align.CENTER);


        //创建画笔
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(20);
        whitePaint.setAntiAlias(true);
        //设置居中
        whitePaint.setTextAlign(Paint.Align.CENTER);


        lyrics = new ArrayList<>();
        Lyric lyric = new Lyric();

        for (int i = 0; i < 1000; i++) {

            lyric.setTimePoint(1000 * i);
            lyric.setSleepTime(1500 + i);
            lyric.setContent(i + "" + i);

            //把歌词添加到集合中
            lyrics.add(lyric);

            //相当于把Lyric lyric = new Lyric();写在for内部一样
            lyric = new Lyric();
        }
    }

    /**
     * 绘制歌词
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (lyrics != null && lyrics.size() > 0) {

            //往上推移
            float plush = 0;


            if (sleepTime == 0) {
                plush = 0;
            } else {
                //平移

                //这句花的时间：休眠时间（显示时间）＝移动距离：总距离（行高）

                //移动距离 = (这句所花时间：休眠时间（显示时间）＊总距离)
                //float delta = ((currentPosition-tempPoint)/sleepTime)*textHeight;

                //屏幕上的坐标 ＝ 行高＋ 移动距离
                plush = textHeight + ((currentPosition - tempPoint) / sleepTime) * textHeight;
            }
            canvas.translate(0, -plush);

            //绘制当前句
            String currentText = lyrics.get(index).getContent();
            canvas.drawText(currentText, width / 2, height / 2, paint);

            //绘制歌词，绘制当前句，绘制前面部分
            float tempY = height / 2;//y轴中间坐标

            for (int i = index - 1; i >= 0; i--) {
                //每一句歌词
                String preContent = lyrics.get(i).getContent();

                tempY = tempY - textHeight;
                if (tempY < 0) {
                    break;
                }
                canvas.drawText(preContent, width / 2, tempY, whitePaint);

            }

            //绘制后面部分
            tempY = height / 2;//y轴中间坐标

            for (int i = index + 1; i < lyrics.size(); i++) {
                //每一句歌词
                String nextContent = lyrics.get(i).getContent();

                tempY = tempY + textHeight;
                if (tempY > height) {
                    break;
                }
                canvas.drawText(nextContent, width / 2, tempY, whitePaint);

            }

        } else {
            //没有歌词
            canvas.drawText("没有歌词", width / 2, height / 2, paint);

        }
    }

    /**
     * 根据当前播放的位置，找出该高亮显示哪一句歌词
     *
     * @param currentPosition
     */
    public void setShowNextLyric(int currentPosition) {

        this.currentPosition = currentPosition;
        if (lyrics == null || lyrics.size() == 0)
            return;

        for (int i = 1; i < lyrics.size(); i++) {

            if (currentPosition < lyrics.get(i).getTimePoint()) {
                int tempIndex = i - 1;

                if (currentPosition >= lyrics.get(tempIndex).getTimePoint()) {
                    //当前正在播放的那句歌词
                    index = tempIndex;

                    sleepTime = lyrics.get(index).getSleepTime();

                    tempPoint = lyrics.get(index).getTimePoint();

                }
            }
        }
        //重新绘制
        invalidate();//主线程

        //子xiancheng
        //  postInvalidate();
    }
}
