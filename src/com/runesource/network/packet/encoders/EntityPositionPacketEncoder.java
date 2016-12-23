package com.runesource.network.packet.encoders;

import com.runesource.game.world.Position;
import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.network.buffer.ByteModification;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.packet.PacketEncoder;

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
