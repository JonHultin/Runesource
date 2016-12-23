package com.runesource.core.world.model.entity.mobile.player;

import java.util.Arrays;

public final class PlayerAppearance {
	
	private static final int[] DEFAULT_APPEARANCE = { 0, 18, 26, 36, 0, 33, 42, 10, 0, 0, 0, 0, 0 };
	
    public static final int GENDER_MALE = 0;

    public static final int GENDER_FEMALE = 1;

    public static final int GENDER = 0;

    public static final int TORSO = 1;

    public static final int ARMS = 2;

    public static final int LEGS = 3;

    public static final int HEAD = 4;

    public static final int HANDS = 5;

    public static final int FEET = 6;

    public static final int BEARD = 7;

    public static final int HAIR_COLOR = 8;

    public static final int TORSO_COLOR = 9;

    public static final int LEG_COLOR = 10;

    public static final int FEET_COLOR = 11;

    public static final int SKIN_COLOR = 12;

	private final int[] appearance;
	
	public PlayerAppearance() {
		this(DEFAULT_APPEARANCE);
	}
	
	public PlayerAppearance(int[] appearance) {
		this.appearance = appearance;
	}
	
	public void set(int index, int value) {
		appearance[index] = value;
	}
	
	public void set(int[] appearance) {
		for (int index = 0; index < appearance.length; index++) {
			set(index, appearance[index]);
		}
	}
	
	public int[] toArray() {
		return Arrays.copyOf(appearance, appearance.length);
	}

}
