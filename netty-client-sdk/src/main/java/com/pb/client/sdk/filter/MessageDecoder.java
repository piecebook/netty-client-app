package com.pb.client.sdk.filter;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.MessageLite;
import com.pb.server.constant.PBCONSTANT;
import com.pb.server.model.BaseMessage;
import com.pb.server.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 消息解码器
 * <p>
 * 网络传输的消息都在这里解码
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
	private static final boolean HAS_PARSER;
	private final MessageLite prototype;
	private final ExtensionRegistryLite extensionRegistry;

	public MessageDecoder(MessageLite prototype) {
		this(prototype, (ExtensionRegistry) null);
	}

	public MessageDecoder(MessageLite prototype, ExtensionRegistry extensionRegistry) {
		this(prototype, (ExtensionRegistryLite) extensionRegistry);
	}

	public MessageDecoder(MessageLite prototype, ExtensionRegistryLite extensionRegistry) {
		if (prototype == null) {
			throw new NullPointerException("prototype");
		} else {
			this.prototype = prototype.getDefaultInstanceForType();
			this.extensionRegistry = extensionRegistry;
		}
	}

	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		int length = msg.readableBytes();
		byte[] array;
		int offset;
		if (msg.hasArray()) {
			array = msg.array();
			offset = msg.arrayOffset() + msg.readerIndex();
		} else {
			array = new byte[length];
			msg.getBytes(msg.readerIndex(), array, 0, length);
			offset = 0;
		}

		if (this.extensionRegistry == null) {
			if (HAS_PARSER) {
				MessageProtos.MessageProto msgproto = (MessageProtos.MessageProto) this.prototype.getParserForType().parseFrom(array, offset, length);
				out.add(toMessage(msgproto));
			} else {
				MessageProtos.MessageProto msgproto = (MessageProtos.MessageProto) this.prototype.newBuilderForType().mergeFrom(array, offset, length).build();
				out.add(toMessage(msgproto));
			}
		} else if (HAS_PARSER) {
			MessageProtos.MessageProto msgproto = (MessageProtos.MessageProto) this.prototype.getParserForType().parseFrom(array, offset, length, this.extensionRegistry);
			out.add(toMessage(msgproto));
		} else {
			MessageProtos.MessageProto msgproto = (MessageProtos.MessageProto) this.prototype.newBuilderForType().mergeFrom(array, offset, length, this.extensionRegistry).build();
			out.add(toMessage(msgproto));
		}

	}

	private BaseMessage toMessage(MessageProtos.MessageProto proto) {
		BaseMessage msg = null;
		switch (proto.getType()) {
			case PBCONSTANT.FILE_FLAG:
				break;
			default:
				msg = new Message(proto.getType(), proto.getMsgId(), proto.getSUid(), proto.getRUid(), proto.getTime(), proto.getMsg(), proto.getSessionId());
		}
		return msg;
	}

	static {
		boolean hasParser = false;

		try {
			MessageLite.class.getDeclaredMethod("getParserForType", new Class[0]);
			hasParser = true;
		} catch (Throwable var2) {
			;
		}

		HAS_PARSER = hasParser;
	}
}
