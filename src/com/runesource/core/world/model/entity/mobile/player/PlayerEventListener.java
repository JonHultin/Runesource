package com.runesource.core.world.model.entity.mobile.player;

import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.world.model.entity.mobile.MobileEntityEventListener;

public interface PlayerEventListener extends MobileEntityEventListener<Player> {

	public void updateState(Player player, ProtocolBuffer block, boolean forceAppearance, boolean noChat);
	
	public void updateAppearance(Player player, ProtocolBuffer block);
	
	public void updateChat(Player player, ProtocolBuffer block);
	
	public void updateLocal(Player player, ProtocolBuffer out, ProtocolBuffer block);
	
	public void updateMovement(Player player, ProtocolBuffer out);
	
	public void updateLocalMovement(Player player, ProtocolBuffer out);
}
