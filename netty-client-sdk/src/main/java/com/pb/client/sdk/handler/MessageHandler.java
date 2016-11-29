package com.pb.client.sdk.handler;

import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;
import com.pb.server.session.PBSession;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by piecebook on 2016/8/8.
 */
public class MessageHandler implements PBIOHandler {
    public Message process(PBSession session, Message msg) {
        System.out.println("From " + msg.getSender() + " :"
                + msg.toString());

        LinkedBlockingQueue msg_list = MsgPipe.getInstance().getRec_msg().get(msg.getSender());
        if (msg_list == null) {
            msg_list = new LinkedBlockingQueue();
            try {
                msg_list.put(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MsgPipe.getInstance().getRec_msg().put(msg.getSender(), msg_list);
        } else {
            try {
                msg_list.put(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Message reply = new Message();
        String msg_key = msg.getSender() + "-" + msg.getMsg_id();
        reply.setContent(msg_key);
        reply.setMsg_id(System.currentTimeMillis());
        reply.setType(PBCONSTANT.ACK_FLAG);
        return reply;
    }
}
