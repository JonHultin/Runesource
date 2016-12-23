package com.runesource.network.channel;

import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.game.world.model.entity.mobile.player.PlayerCredentials;
import com.runesource.network.packet.Packet;
import com.runesource.network.packet.PacketManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public final class ChannelInboundMessageHandler extends SimpleChannelInboundHandler<Object> {

	private PacketManager packetManager = new PacketManager();
	
	private Player player;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof PlayerCredentials) {
			Player player = new Player((PlayerCredentials) msg);
			setPlayer(player);
			player.getEventHandler().register();
		}
		if (msg instanceof Packet) {
			packetManager.notify(player, (Packet) msg);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
