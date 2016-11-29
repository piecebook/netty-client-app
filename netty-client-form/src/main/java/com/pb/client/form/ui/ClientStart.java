package com.pb.client.form.ui;

import com.alibaba.fastjson.JSONObject;
import com.pb.client.sdk.bootstrap.BootStrapClient;
import com.pb.client.sdk.deamon.MsgDeamon;
import com.pb.client.sdk.http.FriendsHttp;
import com.pb.client.sdk.http.MsgHttp;
import com.pb.client.sdk.model.Friend;
import com.pb.client.sdk.model.MsgPipe;
import com.pb.client.sdk.util.MsgUtil;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by piecebook on 2016/9/14.
 */
public class ClientStart {
    private static Main _main = new Main();

    public static void main(String[] args) {
        //nettyClient client = new nettyClient();
        BootStrapClient client = new BootStrapClient();
        client.init();
        MsgPipe.getInstance().setSession(client.getChannel(1));
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
                SysMsgDialog sysMsgDialog = new SysMsgDialog("用户不存在！");
                sysMsgDialog.pack();
                sysMsgDialog.setVisible(true);
                return;
            } else if (PBCONSTANT.flag == -2) {
                PBCONSTANT.flag = 0;
                SysMsgDialog sysMsgDialog = new SysMsgDialog("密码错误!");
                sysMsgDialog.pack();
                sysMsgDialog.setVisible(true);
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
        if (friends != null) {
            for (JSONObject obj : friends) {
                Friend friend = new Friend();
                friend.setId(Long.parseLong(obj.getString("id")));
                friend.setSid(Long.parseLong(obj.getString("sid")));
                friend.setUid(obj.getString("uid"));
                models.addElement(friend.getUid());
                MsgPipe.getInstance().getFriends_win().put(friend.getUid(), 0);
                MsgPipe.getInstance().getFriends().put(friend.getUid(), friend);
            }
        }
        _main.getFriends().setModel(models);


        MsgHttp msgHttp = new MsgHttp();
        List<JSONObject> msg_objs = msgHttp.getOfflineMsg(PBCONSTANT.id, PBCONSTANT.user);
        List<Message> msgs = MsgUtil.buildMsgMulti(msg_objs);
        LinkedBlockingQueue<Message> rec_msgs = null;
        for (Message msg : msgs) {
            try {
                if (msg.getType() == PBCONSTANT.MESSAGE_FLAG)
                    rec_msgs = MsgPipe.getInstance().getRec_msg().get(msg.getSender());
                else rec_msgs = MsgPipe.getInstance().getRec_msg().get(PBCONSTANT.SYSTEM);
                if (null == rec_msgs) {
                    rec_msgs = new LinkedBlockingQueue<>();
                    MsgPipe.getInstance().getRec_msg().put(msg.getSender(), rec_msgs);
                }
                rec_msgs.put(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (null != msgs && msgs.size() != 0) msgHttp.ackOfflineMsg(PBCONSTANT.id, PBCONSTANT.user, msg_objs);
    }

    private static void addfriend(String uid, String sid) {
        Friend friend = new Friend();
        friend.setUid(uid);
        friend.setSid(Long.parseLong(sid));
        DefaultListModel models = (DefaultListModel) _main.getFriends().getModel();
        models.addElement(uid);
        MsgPipe.getInstance().getFriends_win().put(uid, 0);
        MsgPipe.getInstance().getFriends().put(uid, friend);
    }

    public static void run() {
        LinkedBlockingQueue<Message> msg_list = MsgPipe.getInstance().getRec_msg().get(PBCONSTANT.SYSTEM);
        if (msg_list == null) msg_list = new LinkedBlockingQueue<Message>();
        MsgPipe.getInstance().getRec_msg().put(PBCONSTANT.SYSTEM, msg_list);
        while (true) {
            Message rec_msg = null;
            try {
                rec_msg = msg_list.take();
                switch (rec_msg.getType()) {
                    case PBCONSTANT.ADD_FRIENDS_FLAG:
                        Add_Friend_Dialog dialog = new Add_Friend_Dialog(rec_msg.getSender(), PBCONSTANT.ADD_FRIENDS_ACK_FLAG);
                        dialog.pack();
                        dialog.setVisible(true);
                        break;
                    case PBCONSTANT.ADD_FRIENDS_ACK_FLAG:
                        String status = rec_msg.getContent();
                        String result = rec_msg.getContent();
                        if (status == null) {
                            if (result.equals("fl")) {
                                SysMsgDialog sysMsgDialog = new SysMsgDialog(rec_msg.getSender() + "拒绝添加你为好友！");
                                sysMsgDialog.pack();
                                sysMsgDialog.setVisible(true);
                            } else {
                                SysMsgDialog sysMsgDialog = new SysMsgDialog(rec_msg.getSender() + "已同意添加你为好友！");
                                sysMsgDialog.pack();
                                sysMsgDialog.setVisible(true);
                                addfriend(rec_msg.getSender(), rec_msg.getContent());
                            }
                            break;
                        } else {
                            if (result.equals("fl")) {
                                break;
                            } else {
                                SysMsgDialog sysMsgDialog = new SysMsgDialog(rec_msg.getSender().substring(3) + " 已成为你好友！");
                                sysMsgDialog.pack();
                                sysMsgDialog.setVisible(true);
                                addfriend(rec_msg.getSender().substring(3), rec_msg.getContent());
                            }
                        }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
