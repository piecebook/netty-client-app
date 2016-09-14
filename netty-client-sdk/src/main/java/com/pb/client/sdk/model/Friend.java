package com.pb.client.sdk.model;

/**
 * Created by piecebook on 2016/9/14.
 */
public class Friend {
    private long sid;
    private long id;
    private String uid;

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Friend)
            return uid.equals(((Friend) obj).getUid());
        else return false;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "sid=" + sid +
                ", id=" + id +
                ", uid='" + uid + '\'' +
                '}';
    }
}
