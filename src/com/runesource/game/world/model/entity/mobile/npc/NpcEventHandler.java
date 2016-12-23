package com.runesource.game.world.model.entity.mobile.npc;

import com.runesource.game.world.World;
import com.runesource.game.world.model.entity.mobile.MobileEntityEventListener;

public final class NpcEventHandler extends MobileEntityEventListener<Npc> {

	public NpcEventHandler(Npc entity) {
		super(entity);
	}

	@Override public void updateMovement() {

	}

	@Override public void update() {

	}

	@Override public void register() {
		getEntity().setVisible(false);
		World.getSingleton().register(getEntity());
	}

	@Override public void unregister() {
		World.getSingleton().unregister(getEntity());
	}

}
