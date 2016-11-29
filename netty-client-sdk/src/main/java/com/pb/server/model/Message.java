package com.pb.server.model;

/**
 * 消息类
 */
public class Message extends BaseMessage {
    private String content;
    private Long session_id;

    public Message() {
    }

    public Message(String sender, String receiver, String content) {
        super(sender, receiver);
        this.content = content;
    }

    public Message(int type, Long msg_id, String sender, String receiver, Long time_long, String content, Long session_id) {
        super(type, msg_id, sender, receiver, time_long);
        this.content = content;
        this.session_id = session_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Long getSession_id() {
        return session_id;
    }

    public void setSession_id(Long session_id) {
        this.session_id = session_id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", type=" + type +
                ", msg_id=" + msg_id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", create_time='" + create_time + '\'' +
                ", time_long=" + time_long +
                "content='" + content + '\'' +
                ", session_id=" + session_id +
                "} ";
    }
}
