package org.toi.guilds;

class GPlayer {
	
	String name = "Unknown User";
	private int guildRank = 0;
	
	public GPlayer()
	{
		
	}
	
	public GPlayer(String name, int guildLevel)
	{
		this.name = name;
		this.guildRank = guildLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return guildRank;
	}

	public void setRank(int guildRank) {
		this.guildRank = guildRank;
	}
	
	public void promote()
	{
		guildRank++;
	}
	
	public void demote()
	{
		guildRank--;
	}
}
