package com.runesource.core.network.packet.out;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.OutboundPacket;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class RegionalUpdatePacket implements OutboundPacket {

	@Override
	public ProtocolBuffer dispatch(Player player) {
		ProtocolBuffer buffer = new ProtocolBuffer(73);
		buffer.writeShort(player.getPosition().getRegionX() + 6, ByteModification.ADDITION);
		buffer.writeShort(player.getPosition().getRegionY() + 6);
		player.getRegionManager().getPosition().setAs(player.getPosition());
		player.setNeedsPlacement(true);
		return buffer;
	}

}
