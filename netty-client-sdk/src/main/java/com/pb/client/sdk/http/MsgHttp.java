package com.pb.client.sdk.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pb.server.constant.PBCONSTANT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PieceBook on 2016/9/19.
 */
public class MsgHttp {
    public List getOfflineMsg(String id, String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("user_id", id);
        String result = HttpUtil.doPost(PBCONSTANT.HOST + ":8080/msg/get_offline_msg", params);
        if (result != null) {
            JSONArray msgs = JSON.parseArray(result);
            return msgs;
        }
        return null;
    }

    public void ackOfflineMsg(String id, String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("user_id", id);
        String result = HttpUtil.doPost(PBCONSTANT.HOST + ":8080/msg/get_offline_msg", params);
        return;
    }
}
