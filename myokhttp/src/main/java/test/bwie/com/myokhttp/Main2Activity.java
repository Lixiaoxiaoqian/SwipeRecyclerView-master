package test.bwie.com.myokhttp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class Main2Activity extends BaseActivity {

    private TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
        Log.d("zzz",event.getCode()+"zzz");
        switch (event.getCode()) {
            case 1:
                List<GsonBean.DataBean> list = (List<GsonBean.DataBean>) event.getData();
                Log.d("zzz",list.size()+"zzz");
                initView();
                textView3.setText(list.size()+"zzz");
        }
    }

    private void initView() {
        textView3 = (TextView) findViewById(R.id.textView3);
    }
}
