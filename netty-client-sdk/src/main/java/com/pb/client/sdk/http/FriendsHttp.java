package com.pb.client.sdk.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by piecebook on 2016/9/13.
 */
public class FriendsHttp {
    public List getFriends(String id, String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("user_id", id);
        String result = doPost("http://127.0.0.1:8080/friends/get", params);
        if (result != null) {
            JSONArray friends = JSON.parseArray(result);
            return friends;
        }
        return null;
    }

    public List searchFriends(String search_key) {
        Map<String, String> params = new HashMap<>();
        params.put("search_key", search_key);
        String result = doPost("http://127.0.0.1:8080/friends/search", params);
        if (result != null) {
            JSONArray friends = JSON.parseArray(result);
            return friends;
        }
        return null;
    }

    public String doPost(String url, Map<String, String> params) {
        HttpClient client = new HttpClient();
        HttpMethod post = new PostMethod(url);
        for (Map.Entry<String, String> pair : params.entrySet()) {
            ((PostMethod) post).addParameter(pair.getKey(), pair.getValue());
        }
        try {
            client.executeMethod(post);
            if (post.getStatusCode() == HttpStatus.SC_OK) {
                String response = new String(post.getResponseBody());
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    public static void main(String[] args) {
        FriendsHttp friendsHttp = new FriendsHttp();
        //List friends = friendsHttp.getFriends("8", "tom");
        List friends = friendsHttp.searchFriends("piece");
        for (Object o : friends) {
            System.out.println(o.toString());
        }
    }
}
