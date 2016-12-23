package com.runesource.core.network.packet.encoders;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ByteOrder;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketEncoder;
import com.runesource.core.world.model.entity.item.ItemEntity;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class ItemEntityRegistrationPacketEncoder implements PacketEncoder {

	private final ItemEntity entity;

	public ItemEntityRegistrationPacketEncoder(ItemEntity entity) {
		this.entity = entity;
	}

	@Override
	public ProtocolBuffer dispatch(Player player) {
		player.dispatch(new EntityPositionPacketEncoder(entity.getPosition()));
		ProtocolBuffer buffer = new ProtocolBuffer(44);
		buffer.writeShort(entity.getItem().getId(), ByteModification.ADDITION, ByteOrder.LITTLE_ENDIAN);
		buffer.writeShort(entity.getItem().getAmount());
		buffer.writeByte(0);	
		return buffer;
	}
	
}
