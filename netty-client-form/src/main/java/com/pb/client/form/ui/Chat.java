package com.pb.client.form.ui;

import com.pb.client.sdk.callback.SendMsgCallbalk;
import com.pb.client.sdk.model.Friend;
import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.constant.PBCONSTANT;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by piecebook on 2016/9/14.
 */
public class Chat {
    private JPanel chat_panel;
    private JEditorPane send_msg;
    private JButton send;
    private JTextArea msg;
    private JLabel chat_with;
    private JPanel msg_panel;
    private Friend friend;

    public Chat() {
        send.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String msg_value = send_msg.getText();
                send_msg.setText("");
                MsgPipe.getInstance().sendMsg(friend.getUid(), friend.getSid(), PBCONSTANT.MESSAGE_FLAG, msg_value, new SendMsgCallbalk() {
                    @Override
                    public void onSuccess() {
                        System.out.println("sc");
                    }

                    @Override
                    public void onError(Integer errorcode) {
                        System.out.println("fl");
                    }
                });
                msg.append(PBCONSTANT.user + ":\n" + msg_value + "\n");
            }
        });
    }

    public static void main(String[] args) {
        /*JFrame frame = new JFrame("Chat");
        frame.setContentPane(new Chat().chat_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        LinkedBlockingQueue msg_list = MsgPipe.rec_msg.get(receiver);
        while (true) {
            Message rec_msg = msg_list.take();
            msg.append(rec_msg.get("s_uid") + ":\n" + rec_msg.get("msg"));
        }*/
    }

    public JPanel getChat_panel() {
        return chat_panel;
    }

    public JEditorPane getSend_msg() {
        return send_msg;
    }

    public JButton getSend() {
        return send;
    }

    public JTextArea getMsg() {
        return msg;
    }

    public JLabel getChat_with() {
        return chat_with;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }
}
