package org.toi.guilds;

class ItemNeeded {

	private int itemIndex;
	private int amount;
	private int level;

	public ItemNeeded(int itemIndex, int amount)
	{
		this.itemIndex = itemIndex;
		this.amount = amount;
	}
	
	public ItemNeeded(int level, int itemIndex, int amount)
	{
		this.level = level;
		this.itemIndex = itemIndex;
		this.amount = amount;
	}

	public int getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void increaseAmount(int inc)
	{
		this.amount += inc;
	}
}
