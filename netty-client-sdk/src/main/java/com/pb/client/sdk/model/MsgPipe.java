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
    private ConcurrentHashMap<String, LinkedBlockingQueue<Message>> rec_msg = null;
    private Map<String, Friend> friends = null;
    private Map<String, Integer> friends_win = null;
    private Map<Long, SendMsgCallbalk> msgCallbalkMap = null;
    private Map<Long, Message> send_msg = null;

    private static Object lock = new Object();
    private static MsgPipe _instance = new MsgPipe();

    private MsgPipe() {
        rec_msg = new ConcurrentHashMap<>();
        friends = new HashMap<>();
        friends_win = new HashMap<>();
        msgCallbalkMap = new HashMap<>();
        send_msg = new HashMap<>();
    }

    public static MsgPipe getInstance() {
        return _instance;
    }

    public void sendMsg(final String receiver, final Long sid, final int msgType, String content, SendMsgCallbalk callbalk) {
        Message msg_s = new Message();
        msg_s.setType(msgType);
        Long msg_id = System.currentTimeMillis();
        msg_s.setMsg_id(msg_id);
        msg_s.setSender(PBCONSTANT.user);
        msg_s.setContent(content);
        msg_s.setReceiver( receiver);
        msg_s.setSession_id(sid);
        if (session != null) {
            msgCallbalkMap.put(msg_id, callbalk);
            send_msg.put(msg_id, msg_s);
            session.writeAndFlush(msg_s);
        }
    }

    public SocketChannel getSession() {
        return session;
    }

    public void setSession(SocketChannel session) {
        this.session = session;
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

    public Map<Long, SendMsgCallbalk> getMsgCallbalkMap() {
        return msgCallbalkMap;
    }

    public Map<Long, Message> getSend_msg() {
        return send_msg;
    }
}
