package com.runesource.core.network.packet.decoders;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ByteOrder;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketDecoder;
import com.runesource.core.network.packet.Packet;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class ItemInterfaceElementPacketDecoder implements PacketDecoder {

	private static final int INVENTORY_INTERFACE_ID = 3214;
	
	@Override
	public void execute(Player player, Packet packet) {
		ProtocolBuffer buffer = new ProtocolBuffer(packet.getPayload());
		int id = buffer.readShort(ByteModification.ADDITION, ByteOrder.LITTLE_ENDIAN);
		buffer.readByte(ByteModification.NEGATED);// container mode
		int indexFrom = buffer.readShort(ByteModification.ADDITION, ByteOrder.LITTLE_ENDIAN);
		int indexTo = buffer.readShort(ByteOrder.LITTLE_ENDIAN);
		
		switch (id) {
		case INVENTORY_INTERFACE_ID:
			player.getInventoryContainer().swap(indexFrom, indexTo);
			break;
		}
	}

}
