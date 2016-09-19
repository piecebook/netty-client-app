package com.pb.client.sdk.util;

import com.alibaba.fastjson.JSONObject;
import com.pb.server.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PieceBook on 2016/9/19.
 */
public class MsgUtil {
    public static Message buildMsg(JSONObject object) {
        Message msg = new Message();
        msg.setType(object.getByte("type"));
        msg.setMsg_id(object.getIntValue("id"));
        msg.setTime(object.getLong("time_long"));
        msg.setParam("msg", object.getString("content"));
        msg.setParam("s_uid", object.getString("sender"));
        msg.setParam("r_uid", object.getString("receiver"));
        return msg;
    }

    public static List<Message> buildMsgMulti(List<JSONObject> list) {
        List<Message> msgs = new ArrayList<>(list.size());
        for (JSONObject object : list) {
            Message msg = buildMsg(object);
            msgs.add(msg);
        }
        return msgs;
    }
}
