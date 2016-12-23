package com.runesource.core.world.model.entity.item.containers;

import com.runesource.core.network.packet.encoders.ChatMessagePacketEncoder;
import com.runesource.core.network.packet.encoders.ItemElementUpdatePacketEncoder;
import com.runesource.core.network.packet.encoders.ItemElementsUpdatePacketEncoder;
import com.runesource.core.world.model.entity.item.ItemContainer;
import com.runesource.core.world.model.entity.item.ItemDefinition;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class InventoryItemContainer extends ItemContainer {

	private static final int CAPACITY = 28;

	public InventoryItemContainer(Player player) {
		super(player, CAPACITY);
	}

	@Override
	public void add(int id, int amount) {
		if (!available(id)) {
			player.dispatch(new ChatMessagePacketEncoder("You don't have enough inventory space to do that."));
			return;
		}
		for (int index = 0; index < getCapacity(); index ++) {
			if (elements[index] == null) {
				continue;
			}
			if (elements[index].getId() == id) {
				if (ItemDefinition.get(id).isStackable()) {
					elements[index].setAmount(elements[index].getAmount() + amount);
					update();
					return;
				}
			}
		}

		if (!ItemDefinition.get(id).isStackable() && amount > 1) {
			for (int accumulator = 0; accumulator < amount; accumulator ++) {
				for (int index = 0; index < getCapacity(); index ++) {
					if (elements[index] == null) {
						set(index, id, 1);
						break;
					}
				}
			}
			update();
			return;
		}
		
		for (int index = 0; index < getCapacity(); index ++) {
			if (elements[index] == null) {
				set(index, id, amount);
				update(index);
				break;
			}
		}
	}

	@Override
	public void remove(int id, int amount) {
		int deleteCount = 0;
		for (int index = 0; index < getCapacity(); index ++) {
			if (elements[index] == null) {
				continue;
			}
			if (elements[index].getId() == id) {
				if (deleteCount == amount) {
					break;
				}
				if (elements[index].getAmount() > amount && ItemDefinition.get(id).isStackable()) {
					elements[index].setAmount(elements[index].getAmount() - amount);
					update();
					break;
				}
				set(index, -1, 0);
				elements[index] = null;
				update();
				deleteCount++;
			}
		}
	}

	@Override
	public void update(int index) {
		player.dispatch(new ItemElementUpdatePacketEncoder(elements[index], index, 3214));
	}

	@Override
	public void update() {
		player.dispatch(new ItemElementsUpdatePacketEncoder(elements, 3214));
	}

}
