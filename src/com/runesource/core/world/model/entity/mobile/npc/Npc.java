package com.runesource.core.world.model.entity.mobile.npc;

import com.runesource.core.world.Position;
import com.runesource.core.world.model.entity.mobile.MobileEntity;

public class Npc extends MobileEntity {

	private final NpcEventHandler eventHandler = new NpcEventHandler();
	
	private final int id;

	private boolean visible;

	public Npc(Position position, int id) {
		super(position);
		this.id = id;
		this.visible = true;
		this.setUpdateRequired(true);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public NpcEventHandler getEventHandler() {
		return eventHandler;
	}

	public int getId() {
		return id;
	}

}
