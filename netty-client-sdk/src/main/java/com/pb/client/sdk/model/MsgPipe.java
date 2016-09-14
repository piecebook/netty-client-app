package com.pb.client.sdk.model;

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
    public static SocketChannel session = null;
    public static LinkedBlockingQueue<Message> send_msg = new LinkedBlockingQueue<Message>();
    public static ConcurrentHashMap<String, LinkedBlockingQueue<Message>> rec_msg = new ConcurrentHashMap<String, LinkedBlockingQueue<Message>>();

    public static Map<String,Friend> friends = new HashMap<>();
    public static Map<String,Integer> friends_win = new HashMap<>();

    public static void sendMsg(Message msg){
        send_msg.add(msg);
    }
}
