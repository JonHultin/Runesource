package com.runesource.core.world.model.entity.mobile;

import com.runesource.core.world.model.entity.EntityEventListener;

public interface MobileEntityEventListener<T extends MobileEntity> extends EntityEventListener<T> {

	public void updateMovement(T entity);
	
	public void register(T entity);
	
	public void unregister(T entity);
	
}
