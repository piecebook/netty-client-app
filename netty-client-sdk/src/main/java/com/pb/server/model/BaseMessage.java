package com.pb.server.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by piecebook on 2016/11/25.
 */
public class BaseMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Long id;
    protected int type;
    protected Long msg_id;
    protected String sender;
    protected String receiver;
    protected String create_time;
    protected Long time_long;

    public BaseMessage() {

    }

    public BaseMessage(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public BaseMessage(int type, Long msg_id, String sender, String receiver, Long time_long) {
        this.type = type;
        this.msg_id = msg_id;
        this.sender = sender;
        this.receiver = receiver;
        this.time_long = time_long;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Long getTime_long() {
        return time_long;
    }

    public void setTime_long(Long time_long) {
        this.time_long = time_long;
        this.create_time = new Timestamp(time_long).toString();
    }

    public void setTime_long() {
        this.time_long = Timestamp.valueOf(create_time).getTime();
    }

    public Long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(Long msg_id) {
        this.msg_id = msg_id;
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
                "id=" + id +
                ", type=" + type +
                ", msg_id=" + msg_id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", create_time='" + create_time + '\'' +
                ", time_long=" + time_long +
                '}';
    }
}
