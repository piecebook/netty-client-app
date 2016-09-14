package com.pb.client.sdk;

import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.model.Message;

/**
 * Created by piecebook on 2016/9/14.
 */
public class MsgDeamon implements Runnable{
    @Override
    public void run() {
        while (true){
            try {
                Message msg = MsgPipe.send_msg.take();
                MsgPipe.session.writeAndFlush(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
