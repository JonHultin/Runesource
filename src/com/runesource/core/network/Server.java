package com.runesource.core.network;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.runesource.core.network.codec.HandshakeDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public final class Server implements Runnable {

	private final int port;
	
	public Server(int port) {
		this.port = port;
	}

	private void init() throws InterruptedException {
		final ServerBootstrap bootstrap = new ServerBootstrap();
		final EventLoopGroup parent = new NioEventLoopGroup();
		final EventLoopGroup child = new NioEventLoopGroup();
		final SocketAddress address = new InetSocketAddress(port);
		
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("decoder", new HandshakeDecoder());
				ch.pipeline().addLast("handler", new ChannelHandler());
			}
			
		});
		bootstrap.group(parent, child);
		bootstrap.bind(address).channel().closeFuture().sync();
	}
	
	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		try {
			init();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
