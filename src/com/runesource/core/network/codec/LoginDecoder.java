package com.runesource.core.network.codec;

import java.util.List;
import java.util.logging.Logger;

import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.security.SecureCipher;
import com.runesource.core.world.model.entity.mobile.player.PlayerCredentials;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class LoginDecoder extends ByteToMessageDecoder {

	private final Logger logger;
	
	public LoginDecoder() {
		this.logger = Logger.getLogger(getClass().getName());
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		final int opcode = in.readUnsignedByte();
		if (opcode != 16 && opcode != 18) {
			ctx.close();
			logger.info("Login - [description = invalid opcode, action = close]");
			return;
		}
		final int size = in.readUnsignedByte();
		if (size > in.readableBytes()) {
			ctx.close();
			logger.info("Login - [description = message size exceeds buffer capacity, action = close]");
			return;
		}
		final int magicId = in.readUnsignedByte();
		if (magicId != 255) {
			ctx.close();
			logger.info("Login - [description = invalid magic identifier, action = close]");
			return;
		}
		final int version = in.readShort();
		if (version != 317) {
			ctx.close();
			logger.info("Login - [description = invalid client version, action = close]");
			return;
		}
		in.skipBytes(39);
		final long clientSeed = in.readLong();
		final long serverSeed = in.readLong();
		final int seed [] = { (int) (clientSeed >> 32), (int) clientSeed, (int) (serverSeed >> 32), (int) serverSeed };
		SecureCipher secureRead = new SecureCipher(seed);
		for (int i = 0; i < seed.length; i++) {
			seed[i] += 50;
		}
		SecureCipher secureWrite = new SecureCipher(seed);		
		in.skipBytes(4);
		ProtocolBuffer buffer = new ProtocolBuffer(in);
		final String username = buffer.readString();
		final String password = buffer.readString();
		out.add(new PlayerCredentials(ctx.channel(), username, password, secureRead, secureWrite));
		ctx.pipeline().replace("decoder", "decoder", new PacketDecoder(secureRead));
		ctx.pipeline().addAfter("decoder", "encoder", new PacketEncoder(secureWrite));
		logger.info("Login - [username = " + username + " address = " + ctx.channel().remoteAddress() + "]");
	}

}
