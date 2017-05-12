package com.byl.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.byl.recyclerview.other.DataBean;
import com.byl.recyclerview.other.MyRecyclerAdapter;
import com.byl.recyclerview.other.OkHttp;
import com.byl.recyclerview.other.RecyclerBean;
import com.byl.recyclerview.other.RecyclerViewClickListener;
import com.byl.recyclerview.other.Tools;
import com.byl.recyclerview.view.PullBaseView;
import com.byl.recyclerview.view.PullRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements PullBaseView.OnHeaderRefreshListener,
        PullBaseView.OnFooterRefreshListener {
    Context mContext;
    String mPath = "http://m.yunifang.com/yunifang/mobile/goods/" +
            "getall?random=39986&encode=2092d7eb33e8ea0a7a2113f2d9886c90&category_id=17";
    PullRecyclerView recyclerView;
    List<String> mDatas;
    private MyRecyclerAdapter recycleAdapter;
    ArrayList<DataBean> mlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        recyclerView = (PullRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setOnHeaderRefreshListener(this);//设置下拉监听
        recyclerView.setOnFooterRefreshListener(this);//设置上拉监听
        recyclerView.setCanScrollAtRereshing(true);//设置正在刷新时是否可以滑动，默认不可滑动

        recyclerView.setCanPullDown(true);//设置是否可下拉
        recyclerView.setCanPullUp(true);//设置是否可上拉
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    //    recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        //recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,"点击"+position,Toast.LENGTH_SHORT).show();
                Intent Intent=new Intent(MainActivity.this,RecyclerViewImged.class);
                startActivity(Intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this,"长按"+position,Toast.LENGTH_SHORT).show();
            }
        }));
    }
    protected void initData() {
        mlist = new ArrayList<DataBean>();
        OkHttp.getAsync(mPath, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
            }
            @Override
            public void requestSuccess(String result) throws Exception {
                RecyclerBean mReclerBean = Tools.parseJsonWithGson(result, RecyclerBean.class);
                mlist = mReclerBean.getData();
                recycleAdapter = new MyRecyclerAdapter(MainActivity.this, mlist);
                //设置Adapter
                recyclerView.setAdapter(recycleAdapter);

            }
        });

    }

    @Override
    public void onFooterRefresh(PullBaseView view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // mDatas.add("TEXT更多");
                recycleAdapter.notifyDataSetChanged();
                recyclerView.onFooterRefreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onHeaderRefresh(PullBaseView view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  mDatas.add(0, "TEXT新增");
                recycleAdapter.notifyDataSetChanged();
                recyclerView.onHeaderRefreshComplete();
            }
        }, 3000);
    }


   /* class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder;
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list, parent, false);
            holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.tv);
            }
        }
        }
        */

}
