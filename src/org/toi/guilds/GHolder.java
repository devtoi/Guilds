package org.toi.guilds;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;


public final class GHolder {
	
	private ArrayList<Guild> guilds = new ArrayList<Guild>();
	private ArrayList<String> pendingGuildInvites = new ArrayList<String>();
	private tProperties properties = new tProperties("Guilds" + File.separator + "guilds.properties");
	static int guildAreaExpansion = 3;
	static int guildAreaStartSize = 3;
	private ArrayList<Command> guildCommands = new ArrayList<Command>();
	static ArrayList<GuildKind> guildKinds = new ArrayList<GuildKind>();
	private ArrayList<Party> partys = new ArrayList<Party>();
	static Plugin plugin;
	private tPermissions tps = new tPermissions("Guilds" + File.separator + "permissions.txt");
	
	public GHolder(){}
	
	static String gString ()
	{
		return ChatColor.AQUA + "[Guilds] " + ChatColor.YELLOW;
	}
	
	public boolean playerHasEnoughItem(Player player, int iid, int amount)
	{/*
		PlayerInventory inv = new PlayerInventory(player);
		if (inv.hasItem(iid, amount))
			return true;
		else
			return false;*/
		return true;// TODO remove this
	}
	
	public void playerTakeItems(Player player, int iid, int amount)
	{/*
		PlayerInventory inv = new PlayerInventory(player);
		inv.removeItem(iid, amount);*/
	}
	
	public void sendTurninItems (Player player, int id, int kAmount)
	{/*
		String itemName = etc.getDataSource().getItem(id);
		player.sendMessage("You turned in " + kAmount + " " + itemName);*/
	}
	
	public String setGuildHome(Player player) {
		for (Guild guild : this.guilds)
		{
			if (guild.hasMember(player.getName()))
			{
				if (guild.isPlayerAdmin(player.getName()))
				{
					if (guild.isNearArea(player.getLocation().getX(), player.getLocation().getZ(), 2.0))
					{
						guild.setGuildSpawn(player.getLocation());
						return "The guild home is now set!";
					}
					else
						return "The guild home has to be in the guild area!";
				}
				else
					return "You do not have permission to do that!";
			}
		}
		return "You are not in a guild yet!";
	}

	public void printAvaliableCommands (Player player, int page)
	{
		boolean hasCommands = false;
		ArrayList<String> msgList = new ArrayList<String>();
		int counter = 0;
		int nrOfCommandsAvaliable = 0;
		for (Command cmd : this.guildCommands)
		{
			if (tps.canPlayerUseCommand(player.getName(), cmd.getCommand()))
			{
				if (counter < (7 * page))
				{
					if (counter >= (7 * (page - 1)))
					{
						if (cmd.getSyntaxes().equals(""))
							msgList.add(ChatColor.DARK_RED + cmd.getCommand() + ChatColor.WHITE + " - " + cmd.getDescription());
						else
							msgList.add(ChatColor.DARK_RED +  cmd.getCommand() + " " + ChatColor.AQUA + cmd.getSyntaxes() + ChatColor.WHITE + " - " + cmd.getDescription());
					}
					counter++;
				}
				hasCommands = true;
				nrOfCommandsAvaliable++;
			}
		}
		if (!hasCommands)
		{
			player.sendMessage(gString() + "You do not have any avaliable commands");
		}
		else if (page > (nrOfCommandsAvaliable/7) + 1 || page < 1)
		{
			player.sendMessage(gString() + "Invalid pagenumber, " + String.valueOf((nrOfCommandsAvaliable/7)));
		}
		else
		{
			player.sendMessage(gString() + "Command list page " + String.valueOf(page) + "/" + String.valueOf((nrOfCommandsAvaliable/7) + 1) + ":");
			for (String msg : msgList)
			{
				player.sendMessage(msg);
			}
		}
	}
	
