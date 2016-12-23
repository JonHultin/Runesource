package com.runesource.core.world.model.entity.mobile.player;

import com.runesource.core.network.packet.encoders.ChatMessagePacketEncoder;
import com.runesource.core.network.packet.encoders.GameFrameTabPacketEncoder;
import com.runesource.core.network.packet.encoders.LogoutPacketEncoder;
import com.runesource.core.network.packet.encoders.NpcUpdatePacketEncoder;
import com.runesource.core.network.packet.encoders.PlayerRegistrationPacketEncoder;
import com.runesource.core.network.packet.encoders.PlayerUpdatePacketEncoder;
import com.runesource.core.network.packet.encoders.RegionUpdatePacketEncoder;
import com.runesource.core.world.World;
import com.runesource.core.world.model.entity.mobile.MobileEntityEventListener;
import com.runesource.core.world.model.entity.mobile.player.io.PlayerSerialization;

public final class PlayerEventHandler extends MobileEntityEventListener<Player> {

	public PlayerEventHandler(Player entity) {
		super(entity);
	}

	@Override public void updateMovement() {
		getEntity().getMovementHandler().process();
	}

	@Override public void update() {
		getEntity().dispatch(new PlayerUpdatePacketEncoder());
		getEntity().dispatch(new NpcUpdatePacketEncoder());
		PlayerSerialization.serialize(getEntity());
	}

	@Override public void register() {
		getEntity().dispatch(new PlayerRegistrationPacketEncoder());
		getEntity().dispatch(new RegionUpdatePacketEncoder());
		getEntity().setUpdateRequired(true);
		getEntity().setAppearanceUpdateRequired(true);
		getEntity().dispatch(new GameFrameTabPacketEncoder(0, 2423));
		getEntity().dispatch(new GameFrameTabPacketEncoder(1, 3917));
		getEntity().dispatch(new GameFrameTabPacketEncoder(2, 638));
		getEntity().dispatch(new GameFrameTabPacketEncoder(3, 3213));
		getEntity().dispatch(new GameFrameTabPacketEncoder(4, 1644));
		getEntity().dispatch(new GameFrameTabPacketEncoder(5, 5608));
		getEntity().dispatch(new GameFrameTabPacketEncoder(6, 1151));
		getEntity().dispatch(new GameFrameTabPacketEncoder(8, 5065));
		getEntity().dispatch(new GameFrameTabPacketEncoder(9, 5715));
		getEntity().dispatch(new GameFrameTabPacketEncoder(10, 2449));
		getEntity().dispatch(new GameFrameTabPacketEncoder(11, 4445));
		getEntity().dispatch(new GameFrameTabPacketEncoder(12, 147));
		getEntity().dispatch(new GameFrameTabPacketEncoder(13, 6299));
		getEntity().dispatch(new ChatMessagePacketEncoder("Welcome to Runescape."));
		getEntity().getInventoryContainer().update();
		World.getSingleton().register(getEntity());
	}

	@Override public void unregister() {
		getEntity().dispatch(new LogoutPacketEncoder());
		World.getSingleton().unregister(getEntity());
	}

}
