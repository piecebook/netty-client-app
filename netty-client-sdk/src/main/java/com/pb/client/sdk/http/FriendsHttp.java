package com.pb.client.sdk.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.List;

/**
 * Created by piecebook on 2016/9/13.
 */
public class FriendsHttp {
    public List getFriends(String id, String uid) {
        HttpClient client = new HttpClient();
        //TODO: post地址
        HttpMethod post = new PostMethod("http://127.0.0.1:8080/friends/get");
        ((PostMethod) post).addParameter("user_id", id);
        ((PostMethod) post).addParameter("uid", uid);
        try {
            client.executeMethod(post);
            if (post.getStatusCode() == HttpStatus.SC_OK) {
                String response = new String(post.getResponseBody());
                if (response != null) {
                    JSONArray friends = JSON.parseArray(response);
                    return friends;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    public static void main(String[] args) {
        FriendsHttp friendsHttp = new FriendsHttp();
        List friends = friendsHttp.getFriends("8", "tom");
        for (Object o : friends) {
            System.out.println(o.toString());
        }
    }
}
