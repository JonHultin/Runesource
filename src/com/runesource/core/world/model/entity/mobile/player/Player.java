package com.runesource.core.world.model.entity.mobile.player;

import com.runesource.core.network.packet.OutboundPacket;
import com.runesource.core.network.packet.out.RegionalUpdatePacket;
import com.runesource.core.network.security.SecureCipher;
import com.runesource.core.world.Position;
import com.runesource.core.world.model.entity.item.containers.InventoryItemContainer;
import com.runesource.core.world.model.entity.mobile.MobileEntity;
import com.runesource.core.world.model.entity.mobile.MovementHandler;

import io.netty.channel.Channel;

public class Player extends MobileEntity {
	
	private final InventoryItemContainer inventoryContainer = new InventoryItemContainer(this);
	
	private final MovementHandler movementHandler = new MovementHandler(this);
	
	private final PlayerRegionManager regionManager = new PlayerRegionManager();
	
	private final PlayerEventHandler eventHandler = new PlayerEventHandler();
	
	private final PlayerAppearance appearance = new PlayerAppearance();
	
	private final PlayerSettings settings = new PlayerSettings();
	
	private final Channel channel;
	
	private final String username;
	
	private final String password;
	
	private final SecureCipher secureRead;
	
	private final SecureCipher secureWrite;	
	
	private int staffRights = 0;
	
	private int chatColor;
	
	private int chatEffects;
	
	private byte[] chatText;
	
	private boolean forcedMovementRequired;
	
	private boolean appearanceUpdateRequired;
	
	private boolean chatUpdateRequired;
	
	private boolean needsPlacement;
	
	private boolean resetMovementQueue;

	public Player(PlayerCredentials credentials) {
		super(new Position(3222, 3222));
		this.channel = credentials.getChannel();
		this.username = credentials.getUsername();
		this.password = credentials.getPassword();
		this.secureRead = credentials.getDechiper();
		this.secureWrite = credentials.getEncipher();
	}
	
	public void dispatch(OutboundPacket packet) {
		if (!channel.isActive()) {
			return;
		}
		channel.writeAndFlush(packet.dispatch(this));
	}

	public void process() throws Exception {
		movementHandler.process();
	}

	public void teleport(Position position) {
		movementHandler.reset();
		getPosition().setAs(position);
		setResetMovementQueue(true);
		setNeedsPlacement(true);
		dispatch(new RegionalUpdatePacket());
	}

	public void reset() {
		setWalkingDirection(-1);
		setRunningDirection(-1);
		setUpdateRequired(false);
		setForcedMovementRequired(false);
		setAppearanceUpdateRequired(false);
		setChatUpdateRequired(false);
		setResetMovementQueue(false);
		setNeedsPlacement(false);
	}
	
	public InventoryItemContainer getInventoryContainer() {
		return inventoryContainer;
	}

	public PlayerEventHandler getEventHandler() {
		return eventHandler;
	}

	public PlayerAppearance getAppearance() {
		return appearance;
	}

	public MovementHandler getMovementHandler() {
		return movementHandler;
	}

	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	public boolean needsPlacement() {
		return needsPlacement;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		if (appearanceUpdateRequired) {
			setUpdateRequired(true);
		}
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public final boolean isForcedMovementRequired() {
		return forcedMovementRequired;
	}

	public final void setForcedMovementRequired(boolean forcedMovementRequired) {
		this.forcedMovementRequired = forcedMovementRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setStaffRights(int staffRights) {
		this.staffRights = staffRights;
	}

	public int getStaffRights() {
		return staffRights;
	}

	public void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	public boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	public void setChatColor(int chatColor) {
		this.chatColor = chatColor;
	}

	public int getChatColor() {
		return chatColor;
	}

	public void setChatEffects(int chatEffects) {
		this.chatEffects = chatEffects;
	}

	public int getChatEffects() {
		return chatEffects;
	}

	public void setChatText(byte[] chatText) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatUpdateRequired(boolean chatUpdateRequired) {
		if (chatUpdateRequired) {
			setUpdateRequired(true);
		}
		this.chatUpdateRequired = chatUpdateRequired;
	}

	public boolean isChatUpdateRequired() {
		return chatUpdateRequired;
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

	public SecureCipher getSecureRead() {
		return secureRead;
	}

	public SecureCipher getSecureWrite() {
		return secureWrite;
	}

	public boolean isNeedsPlacement() {
		return needsPlacement;
	}

	public PlayerRegionManager getRegionManager() {
		return regionManager;
	}

	public PlayerSettings getSettings() {
		return settings;
	}

}
