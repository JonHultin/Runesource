 package com.runesource.core.world.model.entity.item;

import com.runesource.core.world.model.entity.mobile.player.Player;

public abstract class ItemContainer {

	protected final Item[] elements;
	
	protected final Player player;
	
	protected final int capacity;
	
	public ItemContainer(Player player, int capacity) {
		this.elements = new Item[capacity];
		this.player = player;
		this.capacity = capacity;
	}
	
	public final void swap(int indexFrom, int indexTo) {
		Item item = elements[indexFrom];
		set(indexFrom, elements[indexTo].getId(), elements[indexTo].getAmount());
		set(indexTo, item.getId(), item.getAmount());
		update();
	}
	
	public final void set(int index, int id, int amount) {
		elements[index] = new Item(id, amount);
		update();
	}
	
	public final void removeFrom(int index, int amount) {
		if (elements[index] != null) {
			if (ItemDefinition.get(elements[index].getId()).isStackable()) {
				if (elements[index].getAmount() > amount) {
					elements[index].setAmount(elements[index].getAmount() - amount);
				} else {
					set(index, -1, 0);
				}
			} else {
				set(index, -1, 0);
			}
			if (elements[index].getId() == -1) {
				elements[index] = null;
			}
			update();
		}
	}
	
	public abstract void add(int id, int amount);
	
	public abstract void remove(int id, int amount);
	
	public abstract void update(int index);
	
	public abstract void update();
	
	public final boolean contains(int id) {
		for (Item element : elements) {
			if (element == null) {
				continue;
			}
			if (element.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public final boolean available(int id) {
		if (available() > 0) {
			return true;
		}
		if (contains(id) && ItemDefinition.get(id).isStackable()) {
			return true;
		}
		return false;
	}
	
	public final int available() {
		int count = 0;
		for (Item element : elements) {
			if (element == null) {
				count ++;
			}
		}
		return count;
	}
	
	public final int getCapacity() {
		return capacity;
	}

}
