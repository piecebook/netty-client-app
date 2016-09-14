package com.pb.client.sdk;


import com.pb.client.sdk.bootstrap.BootStrapClient;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;
import io.netty.channel.socket.SocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class nettyClient {
    private static final int PORT = 8000;
    //TODO: HOST地址
    //private static final String HOST = "123.207.120.73";
    private static final String HOST = "127.0.0.1";

    public SocketChannel getSession(){
        BootStrapClient client = new BootStrapClient();
        client.connect(HOST,PORT);
        return client.getChannel();

    }
    public static void main(String[] args) {
        BootStrapClient client = new BootStrapClient();
        client.connect(HOST, PORT);
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
            if (client.login(user, pwd))
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
            msg.setMsg_id(PBCONSTANT.getMsg_id());
            msg.setParam("s_uid", user);
            msg.setParam("msg", content);
            msg.setParam("r_uid", receiver);
            msg.setParam("sid", 1 + "");
            client.getChannel().writeAndFlush(msg);
        }
    }

}
