package com.byl.recyclerview.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.byl.recyclerview.R;
import com.byl.recyclerview.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by fan on 2016/11/10.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private ArrayList<DataBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public MyRecyclerAdapter(Context context, ArrayList<DataBean> datas){
        this.mContext=context;
        this.mDatas=datas;
        inflater= LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {

        return mDatas.size();
    }
    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv.setText(mDatas.get(position).getGoods_name());
        Glide.with(mContext).load(mDatas.get(position).getGoods_img()).asGif().
                override(ScreenUtils.getScreenWidth(mContext)/2,ScreenUtils.getScreenHeight(mContext)/2).error(R.mipmap.ic_launcher).into(holder.img);
    }
    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycleview_item_home,parent,false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            tv=(TextView) view.findViewById(R.id.recyclerview_tv);
            img=(ImageView) view.findViewById(R.id.recyclerview_img);

        }

    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
}