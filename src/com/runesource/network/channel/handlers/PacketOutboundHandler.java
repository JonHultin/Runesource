package com.runesource.network.channel.handlers;

import com.runesource.network.buffer.PacketHeader;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.security.SecureCipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class PacketOutboundHandler extends MessageToByteEncoder<ProtocolBuffer> {

	private final SecureCipher secureWrite;
	
	public PacketOutboundHandler(SecureCipher secureWrite) {
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
