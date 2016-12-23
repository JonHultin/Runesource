package com.runesource.network.packet.decoders;

import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.packet.Packet;
import com.runesource.network.packet.PacketDecoder;

public final class ChatCommandPacketDecoder implements PacketDecoder {

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
			player.getEventHandler().unregister();
			break;
			
		case "move":
			player.setForcedMovementRequired(true);
			player.setUpdateRequired(true);
			break;
		}
	}

}
