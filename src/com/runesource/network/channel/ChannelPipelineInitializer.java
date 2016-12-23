package com.runesource.network.channel;

import com.runesource.network.channel.handlers.HandshakeInboundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public final class ChannelPipelineInitializer extends ChannelInitializer<SocketChannel> {

	@Override protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("decoder", new HandshakeInboundHandler());
		ch.pipeline().addLast("handler", new ChannelInboundMessageHandler());
	}

}
