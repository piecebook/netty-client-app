package com.pb.client.sdk.handler;

import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;
import com.pb.server.session.PBSession;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by piecebook on 2016/9/14.
 */
public class FriendsHandler implements PBIOHandler {
    public Message process(PBSession session, Message msg) {
        LinkedBlockingQueue<Message> list = MsgPipe.getInstance().getRec_msg().get(PBCONSTANT.SYSTEM);
        if (list == null) {
            list = new LinkedBlockingQueue<>();
            MsgPipe.getInstance().getRec_msg().put(PBCONSTANT.SYSTEM, list);
        } else {
            try {
                list.put(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message reply = new Message();
        String msg_key = msg.getSender() + msg.getMsg_id();
        reply.setContent(msg_key);
        reply.setMsg_id(System.currentTimeMillis());
        reply.setType(PBCONSTANT.ACK_FLAG);
        return reply;
    }
}
