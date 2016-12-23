package com.runesource.core.network.packet.encoders;

import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketEncoder;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class LogoutPacketEncoder implements PacketEncoder {

	@Override
	public ProtocolBuffer dispatch(Player player) {
		return new ProtocolBuffer(109);
	}

}
