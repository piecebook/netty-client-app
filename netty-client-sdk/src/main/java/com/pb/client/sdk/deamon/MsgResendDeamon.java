package com.pb.client.sdk.deamon;

import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piecebook on 2016/10/11.
 */
public class MsgResendDeamon implements Runnable {
    @Override
    public void run() {
        Long current_time_15s_before = System.currentTimeMillis() - 15000;
        Long current_time_30s_before = current_time_15s_before - 15000;
        Long current_time_45s_before = current_time_30s_before - 15000;
        Message[] send_msg = (Message[]) MsgPipe.getInstance().getSend_msg().values().toArray();
        List<Message> resend_list = new ArrayList<>();
        List<Message> fail_messages = new ArrayList<>();
        for (int i = 0; i < send_msg.length; i++) {
            if (send_msg[i].getTime() < current_time_45s_before)//45s 之前的消息 视为 发送失败
                fail_messages.add(send_msg[i]);
            else if (send_msg[i].getTime() < current_time_15s_before)//15s 之前的消息，等待ack超时，重发
                resend_list.add(send_msg[i]);
        }

        if (!resend_list.isEmpty()) {
            for (Message msg : resend_list) {
                MsgPipe.getInstance().sendMsg(msg.get("r_uid"), Long.valueOf(msg.get("sid")), msg.getType(), msg.get("msg"), MsgPipe.getInstance().getMsgCallbalkMap().get(msg.getMsg_id()));
            }
        }
        if (!fail_messages.isEmpty()) {
            for (Message msg : fail_messages) {
                MsgPipe.getInstance().getMsgCallbalkMap().get(msg.getMsg_id()).onError(40);
            }
        }
    }
}
