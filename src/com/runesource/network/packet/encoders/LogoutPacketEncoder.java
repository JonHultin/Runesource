package com.runesource.network.packet.encoders;

import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.packet.PacketEncoder;

public final class LogoutPacketEncoder implements PacketEncoder {

	@Override
	public ProtocolBuffer dispatch(Player player) {
		return new ProtocolBuffer(109);
	}

}
