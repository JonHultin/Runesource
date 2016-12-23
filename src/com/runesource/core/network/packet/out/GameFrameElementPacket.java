package com.runesource.core.network.packet.out;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.OutboundPacket;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class GameFrameElementPacket implements OutboundPacket {

	private int index;
	
	private int id;
	
	public GameFrameElementPacket(int index, int id) {
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
