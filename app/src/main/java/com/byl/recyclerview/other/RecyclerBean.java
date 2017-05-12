package com.byl.recyclerview.other;

import java.util.ArrayList;

/**
 * Created by fan on 2016/11/10.
 */

public class RecyclerBean {
    private int code;
    private String msg;

    private ArrayList<DataBean> data;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }
}
