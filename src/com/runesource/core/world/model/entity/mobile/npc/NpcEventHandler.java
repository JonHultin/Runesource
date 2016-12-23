package com.runesource.core.world.model.entity.mobile.npc;

import com.runesource.core.network.buffer.ByteOrder;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.world.World;

public final class NpcEventHandler implements NpcEventListener {

	@Override
	public void updateMovement(Npc npc) {
		throw new UnsupportedOperationException("Npc update movement event isn't supported.");
	}

	@Override
	public void register(Npc npc) {
		npc.setVisible(false);
		World.getSingleton().register(npc);
	}

	@Override
	public void unregister(Npc npc) {
		World.getSingleton().unregister(npc);
	}

	@Override
	public void update(Npc npc) {
		throw new UnsupportedOperationException("Npc update event isn't supported.");
	}

	@Override
	public void updateMovement(Npc npc, ProtocolBuffer out) {
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

	@Override
	public void updateState(Npc npc, ProtocolBuffer block) {
		int mask = 0x0;
		if (mask >= 0x100) {
			mask |= 0x40;
			block.writeShort(mask, ByteOrder.LITTLE_ENDIAN);
		} else {
			block.writeByte(mask);
		}
	}

}
