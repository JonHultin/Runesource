package com.runesource.core.network.packet;

import io.netty.buffer.ByteBuf;

public final class Packet {

	private final int opcode;
	
	private final ByteBuf payload;

	public Packet(int opcode, ByteBuf payload) {
		this.opcode = opcode;
		this.payload = payload;
	}

	public int getOpcode() {
		return opcode;
	}

	public ByteBuf getPayload() {
		return payload;
	}
	
}
