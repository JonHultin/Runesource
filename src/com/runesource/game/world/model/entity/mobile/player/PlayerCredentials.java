package com.runesource.game.world.model.entity.mobile.player;

import com.runesource.network.security.SecureCipher;

import io.netty.channel.Channel;

public final class PlayerCredentials {

	private final Channel channel;
	
	private final String username;
	
	private final String password;
	
	private final SecureCipher dechiper;
	
	private final SecureCipher encipher;

	public PlayerCredentials(Channel channel, String username, String password, SecureCipher dechiper, SecureCipher encipher) {
		this.channel = channel;
		this.username = username;
		this.password = password;
		this.dechiper = dechiper;
		this.encipher = encipher;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public SecureCipher getDechiper() {
		return dechiper;
	}

	public SecureCipher getEncipher() {
		return encipher;
	}
	
}
