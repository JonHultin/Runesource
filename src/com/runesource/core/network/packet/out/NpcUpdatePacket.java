package com.runesource.core.network.packet.out;

import java.util.Iterator;

import com.runesource.core.network.buffer.PacketHeader;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.OutboundPacket;
import com.runesource.core.world.Position;
import com.runesource.core.world.World;
import com.runesource.core.world.model.entity.mobile.npc.Npc;
import com.runesource.core.world.model.entity.mobile.player.Player;
import com.runesource.util.Misc;

public final class NpcUpdatePacket implements OutboundPacket {

	@Override
	public ProtocolBuffer dispatch(Player player) {
		ProtocolBuffer buffer = new ProtocolBuffer(65, PacketHeader.VARIABLE_SHORT);
		ProtocolBuffer block = new ProtocolBuffer();
		try {
			buffer.startBitAccess();
			buffer.writeBits(8, player.getRegionManager().getNpcs().size());
			for (Iterator<Npc> i = player.getRegionManager().getNpcs().iterator(); i.hasNext();) {
				Npc npc = i.next();
				if (npc.getPosition().isViewableFrom(player.getPosition()) && npc.isVisible()) {
					npc.getEventHandler().updateMovement(npc, buffer);
					if (npc.isUpdateRequired()) {
						npc.getEventHandler().updateState(npc, block);
					}
				} else {
					buffer.writeBit(true);
					buffer.writeBits(2, 3);
					i.remove();
				}
			}
			for (Npc npc : World.getSingleton().getNpcs()) {
				if (npc == null || player.getRegionManager().getNpcs().contains(npc) || !npc.isVisible()) {
					continue;
				}
				if (npc.getPosition().isViewableFrom(player.getPosition())) {
					buffer.writeBits(14, npc.getIndex());
					Position delta = Misc.delta(player.getPosition(), npc.getPosition());
					buffer.writeBits(5, delta.getY());
					buffer.writeBits(5, delta.getX());
					buffer.writeBit(npc.isUpdateRequired());
					buffer.writeBits(12, npc.getId());
					buffer.writeBit(true);
					if (npc.isUpdateRequired()) {
						npc.getEventHandler().updateState(npc, block);
					}
				}
			}
			if (block.getInternal().writerIndex() > 0) {
				buffer.writeBits(14, 16383);
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
