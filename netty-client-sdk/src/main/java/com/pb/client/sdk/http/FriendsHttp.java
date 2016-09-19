package com.pb.client.sdk.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pb.server.constant.PBCONSTANT;

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
        String result = HttpUtil.doPost(PBCONSTANT.HOST + ":8080/friends/get", params);
        if (result != null) {
            JSONArray friends = JSON.parseArray(result);
            return friends;
        }
        return null;
    }

    public List searchFriends(String search_key) {
        Map<String, String> params = new HashMap<>();
        params.put("search_key", search_key);
        String result = HttpUtil.doPost(PBCONSTANT.HOST + ":8080/friends/search", params);
        if (result != null) {
            JSONArray friends = JSON.parseArray(result);
            return friends;
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
