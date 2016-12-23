package com.runesource.core.network.packet.out;

import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.OutboundPacket;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class LogoutPacket implements OutboundPacket {

	@Override
	public ProtocolBuffer dispatch(Player player) {
		return new ProtocolBuffer(109);
	}

}
