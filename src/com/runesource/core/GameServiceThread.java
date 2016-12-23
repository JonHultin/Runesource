package com.runesource.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.runesource.core.world.World;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class GameServiceThread extends AbstractScheduledService {

	private static final int TICK_CYCLE = 600;
	
	private final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	private final Phaser phaser = new Phaser(1);
	
	private final World world;
	
	public GameServiceThread() {
		this.world = World.getSingleton();
	}
	
	@Override protected void runOneIteration() throws Exception {
		for (Player player : world.getPlayers()) {
			player.getEventHandler().updateMovement();
		}
		for (Player player : world.getPlayers()) {
			phaser.bulkRegister(world.getPlayers().size());
			service.execute(() -> {
				synchronized (player) {
					try {
						player.getEventHandler().update();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						phaser.arriveAndDeregister();
					}
				}
			});
			phaser.arriveAndAwaitAdvance();
		}
		for (Player player : world.getPlayers()) {
			player.reset();
		}
	}

	@Override protected Scheduler scheduler() {
		return Scheduler.newFixedRateSchedule(TICK_CYCLE, TICK_CYCLE, TimeUnit.MILLISECONDS);
	}

}
