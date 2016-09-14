package com.pb.client.form.daemon;

import com.pb.client.form.ui.Add_Friend_Dialog;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;

import java.util.concurrent.LinkedBlockingQueue;

import static com.pb.client.sdk.model.MsgPipe.rec_msg;

/**
 * Created by piecebook on 2016/9/14.
 */
public class SystemDaemon implements Runnable {
    public void run() {
        LinkedBlockingQueue<Message> msg_list = rec_msg.get(PBCONSTANT.SYSTEM);
        if (msg_list == null) msg_list = new LinkedBlockingQueue<Message>();
        rec_msg.put(PBCONSTANT.SYSTEM, msg_list);
        while (true) {
            Message rec_msg = null;
            try {
                rec_msg = msg_list.take();
                switch (rec_msg.getType()) {
                    case PBCONSTANT.ADD_FRIENDS_FLAG:
                        Add_Friend_Dialog dialog = new Add_Friend_Dialog(rec_msg.get("s_uid"), rec_msg.getType());
                        dialog.pack();
                        dialog.setVisible(true);
                        break;
                    case PBCONSTANT.ADD_FRIENDS_ACK_FLAG:
                        Add_Friend_Dialog dialog2 = new Add_Friend_Dialog(rec_msg.get("s_uid"), rec_msg.getType());
                        dialog2.pack();
                        dialog2.setVisible(true);
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
