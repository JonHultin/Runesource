package com.runesource.game.world.model.entity.item;

import com.runesource.game.world.Position;
import com.runesource.game.world.model.entity.Entity;
import com.runesource.game.world.model.entity.EntityEventListener;

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
