package com.runesource.core.network.codec;

import java.security.SecureRandom;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class HandshakeDecoder extends ByteToMessageDecoder {

	private static final int LOGIN_OPCODE = 14;
	
	private final Logger logger;
	
	public HandshakeDecoder() {
		this.logger = Logger.getLogger(getClass().getName());
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		final int opcode = in.readUnsignedByte();
		switch (opcode) {
		
		case LOGIN_OPCODE:
			in.skipBytes(1);
			ctx.writeAndFlush(ctx.alloc().buffer().writeByte(0).writeLong(0).writeLong(new SecureRandom().nextLong()));
			ctx.pipeline().replace("decoder", "decoder", new LoginDecoder());
			break;
		
		default:
			logger.info("Unsupported handshake operation.");
			throw new UnsupportedOperationException();		
		}
	}

}
