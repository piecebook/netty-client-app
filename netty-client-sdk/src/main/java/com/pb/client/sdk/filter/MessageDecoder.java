package com.pb.client.sdk.filter;

import com.pb.client.sdk.util.PBProtocol;
import com.pb.server.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf inbuf,
						  List<Object> out) throws Exception {
		int body_length = inbuf.readInt();
		byte encode = inbuf.readByte();
		byte enzip = inbuf.readByte();
		byte type = inbuf.readByte();
		long msg_id = inbuf.readLong();
		Message msg = new Message();
		msg.setEncode(encode);
		msg.setEnzip(enzip);
		msg.setType(type);
		msg.setMsg_id(msg_id);
		msg.setLength(body_length);
		msg.setContent(PBProtocol.Decode(encode,enzip,inbuf));
		out.add(msg);
	}

}
