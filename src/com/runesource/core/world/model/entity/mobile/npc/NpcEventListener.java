package com.runesource.core.world.model.entity.mobile.npc;

import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.world.model.entity.mobile.MobileEntityEventListener;

public interface NpcEventListener extends MobileEntityEventListener<Npc> {

	public void updateMovement(Npc npc, ProtocolBuffer out);
	
	public void updateState(Npc npc, ProtocolBuffer block);
}
