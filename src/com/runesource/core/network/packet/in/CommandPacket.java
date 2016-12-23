package com.runesource.core.network.packet.in;

import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.InboundPacket;
import com.runesource.core.network.packet.Packet;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class CommandPacket implements InboundPacket {

	@Override
	public void execute(Player player, Packet packet) {
		ProtocolBuffer buffer = new ProtocolBuffer(packet.getPayload());
		String[] arguments = buffer.readString().split(" ");
		String command = arguments[0].toLowerCase();
		switch (command) {
		case "inventory.add":
			player.getInventoryContainer().add(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]));
			break;
			
		case "inventory.remove":
			player.getInventoryContainer().remove(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]));
			break;
			
		case "inventory.update":
			player.getInventoryContainer().update();
			break;
			
		case "player.logout":
			player.getEventHandler().unregister(player);
			break;
			
		case "move":
			player.setForcedMovementRequired(true);
			player.setUpdateRequired(true);
			break;
		}
	}

}
