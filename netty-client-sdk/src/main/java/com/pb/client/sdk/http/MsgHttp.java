package com.pb.client.sdk.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
        String response = HttpUtil.doPost(PBCONSTANT.BASEURL + "/msg/get_offline_msg", params);
        JSONArray msgs = (JSONArray) HttpUtil.getData(response);
        return msgs;
    }

    public void ackOfflineMsg(String id, String uid, List<JSONObject> friends) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("user_id", id);

        StringBuilder ids = new StringBuilder();
        for (JSONObject friend : friends) {
            ids.append(friend.getLong("id")).append(",");
        }
        params.put("ids_str", ids.toString());

        String response = HttpUtil.doPost(PBCONSTANT.BASEURL + "/msg/ack_offline_msg", params);
        JSONObject result = (JSONObject) HttpUtil.getData(response);
        return;
    }
}
