package com.runesource.network.packet.encoders;

import com.runesource.game.world.model.entity.item.ItemEntity;
import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.network.buffer.ByteModification;
import com.runesource.network.buffer.ByteOrder;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.packet.PacketEncoder;

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
