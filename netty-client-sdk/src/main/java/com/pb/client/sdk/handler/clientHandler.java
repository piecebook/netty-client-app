package com.pb.client.sdk.handler;

import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.Message;
import com.pb.server.session.PBSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class clientHandler extends SimpleChannelInboundHandler<Message> {
    private MessageHandler messageHandler = new MessageHandler();
    private FriendsHandler friendsHandler = new FriendsHandler();
    private MsgCallbalckHandler msgCallbalckHandler = new MsgCallbalckHandler();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idle = (IdleStateEvent) evt;
            switch (idle.state()) {
                case WRITER_IDLE:
                    Message ping = new Message();
                    ping.setType(PBCONSTANT.PING_FLAG);
                    ping.setParam("s_uid", PBCONSTANT.user);
                    ping.setMsg_id(System.currentTimeMillis());
                    ctx.channel().writeAndFlush(ping);
                    break;
                case READER_IDLE:
                    ctx.close();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("inactive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg)
            throws Exception {
        PBSession session = new PBSession();
        session.setSession(ctx.channel());
        Message reply = null;
        switch (msg.getType()) {
            case PBCONSTANT.LOGIN_REPLY_FLAG:
                String result = msg.get("st");
                if (result.equals("sc")) {
                    System.out.println("Login Success!");
                    PBCONSTANT.id = msg.get("id");
                    PBCONSTANT.user = msg.get("r_uid");
                    PBCONSTANT.flag = 1;
                } else if (result.equals("unfound")) {
                    PBCONSTANT.flag = -1;
                } else
                    PBCONSTANT.flag = -2;
                break;
            case PBCONSTANT.MESSAGE_REPLY_FLAG:
                msgCallbalckHandler.process(session, msg);
                break;
            case PBCONSTANT.MESSAGE_FLAG:
                reply = messageHandler.process(session, msg);
                break;
            case PBCONSTANT.ADD_FRIENDS_FLAG:
                reply = friendsHandler.process(session, msg);
                break;
            case PBCONSTANT.ADD_FRIENDS_ACK_FLAG:
                reply = friendsHandler.process(session, msg);
                break;
            case PBCONSTANT.ADD_FRIENDS_MSG_ACK_FLAG:
                break;
            default:
        }
        session.write(reply);
    }

}
