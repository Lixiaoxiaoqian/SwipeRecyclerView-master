package com.byl.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.byl.recyclerview.other.MAdapter;
import com.byl.recyclerview.other.OkHttp;
import com.byl.recyclerview.other.Url;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;


public class RecyclerViewImged extends AppCompatActivity {
    RecyclerView mRV;
    ArrayList<CommunityBean> mlist;
    MAdapter recycleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_imged);
        mRV=(RecyclerView) findViewById(R.id.rv_basefragment);
        mRV.setLayoutManager(new LinearLayoutManager(this));
        initData();
    }

    protected void initData() {
        mlist = new ArrayList<CommunityBean>();
        OkHttp.getAsync(Url.HOME_ONE_URL, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
            }

            @Override
            public void requestSuccess(String result) throws Exception {
              //  CommunityBean mReclerBean = Tools.parseJsonWithGson(result,CommunityBean.class);

                CommunityBean communityBean = new Gson().fromJson(result, CommunityBean.class);
                recycleAdapter = new MAdapter(RecyclerViewImged.this,communityBean);
                //设置Adapter
                mRV.setAdapter(recycleAdapter);

            }
        });
    }
}
