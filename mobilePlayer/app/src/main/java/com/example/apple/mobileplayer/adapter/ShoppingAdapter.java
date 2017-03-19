package com.example.apple.mobileplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.apple.mobileplayer.R;
import com.example.apple.mobileplayer.domain.ShoppingItemData;
import com.example.apple.mobileplayer.view.NumberAddSubView;

import java.util.List;

/**
 * Created by apple on 17/3/16.
 */

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {


    private Context context;
    private List<ShoppingItemData> datas;


    public ShoppingAdapter(Context context, List<ShoppingItemData> datas) {
        this.context = context;
        this.datas = datas;
    }

    /**
     * 创建视图
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(context, R.layout.shopping_adatper_item, null);

        return new ViewHolder(view);
    }

    /**
     * getview方法中绑定数部分代码
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //根据位置得到数据
        final ShoppingItemData data = datas.get(position);

        Glide.with(context)
                .load(data.getImgUrl())
                .placeholder(R.drawable.katong)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);

        holder.tvName.setText(data.getName());
        holder.tvPrice.setText(data.getPrice()+"");

        //设置点击事件
        holder.btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,data.getPrice()+"",Toast.LENGTH_SHORT).show();
            }
        });

        holder.number_add_sub_view.setOnNumberClickLister(new NumberAddSubView.OnNumberClickLister() {
            @Override
            public void onButtonSub(View view, int value) {
               // Toast.makeText(context,"value="+view+"value="+value,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonAdd(View view, int value) {
                //Toast.makeText(context,"value="+view+"value="+value,Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 得到总的条数
     * @return
     */
    public int getDataCount(){
        return datas.size();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 根据指定位置添加数据
     * @param position
     * @param data
     */
    public void addData(int position,List<ShoppingItemData> data) {

        if (data!=null &data.size()>0){
            datas.addAll(0,data);
            notifyItemRangeChanged(position,datas.size());
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        TextView tvName;
        TextView tvPrice;
        Button btBuy;
        NumberAddSubView number_add_sub_view;

        public ViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView)itemView.findViewById(R.id.iv_icon);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            btBuy = (Button) itemView.findViewById(R.id.bt_buy);
            number_add_sub_view = (NumberAddSubView)itemView.findViewById(R.id.number_add_sub_view);
        }
    }

}
