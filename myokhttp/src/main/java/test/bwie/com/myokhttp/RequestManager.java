package test.bwie.com.myokhttp;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.mcxtzhang.commonadapter.rv.mul.BaseMulTypeAdapter;
import com.mcxtzhang.commonadapter.rv.mul.IMulTypeHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @类的用途：
 * @author: 李晓倩
 * @date: 2017/5/10
 */

public class RequestManager {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final String TAG = RequestManager.class.getSimpleName();
    private static final String BASE_URL = "";//请求接口根地址
    private static volatile RequestManager mInstance;//单利引用
    public static final int TYPE_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    public static final int TYPE_POST_FORM = 2;//post请求参数为表单
    private OkHttpClient mOkHttpClient;//okHttpClient 实例
    private Handler okHttpHandler;//全局处理子线程和M主线程通信
    private String mSs;
    private RecyclerView rlv;
    private Context mContext;

    public RequestManager(Context context, RecyclerView rlv) {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        //初始化Handler
        okHttpHandler = new Handler(context.getMainLooper());
        this.rlv=rlv;
        this.mContext=context;
    }

    public static RequestManager getInstance(Context context, RecyclerView rlv) {
        RequestManager inst = mInstance;
        if (inst == null) {
            synchronized (RequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new RequestManager(context.getApplicationContext(),rlv);
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     *  okHttp同步请求统一入口
     * @param actionUrl  接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     */
    public void requestSyn(String actionUrl, int requestType, HashMap<String, String> paramsMap) {
        switch (requestType) {
            case TYPE_GET:
                requestGetBySyn(actionUrl, paramsMap);
                break;
            case TYPE_POST_JSON:
                requestPostBySyn(actionUrl, paramsMap);
                break;
            case TYPE_POST_FORM:
                requestPostBySynWithForm(actionUrl, paramsMap);
                break;
        }
    }

    /**
     * okHttp get同步请求
     * @param actionUrl  接口地址
     * @param paramsMap   请求参数
     */
    private void requestGetBySyn(String actionUrl, HashMap<String, String> paramsMap) {
        StringBuilder tempParams = new StringBuilder();
        try {
            //处理参数
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
            String requestUrl = String.format("%s%s?%s", BASE_URL, actionUrl, tempParams.toString());
            //创建一个请求
            Request request = addHeaders().url(requestUrl).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            final Response response = call.execute();
            final String rel=response.body().string();
            Log.d("zzz","rel:"+rel);
            okHttpHandler.post(new Runnable() {
                @Override
                public void run() {

                    rlv.setLayoutManager(new LinearLayoutManager(mContext));
                    GsonBean gsonBean = GsonUtil.GsonToBean(rel, GsonBean.class);
                    List<GsonBean.DataBean> data = gsonBean.getData();
                    List<GsonBean.RecommpicBean> beanList = gsonBean.getRecommpic();
                    Log.d("hhh",data.size()+"hhh");
                    List<IMulTypeHelper> mdata=new ArrayList<IMulTypeHelper>();
                    mdata.addAll(data);
                    mdata.addAll(beanList);
                    rlv.setAdapter(new BaseMulTypeAdapter(mContext, mdata) {
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
                    });
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * okHttp post同步请求
     * @param actionUrl  接口地址
     * @param paramsMap   请求参数
     */
    private void requestPostBySyn(String actionUrl, HashMap<String, String> paramsMap) {
        try {
            //处理参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
            String requestUrl = String.format("%s%s", BASE_URL, actionUrl);
            //生成参数
            String params = tempParams.toString();
            //创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(body).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            final Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful()) {
                //获取返回数据 可以是String，bytes ,byteStream
                mSs = response.body().string()+"zz";
                Log.e(TAG, "response ----->" + mSs);

            }
            okHttpHandler.post(new Runnable() {
                @Override
                public void run() {

            }
            });

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * okHttp post同步请求表单提交
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     */
    private void requestPostBySynWithForm(String actionUrl, HashMap<String, String> paramsMap) {
        try {
            //创建一个FormBody.Builder
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                //追加表单信息
                builder.add(key, paramsMap.get(key));
            }
            //生成表单实体对象
            RequestBody formBody = builder.build();
            //补全请求地址
            String requestUrl = String.format("%s%s", BASE_URL, actionUrl);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            final Response response = call.execute();
            if (response.isSuccessful()) {
                String sss=response.body().string();
                Log.e("aaa", "response ----->" + sss);
            }
            okHttpHandler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    /**
     * okHttp异步请求统一入口
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     **/
    public <T> Call requestAsyn(String actionUrl, int requestType, HashMap<String, String> paramsMap, ReqCallBack<T> callBack) {
        Call call = null;
        switch (requestType) {
            case TYPE_GET:
                call = requestGetByAsyn(actionUrl, paramsMap, callBack);
                break;
            case TYPE_POST_JSON:
                call = requestPostByAsyn(actionUrl, paramsMap, callBack);
                break;
            case TYPE_POST_FORM:
                call = requestPostByAsynWithForm(actionUrl, paramsMap, callBack);
                break;
        }
        return call;
    }

    /**
     * okHttp get异步请求
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     * @return
     */
    private <T> Call requestGetByAsyn(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        StringBuilder tempParams = new StringBuilder();
        try {
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String requestUrl = String.format("%s%s?%s", BASE_URL, actionUrl, tempParams.toString());
            final Request request = addHeaders().url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     * @return
     */
    private <T> Call requestPostByAsyn(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            String requestUrl = String.format("%s%s", BASE_URL, actionUrl);
            final Request request = addHeaders().url(requestUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求表单提交
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack 请求返回数据回调
     * @param <T> 数据泛型
     * @return
     */

    private <T> Call requestPostByAsynWithForm(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();
            String requestUrl = String.format("%s%s", BASE_URL, actionUrl);
            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e("aqq", "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 统一为请求添加头信息
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder();

        return builder;
    }

    /**
     * 统一同意处理成功信息
     * @param result
     * @param callBack
     * @param <T>
     */

    private <T> void successCallBack(final T result, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     * @param errorMsg
     * @param callBack
     * @param <T>
     */
    private <T> void failedCallBack(final String errorMsg, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqFailed(errorMsg);
                }
            }
        });
    }

}
