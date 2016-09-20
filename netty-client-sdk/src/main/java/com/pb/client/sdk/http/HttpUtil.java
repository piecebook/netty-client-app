package com.pb.client.sdk.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.Map;

/**
 * Created by PieceBook on 2016/9/19.
 */
public class HttpUtil {
    public static String doPost(String url, Map<String, String> params) {
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

    public static JSON getData(String response) {
        if (response != null) {
            JSONObject result = JSON.parseObject(response);
            Integer error_code = result.getInteger("error_code");
            if (null != error_code) {
                if (error_code == 200) {
                    JSON data = (JSON) result.get("data");
                    return data;
                } else {
                    System.out.println(result.getString("reason"));
                    return null;
                }
            } else System.out.println("server error");
        }
        return null;
    }
}
