package com.example.apple.mobileplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.domain.NetAudioPagerData;
import com.example.apple.mobileplayer.utils.Utils;
import com.squareup.picasso.Picasso;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by apple on 2017/2/7.
 * <p>
 * NetAudioPagerAdapter Adapter
 */

public class NetAudioPagerAdapter extends BaseAdapter {

    private final Context context;

    private List<NetAudioPagerData.ListBean> mdatas = new ArrayList<>();

    private Utils utils;

    /**
     * 视屏
     */
    private static final int TYPE_VIDEO = 0;
    /**
     * 图片
     */
    private static final int TYPE_IMAGE = 1;
    /**
     * 文字
     */
    private static final int TYPE_TEXT = 2;
    /**
     * gif图片
     */
    private static final int TYPE_GIF = 3;
    /**
     * 软件推广
     */
    private static final int TYPE_AD = 4;


    public NetAudioPagerAdapter(Context context, List<NetAudioPagerData.ListBean> mediaItem) {
        this.context = context;
        this.mdatas = mediaItem;
        utils = new Utils();
    }

    /**
     * 根据位置得到的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        NetAudioPagerData.ListBean listBean = mdatas.get(position);
        String type = listBean.getType();//video,text,image,gif,ad

        int itemViewType = -1;
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else {
            itemViewType = TYPE_AD;
        }

        return itemViewType;
    }

    @Override
    public int getViewTypeCount() {
        return 5;//五种类型
    }

    @Override
    public int getCount() {
        return mdatas.size();
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

        int itemViewType = getItemViewType(position);//得到类型

        ViewHolder viewHolder;
        if (convertView == null) {
            //初始化

            viewHolder = new ViewHolder();
            convertView = initView(convertView, itemViewType, viewHolder);

            //中间公共部分
            initCommonView(convertView, itemViewType, viewHolder);

            //设置tag
            convertView.setTag(viewHolder);

        } else {

            //获取tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //绑定数据
        NetAudioPagerData.ListBean mediaItem = mdatas.get(position);
        initData(itemViewType, viewHolder, mediaItem, position);

        return convertView;
    }

    void initData(int itemViewType, ViewHolder viewHolder, NetAudioPagerData.ListBean mediaItem, int position) {
        switch (itemViewType) {
            case TYPE_VIDEO:
                bindData(viewHolder, mediaItem);
                viewHolder.jcv_videoPlayer.setUp(mediaItem.getVideo().getVideo().get(0), mediaItem.getVideo().getThumbnail().get(0), null);
                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
                viewHolder.tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");
                //viewHolder.tv_commant_context.setText(mediaItem.getTop_comments().get(0).getContent());
                break;
            case TYPE_IMAGE://图片
                bindData(viewHolder, mediaItem);
                viewHolder.iv_image_icon.setImageResource(R.drawable.katong);
                //  int height = mediaItem.getImage().getHeight() <= DensityUtil.getScreenHeight() * 0.75 ? mediaItem.getImage().getHeight() : (int) (DensityUtil.getScreenHeight() * 0.75);
               // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mediaItem.getImage().getHeight(), mediaItem.getImage().getHeight());
               // viewHolder.iv_image_icon.setLayoutParams(params);


                if (mediaItem.getImage() != null && mediaItem.getImage().getBig() != null && mediaItem.getImage().getBig().size() > 0) {
                    Log.e("nsc", "data===" + mediaItem.getImage().getBig().get(0));
                    Glide.with(context)
                            .load(mediaItem.getImage().getBig().get(0))
                            .placeholder(R.drawable.katong)
                            .error(R.drawable.katong)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.iv_image_icon);

//                    Picasso.with(context).load(mediaItem.getImage().getBig().get(0))
//                            .into(viewHolder.iv_image_icon);
                }

                break;

            case TYPE_TEXT:
                bindData(viewHolder, mediaItem);
                break;

            case TYPE_GIF:
                bindData(viewHolder, mediaItem);
                Glide.with(context)
                        .load(mediaItem.getGif().getImages().get(0))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(viewHolder.iv_image_gif);
                break;

            case TYPE_AD:
                break;

        }
        //设置文本
        //if (!mediaItem.getText().isEmpty()) {
        viewHolder.tv_content.setText(mediaItem.getText());
        // }
    }

    void initCommonView(View convertView, int itemViewType, ViewHolder viewHolder) {
        switch (itemViewType) {
            case TYPE_VIDEO:
            case TYPE_IMAGE:
            case TYPE_TEXT://文字
            case TYPE_GIF://gif
                //加载除开广告部分的公共部分视图
                //user info
                viewHolder.iv_headpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_time_refresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
                viewHolder.iv_right_more = (ImageView) convertView.findViewById(R.id.iv_right_more);

                //bottom
                viewHolder.iv_video_kind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
                viewHolder.tv_video_kind_text = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
                viewHolder.tv_shenhe_ding_number = (TextView) convertView.findViewById(R.id.tv_shenhe_ding_number);
                viewHolder.tv_shenhe_cai_bumber = (TextView) convertView.findViewById(R.id.tv_shenhe_cai_bumber);
                viewHolder.tv_shenhe_share_bumber = (TextView) convertView.findViewById(R.id.tv_shenhe_share_bumber);
                viewHolder.tv_download = (LinearLayout) convertView.findViewById(R.id.tv_download);
                break;
        }

        //中间公共部分，所有的
        viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
    }

    View initView(View convertView, int itemViewType, ViewHolder viewHolder) {
        switch (itemViewType) {
            case TYPE_VIDEO:

                convertView = View.inflate(context, R.layout.all_video_item, null);

                viewHolder.tv_play_nums = (TextView) convertView.findViewById(R.id.tv_play_nums);
                viewHolder.tv_video_duration = (TextView) convertView.findViewById(R.id.tv_video_duration);
                viewHolder.iv_commant = (ImageView) convertView.findViewById(R.id.iv_commant);
                viewHolder.tv_commant_context = (TextView) convertView.findViewById(R.id.tv_commant_context);
                viewHolder.jcv_videoPlayer = (JCVideoPlayer) convertView.findViewById(R.id.jcv_videoPlayer);

                break;

            case TYPE_IMAGE:

                convertView = View.inflate(context, R.layout.all_image_item, null);
                viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);

                break;

            case TYPE_TEXT://文字
                convertView = View.inflate(context, R.layout.all_text_item, null);

                break;

            case TYPE_GIF://gif

                convertView = View.inflate(context, R.layout.all_gif_item, null);
                viewHolder.iv_image_gif = (GifImageView) convertView.findViewById(R.id.iv_image_gif);

                break;

            case TYPE_AD:
                convertView = View.inflate(context, R.layout.all_ad_item, null);
                viewHolder.btn_install = (Button) convertView.findViewById(R.id.btn_install);
               // viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);

                break;
        }
        return convertView;
    }

    private void bindData(ViewHolder viewHolder, NetAudioPagerData.ListBean mediaItem) {
        if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null && mediaItem.getU().getHeader().get(0) != null) {
            x.image().bind(viewHolder.iv_headpic, mediaItem.getU().getHeader().get(0));
        }

        if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
            viewHolder.tv_name.setText(mediaItem.getU().getName() + "");
        }
        viewHolder.tv_time_refresh.setText(mediaItem.getPasstime());

        //设置标签
        List<NetAudioPagerData.ListBean.TagsBean> tagsBeanList = mediaItem.getTags();
        if (tagsBeanList != null && tagsBeanList.size() > 0) {
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < tagsBeanList.size(); i++) {
                buffer.append(tagsBeanList.get(i).getName() + "");
            }
            viewHolder.tv_video_kind_text.setText(buffer.toString());

            //设置点赞，踩 转发
            viewHolder.tv_shenhe_ding_number.setText(mediaItem.getUp() + "");
            viewHolder.tv_shenhe_cai_bumber.setText(mediaItem.getDown() + "");
            viewHolder.tv_shenhe_share_bumber.setText(mediaItem.getForward() + "");
        }
    }

    static class ViewHolder {
        //user info
        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;

        //Bottom
        ImageView iv_video_kind;
        TextView tv_video_kind_text;
        TextView tv_shenhe_ding_number;
        TextView tv_shenhe_cai_bumber;
        TextView tv_shenhe_share_bumber;
        LinearLayout tv_download;

        TextView tv_content;

        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayer jcv_videoPlayer;

        ImageView iv_image_icon;

        GifImageView iv_image_gif;

        Button btn_install;
        //ImageView iv_image_icon;


    }
}
