package com.runesource.core.network.packet.encoders;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketEncoder;
import com.runesource.core.world.Position;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class EntityPositionPacketEncoder implements PacketEncoder {

	private final Position position;
		
	public EntityPositionPacketEncoder(Position position) {
		this.position = position;
	}

	@Override
	public ProtocolBuffer dispatch(Player player) {
		ProtocolBuffer buffer = new ProtocolBuffer(85);
		buffer.writeByte(position.getY() - (player.getRegionManager().getPosition().getRegionY() * 8), ByteModification.NEGATED);		
		buffer.writeByte(position.getX() - (player.getRegionManager().getPosition().getRegionX() * 8), ByteModification.NEGATED);
		return buffer;
	}

}
