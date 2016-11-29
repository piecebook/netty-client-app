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
        msg.setMsg_id(object.getLong("id"));
        msg.setTime_long(object.getLong("time_long"));
        msg.setContent(object.getString("content"));
        msg.setSender(object.getString("sender"));
        msg.setReceiver(object.getString("receiver"));
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
