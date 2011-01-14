package org.toi.guilds;

class GuildArea {
	
	private String Owner;
	private int size;
	private boolean AllowAccess = true;
	private double Xorigin;
	private double Zorigin;
	private boolean isSet = false;
	
	public GuildArea()
	{
		
	}
	
	public GuildArea(int x, int z, boolean access)
	{
		this.AllowAccess = access;
		this.Xorigin = x;
		this.Zorigin = z;
		this.isSet = true;
	}
	
	public GuildArea(String guild)
	{
		this.Owner = guild;
	}
	
	public void xzSet(double x, double z)
	{
		this.Xorigin = x;
		this.Zorigin = z;
	}
	
	public String getOwner() 
	{
		return this.Owner;
	}
	
	public double getX() 
	{
		return this.Xorigin;
	}
	
	public double getZ() 
	{
		return this.Zorigin;
	}
	
	public int getSize() 
	{
		return this.size;
	}
	
	public boolean isAccessible()
	{
		return this.AllowAccess;
	}
	
	public void areaSize(int playerSize)
	{
		int memberCount = playerSize;
		this.isSet = true;
		this.size = GHolder.guildAreaStartSize + (memberCount * GHolder.guildAreaExpansion);
	}
	
	public boolean isAllowAccess() {
		return AllowAccess;
	}

	public void setAllowAccess(boolean allowAccess) {
		AllowAccess = allowAccess;
	}

	public double getXorigin() {
		return Xorigin;
	}

	public void setXorigin(double xorigin) {
		Xorigin = xorigin;
	}

	public double getZorigin() {
		return Zorigin;
	}

	public void setZorigin(double zorigin) {
		Zorigin = zorigin;
	}

	public boolean isSet() {
		return isSet;
	}

	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}

	public void setOwner(String owner) {
		Owner = owner;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	
}
