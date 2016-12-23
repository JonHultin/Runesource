package com.runesource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.runesource.core.network.Server;
import com.runesource.core.world.World;

public final class Runesource {

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);
	
	private static final Logger LOGGER = Logger.getLogger(Runesource.class.getName());

	public static void main(String[] args) {
		LOGGER.info("Initializing server communications.");
		EXECUTOR_SERVICE.submit(new Server(43594));
		EXECUTOR_SERVICE.submit(World.getSingleton());
		LOGGER.info("Runesource framework is now online.");
	}

}
