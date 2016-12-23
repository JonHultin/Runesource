package com.runesource.core.world.model.entity.mobile.player;

public final class PlayerSettings {

	private boolean mouseButtons;
	
	private boolean splitChat;
	
	private boolean chatEffects;
	
	private boolean acceptAid;
	
	private int brightness;

	public PlayerSettings() {
		this(true, false, true, false, 2);
	}
	
	public PlayerSettings(boolean mouseButtons, boolean splitChat, boolean chatEffects, boolean acceptAid, int brightness) {
		this.mouseButtons = mouseButtons;
		this.splitChat = splitChat;
		this.chatEffects = chatEffects;
		this.acceptAid = acceptAid;
		this.brightness = brightness;
	}

	public boolean isMouseButtons() {
		return mouseButtons;
	}

	public void setMouseButtons(boolean mouseButtons) {
		this.mouseButtons = mouseButtons;
	}

	public boolean isSplitChat() {
		return splitChat;
	}

	public void setSplitChat(boolean splitChat) {
		this.splitChat = splitChat;
	}

	public boolean isChatEffects() {
		return chatEffects;
	}

	public void setChatEffects(boolean chatEffects) {
		this.chatEffects = chatEffects;
	}

	public boolean isAcceptAid() {
		return acceptAid;
	}

	public void setAcceptAid(boolean acceptAid) {
		this.acceptAid = acceptAid;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

}
