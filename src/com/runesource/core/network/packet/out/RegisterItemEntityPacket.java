package com.runesource.core.network.packet.out;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ByteOrder;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.OutboundPacket;
import com.runesource.core.world.model.entity.item.ItemEntity;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class RegisterItemEntityPacket implements OutboundPacket {

	private final ItemEntity entity;

	public RegisterItemEntityPacket(ItemEntity entity) {
		this.entity = entity;
	}

	@Override
	public ProtocolBuffer dispatch(Player player) {
		player.dispatch(new EntityCoordinatePacket(entity.getPosition()));
		ProtocolBuffer buffer = new ProtocolBuffer(44);
		buffer.writeShort(entity.getItem().getId(), ByteModification.ADDITION, ByteOrder.LITTLE_ENDIAN);
		buffer.writeShort(entity.getItem().getAmount());
		buffer.writeByte(0);	
		return buffer;
	}
	
}
