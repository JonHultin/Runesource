package com.runesource.core.world.model.entity.mobile;

import com.runesource.core.world.model.entity.EntityEventListener;

public abstract class MobileEntityEventListener<T extends MobileEntity<?>> extends EntityEventListener<T> {

	public MobileEntityEventListener(T entity) {
		super(entity);
	}

	public abstract void updateMovement();
	
}
