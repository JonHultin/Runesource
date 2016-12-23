package com.runesource.core.network.packet.encoders;

import java.util.Iterator;

import com.runesource.core.network.buffer.ByteOrder;
import com.runesource.core.network.buffer.PacketHeader;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketEncoder;
import com.runesource.core.world.Position;
import com.runesource.core.world.World;
import com.runesource.core.world.model.entity.mobile.npc.Npc;
import com.runesource.core.world.model.entity.mobile.player.Player;
import com.runesource.util.Misc;

public final class NpcUpdatePacketEncoder implements PacketEncoder {

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
					updateMovement(npc, buffer);
					if (npc.isUpdateRequired()) {
						updateState(npc, block);
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
						updateState(npc, block);
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

	private void updateState(Npc npc, ProtocolBuffer block) {
		int mask = 0x0;
		if (mask >= 0x100) {
			mask |= 0x40;
			block.writeShort(mask, ByteOrder.LITTLE_ENDIAN);
		} else {
			block.writeByte(mask);
		}
	}
	
	private void updateMovement(Npc npc, ProtocolBuffer out) {
		if (npc.getWalkingDirection() == -1) {
			if (npc.isUpdateRequired()) {
				out.writeBit(true);
				out.writeBits(2, 0);
			} else {
				out.writeBit(false);
			}
		} else {
			out.writeBit(true);
			out.writeBits(2, 1);
			out.writeBits(3, npc.getWalkingDirection());
			out.writeBit(true);
		}
	}
}
