package com.pb.client.form.ui;

import com.pb.client.sdk.callback.SendMsgCallbalk;
import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.constant.PBCONSTANT;

import javax.swing.*;
import java.awt.event.*;

public class Add_Friend_Dialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel info;
    private String friend;
    private int type;


    public Add_Friend_Dialog(String friend, int type) {
        //TODO:添加好友有问题
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
        if (MsgPipe.getInstance().getFriends().get(friend) == null) {
            String content = "sys";
            if (type == PBCONSTANT.ADD_FRIENDS_ACK_FLAG) content="sc";
            MsgPipe.getInstance().sendMsg(friend, 0L, type, content, new SendMsgCallbalk() {
                @Override
                public void onSuccess() {
                    System.out.println("sended");
                }

                @Override
                public void onError(Integer errorcode) {
                    System.out.println("error");
                }
            });
        }else {
            SysMsgDialog sysMsgDialog = new SysMsgDialog(friend + "已经是你好友！");
            sysMsgDialog.pack();
            sysMsgDialog.setVisible(true);
        }
        dispose();
    }

    private void onCancel() {
        if (type == PBCONSTANT.ADD_FRIENDS_ACK_FLAG) {
            MsgPipe.getInstance().sendMsg(friend, 0L, type, "fl", new SendMsgCallbalk() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Integer errorcode) {

                }
            });
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
