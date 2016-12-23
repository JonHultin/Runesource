package com.runesource.core.network.packet.out;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.OutboundPacket;
import com.runesource.core.world.Position;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class EntityCoordinatePacket implements OutboundPacket {

	private final Position position;
		
	public EntityCoordinatePacket(Position position) {
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
