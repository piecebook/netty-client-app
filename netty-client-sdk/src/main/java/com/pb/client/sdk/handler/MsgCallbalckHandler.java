package com.pb.client.sdk.handler;

import com.pb.client.sdk.callback.SendMsgCallbalk;
import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.model.Message;
import com.pb.server.session.PBSession;

/**
 * Created by piecebook on 2016/10/9.
 */
public class MsgCallbalckHandler implements PBIOHandler {

    @Override
    public Message process(PBSession session, Message msg) {
        SendMsgCallbalk callbalk = MsgPipe.getInstance().getMsgCallbalkMap().get(msg.getContent());
        MsgPipe.getInstance().getSend_msg().remove(msg.getContent());
        if (callbalk != null) callbalk.onSuccess();
        return null;
    }
}
