package com.pb.client.sdk.http;

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
        String response = HttpUtil.doPost(PBCONSTANT.BASEURL + "/friends/get", params);
        JSONArray friends = (JSONArray) HttpUtil.getData(response);
        return friends;
    }

    public List searchFriends(String search_key) {
        Map<String, String> params = new HashMap<>();
        params.put("search_key", search_key);
        String response = HttpUtil.doPost(PBCONSTANT.BASEURL + "/friends/search", params);
        JSONArray friends = (JSONArray) HttpUtil.getData(response);
        return friends;
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
