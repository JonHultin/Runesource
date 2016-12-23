package com.runesource.core.network.packet.out;

import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.OutboundPacket;
import com.runesource.core.world.World;
import com.runesource.core.world.model.entity.mobile.player.Player;
import com.runesource.core.world.model.entity.mobile.player.io.PlayerSerialization;

public final class RegisterPlayerPacket implements OutboundPacket {

	@Override
	public ProtocolBuffer dispatch(Player player) {
		int responseCode = 2;
		for (Player other : World.getSingleton().getPlayers()) {
			if (player == null) {
				continue;
			}
			if (other.getUsername().equals(player.getUsername())) {
				responseCode = 5;
			}
		}
		int status = PlayerSerialization.unserialize(player);
		if (status == 2) {
			responseCode = 3;
		}
		ProtocolBuffer buffer = new ProtocolBuffer();
		buffer.writeByte(responseCode);
		buffer.writeByte(player.getStaffRights());
		buffer.writeByte(0);
		return buffer;
	}

}
