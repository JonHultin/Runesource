package com.runesource.core.network.packet.encoders;

import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ByteOrder;
import com.runesource.core.network.buffer.PacketHeader;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketEncoder;
import com.runesource.core.world.model.entity.item.Item;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class ItemElementsUpdatePacketEncoder implements PacketEncoder {

	private final Item[] items;

	private final int id;

	public ItemElementsUpdatePacketEncoder(Item[] items, int id) {
		this.items = items;
		this.id = id;
	}

	@Override
	public ProtocolBuffer dispatch(Player player) {
		ProtocolBuffer buffer = new ProtocolBuffer(53, PacketHeader.VARIABLE_SHORT);
		buffer.writeShort(id);
		buffer.writeShort(items.length);
		for (int index = 0; index < items.length; index++) {
			Item item = items[index];
			if (item != null) {
				if (item.getAmount() > 254) {
					buffer.writeByte(255);
					buffer.writeInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE_ENDIAN);
				} else {
					buffer.writeByte(item.getAmount());
				}
				buffer.writeShort(item.getId() + 1, ByteModification.ADDITION, ByteOrder.LITTLE_ENDIAN);
			} else {
				buffer.writeByte(0);
				buffer.writeShort(0, ByteModification.ADDITION, ByteOrder.LITTLE_ENDIAN);
			}
		}
		return buffer;
	}

}
