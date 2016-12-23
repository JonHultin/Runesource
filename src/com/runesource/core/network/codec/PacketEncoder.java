package com.runesource.core.network.codec;

import com.runesource.core.network.buffer.PacketHeader;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.security.SecureCipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class PacketEncoder extends MessageToByteEncoder<ProtocolBuffer> {

	private final SecureCipher secureWrite;
	
	public PacketEncoder(SecureCipher secureWrite) {
		this.secureWrite = secureWrite;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, ProtocolBuffer msg, ByteBuf out) throws Exception {
		if(msg.getPacketHeader() == PacketHeader.RAW) {
			out.writeBytes(msg.getInternal());
			return;
		}
		out.writeByte(msg.getOpcode() + secureWrite.nextValue());
        if (msg.getPacketHeader() == PacketHeader.VARIABLE) {
            out.writeByte(msg.getInternal().readableBytes());
        } else if (msg.getPacketHeader() == PacketHeader.VARIABLE_SHORT) {
            out.writeShort(msg.getInternal().readableBytes());
        }
		out.writeBytes(msg.getInternal());
	}

}
