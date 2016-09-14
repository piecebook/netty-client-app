package com.pb.server.constant;

import java.util.concurrent.atomic.AtomicInteger;

public class PBCONSTANT {
    public static AtomicInteger msg_id = new AtomicInteger(1);
    public static final byte LOGIN_FLAG = 1;
    public static final byte LOGIN_REPLY_FLAG = 11;
    public static final byte MESSAGE_FLAG = 2;
    public static final byte MESSAGE_REPLY_FLAG = 21;
    public static final byte LOGOUT_FLAG = 3;
    public static final byte ACK_FLAG = 5;


    public static final String ACK = "ack";
    public static final String PING = "ping";
    public static final String LOGIN = "login";
    public static final String LOGIN_REPLY = "login_reply";
    public static final String MESSAGE = "message";
    public static final String SYSTEM = "PB system";
    public static final String SUCCESS = "success";

    public static final String MESSAGE_REPLY = "message_reply";
    public static final String USER_OFFLINE = "user has off line";
    public static final String FORCEOFFLINE = "force off line";
    public static final String FAIL = "fail";
    public static final String LOGOUT = "logout";
    public static final String LOGOUT_REPLY = "logout_reply";

    public static volatile int flag = 0;
    public static volatile String user = "";
    public static volatile String id = null;

    public static int getMsg_id() {
        int id = msg_id.getAndIncrement();
        if (id > (1 << 15)) msg_id.set(0);
        return id;
    }
}
