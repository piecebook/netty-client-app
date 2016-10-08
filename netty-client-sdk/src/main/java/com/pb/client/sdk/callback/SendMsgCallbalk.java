package com.pb.client.sdk.callback;

/**
 * Created by piecebook on 2016/10/8.
 */
public interface SendMsgCallbalk {
    public void onSuccess();

    public void onError(Integer errorcode);
}
