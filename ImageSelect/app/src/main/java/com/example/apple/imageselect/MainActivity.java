package com.example.apple.imageselect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private ArrayList<String> selectPath = new ArrayList<>();
    private MyTestAdapter myTestAdapter;

    private ItemTouchHelper itemTouchHelper;
    private boolean isMove = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            selectPath.add("/storage/emulated/0/DCIM/Camera/IMG_20171014_150621.jpg");
        }

        myTestAdapter.setData(selectPath);
        mRecyclerView.setAdapter(myTestAdapter);
        myTestAdapter.notifyDataSetChanged();
    }

    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

        // LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myTestAdapter = new MyTestAdapter(getApplication(), selectPath);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mRecyclerView.setAdapter(myTestAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        myTestAdapter.myItemOnClickListener(new MyTestAdapter.MyItemOnClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (position == 0) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ImageSelectActivity.class);
                    startActivityForResult(intent, 1);
                } else {

                    Bitmap bitmap = BitmapFactory.decodeFile(selectPath.get(position));
                    Drawable drawable = new BitmapDrawable(bitmap);

                }
            }
        });


        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition!=0 && toPosition!=0 && selectPath!=null) {
                    if (fromPosition < toPosition) {
                        //分别把中间所有的item的位置重新交换
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(selectPath, i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(selectPath, i, i - 1);
                        }
                    }
                    isMove = true;
                    myTestAdapter.notifyItemMoved(fromPosition, toPosition);
                    //返回true表示执行拖动
                    Log.e("nsc", "onMove fromPosition=" + fromPosition + " toPosition=" + toPosition);
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                Log.e("nsc", "onSwiped position=" + position + " direction＝"+viewHolder.getPosition());
               // mData.remove(position);
                if (position==0) {
                 myTestAdapter.notifyDataSetChanged();
                }else {
                    if (!isMove && selectPath!=null && selectPath.size()>0) {
                        selectPath.remove(position);
                    }
                    myTestAdapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                // Log.e("nsc", "onChildDraw actionState="+actionState + " dx="+dX + " dy ="+dY);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //左右滑动时改变Item的透明度
                    final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                //当选中Item时候会调用该方法，重写此方法可以实现选中时候的一些动画逻辑
                Log.e("nsc", "onSelectedChanged");
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getPosition();
                Log.e("nsc", "clearView="+selectPath + " position="+position);
                super.clearView(recyclerView, viewHolder);
                //当动画已经结束的时候调用该方法，重写此方法可以实现恢复Item的初始状态
                if (isMove) {
                    isMove = false;
                    myTestAdapter.notifyDataSetChanged();
                }
            }
        };

        itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            selectPath = data.getStringArrayListExtra("selectPath");
            Log.e(TAG, "selectPath=" + selectPath.size());
            selectPath.add(0, selectPath.get(0));
            myTestAdapter.setData(selectPath);
            mRecyclerView.setAdapter(myTestAdapter);
            myTestAdapter.notifyDataSetChanged();
        }
    }
}
