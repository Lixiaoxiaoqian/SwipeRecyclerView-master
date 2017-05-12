package test.bwie.com.myokhttp;

/**
 * @类的用途：
 * @author: 李晓倩
 * @date: 2017/5/10
 */

public interface ReqCallBack<T> {
    /**
     * 响应成功
     */
    void onReqSuccess(T result);

    /**
     * 响应失败
     */
    void onReqFailed(String errorMsg);

}
