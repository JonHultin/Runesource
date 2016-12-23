package com.runesource.core.network.packet.encoders;

import com.runesource.core.network.buffer.PacketHeader;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketEncoder;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class ChatMessagePacketEncoder implements PacketEncoder {

	private final String message;
	
	public ChatMessagePacketEncoder(String message) {
		this.message = message;
	}

	@Override
	public ProtocolBuffer dispatch(Player player) {
		ProtocolBuffer buffer = new ProtocolBuffer(253, PacketHeader.VARIABLE);
		buffer.writeString(message);
		return buffer;
	}

}
