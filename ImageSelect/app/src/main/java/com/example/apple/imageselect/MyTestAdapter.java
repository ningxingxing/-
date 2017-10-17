package com.example.apple.imageselect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by apple on 17/10/9.
 */

public class MyTestAdapter extends RecyclerView.Adapter<MyTestAdapter.MyViewHolder> {

    private ArrayList<String> mSelectPath;
    private Context mContext;
    private LayoutInflater inflater;

    private MyItemOnClickListener myItemOnClickListener;

    public interface MyItemOnClickListener {
        void onItemClickListener(View view, int position);
    }

    public void myItemOnClickListener(MyItemOnClickListener myItemOnClickListener) {
        this.myItemOnClickListener = myItemOnClickListener;
    }


    public MyTestAdapter(Context mContext, ArrayList<String> selectPath) {
        this.mContext = mContext;
        this.mSelectPath = selectPath;

        inflater = LayoutInflater.from(mContext);
    }

    public void setData(ArrayList<String> mSelectPath) {
        this.mSelectPath = mSelectPath;

    }

    @Override
    public int getItemCount() {
        int count  = 0;
        if (mSelectPath!=null && mSelectPath.size()>0){
            count = mSelectPath.size();
        }
        return count;
    }

    @Override
    public MyTestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


       // MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_my_text, parent, false));
        View view = inflater.inflate(R.layout.adapter_my_text, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        if (position==0){
            holder.image.setBackgroundResource(R.drawable.ic_launcher);
        }else {
            Glide.with(mContext)
                    .load(mSelectPath.get(position))
                    .centerCrop()
                    .into(holder.image);
        }


        holder.tvText.setText(mSelectPath.get(position));

        holder.llImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myItemOnClickListener.onItemClickListener(v, position);

            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        LinearLayout llImage;
        TextView tvText;


        public MyViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            llImage = (LinearLayout) itemView.findViewById(R.id.ll_image);
            tvText = (TextView)itemView.findViewById(R.id.tv_text);
        }
    }


}
