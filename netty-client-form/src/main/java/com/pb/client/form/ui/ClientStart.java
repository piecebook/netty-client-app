package com.pb.client.form.ui;

import com.alibaba.fastjson.JSONObject;
import com.pb.client.sdk.MsgDeamon;
import com.pb.client.sdk.http.FriendsHttp;
import com.pb.client.sdk.model.Friend;
import com.pb.client.sdk.model.MsgPipe;
import com.pb.client.sdk.nettyClient;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static com.pb.client.sdk.model.MsgPipe.rec_msg;

/**
 * Created by piecebook on 2016/9/14.
 */
public class ClientStart {
    private static Main _main = new Main();

    public static void main(String[] args) {
        nettyClient client = new nettyClient();
        MsgPipe.session = client.getSession();
        Thread thread = new Thread(new MsgDeamon());
        thread.start();
        login();
        while (PBCONSTANT.flag != 1) {

        }
        main_form();

        run();

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
                login_form.getPwd().setText("用户不存在！");
                return;
            } else if (PBCONSTANT.flag == -2) {
                PBCONSTANT.flag = 0;
                login_form.getPwd().setText("密码错误！");
                return;
            }
        }
    }

    private static void main_form() {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(_main.getMain_panel());
        _main.getUsername().setText(PBCONSTANT.user);
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

    private static void addfriend(String uid, String sid) {
        Friend friend = new Friend();
        friend.setUid(uid);
        friend.setSid(Long.parseLong(sid));
        DefaultListModel models = (DefaultListModel) _main.getFriends().getModel();
        models.addElement(uid);
        MsgPipe.friends_win.put(uid, 0);
        MsgPipe.friends.put(uid, friend);
    }

    public static void run() {
        LinkedBlockingQueue<Message> msg_list = rec_msg.get(PBCONSTANT.SYSTEM);
        if (msg_list == null) msg_list = new LinkedBlockingQueue<Message>();
        rec_msg.put(PBCONSTANT.SYSTEM, msg_list);
        while (true) {
            Message rec_msg = null;
            try {
                rec_msg = msg_list.take();
                switch (rec_msg.getType()) {
                    case PBCONSTANT.ADD_FRIENDS_FLAG:
                        Add_Friend_Dialog dialog = new Add_Friend_Dialog(rec_msg.get("s_uid"), PBCONSTANT.ADD_FRIENDS_ACK_FLAG);
                        dialog.pack();
                        dialog.setVisible(true);
                        break;
                    case PBCONSTANT.ADD_FRIENDS_ACK_FLAG:
                        String status = rec_msg.get("st");
                        String result = rec_msg.get("msg");
                        if (status == null) {
                            if (result.equals("fl")) {
                                SysMsgDialog sysMsgDialog = new SysMsgDialog(rec_msg.get("s_uid") + "拒绝添加你为好友！");
                                sysMsgDialog.pack();
                                sysMsgDialog.setVisible(true);
                            } else {
                                SysMsgDialog sysMsgDialog = new SysMsgDialog(rec_msg.get("s_uid") + "已同意添加你为好友！");
                                sysMsgDialog.pack();
                                sysMsgDialog.setVisible(true);
                                addfriend(rec_msg.get("s_uid"), rec_msg.get("msg"));
                            }
                            break;
                        } else {
                            if (result.equals("fl")) {
                                break;
                            } else {
                                SysMsgDialog sysMsgDialog = new SysMsgDialog(rec_msg.get("s_uid").substring(3) + " 已成为你好友！");
                                sysMsgDialog.pack();
                                sysMsgDialog.setVisible(true);
                                addfriend(rec_msg.get("s_uid").substring(3), rec_msg.get("msg"));
                            }
                        }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
