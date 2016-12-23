package com.runesource.core.network.packet.encoders;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketEncoder;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class GameFrameTabPacketEncoder implements PacketEncoder {

	private int index;
	
	private int id;
	
	public GameFrameTabPacketEncoder(int index, int id) {
		this.index = index;
		this.id = id;
	}

	@Override
	public ProtocolBuffer dispatch(Player player) {
		ProtocolBuffer buffer = new ProtocolBuffer(71);
		buffer.writeShort(id);
		buffer.writeByte(index, ByteModification.ADDITION);
		return buffer;
	}

}
