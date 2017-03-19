package com.example.apple.mobileplayer.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.mobileplayer.R;

/**
 * Created by apple on 17/3/17.
 */

public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    private Button btn_sub;
    private TextView tv_value;
    private Button btn_add;

    private int value = 1;
    private int minValue = 1;
    private int maxValue = 10;

    private OnNumberClickLister listener;

    public int getValue() {

        String valueStr = tv_value.getText().toString().trim();
        if (!TextUtils.isEmpty(valueStr)) {

            value = Integer.valueOf(valueStr);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        tv_value.setText(value + "");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public NumberAddSubView(Context context) {
        this(context, null);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(context, R.layout.number_add_sub_view, this);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        tv_value = (TextView) findViewById(R.id.tv_value);
        btn_add = (Button) findViewById(R.id.btn_add);

        getValue();

        //设置点击事件
        btn_sub.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        if (attrs != null) {
            TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.NumberAddSubView);

            //设置值
            int value = typedArray.getInt(R.styleable.NumberAddSubView_value, 0);
            if (value > 0){
                setValue(value);
            }

            //设置最小值
            int minValue = typedArray.getInt(R.styleable.NumberAddSubView_minValue, 0);
            if (value > 0){
                setMinValue(minValue);
            }

            //设置最大值
            int maxValue = typedArray.getInt(R.styleable.NumberAddSubView_maxValue, 0);
            if (value > 0){
                setMaxValue(maxValue);
            }
            //设置背景
            Drawable numberAddSubBackground = typedArray.getDrawable(R.styleable.NumberAddSubView_NumberAddSubBackgroind);
            if (numberAddSubBackground!=null){
                setBackground(numberAddSubBackground);
            }
            //加号背景
            Drawable numberAddBackground = typedArray.getDrawable(R.styleable.NumberAddSubView_NumberAddBackgroind);
            if (numberAddBackground!=null){
                btn_add.setBackground(numberAddBackground);
            }
            //减号背景
            Drawable numberSubBackground = typedArray.getDrawable(R.styleable.NumberAddSubView_NumberSubBackgroind);
            if (numberSubBackground!=null){
                btn_add.setBackground(numberSubBackground);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_sub://减
                subNumber();
                if (listener != null) {
                    listener.onButtonSub(v, value);
                }

                break;

            case R.id.btn_add://加
                addNumber();
                if (listener != null) {
                    listener.onButtonAdd(v, value);
                }
                break;
        }
    }

    private void addNumber() {
        if (value < maxValue) {
            value++;
        }
        setValue(value);
    }

    private void subNumber() {
        if (value > minValue) {
            value--;
        }
        setValue(value);
    }

    /**
     * 监听数字增加减少控件
     */
    public interface OnNumberClickLister {
        /**
         * 当减少按钮被点击的时候回调
         *
         * @param view
         * @param value
         */
        void onButtonSub(View view, int value);

        /**
         * 当增加按钮被点击的时候回调
         *
         * @param view
         * @param value
         */
        void onButtonAdd(View view, int value);
    }


    /**
     * 监听数字按钮
     *
     * @param listener
     */
    public void setOnNumberClickLister(OnNumberClickLister listener) {
        this.listener = listener;
    }

}
