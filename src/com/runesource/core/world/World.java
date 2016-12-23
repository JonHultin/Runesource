package com.runesource.core.world;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runesource.core.world.model.entity.mobile.npc.Npc;
import com.runesource.core.world.model.entity.mobile.player.Player;
import com.runesource.util.IndexQueue;

public final class World implements Runnable {

	private static final World singleton = new World();

	private final Map<Integer, Player> playerRespitory = new ConcurrentHashMap<>(2000);

	private final IndexQueue playerIndices = new IndexQueue(2000);

	private final Map<Integer, Npc> npcRespitory = new ConcurrentHashMap<>(8000);

	private final IndexQueue npcIndices = new IndexQueue(8000);
			
	private World() { }

	public void register(Player player) {
		if (contains(player)) {
			return;
		}
		player.setIndex(playerIndices.nextValue());
		playerRespitory.put(player.getIndex(), player);
	}

	public void unregister(Player player) {
		playerRespitory.remove(player.getIndex());
		playerIndices.openValue(player.getIndex());
	}

	public void register(Npc npc) {
		npc.setIndex(npcIndices.nextValue());
		npcRespitory.put(npc.getIndex(), npc);
	}

	public void unregister(Npc npc) {
		npcRespitory.remove(npc.getIndex());
		npcIndices.openValue(npc.getIndex());
	}

	public boolean contains(Player player) {
		for (Player other : getPlayers()) {
			if (player.getUsername().equals(other.getUsername())) {
				return true;
			}
		}
		return false;
	}
	
	public Collection<Player> getPlayers() {
		return playerRespitory.values();
	}

	public Collection<Npc> getNpcs() {
		return npcRespitory.values();
	}

	public static World getSingleton() {
		return singleton;
	}

	@Override
	public void run() {
		while (true) {
			long start = System.currentTimeMillis();
			try {
				synchronized (this) {
					playerRespitory.values().forEach($it -> $it.getEventHandler().updateMovement($it));
					playerRespitory.values().forEach($it -> $it.getEventHandler().update($it));
					playerRespitory.values().forEach($it -> $it.reset());
					//System.out.println("Current Player Population: " + getPlayers().size());
				}
				long elapsed = System.currentTimeMillis() - start;
				long sleepTime = 600 - elapsed;
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
