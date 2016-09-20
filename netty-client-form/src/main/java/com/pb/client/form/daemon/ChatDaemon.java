package com.pb.client.form.daemon;

import com.pb.client.form.ui.Chat;
import com.pb.client.sdk.model.Friend;
import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.model.Message;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.LinkedBlockingQueue;

import static com.pb.client.sdk.model.MsgPipe.rec_msg;

/**
 * Created by piecebook on 2016/9/14.
 */
public class ChatDaemon implements Runnable {
    private Friend receiver;
    private JFrame frame = new JFrame("Chat");

    public ChatDaemon(Friend receiver) {
        this.receiver = receiver;
    }

    public void run() {
        System.out.println("chatting");
        Chat chat_form = new Chat();
        chat_form.setFriend(receiver);
        chat_form.getChat_with().setText(receiver.getUid());
        frame.setContentPane(chat_form.getChat_panel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MsgPipe.friends_win.put(receiver.getUid(), 0);
                super.windowClosing(e);
            }
        });
        frame.pack();
        frame.setVisible(true);
        LinkedBlockingQueue<Message> msg_list = rec_msg.get(receiver.getUid());
        if (msg_list == null) {
            msg_list = new LinkedBlockingQueue<Message>();
            rec_msg.put(receiver.getUid(), msg_list);
        }
        while (true) {
            Message rec_msg = null;
            try {
                rec_msg = msg_list.take();
                System.out.println(rec_msg.toString());
                chat_form.getMsg().append(rec_msg.get("s_uid") + ":\n" + rec_msg.get("msg") + "\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
