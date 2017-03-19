package com.example.apple.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.domain.MediaItem;
import com.example.apple.mobileplayer.utils.Utils;

import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by apple on 2017/2/7.
 * <p>
 * VideoPager Adapter
 */

public class NetVideoPagerAdapter extends BaseAdapter {

    private final Context context;

    private ArrayList<MediaItem> mediaItems = new ArrayList<>();

    private Utils utils;

    public NetVideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItem) {
        this.context = context;
        this.mediaItems = mediaItem;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        utils = new Utils();

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_netvideo_pager, null);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据position得到列表中对应位置的数据
        MediaItem mediaItem = mediaItems.get(position);

        viewHolder.tvName.setText(mediaItem.getName());
        viewHolder.tvDesc.setText(mediaItem.getDesc());

        //使用xUtils3请求图片
        //x.image().bind(viewHolder.ivIcon,mediaItem.getImageUrl());

        //使用Glide请求图片
        Glide.with(context)
                .load(mediaItem.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.ivIcon);

        //使用picasso
//        Picasso.with(context)
//                .load(mediaItem.getImageUrl())
//                .placeholder(R.mipmap.ic_launcher)
//               // .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(viewHolder.ivIcon);

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.rl_image)
        RelativeLayout rlImage;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_desc)
        TextView tvDesc;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
