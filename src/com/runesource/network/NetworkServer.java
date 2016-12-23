package com.runesource.network;

import java.util.logging.Logger;

import com.runesource.game.GameServiceThread;
import com.runesource.network.channel.ChannelPipelineInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public final class NetworkServer {

	private final Logger logger = Logger.getLogger(NetworkServer.class.getName());
	
	public void init() {
		logger.info("Initializing game service thread.");
		GameServiceThread service = new GameServiceThread();
		service.startAsync();
		
		logger.info("Initializing network bootstrap.");
		ServerBootstrap bootstrap = new ServerBootstrap();		
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelPipelineInitializer());
		bootstrap.group(new NioEventLoopGroup());
		bootstrap.bind(43594).syncUninterruptibly();
		logger.info("Server is now online.");
	}
	
}
