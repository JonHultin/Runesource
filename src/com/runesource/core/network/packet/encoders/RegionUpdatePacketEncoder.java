package com.runesource.core.network.packet.encoders;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketEncoder;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class RegionUpdatePacketEncoder implements PacketEncoder {

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
