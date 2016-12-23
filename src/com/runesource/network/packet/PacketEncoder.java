package com.runesource.network.packet;

import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.network.buffer.ProtocolBuffer;

public interface PacketEncoder {

	public ProtocolBuffer dispatch(final Player player);
}