	public String modifyGuildValue(String function, String value, String guildName, String playerName)
	{
		String line = "Failed to modify permission!";
		int guildIndex = -1;
		for (int i = 0; i < this.guilds.size(); i++)
		{
			if (this.guilds.get(i).getName().equalsIgnoreCase(guildName))
			{
				guildIndex = i;
				break;
			}
		}
		if (guildIndex > -1)
		{
			if (this.guilds.get(guildIndex).isPlayerAdmin(playerName))
			{
				if (GHolder.isIntNumber(value))
				{
					int rank = Integer.valueOf(value);
					if (rank >= 0)
					{
						if (function.equalsIgnoreCase("invite") || function.equalsIgnoreCase("inv"))
						{
							this.guilds.get(guildIndex).setPerInvite(rank);
							line = "Invite permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("kick"))
						{
							this.guilds.get(guildIndex).setPerKick(rank);
							line = "Kick permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("promote") || function.equalsIgnoreCase("prom"))
						{
							this.guilds.get(guildIndex).setPerPromote(rank);
							line = "Promote permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("demote") || function.equalsIgnoreCase("dem"))
						{
							this.guilds.get(guildIndex).setPerDemote(rank);
							line = "Demote permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("build") || function.equalsIgnoreCase("bld"))
						{
							this.guilds.get(guildIndex).setPerBuild(rank);
							line = "Build permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("destroy") || function.equalsIgnoreCase("dstry"))
						{
							this.guilds.get(guildIndex).setPerDestroy(rank);
							line = "Destroy permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("sethome") || function.equalsIgnoreCase("sh"))
						{
							this.guilds.get(guildIndex).setPerSetHome(rank);
							line = "Set home permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("usechest") || function.equalsIgnoreCase("uc"))
						{
							this.guilds.get(guildIndex).setPerChest(rank);
							line = "Use chest permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("useworkbench") || function.equalsIgnoreCase("uw"))
						{
							this.guilds.get(guildIndex).setPerWorkbench(rank);
							line = "Use workbench permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("usefurnace") || function.equalsIgnoreCase("uf"))
						{
							this.guilds.get(guildIndex).setPerFurnace(rank);
							line = "Use furnace permission changed to rank " + rank;
						}
						else if (function.equalsIgnoreCase("color") || function.equalsIgnoreCase("clr"))
						{
							if (this.getColor(value) != null)
							{
								this.guilds.get(guildIndex).setColor(this.getColor(value));
								line = this.guilds.get(guildIndex).getColor() + this.guilds.get(guildIndex).getName() + ChatColor.YELLOW + "'s guild color changed to " + value;
							}
							else
								line = "Invalid color number! use 0-9";
						}
						else
							line = "Unknown function: " + function;
					}
					else
						line = "Rank must be a positive value!";
				}
				else if (GHolder.isBoolean(value))
				{
					boolean bool = Boolean.parseBoolean(value);
					if (function.equalsIgnoreCase("joinable"))
					{
						this.guilds.get(guildIndex).setJoinable(bool);
						if (bool)
							line = this.guilds.get(guildIndex).getColor() + this.guilds.get(guildIndex).getName() + ChatColor.YELLOW + " is now joinable";
						else
							line = this.guilds.get(guildIndex).getColor() + this.guilds.get(guildIndex).getName() + ChatColor.YELLOW + " is now unjoinable";
					}
					else if (function.equalsIgnoreCase("access"))
					{
						this.guilds.get(guildIndex).getGuildArea().setAllowAccess(bool);
						if (bool)
							line = this.guilds.get(guildIndex).getColor() + this.guilds.get(guildIndex).getName() + ChatColor.YELLOW + "'s Guild area is now accessable";
						else
							line = this.guilds.get(guildIndex).getColor() + this.guilds.get(guildIndex).getName() + ChatColor.YELLOW + "'s Guild area is now unaccessable";
					}
					else
						line = "Unknown function: " + function;
				}
				else if (this.getColor(value) != null)
				{
					if (function.equalsIgnoreCase("color") || function.equalsIgnoreCase("clr"))
					{
						this.guilds.get(guildIndex).setColor(this.getColor(value));
						line = this.guilds.get(guildIndex).getColor() + this.guilds.get(guildIndex).getName() + ChatColor.YELLOW + "'s guild color changed to " + value;
					}
					else
						line = "Unknown function: " + function;
				}
				else
					line = "Could not interpret value, type /gfunclist for a list";
				this.guilds.get(guildIndex).saveToFile();
			}
			else
				line = "You don't have permission to do that!";
		}
		else
			line = "Could not find guild";
		return line;
	}
	
	static boolean isIntNumber(String num){
	    try{
	        Integer.parseInt(num);
	    } catch(NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	static boolean isBoolean (String bool){
	    if (bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("false"))
	    	return true;
	    else
	    	return false;
	}
	
	public boolean telePlayerToGuildArea (Player player)
	{
		for (Guild guild : this.guilds)
		{
			if (guild.hasMember(player.getName()))
			{
				player.teleportTo(guild.getGuildSpawn());
				return true;
			}
		}
		return false;
	}
	
	public String getColor (String clr)
	{
		if (clr.equalsIgnoreCase("black") || clr.equalsIgnoreCase("0"))
			return "§0";
		else if (clr.equalsIgnoreCase("darkblue") || clr.equalsIgnoreCase("1"))
			return "§1";
		else if (clr.equalsIgnoreCase("darkgreen") || clr.equalsIgnoreCase("2"))
			return "§2";
		else if (clr.equalsIgnoreCase("lightblue") || clr.equalsIgnoreCase("3"))
			return "§3";
		else if (clr.equalsIgnoreCase("darkred") || clr.equalsIgnoreCase("4"))
			return "§4";
		else if (clr.equalsIgnoreCase("purple") || clr.equalsIgnoreCase("5"))
			return "§5";
		else if (clr.equalsIgnoreCase("orange") || clr.equalsIgnoreCase("6"))
			return "§6";
		else if (clr.equalsIgnoreCase("gray") || clr.equalsIgnoreCase("7"))
			return "§7";
		else if (clr.equalsIgnoreCase("darkgray") || clr.equalsIgnoreCase("8"))
			return "§8";
		else if (clr.equalsIgnoreCase("blue") || clr.equalsIgnoreCase("9"))
			return "§9";
		else if (clr.equalsIgnoreCase("bright green") || clr.equalsIgnoreCase("A"))
			return "§a";
		else if (clr.equalsIgnoreCase("teal") || clr.equalsIgnoreCase("B"))
			return "§b";
		else if (clr.equalsIgnoreCase("red") || clr.equalsIgnoreCase("C"))
			return "§c";
		else if (clr.equalsIgnoreCase("pink") || clr.equalsIgnoreCase("D"))
			return "§d";
		else if (clr.equalsIgnoreCase("yellow") || clr.equalsIgnoreCase("E"))
			return "§e";
		else if (clr.equalsIgnoreCase("white") || clr.equalsIgnoreCase("F"))
			return "§f";
		else
			return null;
	}
	
	public String preJoinGuild(String guildName, String playerName)
	{
		String line = "Guild not found!";
		for (Guild guild : this.guilds)
		{
			if (guild.getName().equalsIgnoreCase(guildName))
			{
				if (guild.hasMember(playerName))
				{
					line = "You are already in " + guild.getColor() + guild.getName();
					break;
				}
				else
				{
					if (guild.isJoinable())
					{
						this.pendingGuildInvites.add(playerName.toLowerCase() + ";" + guildName.toLowerCase());
						line = "Join " + guild.getColor() + guild.getName() + ChatColor.YELLOW + "? Type /gjyes or /gjno";
						break;
					}
					else
					{
						line = "Guild " + guild.getColor() + guild.getName() + ChatColor.YELLOW + " is not joinable!";
						break;
					}
				}
			}
		}
		return gString() + line;
	}
	
	public String joinGuild(String guildName, String playerName)
	{
		boolean found = false;
		String line = "Guild not found!";
		
		for (Guild guild : this.guilds)
		{
			if (guild.getName().equalsIgnoreCase(guildName))
			{
				if (guild.isJoinable())
				{
					for (GPlayer plr : guild.getPlayers())
					{
						if (plr.getName().equalsIgnoreCase(playerName))
							found = true;
					}
					if (!found)
					{
						guild.addPlayer(playerName);
						guild.getArea().areaSize(guild.getPlayers().size());
						return "You joined " + guild.getColor() + guildName + ChatColor.YELLOW + "!";
					}
					else 
					{
						line = "You are already in " + guild.getColor() + guild.getName();
						break;
					}
						
				}
				else 
					line = guild.getColor() + guildName + ChatColor.YELLOW + " is not joinable.";
			}
		}
		return line;
	}
	
	public void sendMsgToPlayer(String playerName, String message)
	{
		try
		{
			Player player = this.plugin.getServer().getPlayer(playerName);
			if (player.isOnline())
				player.sendMessage(message);
		}
		catch(NullPointerException npe){}
	}
	
	public String inviteToGuild(String guildName, String playerName, String adderName)
	{
		String facColor = "";
		boolean found = false;
		boolean hasRank = false;
		String line = "Guild not found!";
		for (Guild fac : this.guilds)
		{
			if (fac.getName().equalsIgnoreCase(guildName))
			{
				for (GPlayer plr : fac.getPlayers())
				{
					if (plr.getName().equalsIgnoreCase(playerName))
						found = true;
					if (plr.getName().equalsIgnoreCase(adderName))
					{
						if (plr.getRank() >= fac.getPerInvite() || fac.isPlayerAdmin(plr.getName()))
							hasRank = true;
					}
				}
				if (!found && hasRank)
				{
					this.pendingGuildInvites.add(playerName.toLowerCase() + ";" + guildName.toLowerCase());
					this.sendMsgToPlayer(playerName, gString() + this.playerOnline(adderName) + " invited you to " + fac.getColor() + fac.getName() +
											ChatColor.YELLOW + "! Type /gjyes or /gjno");
					line = 	gString() + "You invited " + playerOnline(playerName) +
							ChatColor.YELLOW + " to " + facColor + guildName + ChatColor.YELLOW + "!";
				}
				else if (!found && !hasRank)
					line = "You do not have permission to do that!";
				else if (hasRank && found)
					line = 	playerOnline(playerName) + " is already in " + facColor + guildName + ChatColor.YELLOW + "!";
				break;
			}
		}
		return line;
	}
	
	public String playerOnline(String playerName)
	{
		Player plr = plugin.getServer().getPlayer(playerName);
		if (plr != null)
		{
			if (!plr.getName().equals(""))
				return getPlayerWithGuildColor(playerName) + ChatColor.YELLOW;
		}
		return playerName;
	}
	
	public String getPlayerWithGuildColor(String playerName)
	{
		for (Guild guild : this.guilds)
		{
			if (guild.hasMember(playerName))
				return guild.getColor() + playerName + ChatColor.WHITE;
		}
		return playerName;
	}
	
	public String kickPlayerFromGuild(String guildName, String name, String kickerName)
	{
		int index = -1;
		String line = "Guild not found!";
		int kickerRank = - 2;
		int kickeeRank = - 2;
		for (Guild fac : this.guilds)
		{
			if (fac.getName().equalsIgnoreCase(guildName))
			{	
				for (int i = 0; i < fac.getPlayers().size(); i++)
				{
					if (fac.getPlayers().get(i).getName().equalsIgnoreCase(name))
					{
						index = i;
						kickeeRank = fac.getPlayers().get(i).getRank();
					}
					if (fac.getPlayers().get(i).getName().equalsIgnoreCase(kickerName))
					{
						kickerRank = fac.getPlayers().get(i).getRank();
					}
				}
				if ((index != -1 && kickerRank != -2 && kickeeRank != -2 && kickerRank > kickeeRank && kickerRank >= fac.getPerKick()) || fac.isPlayerAdmin(kickerName))
				{
					fac.getPlayers().remove(index);
					fac.getArea().areaSize(fac.getPlayers().size());
					line = playerOnline(name) + " was removed from " + fac.getColor() + fac.getName() + ChatColor.YELLOW + "!";
				}
				else if (index != -1 && kickerRank != -2 && kickeeRank != -2 && kickerRank <= kickeeRank)
				{
					line = "You can't kick someone who is higher or equal rank!";
				}
				else if (index == -1)
				{
					line = "Could not find " + playerOnline(name);
				}
				else
					line = "Insufficient permission!";
				break;
			}
		}
		return gString() + line;
	}
	
	public String removePlayerFromGuild(String guildName, String name)
	{
		int index = -1;
		String line = "Guild not found!";
		for (Guild fac : this.guilds)
		{
			if (fac.getName().equalsIgnoreCase(guildName))
			{	
				for (int i = 0; i < fac.getPlayers().size(); i++)
				{
					if (fac.getPlayers().get(i).getName().equalsIgnoreCase(name))
					{
						index = i;
					}
				}
				if (index != -1)
				{
					fac.getPlayers().remove(index);
					fac.getArea().areaSize(fac.getPlayers().size());
					line = 	gString() + this.playerOnline(name) + 
							" was removed from " + fac.getColor() + fac.getName() + ChatColor.YELLOW + "!";
					fac.saveToFile();
				}
				else
					line = gString() + "Could not find " + this.playerOnline(name) + " in " + fac.getColor() + guildName + ChatColor.YELLOW + "!";
				break;
			}
		}
		return line;
	}
	
	public String removeGuild(String guildName)
	{
		int index = -1;
		String line = "Guild not found!";
		for (int i = 0; i < this.guilds.size(); i++)
		{
			if (this.guilds.get(i).getName().equalsIgnoreCase(guildName))
			{
				index = i;
			}
		}
		if (index != -1)
		{
			line = gString() +  this.guilds.get(index).getColor() + guildName + ChatColor.YELLOW + " was removed!";
			this.guilds.remove(index);
		}
		return line;
	}
	
	public String addGuild(String guildName, String playerName, String guildKind)
	{
		boolean found = false;
		String line = "Guild not found!";
		ChatColor facColor = ChatColor.YELLOW;
		for (Guild fac : this.guilds)
		{
			if (fac.getName().equalsIgnoreCase(guildName))
			{
				found = true;
				facColor = fac.getColor();
				break;
			}
		}
		if (!found)
		{
			Guild guildToAdd = new Guild(guildName);
			guildToAdd.addAdmin(playerName);
			guildToAdd.setName(guildName);
			guildToAdd.addPlayer(playerName);
			guildToAdd.setKind(this.getGuildKindFromName(guildKind));
			this.removePlayerFromGuild(this.getPlayerGuild(playerName), playerName);
			guildToAdd.saveToFile();
			this.guilds.add(guildToAdd);
			line = gString() +  guildToAdd.getColor() + guildName + ChatColor.YELLOW + " was added!";
		}
		else
			line = gString() +  facColor + guildName + ChatColor.YELLOW + " already exists!";
		return line;
	}
	
	private GuildKind getGuildKindFromName(String gkName)
	{
		for (GuildKind gk : GHolder.guildKinds)
		{
			if (gk.getName().equalsIgnoreCase(gkName))
				return gk;
		}
		return null;
	}
	
	private String addGuildAuto(String guildName)
	{
		boolean found = false;
		String line = "Guild not found!";
		for (Guild fac : this.guilds)
		{
			if (fac.getName().equalsIgnoreCase(guildName))
			{
				found = true;
				break;
			}
		}
		if (!found)
		{
			Guild guildToAdd = new Guild(guildName);
			if (guildToAdd.loadFromFile(guildName) == true)
			{
				this.guilds.add(guildToAdd);
				line = "[Guilds] " + guildName + " was added!";
			}
			else
				line = "[Guilds] " + "Failed to add guild!";
		}
		else
			line = "[Guilds] " + guildName + " already exists!";
		return line;
	}
	
	public String activateGuild(String name)
	{
		String rtrln = "";
		ArrayList<String> guildsActivated = new ArrayList<String>();
		boolean has = false;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("Guilds" + File.separator + "active-guilds.txt"));
			try
			{
				String line = br.readLine();
				if (line != null)
				{
					while (line != null)
					{
						if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
						{
							if (line.equalsIgnoreCase(name))
							{
								rtrln = "Guild is already activated";
								has = true;
								break;
							}
							guildsActivated.add(line);
						}
						line = br.readLine();
					}
				}
			}
			finally
			{
				br.close();
			}
			if (!has)
			{
				BufferedWriter bw = new BufferedWriter(new FileWriter("Guilds" + File.separator + "active-guilds.txt"));
				try
				{
					guildsActivated.add(name);
					boolean first = true;
					for (String gld : guildsActivated)
					{
						if (first)
						{
							first = false;
							bw.write(gld);
						}
						else
						{
							bw.newLine();
							bw.write(gld);
						}
					}
					rtrln = "Activated " + name + "!";
				}
				finally
				{
					bw.flush();
					bw.close();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return rtrln;
	}
	
	public boolean addGuild(Guild fac)
	{
		if (!this.guilds.contains(fac))
		{
			this.guilds.add(fac);
			return true;
		}
		else
			return false;
	}
	
	protected boolean loadGuilds()
	{
		this.guilds.clear();
		boolean s = false;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("Guilds" + File.separator + "active-guilds.txt"));
			try
			{
				String line = br.readLine();
				while (line != null)
				{
					if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
					{
						System.out.println( this.addGuildAuto(line));
					}
					line = br.readLine();
				}
				s = true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				s = false;
			}
			finally
			{
				br.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			s = false;
		}
		return s;
	}

	public boolean saveGuilds()
	{
		boolean s = false;
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter("Guilds" + File.separator + "active-guilds.txt"));
			try
			{
				for (Guild fac : this.guilds)
				{
					fac.saveToFile();
					bw.write(fac.getName());
					bw.newLine();
				}
				s = true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				s = false;;
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
			s = false;
		}
		return s;
	}
	
	public String setArea(double x, double z, String name)
	{
		String message = gString() + "Failed to set area.";
		for(Guild guild : guilds)
		{
			boolean found = false;
			for(GPlayer player : guild.getPlayers())
			{
				if(player.getName().equals(name))
				{
					if (player.getRank() >= guild.getPerSetHome() || guild.isPlayerAdmin(name))
					{
						guild.getGuildArea().xzSet(x, z);
						guild.getGuildArea().areaSize(guild.getPlayers().size());
						message = gString() + "A new home has been set for the guild: " + guild.getColor() +  guild.getName();
						guild.saveToFile();
						found = true;
						break;
					}
					else
					{
						message = gString() + "You do not have permission to do that!";
						found = true;
					}
				}
			}
			if (found)
				break;
		}
		
		return message;
	}
	
	public String getPlayerGuildWithColor(String name)
	{
		String line = "Unknown guild";
		for(Guild guild : guilds)
		{
			if (guild.hasMember(name))
			{
				line = guild.getColor() + guild.getName() + ChatColor.YELLOW;
				break;
			}
		}
		return line;
	}
	
	public String getPlayerGuild(String name)
	{
		for(Guild guild : guilds)
		{
			if (guild.hasMember(name))
			{
				return guild.getName();
			}
		}
		return "Unknown guild";
	}
	
	public String getGuild(String name)
	{
		String message = gString() + "You are not in any guild yet.";
		for(Guild guild : guilds)
		{
			if (guild.hasMember(name))
			{
				message = gString() + "Your guild: " + guild.getColor() + guild.getName();
				break;
			}
		}
		
		return message;
	}
	
	public void loadConfig()
	{
		try {
            properties.load();
            System.out.println("[Guilds] Config Loaded!");
        } catch (IOException e) {
        	System.out.println("[Guilds] Failed to load configuration: "
                    + e.getMessage());
        }
        guildAreaExpansion = properties.getInt("Guild area expansion rate", 5);
        guildAreaStartSize = properties.getInt("Guild area start size", 5);
        properties.save();
	}

	public String addGuildKind(String kindName)
	{
		boolean found = false;
		String line = "GuildKind not found!";
		for (GuildKind gk : GHolder.guildKinds)
		{
			if (gk.getName().equalsIgnoreCase(kindName))
			{
				found = true;
				break;
			}
		}
		if (!found)
		{
			GuildKind guildKindToAdd = new GuildKind(kindName);
			if (guildKindToAdd.readFromFile() == true)
			{
				GHolder.guildKinds.add(guildKindToAdd);
				line = "[Guilds] " + kindName + " guild kind was added!";
			}
			else
				line = "[Guilds] " + "Failed to add guild kind!";
		}
		else
			line = "[Guilds] " + kindName + " guild kind already exists!";
		return line;
	}
	
	public String joinParty(String playerName, String partyName)
	{
		String rtln = "Failed to join " + partyName + " party!";
		boolean partyExists = false;
		boolean playerHasParty = false;
		for (Party party : this.partys)
		{
			if (party.getName().equalsIgnoreCase(partyName))
			{
				partyExists = true;
				for (String player : party.getPlayers())
				{
					if (player.equalsIgnoreCase(playerName))
						playerHasParty = true;
				}
				if (!playerHasParty)
				{
					partyExists = true;
					if (party.addPlayer(playerName))
					{
						rtln = "You joined " + partyName + " party!";
					}
					else
						rtln = "You are already in " + partyName + " party!";
				}
				else
					rtln = "You are already in a party!";
			}
		}
		if (!partyExists)
		{
			rtln = partyName + " party doesn't exist!";
		}
		return rtln;
	}
	
	public String leaveParty(String playerName)
	{
		for (Party prty : this.partys)
		{
			for (String player : prty.getPlayers())
			{
				if (player.equalsIgnoreCase(playerName))
				{
					if (prty.removePlayer(playerName))
						return "You left " + prty.getName() + " party";
					else
						return "Failed to remove " + playerName + " from " + prty.getName() + " party";
				}
			}
		}
		return "You are not in a party!";
	}
	
	public String startParty(String playerName, String partyName)
	{
		for (Party party : this.partys)
		{
			if (party.getName().equalsIgnoreCase(partyName))
			{
				return "Party already exists!";
			}
		}
		this.partys.add(new Party(partyName, playerName));
		return "You started " + partyName + " party!";
	}
	
	public boolean loadGuildKinds()
	{
		this.guilds.clear();
		boolean s = false;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("Guilds" + File.separator + "GuildKinds" + File.separator + "active-kinds.txt"));
			try
			{
				String line = br.readLine();
				while (line != null)
				{
					if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
					{
						System.out.println(addGuildKind(line));
					}
					line = br.readLine();
				}
				s = true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				s = false;
			}
			finally
			{
				br.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			s = false;
		}
		return s;
	}
	
	public boolean sendPartyMessage(Player player, String message)
	{
		boolean isParty = false;
		for (Party party : this.partys)
		{
			for (String plr : party.getPlayers())
			{
				if (player.getName().equalsIgnoreCase(plr))
				{
					isParty = true;
					break;
				}
			}
			if(isParty)
			{
				for (String plr : party.getPlayers())
				{
					this.sendMsgToPlayer(plr, ChatColor.BLUE + "[Party] <" + getPlayerWithGuildColor(plr) + ChatColor.BLUE + "> " + message);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean sendGuildMessage(Player player, String message)
	{
		boolean isGuild = false;
		for (Guild guild : this.guilds)
		{
			if (guild.hasMember(player.getName()))
				isGuild = true;
			if(isGuild)
			{
				for (GPlayer plr : guild.getPlayers())
				{
					this.sendMsgToPlayer(plr.getName(), ChatColor.GREEN + "[Guild] <" + guild.getColor() + player.getName() + ChatColor.GREEN + "> " + message);
				}
				return true;
			}
		}
		return false;
	}

	public void addCmd(String cmd, String par, String desc)
	{
		this.guildCommands.add(new Command(cmd, par, desc));
		this.tps.addCmd(cmd);
	}

	public ArrayList<Guild> getGuilds() {
		return guilds;
	}

	public void setGuilds(ArrayList<Guild> guilds) {
		this.guilds = guilds;
	}

	public ArrayList<String> getPendingGuildInvites() {
		return pendingGuildInvites;
	}

	public void setPendingGuildInvites(ArrayList<String> pendingGuildInvites) {
		this.pendingGuildInvites = pendingGuildInvites;
	}

	public tPermissions getTps() {
		return tps;
	}

	public void setTps(tPermissions tps) {
		this.tps = tps;
	}

	public ArrayList<Party> getPartys() {
		return partys;
	}

	public void setPartys(ArrayList<Party> partys) {
		this.partys = partys;
	}
	
}
