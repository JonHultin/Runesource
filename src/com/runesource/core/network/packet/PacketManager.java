package com.runesource.core.network.packet;

import com.runesource.core.network.packet.in.CommandPacket;
import com.runesource.core.network.packet.in.ItemContainerElementSwapPacket;
import com.runesource.core.network.packet.in.MovementPacket;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class PacketManager {

	private final InboundPacket listeners [] = new InboundPacket[257];
	
	public PacketManager() {
		listeners[214] = new ItemContainerElementSwapPacket();
		listeners[103] = new CommandPacket();
		listeners[98] = new MovementPacket();
		listeners[164] = new MovementPacket();
		listeners[248] = new MovementPacket();
	}
	
	public void notify(Player player, Packet packet) {
		if (listeners[packet.getOpcode()] == null) {
			
		} else {
			listeners[packet.getOpcode()].execute(player, packet);
		}
	}
}
