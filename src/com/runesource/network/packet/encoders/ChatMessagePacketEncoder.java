package com.runesource.network.packet.encoders;

import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.network.buffer.PacketHeader;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.packet.PacketEncoder;

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
