package com.pb.client.sdk.bootstrap;

import com.pb.client.sdk.filter.MessageDecoder;
import com.pb.client.sdk.filter.MessageEncoder;
import com.pb.client.sdk.handler.clientHandler;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class BootStrapClient {
	private SocketChannel channel = null;

    private int maxFrameLength = 1048;
    private int lengthFieldOffset = 0;
    private int lengthFieldLength = 4;
    private int lengthAdjustment = 5;
    private int initialBytesToStrip = 0;

	public boolean login(String user, String pwd) {
		if (channel == null) {
			System.out.println("Connect first!");
			return false;
		} else {
			Message msg = new Message();

            msg.setType(PBCONSTANT.LOGIN_FLAG);
			msg.setMsg_id(PBCONSTANT.getMsg_id());
            msg.setParam("s_uid", user);
            msg.setParam("pwd", pwd);
            msg.setParam("r_uid", PBCONSTANT.SYSTEM);
			channel.writeAndFlush(msg);
			System.out.println("login:"+msg.toString());
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

	public void connect(String host, int port) {
		EventLoopGroup workergroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
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
					channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(maxFrameLength,lengthFieldOffset,lengthFieldLength,lengthAdjustment,initialBytesToStrip));
					channel.pipeline().addLast(new MessageDecoder());
					channel.pipeline().addLast(new clientHandler());

				}

			});
			ChannelFuture future = bootstrap.connect(host, port).sync();
			if (future.isSuccess()) {
				channel = (SocketChannel) future.channel();
				System.out.println("Connect Server:" + channel.remoteAddress() +" Success!");
			}
		} catch (Exception e) {
			System.out.println("Connect Server Fail!");
			//e.printStackTrace();
			System.exit(-1);
		}

	}

	public SocketChannel getChannel() {
		return channel;
	}
}
