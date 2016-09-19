package com.pb.client.form.ui;

import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;

import javax.swing.*;
import java.awt.event.*;

public class Add_Friend_Dialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel info;
    private String friend;
    private byte type;


    public Add_Friend_Dialog(String friend, byte type) {
        this.type = type;
        this.friend = friend;
        this.info.setText("你要加 " + friend + " 为好友吗？");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (MsgPipe.friends.get(friend) != null) {
            Message msg = new Message();
            msg.setType(type);
            msg.setParam("sid", 0 + "");
            msg.setParam("msg", "sys");
            msg.setParam("r_uid", friend);
            msg.setParam("s_uid", PBCONSTANT.user);
            if (type == PBCONSTANT.ADD_FRIENDS_ACK_FLAG) msg.setParam("msg", "sc");
            msg.setMsg_id(PBCONSTANT.getMsg_id());
            MsgPipe.sendMsg(msg);
        }else {
            SysMsgDialog sysMsgDialog = new SysMsgDialog(friend + "已经是你好友！");
            sysMsgDialog.pack();
            sysMsgDialog.setVisible(true);
        }
        dispose();
    }

    private void onCancel() {
        if (type == PBCONSTANT.ADD_FRIENDS_ACK_FLAG) {
            Message msg = new Message();
            msg.setType(type);
            msg.setParam("sid", 0 + "");
            msg.setParam("msg", "sys");
            msg.setParam("r_uid", friend);
            msg.setParam("s_uid", PBCONSTANT.user);
            msg.setParam("msg", "fl");
            msg.setMsg_id(PBCONSTANT.getMsg_id());
            MsgPipe.sendMsg(msg);
        }
        dispose();
    }

    public static void main(String[] args) {
        Add_Friend_Dialog dialog = new Add_Friend_Dialog("piecebook", PBCONSTANT.ADD_FRIENDS_ACK_FLAG);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
