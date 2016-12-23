package com.runesource.core.world.model.entity.item;

import com.runesource.core.world.Position;
import com.runesource.core.world.model.entity.Entity;
import com.runesource.core.world.model.entity.EntityEventListener;

public final class ItemEntity extends Entity<EntityEventListener<ItemEntity>> {
	
	private final Item item;
	
	private boolean visible;
	
	public ItemEntity(Position position, Item item) {
		super(position);
		this.item = item;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Item getItem() {
		return item;
	}

	@Override public EntityEventListener<ItemEntity> getEventHandler() {
		return null;
	}
	
}
