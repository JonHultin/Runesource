package com.runesource.network.packet;

import com.runesource.game.world.model.entity.mobile.player.Player;

public interface PacketDecoder {

	public void execute(Player player, Packet packet);
}
