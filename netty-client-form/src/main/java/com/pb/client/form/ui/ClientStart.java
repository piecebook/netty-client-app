package com.pb.client.form.ui;

import com.alibaba.fastjson.JSONObject;
import com.pb.client.sdk.MsgDeamon;
import com.pb.client.sdk.http.FriendsHttp;
import com.pb.client.sdk.model.Friend;
import com.pb.client.sdk.model.MsgPipe;
import com.pb.client.sdk.nettyClient;
import com.pb.server.constant.PBCONSTANT;

import javax.swing.*;
import java.util.List;

/**
 * Created by piecebook on 2016/9/14.
 */
public class ClientStart {
    public static void main(String[] args) {
        nettyClient client = new nettyClient();
        MsgPipe.session = client.getSession();
        Thread thread = new Thread(new MsgDeamon());
        thread.start();
        login();
        while (PBCONSTANT.flag != 1) {

        }
        main_form();

    }


    private static void login() {
        JFrame frame = new JFrame("Login");
        Login login_form = new Login();
        frame.setContentPane(login_form.getMian_panel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        while (true) {
            // System.out.println(PBCONSTANT.flag);
            if (PBCONSTANT.flag == 1) {
                frame.setVisible(false);
                return;
            } else if (PBCONSTANT.flag == -1) {
                PBCONSTANT.flag = 0;
                login_form.getPwd().setText("登录失败！");
                return;
            }
        }
    }

    private static void main_form() {
        Main _main = new Main();
        JFrame frame = new JFrame("Main");
        frame.setContentPane(_main.getMain_panel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        FriendsHttp friendsHttp = new FriendsHttp();
        List<JSONObject> friends = friendsHttp.getFriends(PBCONSTANT.id, PBCONSTANT.user);

        DefaultListModel models = new DefaultListModel();
        for (JSONObject obj : friends) {
            Friend friend = new Friend();
            friend.setId(Long.parseLong(obj.getString("id")));
            friend.setSid(Long.parseLong(obj.getString("sid")));
            friend.setUid(obj.getString("uid"));
            models.addElement(friend.getUid());
            MsgPipe.friends_win.put(friend.getUid(), 0);
            MsgPipe.friends.put(friend.getUid(), friend);
        }
        _main.getFriends().setModel(models);
    }
}
