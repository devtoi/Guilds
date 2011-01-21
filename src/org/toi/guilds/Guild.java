package org.toi.guilds;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

class Guild {

	private ArrayList<GPlayer> players = new ArrayList<GPlayer>();
	private String name = "";
	private int score = 0;
	private ChatColor color;
	private boolean joinable = true;
	private GuildArea area = new GuildArea();
	private Hashtable<String, Integer> perms = new Hashtable<String, Integer>();
    private ArrayList<String> admins = new ArrayList<String>();
	private GuildKind kind;
	private ArrayList<ItemNeeded> items = new ArrayList<ItemNeeded>();
	private Location guildSpawn;
    
	public Guild()
	{
		
	}
	
	public Guild(String name)
	{
		this.name = name;
		this.score = 0;
		this.color = ChatColor.WHITE;
		this.area = new GuildArea(this.name);
	}
	
	public Guild(String name, int score)
	{
		this.name = name;
		this.score = score;
		this.color = ChatColor.WHITE;
	}

	public Guild(String name, int score, ChatColor color)
	{
		this.name = name;
		this.score = score;
		this.color = color;
	}

	public Guild(String name, int score, ChatColor color, ArrayList<GPlayer> players)
	{
		this.name = name;
		this.score = score;
		this.color = color;
		this.players = players;
	}

	public Guild(String name, int score, ChatColor color, GuildKind kind)
	{
		this.name = name;
		this.score = score;
		this.color = color;
		this.kind = kind;
	}
	
	public void setPerm(String key, int value)
	{
		this.perms.put(key, value);
	}
	
	public int getPerm(String key)
	{
		if (this.perms.containsKey(key))
			return this.perms.get(key);
		else
			return 0;
	}
	
	public boolean promotePlayer(String playerToPromote, String promoterName)
	{
		for (GPlayer gp : this.players)
		{
			if (gp.getName().equalsIgnoreCase(playerToPromote))
			{
				gp.promote();
				return true;
			}
		}
		return false;
	}
	
	public void promotePlayer(String admin, int amount)
	{
		for (GPlayer gp : this.players)
		{
			if (gp.getName().equalsIgnoreCase(admin))
			{
				gp.setRank(amount);
			}
		}
	}
	
	public boolean demotePlayer(String playerToDemote, String demoterName)
	{
		for (GPlayer gp : this.players)
		{
			if (gp.getName().equalsIgnoreCase(playerToDemote))
			{
				gp.demote();
				return true;
			}
		}
		return false;
	}
	
	public boolean isInArea(double x, double z)
	{
		boolean inside = false;
		if (x < area.getXorigin() + this.area.getSize() - 1 && x > area.getXorigin() - this.area.getSize() - 1 &&
			z < area.getZorigin() + this.area.getSize() && z > area.getZorigin() - this.area.getSize())
		{
			inside = true;
		}
		return inside;
	}
	
	public boolean isNearArea(double x, double z, double tolerance)
	{
		boolean inside = false;
		if (x < area.getXorigin() + this.area.getSize() + tolerance - 1 && x > area.getXorigin() - this.area.getSize() - tolerance - 1 &&
			z < area.getZorigin() + this.area.getSize() + tolerance && z > area.getZorigin() - this.area.getSize() - tolerance)
		{
			inside = true;
		}
		return inside;
	}
	
