package com.byl.recyclerview.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by fan on 2016/11/18.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{

    protected Context context;


    public BaseViewHolder(View view, Context context) {
        super(view);
        this.context = context;
    }


    public void initData(Object data){

    }

}
