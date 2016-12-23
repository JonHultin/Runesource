package com.runesource.core.world.model.entity;

import com.runesource.core.world.Position;

public abstract class Entity<T extends EntityEventListener<?>> {

	private final Position position;
	
	private int index;
	
	public Entity(Position position) {
		this.position = position;
	}

	public final int getIndex() {
		return index;
	}

	public final void setIndex(int index) {
		this.index = index;
	}

	public final Position getPosition() {
		return position;
	}

	public abstract T getEventHandler();
}
