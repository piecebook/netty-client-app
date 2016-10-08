package com.pb.client.sdk.model;

import com.pb.client.sdk.callback.SendMsgCallbalk;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;
import io.netty.channel.socket.SocketChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by piecebook on 2016/9/14.
 */
public class MsgPipe {
    private SocketChannel session = null;
    private LinkedBlockingQueue<Message> send_msg = null;
    private ConcurrentHashMap<String, LinkedBlockingQueue<Message>> rec_msg = null;
    private Map<String, Friend> friends = null;
    private Map<String, Integer> friends_win = null;
    private Map<Long, SendMsgCallbalk> msgCallbalkMap = null;

    private static Object lock = new Object();
    private static MsgPipe _instance = new MsgPipe();

    private MsgPipe() {
        send_msg = new LinkedBlockingQueue<>();
        rec_msg = new ConcurrentHashMap<>();
        friends = new HashMap<>();
        friends_win = new HashMap<>();
        msgCallbalkMap = new HashMap<>();
    }

    public static MsgPipe getInstance() {
        return _instance;
    }

    public void sendMsg(Message msg) {
        send_msg.add(msg);
    }

    public void sendMsg(final String receiver, final Long sid, final Byte msgType, String content, SendMsgCallbalk callbalk) {
        Message msg_s = new Message();
        msg_s.setType(msgType);
        Long msg_id = System.currentTimeMillis();
        msg_s.setMsg_id(msg_id);
        msg_s.setParam("s_uid", PBCONSTANT.user);
        msg_s.setParam("msg", content);
        msg_s.setParam("r_uid", receiver);
        msg_s.setParam("sid", sid + "");
        if (session != null) {
            session.writeAndFlush(msg_s);
            msgCallbalkMap.put(msg_id, callbalk);
        }
    }

    public SocketChannel getSession() {
        return session;
    }

    public void setSession(SocketChannel session) {
        this.session = session;
    }

    public LinkedBlockingQueue<Message> getSend_msg() {
        return send_msg;
    }

    public void setSend_msg(LinkedBlockingQueue<Message> send_msg) {
        this.send_msg = send_msg;
    }

    public ConcurrentHashMap<String, LinkedBlockingQueue<Message>> getRec_msg() {
        return rec_msg;
    }

    public void setRec_msg(ConcurrentHashMap<String, LinkedBlockingQueue<Message>> rec_msg) {
        this.rec_msg = rec_msg;
    }

    public Map<String, Friend> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, Friend> friends) {
        this.friends = friends;
    }

    public Map<String, Integer> getFriends_win() {
        return friends_win;
    }

    public void setFriends_win(Map<String, Integer> friends_win) {
        this.friends_win = friends_win;
    }

}
