package com.runesource.core.network.packet.out;

import com.runesource.core.network.buffer.PacketHeader;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.OutboundPacket;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class PlayerUpdatePacket implements OutboundPacket {

	@Override
	public ProtocolBuffer dispatch(Player player) {
		ProtocolBuffer buffer = new ProtocolBuffer(81, PacketHeader.VARIABLE_SHORT);
		ProtocolBuffer block = new ProtocolBuffer();
		try {
			buffer.startBitAccess();
			player.getEventHandler().updateMovement(player, buffer);
			if (player.isUpdateRequired()) {
				player.getEventHandler().updateState(player, block, false, true);
			}
			player.getEventHandler().updateLocal(player, buffer, block);
			if (block.getInternal().writerIndex() > 0) {
				buffer.writeBits(11, 2047);
				buffer.endBitAccess();
				buffer.writeBytes(block.getInternal());
			} else {
				buffer.endBitAccess();
			}
		} catch (Exception e) {
			buffer.release();
			e.printStackTrace();
		} finally {
			block.release();
		}
		return buffer;
	}

}
