package com.example.apple.vitamiodemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.vitamiodemo.R;
import com.example.apple.vitamiodemo.data.MediaItem;
import com.example.apple.vitamiodemo.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by apple on 2017/2/7.
 *
 * VideoPager Adapter
 */

public class VideoListAdapter extends BaseAdapter{

    private boolean isVideo = false;

    private final Context context;

    private ArrayList<MediaItem> mediaItems = new ArrayList<>();

    private Utils utils;

    public VideoListAdapter(Context context,ArrayList<MediaItem> mediaItem,boolean isVideo) {
        this.context = context;
        this.mediaItems = mediaItem;
        this.isVideo = isVideo;
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

            convertView = View.inflate(context, R.layout.item_video_pager, null);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        //根据position得到列表中对应位置的数据
        MediaItem mediaItem = mediaItems.get(position);
        viewHolder.tvName.setText(mediaItem.getName());

        viewHolder.tvSize.setText(FormetFileSize(mediaItem.getSize()));
        viewHolder.tvTime.setText(utils.stringForTime((int) mediaItem.getDuration()));

        if (!isVideo){
            //音频
            viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_size)
        TextView tvSize;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 转换文件大小
     * @param fileS
     * @return fileSizeString
     */
    public String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
}
