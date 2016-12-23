package com.runesource.game.world.model.entity.mobile.npc;

import com.runesource.game.world.Position;
import com.runesource.game.world.model.entity.mobile.MobileEntity;

public class Npc extends MobileEntity<NpcEventHandler> {

	private final NpcEventHandler eventHandler = new NpcEventHandler(this);
	
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

	public int getId() {
		return id;
	}

	@Override public NpcEventHandler getEventHandler() {
		return eventHandler;
	}

}
