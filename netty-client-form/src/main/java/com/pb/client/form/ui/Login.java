package com.pb.client.form.ui;

import com.pb.client.sdk.model.MsgPipe;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by piecebook on 2016/9/14.
 */
public class Login {
    private JPanel mian_panel;
    private JTextField uid;
    private JTextField pwd;
    private JButton login;
    private JLabel uid_lable;
    private JLabel pwd_lable;

    public Login() {
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String uid_value = uid.getText();
                String pwd_value = pwd.getText();

                Message msg = new Message();

                msg.setType(PBCONSTANT.LOGIN_FLAG);
                msg.setMsg_id(PBCONSTANT.getMsg_id());
                msg.setParam("s_uid", uid_value);
                msg.setParam("pwd", pwd_value);
                msg.setParam("r_uid", PBCONSTANT.SYSTEM);

                MsgPipe.sendMsg(msg);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().mian_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMian_panel() {
        return mian_panel;
    }

    public JTextField getUid() {
        return uid;
    }

    public JTextField getPwd() {
        return pwd;
    }

    public JButton getLogin() {
        return login;
    }

    public JLabel getUid_lable() {
        return uid_lable;
    }

    public JLabel getPwd_lable() {
        return pwd_lable;
    }
}
