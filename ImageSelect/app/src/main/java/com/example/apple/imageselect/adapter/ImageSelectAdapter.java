package com.example.apple.imageselect.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.apple.imageselect.R;
import com.example.apple.imageselect.data.ImageData;

import java.util.List;


/**
 * Created by 550211 on 2017/10/8.
 */

public class ImageSelectAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageData> mImagePath;

    public ImageSelectAdapter(Context mContext, List<ImageData> imagePath) {
        this.mContext = mContext;
        this.mImagePath = imagePath;
    }

    public void setData(List<ImageData> mImagePath) {
        this.mImagePath = mImagePath;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mImagePath != null && mImagePath.size() > 0) {
            count = mImagePath.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = null;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.adapter_select_image, null);
            mViewHolder.ivSelect = (ImageView) convertView.findViewById(R.id.iv_select);
            mViewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext)
                .load(mImagePath.get(position).getPath())
                .asBitmap()
                .dontAnimate()
                .centerCrop()
                .into(mViewHolder.image);

        if (mImagePath.get(position).isSelect()) {
            mViewHolder.ivSelect.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.ivSelect.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView ivSelect;
        ImageView image;
    }
}
