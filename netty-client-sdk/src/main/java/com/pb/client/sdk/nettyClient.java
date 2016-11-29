package com.pb.client.sdk;


import com.pb.client.sdk.bootstrap.BootStrapClient;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class nettyClient {
    private static final int PORT = 8000;
    //TODO: HOST地址
    //private static final String HOST = "123.207.120.73";
    private static final String HOST = PBCONSTANT.HOST;

    public static void main(String[] args) {
        BootStrapClient.init();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));

        String user = "";
        String pwd = "";
        while (true) {
            try {
                System.out.println("user:");
                user = reader.readLine();
                System.out.println("pwd:");
                pwd = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (BootStrapClient.login(user, pwd))
                break;
        }
        while (true) {
            String receiver = "";
            String content = "";
            try {
                System.out.println("to:");
                receiver = reader.readLine();
                System.out.println("message:");
                content = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.setType(PBCONSTANT.MESSAGE_FLAG);
            msg.setMsg_id(System.currentTimeMillis());
            msg.setSender(user);
            msg.setContent(content);
            msg.setReceiver(receiver);
            msg.setSession_id(1L);
            BootStrapClient.getChannel().writeAndFlush(msg);
        }
    }

}
