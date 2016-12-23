package com.runesource.core.network.packet;

import com.runesource.core.network.packet.decoders.ChatCommandPacketDecoder;
import com.runesource.core.network.packet.decoders.ItemInterfaceElementPacketDecoder;
import com.runesource.core.network.packet.decoders.MovementPacketDecoder;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class PacketManager {

	private final PacketDecoder listeners [] = new PacketDecoder[257];
	
	public PacketManager() {
		listeners[214] = new ItemInterfaceElementPacketDecoder();
		listeners[103] = new ChatCommandPacketDecoder();
		listeners[98] = new MovementPacketDecoder();
		listeners[164] = new MovementPacketDecoder();
		listeners[248] = new MovementPacketDecoder();
	}
	
	public void notify(Player player, Packet packet) {
		if (listeners[packet.getOpcode()] == null) {
			
		} else {
			listeners[packet.getOpcode()].execute(player, packet);
		}
	}
}
