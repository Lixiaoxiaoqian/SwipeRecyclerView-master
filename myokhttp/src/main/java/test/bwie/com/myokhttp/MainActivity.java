package test.bwie.com.myokhttp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxtzhang.commonadapter.rv.OnItemClickListener;
import com.mcxtzhang.commonadapter.rv.mul.BaseMulTypeAdapter;
import com.mcxtzhang.commonadapter.rv.mul.IMulTypeHelper;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private TextView tv;
    private String mResult;
    private String url = "http://admin.wap.china.com/user/NavigateTypeAction.do?processID=getNavigateNews&page=1&code=news&pageSize=20&parentid=0&type=1";
    private String url2 = "http://admin.wap.china.com/user/NavigateTypeAction.do";
    private String url3 = "http://192.168.23.213:3000/json/index.json";
    private SwipeMenuRecyclerView rlv;
    private SwipeRefreshLayout swipe;
    private List<IMulTypeHelper> mMdata;
    private BaseMulTypeAdapter mAdapter;
    private Button btnall;
    private Button btnno;
    private Button btnsure;
    private ListAdapter mListAdapter;
    private List<GsonBean.DataBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("processID", "getNavigateNews");
                map.put("page", "1");
                map.put("code", "news");
                map.put("pageSize", "20");
                map.put("parentid", "0");
                map.put("type", "1");
                //RequestManager.getInstance(MainActivity.this,rlv).requestSyn(url2, RequestManager.TYPE_GET, map);
                RequestManager.getInstance(MainActivity.this, rlv).requestAsyn(url2, RequestManager.TYPE_GET, map, new ReqCallBack<String>() {
                    @Override
                    public void onReqSuccess(String result) {
                        rlv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        GsonBean gsonBean = GsonUtil.GsonToBean(result, GsonBean.class);
                        mData = gsonBean.getData();
                        List<GsonBean.RecommpicBean> beanList = gsonBean.getRecommpic();
                        Log.d("hhh", mData.size() + "hhh");
                        mListAdapter = new ListAdapter(mData, MainActivity.this);
                        rlv.setAdapter(mListAdapter);
                        mListAdapter.setRecyclerViewOnItemClickListener(new ListAdapter.RecyclerViewOnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position) {
                                //点击事件
                                //设置选中的项
                                mListAdapter.setSelectItem(position);
                            }

                            @Override
                            public boolean onItemLongClickListener(View view, int position) {
                                //长按事件
                                mListAdapter.setShowBox();
                                //设置选中的项
                                mListAdapter.setSelectItem(position);
                                mListAdapter.notifyDataSetChanged();
                                return true;
                            }
                        });
                     /*   mMdata = new ArrayList<IMulTypeHelper>();
                        mMdata.addAll(data);
                        mMdata.addAll(beanList);
                        mAdapter = new BaseMulTypeAdapter(MainActivity.this, mMdata) {
                            @Override
                            public void convert(ViewHolder holder, final IMulTypeHelper iMulTypeHelper) {
                                super.convert(holder, iMulTypeHelper);

                                switch (iMulTypeHelper.getItemLayoutId()) {
                                    case R.layout.rlv_itemone:

                                        break;
                                    case R.layout.rlv_itemtwo:

                                        break;

                                }
                            }
                        };
                        mAdapter.setOnItemClickListener(onItemClickListener);
                        rlv.setItemAnimator(new DefaultItemAnimator());
                        rlv.setSwipeMenuCreator(swipeMenuCreator);
                        // 设置菜单Item点击监听。
                        rlv.setSwipeMenuItemClickListener(menuItemClickListener);
                        rlv.setAdapter(mAdapter);*/

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                    }
                });
            }
        }).start();
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(ViewGroup parent, View view, Object o, int position) {
            Toast.makeText(MainActivity.this, "我是第" + position + "条。", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
            return false;
        }

    };

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(MainActivity.this)
                        .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
                        .setImage(R.mipmap.ic_action_add) // 图标。
                        .setWidth(width) // 宽度。
                        .setHeight(height); // 高度。
                swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(MainActivity.this)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);

                swipeLeftMenu.addMenuItem(closeItem); // 添加一个按钮到左侧菜单。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MainActivity.this)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(MainActivity.this)
                        .setBackgroundDrawable(R.drawable.selector_purple)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

                SwipeMenuItem addItem = new SwipeMenuItem(MainActivity.this)
                        .setBackgroundDrawable(R.drawable.selector_green)
                        .setText("添加")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
            }
        }
    };


    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView
         *                        #RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(MainActivity.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(MainActivity.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }

            // TODO 推荐调用Adapter.notifyItemRemoved(position)，也可以Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 删除按钮被点击。
                mMdata.remove(adapterPosition);
                mAdapter.notifyItemRemoved(adapterPosition);
            }
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_all_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_open_rv_menu) {
            rlv.smoothOpenRightMenu(0);
        }
        return true;
    }


    private void initView() {

        rlv = (SwipeMenuRecyclerView) findViewById(R.id.recycleview);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swip);
        swipe.setOnRefreshListener(this); // 设置刷新监听
        swipe.setColorSchemeResources(R.color.orange, R.color.green, R.color.purple); // 进度动画颜色
        swipe.setProgressBackgroundColorSchemeResource(R.color.swipefefresh_bg); // 进度背景颜色

        btnall = (Button) findViewById(R.id.btnall);
        btnall.setOnClickListener(this);
        btnno = (Button) findViewById(R.id.btnno);
        btnno.setOnClickListener(this);
        btnsure = (Button) findViewById(R.id.btnsure);
        btnsure.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        swipe.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                swipe.setRefreshing(true); // 是否显示刷新进度;false:不显示
            }
        }, 3000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnall:
                Map<Integer, Boolean> map = mListAdapter.getMap();
                for (int i = 0; i < map.size(); i++) {
                    map.put(i, true);
                    mListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.btnno:
                Map<Integer, Boolean> m = mListAdapter.getMap();
                for (int i = 0; i < m.size(); i++) {
                    m.put(i, false);
                    mListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.btnsure:
                Map<Integer, Boolean> mp = mListAdapter.getMap();
                List<GsonBean.DataBean> alist=new ArrayList<>();
                for (int i = 0; i < mp.size(); i++) {
                    if (mp.get(i)) {
                        alist.add(mData.get(i));
                    }
                }
                EventBusUtil.sendStickyEvent(new Event(1,alist));
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                break;
        }
    }

}
