package com.runesource.network.packet.encoders;

import com.runesource.game.world.model.entity.item.Item;
import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.network.buffer.PacketHeader;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.packet.PacketEncoder;

public final class ItemElementUpdatePacketEncoder implements PacketEncoder {

	private final Item item;
	
	private final int index;
	
	private final int id;
		
	public ItemElementUpdatePacketEncoder(Item item, int index, int id) {
		this.item = item;		
		this.index = index;
		this.id = id;
	}

	@Override
	public ProtocolBuffer dispatch(Player player) {
		ProtocolBuffer buffer = new ProtocolBuffer(34, PacketHeader.VARIABLE_SHORT);
		buffer.writeShort(id);
		if (index > 128) {
			buffer.writeShort(index);
		} else {
			buffer.writeByte(index);
		}
		if (item != null) {
			buffer.writeShort(item.getId() + 1);
			if (item.getAmount() > 254) {
				buffer.writeByte(255);
				buffer.writeInt(item.getAmount());
			} else {
				buffer.writeByte(item.getAmount());
			}
		} else {
			buffer.writeShort(0);
			buffer.writeByte(0);
		}
		return buffer;
	}

}
