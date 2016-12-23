package com.runesource.game.world.model.entity;

public abstract class EntityEventListener<T extends Entity<?>> {

	private final T entity;
	
	public EntityEventListener(T entity) {
		this.entity = entity;
	}
	
	public final T getEntity() {
		return entity;
	}

	public abstract void update();
	
	public abstract void register();
	
	public abstract void unregister();
}
