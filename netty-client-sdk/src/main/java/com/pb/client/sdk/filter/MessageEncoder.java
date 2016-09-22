package com.pb.client.sdk.filter;

import com.pb.client.sdk.util.PBProtocol;
import com.pb.server.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf outbuf)
            throws Exception {
        byte[] body = PBProtocol.Encode(msg.getEncode(), msg.getEnzip(), msg.getContent());
        int body_length = body.length;
        outbuf.writeInt(body_length);
        outbuf.writeByte(msg.getEncode());
        outbuf.writeByte(msg.getEnzip());
        outbuf.writeByte(msg.getType());
        long id = msg.getMsg_id();
        System.out.println(id);
        byte[] msg_id = new byte[8];
        for (int i = 7; i >= 0; i--) {
            msg_id[i] = (byte) id;
            id = id >> 8;
        }
        outbuf.writeBytes(msg_id);
        outbuf.writeBytes(body);
        System.out.println(outbuf.readableBytes());
        ctx.flush();
    }

}
