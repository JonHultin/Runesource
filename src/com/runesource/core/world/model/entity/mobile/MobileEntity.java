package com.runesource.core.world.model.entity.mobile;

import com.runesource.core.world.Position;
import com.runesource.core.world.model.entity.Entity;

public abstract class MobileEntity<T extends MobileEntityEventListener<?>> extends Entity<T> {

	private boolean updateRequired;
	
	private int walkingDirection = -1;
	
	private int runningDirection = -1;
	
	public MobileEntity(Position position) {
		super(position);
	}

	public final boolean isUpdateRequired() {
		return updateRequired;
	}

	public final void setUpdateRequired(boolean updateRequired) {
		this.updateRequired = updateRequired;
	}

	public final int getWalkingDirection() {
		return walkingDirection;
	}

	public final void setWalkingDirection(int walkingDirection) {
		this.walkingDirection = walkingDirection;
	}

	public final int getRunningDirection() {
		return runningDirection;
	}

	public final void setRunningDirection(int runningDirection) {
		this.runningDirection = runningDirection;
	}

}
