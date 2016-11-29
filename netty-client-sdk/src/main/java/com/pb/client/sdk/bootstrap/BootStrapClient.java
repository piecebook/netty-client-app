package com.pb.client.sdk.bootstrap;

import com.pb.client.sdk.filter.MessageDecoder;
import com.pb.client.sdk.filter.MessageEncoder;
import com.pb.client.sdk.filter.MessageProtos;
import com.pb.client.sdk.handler.clientHandler;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class BootStrapClient {
    private static volatile Bootstrap bootstrap;
    private static volatile EventLoopGroup workergroup;
    private static volatile boolean isClose = false;
    private static final String HOST = "123.207.120.73";
    private static final int PORT = 8000;
    private static SocketChannel channel = null;


    public static boolean login(String user, String pwd) {
        if (channel == null) {
            System.out.println("init first!");
            return false;
        } else {
            Message msg = new Message();

            msg.setType(PBCONSTANT.LOGIN_FLAG);
            msg.setMsg_id(System.currentTimeMillis());
            msg.setSender(user);
            msg.setContent(pwd);
            msg.setReceiver(PBCONSTANT.SYSTEM);
            channel.writeAndFlush(msg);
            System.out.println("login:" + msg.toString());
            while (true) {
                // System.out.println(PBCONSTANT.flag);
                if (PBCONSTANT.flag == 1) {
                    PBCONSTANT.user = user;
                    return true;
                } else if (PBCONSTANT.flag == -1) {
                    PBCONSTANT.flag = 0;
                    return false;
                }
            }
        }
    }

    public static void init() {
        final int maxFrameLength = 1048;
        final int lengthFieldOffset = 0;
        final int lengthFieldLength = 4;
        final int lengthAdjustment = 11;
        final int initialBytesToStrip = 0;
        final int READ_IDLE_TIME_OUT = 5;
        final int WRITE_IDLE_TIME_OUT = 4;
        final int READ_WRITE_IDLE_TIME_OUT = 4;


        isClose = false;
        workergroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workergroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel channel)
                    throws Exception {
                channel.pipeline().addLast(new MessageEncoder());
                //channel.pipeline().addLast(new ObjectEncoder());
                // channel.pipeline().addLast(new MessageDecoder());
                //channel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip));
                channel.pipeline().addLast(new MessageDecoder(MessageProtos.MessageProto.getDefaultInstance()));
                channel.pipeline().addLast(new IdleStateHandler(READ_IDLE_TIME_OUT, WRITE_IDLE_TIME_OUT, READ_WRITE_IDLE_TIME_OUT, TimeUnit.MINUTES));
                channel.pipeline().addLast(new clientHandler());

            }

        });
        doConnect();
    }

    public static void doConnect() {
        if (isClose) return;
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(HOST, PORT));
        //lambda表达式
        future.addListener((ChannelFuture channelFuture) -> {
                    if (channelFuture.isSuccess()) {
                        channel = (SocketChannel) channelFuture.channel();
                        System.out.println("Connect Server:" + getServerInfo() + " Success!");
                    } else {
                        System.out.println("Connect Fail! Retrying " + getServerInfo());
                        channelFuture.channel().eventLoop().schedule(() -> doConnect(), 3, TimeUnit.SECONDS);
                    }
                }
        );
    }

    private static String getServerInfo() {
        return String.format("{\"RemoteHost\":%s \"RemotePort\":%d}", HOST, PORT);
    }

    public static void close() {
        isClose = true;
        workergroup.shutdownGracefully();
        System.out.println("client close");
    }

    public static SocketChannel getChannel() {
        return channel;
    }

    public SocketChannel getChannel(int flag) {
        return channel;
    }
}
