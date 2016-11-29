package com.pb.client.sdk.filter;

import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.BaseMessage;
import com.pb.server.model.Message;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 消息编码器
 * <p>
 * 网络传输的消息都会在这里进行编码
 */
public class MessageEncoder extends MessageToMessageEncoder<BaseMessage> {
    public MessageEncoder() {
    }

    protected void encode(ChannelHandlerContext ctx, BaseMessage msg, List<Object> out) throws Exception {
        if (msg instanceof BaseMessage) {
            out.add(Unpooled.wrappedBuffer(toByteArray(msg)));
        } else {
            throw new Exception("Not suport type");

        }
    }

    private byte[] toByteArray(BaseMessage bmsg) {
        MessageProtos.MessageProto proto = null;
        switch (bmsg.getType()) {
            case PBCONSTANT.FILE_FLAG:
                break;
            default:
                Message msg = (Message) bmsg;
                proto = MessageProtos.MessageProto
                        .newBuilder()
                        .setType(msg.getType())
                        .setSUid(msg.getSender())
                        .setRUid(msg.getReceiver())
                        .setMsgId(msg.getMsg_id())
                        .setMsg(msg.getContent())
                        .setSessionId(msg.getSession_id())
                        .setTime(msg.getTime_long())
                        .build();
        }
        return proto.toByteArray();
    }

}
