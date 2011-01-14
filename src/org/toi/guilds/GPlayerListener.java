package org.toi.guilds;
import org.bukkit.ChatColor;
import org.bukkit.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;


public class GPlayerListener extends PlayerListener{
	
	private GHolder gholder;
	
    public GPlayerListener(GHolder gholder) {
    	this.gholder = gholder;
    }
	
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		boolean stop = false;
		
		for (Guild guild : gholder.getGuilds())
		{
			if (guild.isInArea(player.getLocation().getX(), player.getLocation().getZ()))
			{
				if (gholder.getPlayerGuild(player.getName()).equalsIgnoreCase(guild.getName()))
				{
					stop = false;
					break;
				}
				else
					stop = true;
			}
		}
		if (stop)
		{
			player.teleportTo(event.getFrom());
		}
    }
	
	public void onPlayerCommand(PlayerChatEvent event)
	{
		Player player = event.getPlayer();
		String[] split = event.getMessage().split(" ");
		
		if (split[0].equalsIgnoreCase("/addguild") && gholder.getTps().canPlayerUseCommand(player.getName(), "/addguild"))
		{
			if (split.length > 1)
			{
				if (split.length > 2)
				{
					if (!split[1].equals(""))
						player.sendMessage(gholder.addGuild(split[1], player.getName(), split [2]));
					else
						player.sendMessage(GHolder.gString() + "No guild name defined");
				}
				else
				{
					player.sendMessage(GHolder.gString() + "You need to define a guildkind!");
				}
			}
			else 
			{
				player.sendMessage(GHolder.gString() + "No guild name defined!");
			}
			
		}
		else if (split[0].equalsIgnoreCase("/saveguild") && gholder.getTps().canPlayerUseCommand(player.getName(), "/saveguild"))
		{
			if (split.length > 1 )
			{
				if (!split[1].equals(""))
				{
					boolean found = false;
					for (Guild fac : gholder.getGuilds())
					{
						if (fac.getName().equalsIgnoreCase(split[1]))
						{	
							fac.saveToFile();
							found = true;
						}
					}
					if (found)
						player.sendMessage(GHolder.gString() + "guild " + split[1] + " saved.");
					else
						player.sendMessage(GHolder.gString() + "guild not found!");
				}
				else
					player.sendMessage(GHolder.gString() + "No guild name defined");
			}
			else 
				player.sendMessage(GHolder.gString() + "No guild name defined");
		}
		else if (split[0].equalsIgnoreCase("/loadguild") && gholder.getTps().canPlayerUseCommand(player.getName(), "/loadguild"))
		{
			if (split.length > 1)
			{
				if (!split[1].equals(""))
				{
					Guild guildToAdd = new Guild();
					if (guildToAdd.loadFromFile(split[1]) == true)
					{
						if (gholder.addGuild(guildToAdd) == true)
							player.sendMessage(GHolder.gString() + guildToAdd.getColor() + guildToAdd.getName() + ChatColor.YELLOW + " added!");
						else
							player.sendMessage(GHolder.gString() + guildToAdd.getColor() + guildToAdd.getName() + ChatColor.YELLOW + " failed to be added!");
					}
					else
						player.sendMessage(GHolder.gString() + "Failed to load guild!");
				}
				else 
					player.sendMessage(GHolder.gString() + "No guild name defined");
			}
			else 
				player.sendMessage(GHolder.gString() + "No guild name defined");
		}
		else if (split[0].equalsIgnoreCase("/removeguild"))
		{
			if (split.length > 1)
			{
				if (!split[1].equals(""))
				{
					if (gholder.getTps().canPlayerUseCommand(player.getName(), "/removeanyguild"))
						player.sendMessage(gholder.removeGuild(split[1]));
					else if (gholder.getTps().canPlayerUseCommand(player.getName(), "/removeguild"))
					{
						for (Guild guild : gholder.getGuilds())
						{
							if (guild.isPlayerAdmin(player.getName()))
							{
								player.sendMessage(gholder.removeGuild(split[1]));
								event.setCancelled(true);
							}
						}
						player.sendMessage(GHolder.gString() + "Could not delete guild");
					}
				}
				else if (gholder.getTps().canPlayerUseCommand(player.getName(), "/removeguild"))
				{
					for (Guild guild : gholder.getGuilds())
					{
						if (guild.isPlayerAdmin(player.getName()))
						{
							player.sendMessage(gholder.removeGuild(split[1]));
						}
					}
					player.sendMessage(GHolder.gString() + "Could not delete guild");
				}
			}
			else if (gholder.getTps().canPlayerUseCommand(player.getName(), "/removeguild"))
			{
				for (Guild guild : gholder.getGuilds())
				{
					if (guild.isPlayerAdmin(player.getName()))
					{
						player.sendMessage(gholder.removeGuild(split[1]));
						event.setCancelled(true);
					}
				}
				player.sendMessage(GHolder.gString() + "Could not delete guild");
			}
		}
		else if (split[0].equalsIgnoreCase("/guildlist") && gholder.getTps().canPlayerUseCommand(player.getName(), "/guildlist"))
		{
			String guildList = "Guilds: ";
			boolean first = true;
			for (Guild fac : gholder.getGuilds())
			{
				if (fac.isJoinable())
				{
					if (first)
					{
						guildList += fac.getColor() + fac.getName();
						first = false;
					}
					else
						guildList +=  ", " + fac.getColor() + fac.getName();
				}
			}
			player.sendMessage(guildList);
		}
		else if (split[0].equalsIgnoreCase("/saveallguilds") && gholder.getTps().canPlayerUseCommand(player.getName(), "/saveallguilds"))
		{
			if (gholder.saveGuilds() == true)
				player.sendMessage(GHolder.gString() + "All guilds saved");
			else
				player.sendMessage(GHolder.gString() + "Failed to save guilds!");
		}
		else if (split[0].equalsIgnoreCase("/reloadguilds") && gholder.getTps().canPlayerUseCommand(player.getName(), "/reloadguilds"))
		{
			if (gholder.loadGuilds() == true)
				player.sendMessage(GHolder.gString() + "Guilds reloaded!");
			else
				player.sendMessage(GHolder.gString() + "Failed to reload guilds!");
		}
		else if ((split[0].equalsIgnoreCase("/joinguild") || split[0].equalsIgnoreCase("/jguild")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/joinguild"))
		{
			if (split.length > 1)
			{
				player.sendMessage(gholder.preJoinGuild(split[1], player.getName()));
			}
			else
				player.sendMessage(GHolder.gString() + "No guild name defined!");
			event.setCancelled(true);
		}
		else if ((split[0].equalsIgnoreCase("/leaveguild") || split[0].equalsIgnoreCase("/lguild")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/leaveguild"))
		{
			player.sendMessage(gholder.removePlayerFromGuild(gholder.getPlayerGuild(player.getName()), player.getName()));
		}
		else if ((split[0].equalsIgnoreCase("/ginvite") || split[0].equalsIgnoreCase("/guildinvite")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/guildinvite"))
		{
			if (split.length > 1)
			{
				if (!split[1].equals(""))
					player.sendMessage(gholder.inviteToGuild(gholder.getPlayerGuild(player.getName()), split[1], player.getName()));
				else
					player.sendMessage(GHolder.gString() + "No username defined!");
			}
			else
				player.sendMessage(GHolder.gString() + "No username defined!");
		}
		else if ((split[0].equalsIgnoreCase("/gkick") || split[0].equalsIgnoreCase("/guildkick")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/guildkick"))
		{
			if (split.length > 1)
			{
				if (!split[1].equals(""))
				{
					player.sendMessage(gholder.kickPlayerFromGuild(gholder.getPlayerGuild(player.getName()), split[1], player.getName()));
				}
				else
					player.sendMessage(GHolder.gString() + "No username defined!");
			}
			else
				player.sendMessage(GHolder.gString() + "No username defined!");
		}
		else if ((split[0].equalsIgnoreCase("/setguildhome") || split[0].equalsIgnoreCase("/sgh")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/setguildhome"))
		{
			player.sendMessage(GHolder.gString() + gholder.setGuildHome(player));
		}
		else if ((split[0].equalsIgnoreCase("/setguildarea") || split[0].equalsIgnoreCase("/sga")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/setguildhome"))
		{
			player.sendMessage(gholder.setArea((int)player.getLocation().getX(), (int)player.getLocation().getZ(), player.getName()));
		}
		else if ((split[0].equalsIgnoreCase("/guildhome") || split[0].equalsIgnoreCase("/gh")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/guildhome"))
		{
			if (!gholder.telePlayerToGuildArea(player))
			{
				player.sendMessage(GHolder.gString() + "You are not in any guild yet!");
			}
		}
		else if (split[0].equalsIgnoreCase("/myguild") && gholder.getTps().canPlayerUseCommand(player.getName(), "/myguild"))
		{
			player.sendMessage(gholder.getGuild(player.getName()));
		}
		else if (split[0].equalsIgnoreCase("/gjyes") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gjyes"))
		{
			String line = "You don't have any guild requests pending!";
			for (int i = 0; i < gholder.getPendingGuildInvites().size(); i++)
			{
				if (gholder.getPendingGuildInvites().get(i) != null)
				{
					String[] sp = gholder.getPendingGuildInvites().get(i).split(";");
					if (sp.length == 2)
					{
						if (sp[0].equalsIgnoreCase(player.getName()))
						{
							line = gholder.joinGuild(sp[1], sp[0]);
							gholder.getPendingGuildInvites().remove(i);
							break;
						}
					}
				}
			}
			player.sendMessage(GHolder.gString() + line);
		}
		else if (split[0].equalsIgnoreCase("/gjno") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gjno"))
		{
			String line = "You don't have any guild requests pending!";
			for (int i = 0; i < gholder.getPendingGuildInvites().size(); i++)
			{
				if (gholder.getPendingGuildInvites().get(i) != null)
				{
					String[] sp = gholder.getPendingGuildInvites().get(i).split(";");
					if (sp.length == 2)
					{
						if (sp[0].equalsIgnoreCase(player.getName()))
						{
							gholder.getPendingGuildInvites().remove(i);
							line = "You declined the guild request!";
							break;
						}
					}
				}
			}
			player.sendMessage(GHolder.gString() + line);
			event.setCancelled(true);
		}
		else if (split[0].equalsIgnoreCase("/gpromote") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gpromote"))
		{
			if (split.length > 1)
			{
				String line = "";
				if (split[1] != null)
				{
					if (split[1] != "")
					{
						line = "Failed to promote " + gholder.playerOnline(split[1]);
						boolean success = false;
						for (Guild guild : gholder.getGuilds())
						{
							if (guild.getName().equalsIgnoreCase(gholder.getPlayerGuild(player.getName())))
							{
								if (guild.hasMember(split[1]))
								{
									if ((guild.getPlayerRank(player.getName()) > guild.getPlayerRank(split[1]) && guild.getPlayerRank(player.getName()) >= guild.getPerPromote()) || guild.isPlayerAdmin(split[1]))
									{
										success = true;
										guild.promotePlayer(split[1], player.getName());
										player.sendMessage(GHolder.gString() + guild.getColor() + player.getName() + ChatColor.YELLOW + " promoted you!");
									}
									else
										line = "Insufficent rank to promote " + gholder.playerOnline(split[1]) + ", rank " + guild.getPerPromote() + " needed";
								}
								else
									line = gholder.playerOnline(split[1]) + " is not in " + guild.getName();
							}
						}
						if (success)
							line = "Promoted " + gholder.playerOnline(split[1]);
					}
				}
				player.sendMessage(GHolder.gString() + line);
			}
			else
				player.sendMessage(GHolder.gString() + "No name defined");
			event.setCancelled(true);
		}
		else if (split[0].equalsIgnoreCase("/gdemote") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gdemote"))
		{
			if (split.length > 1)
			{
				String line = "";
				if (split[1] != null)
				{
					if (split[1] != "")
					{
						line = "Failed to demote " + gholder.playerOnline(split[1]);
						boolean success = false;
						for (Guild guild : gholder.getGuilds())
						{
							if (guild.getName().equalsIgnoreCase(gholder.getPlayerGuild(player.getName())))
							{
								if (guild.hasMember(split[1]))
								{
									int demoterRank = guild.getPlayerRank(player.getName());
									int demoteeRank = guild.getPlayerRank(split[1]);
									if (demoteeRank >= 0)
									{
										if ((demoterRank > demoteeRank && demoterRank >= guild.getPerDemote()) || guild.isPlayerAdmin(split[1]))
										{
											success = true;
											guild.demotePlayer(split[1], player.getName());
											player.sendMessage(GHolder.gString() + guild.getColor() + player.getName() + ChatColor.YELLOW + " demoted you!");
										}
										else
											line = "Insufficent rank to demote " + gholder.playerOnline(split[1]) + ", rank " + guild.getPerDemote() + " needed";;
									}
									else
										line = "Can not demote " + gholder.playerOnline(split[1]) + " to negative rank!";
								}
								else
									line = gholder.playerOnline(split[1]) + " is not in " + guild.getName();
							}
						}
						if (success)
							line = "Demoted " + gholder.playerOnline(split[1]);
					}
				}
				player.sendMessage(GHolder.gString() + line);
			}
			else
				player.sendMessage(GHolder.gString() + "No name defined");
			event.setCancelled(true);
		}
		else if (split[0].equalsIgnoreCase("/gset") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gset"))
		{
			if (split.length > 1)
			{
				if (split.length > 2)
				{
					player.sendMessage(GHolder.gString() + gholder.modifyGuildValue(split[1], split[2], gholder.getPlayerGuild(player.getName()), player.getName()));
				}
				else
					player.sendMessage(GHolder.gString() + "You did not define a value!");
			}
			else 
				player.sendMessage(GHolder.gString() + "You did not define a function!");
			event.setCancelled(true);
		}
		else if (split[0].equalsIgnoreCase("/gpermlist") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gpermlist"))
		{
			String playerGuild = gholder.getPlayerGuild(player.getName());
			if (!playerGuild.equalsIgnoreCase("Unknown guild"))
			{
				int index = -1;
				for (int i = 0; i < gholder.getGuilds().size(); i++)
				{
					if (gholder.getGuilds().get(i).getName().equalsIgnoreCase(playerGuild))
					{
						index = i;
					}
				}
				if (index > -1)
				{
					player.sendMessage("Permission list for " + gholder.getPlayerGuildWithColor(player.getName()) + ":");
					player.sendMessage("Invite: " + gholder.getGuilds().get(index).getPerInvite());
					player.sendMessage("Kick: " + gholder.getGuilds().get(index).getPerKick());
					player.sendMessage("Promote: " + gholder.getGuilds().get(index).getPerPromote());
					player.sendMessage("Demote: " + gholder.getGuilds().get(index).getPerDemote());
					player.sendMessage("Build in guild area: " + gholder.getGuilds().get(index).getPerBuild());
					player.sendMessage("Destroy in guild area: " + gholder.getGuilds().get(index).getPerDestroy());
					player.sendMessage("Set home: " + gholder.getGuilds().get(index).getPerSetHome());
					player.sendMessage("Use chest: " + gholder.getGuilds().get(index).getPerChest());
					player.sendMessage("Use workbench: " + gholder.getGuilds().get(index).getPerWorkbench());
					player.sendMessage("Use furnace: " + gholder.getGuilds().get(index).getPerFurnace());
				}
			}
			else 
				player.sendMessage(GHolder.gString() + "You are not in any guild!");
			event.setCancelled(true);
		}
		else if ((split[0].equalsIgnoreCase("/gkindlist") || split[0].equalsIgnoreCase("/gkl")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/gkindlist"))
		{
			boolean first = true;
			String str = "";
			for (GuildKind gk : GHolder.guildKinds)
			{
				if (first)
				{
					str += " " + gk.getName();
					first = false;
				}
				else
					str += ", " + gk.getName();
			}
			if (!str.equals(""))
				player.sendMessage(GHolder.gString() + ChatColor.WHITE + "Avaliable guild kinds: " + str);
			else
				player.sendMessage(GHolder.gString() + "There are no guild kinds yet :C");
		}
		else if (split[0].equalsIgnoreCase("/gfunclist") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gfunclist"))
		{
			player.sendMessage("Function list:");
			player.sendMessage("function/functionshort - Description");
			player.sendMessage("invite/inv <number> - Set rank needed to invite players");
			player.sendMessage("kick <number> - Set rank needed to kick players");
			player.sendMessage("promote/prom <number> - Set rank needed to promote players");
			player.sendMessage("demote/dem <number> - Set rank needed to demote players");
			player.sendMessage("build/bld <number> - Set rank needed to build in guild area");
			player.sendMessage("destroy/dstry <number> - Set rank needed to destroy blocks in guild area");
			player.sendMessage("sethome/sh <number> - Set rank needed to set the guild area");
			player.sendMessage("usechest/uc <number> - Set rank needed to use chests in the guild area");
			player.sendMessage("useworkbench/uw <number> - Set rank needed to use workbenches in the guild area");
			player.sendMessage("usefurnace/uf <number> - Set rank needed to use furnaces in the guild area");
			player.sendMessage("joinable <true/false> - Set wheter the guild is joinable or not");
			player.sendMessage("access <true/false> - Set wheter non-guild members can walk into your guild area");
			player.sendMessage("color <colorname> - Set the color of the guild");
		}
		else if (split[0].equalsIgnoreCase("/gmembers") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gmembers"))
		{
			if (split.length > 1)
			{
				if (!split[1].equals(""))
				{
					for (Guild guild : gholder.getGuilds())
					{
						if (guild.getName().equalsIgnoreCase(split[1]))
						{
							String str = "";
							boolean first = true;
							for (GPlayer gp : guild.getPlayers())
							{
								if (first)
									str += " " + gp.getName();
								else
									str += ", " + gp.getName();
								first = false;
							}
							player.sendMessage("Players in " + guild.getColor() + guild.getName() + ChatColor.WHITE + ":" + str);
						}
						break;
					}
				}
				else
					player.sendMessage(GHolder.gString() + "No guild name defined!");
			}
			else
			{
				if (gholder.getPlayerGuild(player.getName()).equalsIgnoreCase("unknown guild"))
				{
					player.sendMessage(GHolder.gString() + "You don't have a guild yet!");
				}
				else
				{
					String playerGuild = gholder.getPlayerGuild(player.getName());
					for (Guild guild : gholder.getGuilds())
					{
						if (guild.getName().equalsIgnoreCase(playerGuild))
						{
							String str = "";
							boolean first = true;
							for (GPlayer gp : guild.getPlayers())
							{
								if (first)
									str += " " + gp.name;
								else
									str += ", " + gp.name;
								first = false;
							}
							player.sendMessage("Players in " + guild.getColor() + guild.getName() + ChatColor.WHITE + ":" + str);
							break;
						}
					}
				}
			}
			event.setCancelled(true);
		}
		else if (split[0].equalsIgnoreCase("/gadmins") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gadmins"))
		{
			if (split.length > 1)
			{
				if (!split[1].equals(""))
				{
					for (Guild guild : gholder.getGuilds())
					{
						if (guild.getName().equalsIgnoreCase(split[1]))
						{
							String str = "";
							boolean first = true;
							for (String admin : guild.getAdmins())
							{
								if (first)
									str += " " + admin;
								else
									str += ", " + admin;
								first = false;
							}
							player.sendMessage("Admins in " + guild.getColor() + guild.getName() + ChatColor.WHITE + ":" + str);
						}
						break;
					}
				}
				else
					player.sendMessage(GHolder.gString() + "No guild name defined!");
			}
			else
			{
				if (gholder.getPlayerGuild(player.getName()).equalsIgnoreCase("unknown guild"))
				{
					player.sendMessage(GHolder.gString() + "You don't have a guild yet!");
				}
				else
				{
					String playerGuild = gholder.getPlayerGuild(player.getName());
					for (Guild guild : gholder.getGuilds())
					{
						if (guild.getName().equalsIgnoreCase(playerGuild))
						{
							String str = "";
							boolean first = true;
							for (String admin : guild.getAdmins())
							{
								if (first)
									str += " " + admin;
								else
									str += ", " + admin;
								first = false;
							}
							player.sendMessage("Admins in " + guild.getColor() + guild.getName() + ChatColor.WHITE + ":" + str);
							break;
						}
					}
				}
			}
		event.setCancelled(true);
		}
		else if (split[0].equalsIgnoreCase("/guild"))
		{
			if (split.length > 1)
			{
				if (!split[1].equals(""))
				{
					if (GHolder.isIntNumber(split[1]))
						gholder.printAvaliableCommands(player, Integer.valueOf(split[1]));
					else
						player.sendMessage(GHolder.gString() + "You need to define a pagenumber!");
				}
			}
			else
				gholder.printAvaliableCommands(player, 1);
			event.setCancelled(true);
		}
		else if((split[0].equalsIgnoreCase("/gupgrade") || split[0].equalsIgnoreCase("/gup")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/gupgrade"))
		{
			String playerGuild = gholder.getPlayerGuild(player.getName());
			for (Guild guild : gholder.getGuilds())
			{
				if (guild.getName().equalsIgnoreCase(playerGuild))
				{
					if (guild.getScore() < guild.getKind().getMaxLevel() || guild.getKind().getMaxLevel() == -1)
					{
						if (guild.doUpgrade())
						{
							player.sendMessage(GHolder.gString() + "Upgraded " + guild.getColor() + guild.getName() + ChatColor.YELLOW + " to level " + guild.getScore());
						}
						else
						{
							player.sendMessage(GHolder.gString() + "Not enough material to upgrade " + guild.getColor() + guild.getName() + ChatColor.YELLOW + "!");
						}
					}
					else
						player.sendMessage(GHolder.gString() + guild.getColor() + guild.getName() + ChatColor.YELLOW + " have already reached max level");
					event.setCancelled(true);
				}
			}
			player.sendMessage(GHolder.gString() + "You are not in any guild");
			event.setCancelled(true);
		}
		else if((split[0].equalsIgnoreCase("/gnextlevel") || split[0].equalsIgnoreCase("/gnl")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/gnextlevel"))
		{
			String playerGuild = gholder.getPlayerGuild(player.getName());
			for (Guild guild : gholder.getGuilds())
			{
				if (guild.getName().equalsIgnoreCase(playerGuild))
				{
					guild.printNeededItemsToLevel(player);
					event.setCancelled(true);
				}
			}
			player.sendMessage(GHolder.gString() + "You are not in any guild");
			event.setCancelled(true);
		}
		else if((split[0].equalsIgnoreCase("/gturnin") || split[0].equalsIgnoreCase("/gti")) && gholder.getTps().canPlayerUseCommand(player.getName(), "/gturnin"))
		{
			if (split.length > 1)
			{
				if (split.length > 2)
				{
					String playerGuild = gholder.getPlayerGuild(player.getName());
					for (Guild guild : gholder.getGuilds())
					{
						if (guild.getName().equalsIgnoreCase(playerGuild))
						{
							if (GHolder.isIntNumber(split[1]) && GHolder.isIntNumber(split[2]))
							{
								int itemId = Integer.valueOf(split[1]);
								int amount = Integer.valueOf(split[2]);
								if (itemId > 0 && amount > 0)
								{
									if (gholder.playerHasEnoughItem(player, itemId, amount))
									{
										guild.turnInItems(player, itemId, amount);
										gholder.playerTakeItems(player, itemId, amount);
										gholder.sendTurninItems(player, itemId, amount);
										event.setCancelled(true);
									}
									else
									{
										player.sendMessage(GHolder.gString() + "You don't have enough material to do that!");
										event.setCancelled(true);
									}
								}
								else
									player.sendMessage(GHolder.gString() + "Values need to be above 0!");
							}
							else if (GHolder.isIntNumber(split[2]))
							{
								
								// TODO edit this int itemId = etc.getDataSource().getItem(split[1]);
								int itemId = Integer.valueOf(split[1]);
								int amount = Integer.valueOf(split[2]);
								if (itemId != 0)
								{
									if (Integer.valueOf(split[2]) > 0)
									{
										if (gholder.playerHasEnoughItem(player, itemId, amount))
										{
											guild.turnInItems(player, itemId, amount);
											gholder.playerTakeItems(player, itemId, amount);
											gholder.sendTurninItems(player, itemId, amount);
											event.setCancelled(true);
										}
										else
											player.sendMessage(GHolder.gString() + "You don't have enough material to do that!");
									}
									else
										player.sendMessage(GHolder.gString() + "Amount need to be above 0!");
								}
								else
									player.sendMessage(GHolder.gString() + "Failed to interpret item name");
							}
							else
								player.sendMessage(GHolder.gString() + "You need to write numbers");
							event.setCancelled(true);
						}
					}
					player.sendMessage(GHolder.gString() + "You are not in any guild");
				}
				else
					player.sendMessage(GHolder.gString() + "You need do define an amount");
			}
			else
				player.sendMessage(GHolder.gString() + "You need do define an itemid");
			event.setCancelled(true);
		}
		else if(split[0].equalsIgnoreCase("/gactivate") && gholder.getTps().canPlayerUseCommand(player.getName(), "/gactivate"))
		{
			String playerGuild = gholder.getPlayerGuild(player.getName());
			if (playerGuild.equalsIgnoreCase("unknown guild"))
				player.sendMessage(GHolder.gString() + "You are not in any guild");
			else
			{
				player.sendMessage(GHolder.gString() + gholder.activateGuild(playerGuild));
			}
			event.setCancelled(true);
		}
		else if(split[0].equalsIgnoreCase("/pjoin") && gholder.getTps().canPlayerUseCommand(player.getName(), "/pjoin"))
		{
			if (split.length > 1)
			{
				if (!split[1].equals(""))
					player.sendMessage(GHolder.gString() + gholder.joinParty(player.getName(), split[1]));
				else
					player.sendMessage(GHolder.gString() + "You need to specify a party name");
			}
			else
			{
				player.sendMessage(GHolder.gString() + "You need to specify a party name");
			}
			event.setCancelled(true);
		}
		else if(split[0].equalsIgnoreCase("/pstart") && gholder.getTps().canPlayerUseCommand(player.getName(), "/pstart"))
		{
			if (split.length > 1)
			{
				if (!split[1].equals(""))
					player.sendMessage(GHolder.gString() + gholder.startParty(player.getName(), split[1]));
				else
					player.sendMessage(GHolder.gString() + "You need to specify a party name");
			}
			else
			{
				player.sendMessage(GHolder.gString() + "You need to specify a party name");
			}
			event.setCancelled(true);
		}
		else if(split[0].equalsIgnoreCase("/pleave") && gholder.getTps().canPlayerUseCommand(player.getName(), "/pleave"))
		{
			player.sendMessage(GHolder.gString() + gholder.leaveParty(player.getName()));
			event.setCancelled(true);
		}
		else if(split[0].equalsIgnoreCase("/g"))
		{
			if (split.length > 1)
			{
				String message = "";
				boolean first = true;
				boolean skip = true;
				for (String str : split)
				{
					if (skip)
						skip = false;
					else if (first)
					{
						first = false;
						message += str;
					}
					else
					{
						message += " " + str;
					}
				}
				if (!gholder.sendGuildMessage(player, message))
				{
					player.sendMessage(GHolder.gString() + "You don't have a guild yet!");
				}
			}
			event.setCancelled(true);
		}
		else if(split[0].equalsIgnoreCase("/p"))
		{
			if (split.length > 1)
			{
				String message = "";
				boolean first = true;
				boolean skip = true;
				for (String str : split)
				{
					if (skip)
						skip = false;
					else if (first)
					{
						first = false;
						message += str;
					}
					else
					{
						message += " " + str;
					}
				}
				if (!gholder.sendPartyMessage(player, message))
				{
					player.sendMessage(GHolder.gString() + "You are not in a party");
				}
			}
			event.setCancelled(true);
		}
	}
}
