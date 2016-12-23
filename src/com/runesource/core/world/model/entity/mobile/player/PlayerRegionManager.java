package com.runesource.core.world.model.entity.mobile.player;

import java.util.Collection;
import java.util.LinkedList;

import com.runesource.core.world.Position;
import com.runesource.core.world.model.entity.item.ItemEntity;
import com.runesource.core.world.model.entity.mobile.npc.Npc;

public final class PlayerRegionManager {

	private final Collection<ItemEntity> items = new LinkedList<>();
	
	private final Collection<Player> players = new LinkedList<>();
	 
	private final Collection<Npc> npcs = new LinkedList<Npc>();
	
	private final Position position;
	
	public PlayerRegionManager() {
		this.position = new Position();
	}

	public void add(Player player) {
		players.add(player);
	}
	
	public Collection<ItemEntity> getItems() {
		return items;
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public Collection<Npc> getNpcs() {
		return npcs;
	}

	public Position getPosition() {
		return position;
	}
	
}
