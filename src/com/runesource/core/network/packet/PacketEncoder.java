package com.runesource.core.network.packet;

import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.world.model.entity.mobile.player.Player;

public interface PacketEncoder {

	public ProtocolBuffer dispatch(final Player player);
}
