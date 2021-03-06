package com.runesource.network.packet.encoders;

import com.runesource.game.world.World;
import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.game.world.model.entity.mobile.player.io.PlayerSerialization;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.packet.PacketEncoder;

public final class PlayerRegistrationPacketEncoder implements PacketEncoder {

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
