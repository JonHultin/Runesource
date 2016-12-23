package com.runesource.network.packet.encoders;

import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.network.buffer.ByteModification;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.packet.PacketEncoder;

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
