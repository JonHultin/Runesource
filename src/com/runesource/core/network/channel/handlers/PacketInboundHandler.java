package com.runesource.core.network.channel.handlers;

import java.util.List;

import com.runesource.core.network.packet.Packet;
import com.runesource.core.network.security.SecureCipher;
import com.runesource.util.Misc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class PacketInboundHandler extends ByteToMessageDecoder {

	private final SecureCipher decipher;
	
	public PacketInboundHandler(SecureCipher decipher) {
		this.decipher = decipher;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int opcode = (in.readUnsignedByte() - decipher.nextValue()) & 0xFF;
		int size = Misc.packetLengths[opcode];
		
		if (size == -1) {
			size = in.readUnsignedByte();
		}
		
		if (size > in.readableBytes()) {
			return;
		}
		byte data [] = new byte[size];
		in.readBytes(data);
		ByteBuf payload = Unpooled.buffer(size);
		payload.writeBytes(data);
		out.add(new Packet(opcode, payload));
	}

}
