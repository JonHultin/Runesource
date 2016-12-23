package com.runesource;

import com.runesource.core.network.NetworkServer;

public final class Runesource {

	public static void main(String[] args) {
		NetworkServer server = new NetworkServer();
		server.init();
	}

}
