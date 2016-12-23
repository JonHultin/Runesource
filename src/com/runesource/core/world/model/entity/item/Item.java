package com.runesource.core.world.model.entity.item;

public final class Item {

	private int id;

	private int amount;

	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final int getAmount() {
		return amount;
	}

	public final void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void incrementAmountBy(int amount) {
		if(((long)this.amount + amount) > Integer.MAX_VALUE) {
			this.amount = Integer.MAX_VALUE;
		} else {
			this.amount += amount;
		}
	}

}
