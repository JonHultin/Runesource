package com.runesource.core.network.channel.handlers;

import java.security.SecureRandom;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class HandshakeInboundHandler extends ByteToMessageDecoder {

	private static final int LOGIN_OPCODE = 14;
	
	private final Logger logger;
	
	public HandshakeInboundHandler() {
		this.logger = Logger.getLogger(getClass().getName());
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		final int opcode = in.readUnsignedByte();
		switch (opcode) {
		
		case LOGIN_OPCODE:
			in.skipBytes(1);
			ctx.writeAndFlush(ctx.alloc().buffer().writeByte(0).writeLong(0).writeLong(new SecureRandom().nextLong()));
			ctx.pipeline().replace("decoder", "decoder", new LoginInboundHandler());
			break;
		
		default:
			logger.info("Unsupported handshake operation.");
			throw new UnsupportedOperationException();		
		}
	}

}
