package com.runesource.core.world.model.entity;

public interface EntityEventListener<T extends Entity> {

	public void update(T entity);
}