	public boolean hasMember(String playername)
	{
		for(GPlayer player : this.players)
		{
			if(player.getName().equalsIgnoreCase(playername))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean loadFromFile(String name)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("Guilds" + File.separator + name + ".txt"));
			try
			{
				String line = br.readLine();
				if (line != null)
				{
					while (line != null)
					{
						if (!line.equalsIgnoreCase("-----Permissions-----"))
						{
							if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
							{
								if (line.startsWith("name"))
								{
									String[] theName = line.split(":");
									if (!theName[1].equalsIgnoreCase("") && theName[1] != null)
										this.name = theName[1];
								}
								else if (line.startsWith("color"))
								{
									String[] theColor = line.split(":");
									if (!theColor[1].equalsIgnoreCase("") && theColor[1] != null)
									{
										this.color = ChatColor.getByCode(Integer.valueOf(theColor[1], 16));
									}
								}
								else if (line.startsWith("score"))
								{
									String[] theScore = line.split(":");
									if (!theScore[1].equalsIgnoreCase("") && theScore[1] != null)
										this.score = Integer.valueOf(theScore[1]);
								}
								else if (line.startsWith("joinable"))
								{
									String[] theJoinable = line.split(":");
									if (!theJoinable[1].equalsIgnoreCase("") && theJoinable[1] != null)
									{
										this.joinable = Boolean.parseBoolean(theJoinable[1]);
									}
								}
								else if (line.startsWith("kind"))
								{
									String[] kind = line.split(":");
									if (kind.length >= 2)
									{
										if (!kind[1].equalsIgnoreCase("") && kind[1] != null)
										{
											boolean added = false;
											for (GuildKind gk : GHolder.guildKinds)
											{
												if (gk.getName().equalsIgnoreCase(kind[1]))
												{
													this.kind = new GuildKind(gk);
													added = true;
													break;
												}
											}
											if (!added)
												System.out.println("[Guilds] Failed to load " + kind[1] + " guild kind");
										}
									}
								}
								else if (line.startsWith("admins"))
								{
									String[] splt = line.split(":");
									if (splt.length >= 2)
									{
										if (splt[1] != null)
										{
											if (!splt[1].equals(""))
											{
												String[] admins = splt[1].split(",");
												if (admins != null)
												{
													for (String admin : admins)
													{
														this.admins.add(admin);
													}
												}
											}
										}
									}
								}
								else if (line.startsWith("guildspawn"))
								{
									String[] ln = line.split(":");
									if (ln.length >= 2)
									{
										String[] spawnPoint = ln[1].split(",");
										if (spawnPoint != null)
										{
											if (spawnPoint.length == 3)
											{
												this.guildSpawn = new Location(
														GHolder.plugin.getServer().getWorlds()[0],
														Double.valueOf(spawnPoint[0]), 
														Double.valueOf(spawnPoint[1]),
														Double.valueOf(spawnPoint[2]));
											}
										}
									}
								}
							}
							line = br.readLine();
						}
						else
							break;
					}
					line = br.readLine();
					while (line != null)
					{
						if (!line.equalsIgnoreCase("-----Players-----"))
						{
							if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
							{
								String[] vals = line.split(":");
								if (vals.length >= 2)
									this.perms.put(vals[0], Integer.valueOf(vals[1]));
								else
									GHolder.log.info("[Guilds] Failed to interpret " + vals[0] + ":" + vals[1]);
							}
							line = br.readLine();
						}
						else
							break;
					}
					while (line != null)
					{
						if (!line.equalsIgnoreCase("-----Area-----"))
						{
							if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
							{
								String lineSplit [] = line.split(",");
								if (lineSplit.length > 1)
								{
									if  (!lineSplit[1].equalsIgnoreCase(""))
										this.players.add(new GPlayer(lineSplit[0], Integer.valueOf(lineSplit[1])));
								}
							}
							line = br.readLine();
						}
						else
							break;
					}
					if (line != null)
					{
						GuildArea tempGA = new GuildArea();
						tempGA.areaSize(this.players.size());
						int countFoundAll = 3;
						line = br.readLine();
						while (line != null)
						{
							if (!line.equalsIgnoreCase("-----Items-----"))
							{
								if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
								{
									if (line.startsWith("x"))
									{
										String[] x = line.split(":");
										if (!x[1].equalsIgnoreCase("") && x[1] != null)
											{
												tempGA.setXorigin(new Double(x[1]));
												countFoundAll++;
											}
									}
									else if (line.startsWith("z"))
									{
										String[] z = line.split(":");
										if (!z[1].equalsIgnoreCase("") && z[1] != null)
											{
												tempGA.setZorigin(new Double(z[1]));
												countFoundAll++;
											}
									}
									else if (line.startsWith("allow-access"))
									{
										String[] allow = line.split(":");
										if (!allow[1].equalsIgnoreCase("") && allow[1] != null)
											{
												tempGA.setAllowAccess(Boolean.parseBoolean(allow[1]));
												countFoundAll++;
											}
									}
								}
								line = br.readLine();
							}
							else
								break;
						}
						if (countFoundAll >= 3)
						{
							this.area = tempGA;
						}
						line = br.readLine();
						while (line != null)
						{
							if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
							{
								String[] linesplt = line.split(",");
								if (linesplt.length > 1)
								{
									if (GHolder.isIntNumber(linesplt[0]) && GHolder.isIntNumber(linesplt[1]))
									{
										int iid = Integer.valueOf(linesplt[0]);
										int amount = Integer.valueOf(linesplt[1]);
										if (iid >= 0 && amount >= 0)
											this.items.add(new ItemNeeded(iid, amount));
									}
								}
							}
							line = br.readLine();
						}
					}
				}
					
				return true;
			}
			catch (IOException e)
			{
				return false;
			}
			finally
			{
				br.close();
			}
		}
		catch (IOException e)
		{
			return false;
		}
	}
	
	public void saveToFile()
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter("Guilds" + File.separator + this.name + ".txt"));
			try
			{
				bw.write("name:" + this.name);
				bw.newLine();
				bw.write("score:" + this.score);
				bw.newLine();
				bw.write("color:" + this.color.toString().substring(1));
				bw.newLine();
				bw.write("joinable:" + this.joinable);
				bw.newLine();
				bw.write("//Separate admin names with a comma (,) without spaces");
				bw.newLine();
				bw.write("admins:");
				boolean first = true;
				for (String admin : this.admins)
				{
					if (first)
						bw.write(admin);
					else
						bw.write("," + admin);
					first = false;
				}
				bw.newLine();
				bw.write("kind:");
				try
				{
					bw.write(this.kind.getName());
				}
				catch (NullPointerException npe)
				{
					System.out.println("[Guilds] Guild " + this.getName() + " has no guild kind!\nPlease add one in the guild file");
				}
				bw.newLine();
				bw.write("guildspawn:");
				if (this.guildSpawn != null)
					bw.write(String.valueOf(this.guildSpawn.getX()) + "," + String.valueOf(this.guildSpawn.getY()) + "," + String.valueOf(this.guildSpawn.getBlockZ()));
				bw.newLine();
				
				bw.write("-----Permissions-----");
				bw.newLine();
				for (Enumeration<String> e = this.perms.keys(); e.hasMoreElements();)
				{
					String key = (String)e.nextElement();
					Integer val = (Integer)this.perms.get(key);
					bw.write(key + ":" + val);
					bw.newLine();
				}
				bw.write("-----Players-----");
				bw.newLine();
				for (GPlayer fwp : this.players)
				{
					bw.write(fwp.getName() + "," + fwp.getRank());
					bw.newLine();
				}
				
				bw.write("-----Area-----");
				bw.newLine();
				bw.write("x:" + this.area.getX());
				bw.newLine();
				bw.write("z:" + this.area.getZ());
				bw.newLine();
				bw.write("allow-access:" + this.area.isAccessible());
				bw.newLine();
				bw.write("-----Items-----");
				bw.newLine();
				boolean firstin = true;
				for (ItemNeeded in : this.items)
				{
					if (firstin)
					{
						bw.write(String.valueOf(in.getItemIndex()) + "," + String.valueOf(in.getItemIndex()));
						firstin = false;
					}
					else
					{
						bw.newLine();
						bw.write(String.valueOf(in.getItemIndex()) + "," + String.valueOf(in.getItemIndex()));
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				bw.flush();
				bw.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<GPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<GPlayer> players) {
		this.players = players;
	}

	//GuildArea stuff
	public GuildArea getGuildArea()
	{
		area.areaSize(players.size());
		return area;
	}
	
	//GuildArea stuff
	public void setGuildArea(GuildArea temparea)
	{
		area = temparea;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void addPlayer(String name)
	{
		boolean contains = false;
		for (GPlayer fwp : this.players)
		{
			if (fwp.name.equalsIgnoreCase(name))
			{
				contains = true;
			}
		}
		if (!contains)
		{
			this.players.add(new GPlayer(name, 0));
		}
	}

	public boolean doUpgrade()
	{
		if (kind.hasEnoughForNextLevel(this.score, this.items))
		{
			score++;
			this.saveToFile();
			return true;
		}
		else
			return false;
	}
	
	public void printNeededItemsToLevel(Player player)
	{
		if (this.score < this.kind.getMaxLevel())
		{
			player.sendMessage("Items needed for next level " + String.valueOf((score + 1) + "/" + String.valueOf(this.kind.getMaxLevel())));
			for (ItemNeeded kin : this.kind.getItemsNeeded())
			{
				if (kin.getLevel() == score + 1)
				{
					if (this.items.size() > 0)
					{
						boolean foundItem = false;
						for (ItemNeeded gin : this.items)
						{
							if (gin.getItemIndex() == kin.getItemIndex())
							{
								foundItem = true;
								sendRequItems(player, kin.getItemIndex(), gin.getAmount(), kin.getAmount());
							}
						}
						if (!foundItem)
						{
							sendRequItems(player, kin.getItemIndex(), 0, kin.getAmount());
						}
					}
					else
						sendRequItems(player, kin.getItemIndex(), 0, kin.getAmount());
				}
			}
		}
		else
			player.sendMessage(GHolder.gString() + "Your guild is at maxlevel!");
	}
	
	private void sendRequItems (Player player, int id, int gAmount, int kAmount)
	{
		String itemName = Material.getMaterial(id).toString();
		if (itemName != null)
			player.sendMessage(itemName + " - " + gAmount + "/" + kAmount);
	}
	
	public int getPlayerRank(String playerName)
	{
		for (GPlayer gp : this.players)
		{
			if (gp.getName().equalsIgnoreCase(playerName))
				return gp.getRank();
		}
		return 0;
	}
	
	public ChatColor getColor() {
		return color;
	}

	public void turnInItems(Player player, Material mat, int amount)
	{/*
		boolean hasItem = false;
		int itemPlace = -1;
		for (int i = 0; i < this.items.size(); i++)
		{
			if (this.items.get(i).getItemIndex() == itemIndex)
			{
				hasItem = true;
				itemPlace = i;
				break;
			}
		}
		
		if (!hasItem)
			this.items.add(new ItemNeeded(itemIndex, amount));
		else
			this.items.get(itemPlace).increaseAmount(amount);*/
	}
	
	public boolean addAdmin (String name)
	{
		boolean exists = false;
		for (String admin : this.admins)
		{
			if (admin.equalsIgnoreCase(name))
				exists = true;
		}
		if (!exists)
			this.admins.add(name);
		return exists;
	}
	
	public boolean isPlayerAdmin (String name)
	{
		if (this.admins.contains(name))
			return true;
		else
			return false;
	}
	
	public void setColor(String color) {
		this.color = ChatColor.valueOf(color);
	}

	public boolean isJoinable() {
		return joinable;
	}

	public void setJoinable(boolean joinable) {
		this.joinable = joinable;
	}

	public GuildArea getArea() {
		return area;
	}

	public void setArea(GuildArea area) {
		this.area = area;
	}

	public ArrayList<String> getAdmins() {
		return admins;
	}

	public void setAdmins(ArrayList<String> admins) {
		this.admins = admins;
	}

	public GuildKind getKind() {
		return kind;
	}

	public void setKind(GuildKind kind) {
		this.kind = kind;
	}

	public ArrayList<ItemNeeded> getItems() {
		return items;
	}

	public void setItems(ArrayList<ItemNeeded> items) {
		this.items = items;
	}

	public Location getGuildSpawn() {
		return guildSpawn;
	}

	public void setGuildSpawn(Location guildSpawn) {
		this.guildSpawn = guildSpawn;
	}
	
}
