package com.pb.client.sdk.handler;

import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;
import com.pb.server.session.PBSession;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by piecebook on 2016/8/8.
 */
public class MessageHandler {
    public void process(PBSession session, Message msg) {
        System.out.println("From " + msg.get("s_uid") + " :"
                + msg.toString());

        LinkedBlockingQueue msg_list = MsgPipe.rec_msg.get(msg.get("s_uid"));
        if(msg_list==null){
            msg_list = new LinkedBlockingQueue();
            MsgPipe.rec_msg.put(msg.get("s_uid"),msg_list);
        }else{
            try {
                msg_list.put(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Message reply = new Message();
        String msg_key = msg.get("s_uid") + msg.getMsg_id();
        reply.setParam("msg_key", msg_key);
        reply.setMsg_id(PBCONSTANT.getMsg_id());
        reply.setType(PBCONSTANT.ACK_FLAG);
        session.write(reply);
    }
}
