package com.pb.client.sdk.handler;

import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;
import com.pb.server.session.PBSession;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by piecebook on 2016/9/14.
 */
public class FriendsHandler {
    public void process(PBSession session, Message msg) {
        Message reply = new Message();
        LinkedBlockingQueue<Message> list = MsgPipe.rec_msg.get(PBCONSTANT.SYSTEM);
        if (list == null) {
            list = new LinkedBlockingQueue<>();
            MsgPipe.rec_msg.put(PBCONSTANT.SYSTEM, list);
        } else {
            try {
                list.put(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
