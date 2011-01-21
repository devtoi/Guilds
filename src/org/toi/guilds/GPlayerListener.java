package org.toi.guilds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.craftbukkit.CraftWorld;


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
			// Check if the area is accessible
			if (!guild.getArea().isAccessible())
			{
				// Check if player is in the guild area
				if (guild.isInArea(player.getLocation().getX(), player.getLocation().getZ()))
				{
					// If the player is in the guild allow him access, else not
					if (gholder.getPlayerGuild(player.getName()).equalsIgnoreCase(guild.getName()))
					{
						stop = false;
						break;
					}
					else
						stop = true;
				}
			}
		}
		// If player was not allowed access take him back to where he came from
		if (stop)
		{
			// Workaround provided by DarkGrave
			if(!((CraftWorld)player.getWorld()).getHandle().A.a(event.getFrom().getBlockX() >> 4, event.getFrom().getBlockZ() >> 4))
			{
			    ((CraftWorld)player.getWorld()).getHandle().A.d(event.getFrom().getBlockX() >> 4, event.getFrom().getBlockZ() >> 4);
			}

			player.teleportTo(event.getFrom());
			event.setTo(event.getFrom());
			event.setCancelled(true);
		}
    }
	
	public void onPlayerCommand(PlayerChatEvent event)
	{
		Player player = event.getPlayer();
		String[] split = event.getMessage().split(" ");
		
		// Verify that this is a guilds command
		if (split[0].equalsIgnoreCase("/guild") || split[0].equalsIgnoreCase("/gg"))
		{
			if (split.length >= 2)
			{
				if (split[1].equalsIgnoreCase("create") && gholder.getTps().canPlayerUseCommand(player.getName(), "create"))
				{
					if (split.length > 2)
					{
						if (split.length > 3)
						{
							if (!split[2].equals(""))
								player.sendMessage(gholder.addGuild(split[2], player.getName(), split[3]));
							else
								player.sendMessage(GHolder.gString() + "No guild name defined");
						}
						else
						{
							player.sendMessage(GHolder.gString() + "You need to define a guildkind!");
						}
					}
					else 
						player.sendMessage(GHolder.gString() + "No guild name defined!");
				}
				else if (split[1].equalsIgnoreCase("save") && gholder.getTps().canPlayerUseCommand(player.getName(), "save"))
				{
					if (split.length > 2)
					{
						if (!split[2].equals(""))
						{
							boolean found = false;
							for (Guild guild : gholder.getGuilds())
							{
								if (guild.getName().equalsIgnoreCase(split[2]))
								{	
									guild.saveToFile();
									found = true;
								}
							}
							if (found)
								player.sendMessage(GHolder.gString() + "Guild " + split[2] + " saved.");
							else
								player.sendMessage(GHolder.gString() + "The guild " + split[2] + " not found!");
						}
						else
							player.sendMessage(GHolder.gString() + "No guild name defined");
					}
					else 
						player.sendMessage(GHolder.gString() + "No guild name defined");
				}
				else if (split[1].equalsIgnoreCase("load") && gholder.getTps().canPlayerUseCommand(player.getName(), "load"))
				{
					if (split.length > 2)
					{
						if (!split[2].equals(""))
						{
							Guild guildToAdd = new Guild();
							if (guildToAdd.loadFromFile(split[2]) == true)
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
				else if (split[1].equalsIgnoreCase("remove") && gholder.getTps().canPlayerUseCommand(player.getName(), "remove"))
				{
					boolean fail = true;
					for (Guild guild : gholder.getGuilds())
					{
						if (guild.isPlayerAdmin(player.getName()))
						{
							player.sendMessage(gholder.removeGuild(split[2]));
							fail = false;
						}
					}
					if (fail)
						player.sendMessage(GHolder.gString() + "Failed to remove guild");
				}
				else if (split[1].equalsIgnoreCase("removeany") && gholder.getTps().canPlayerUseCommand(player.getName(), "removeany"))
				{
					if (split.length > 2)
					{
						player.sendMessage(gholder.removeGuild(split[2]));
					}
					else
						player.sendMessage(GHolder.gString() + "You need to define a guild to remove!");
				}
				else if (split[1].equalsIgnoreCase("list") && gholder.getTps().canPlayerUseCommand(player.getName(), "list"))
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
				else if (split[1].equalsIgnoreCase("saveall") && gholder.getTps().canPlayerUseCommand(player.getName(), "saveall"))
				{
					if (gholder.saveGuilds() == true)
						player.sendMessage(GHolder.gString() + "All guilds saved");
					else
						player.sendMessage(GHolder.gString() + "Failed to save guilds!");
				}
				else if (split[1].equalsIgnoreCase("reloadall") && gholder.getTps().canPlayerUseCommand(player.getName(), "reloadall"))
				{
					if (gholder.loadGuilds() == true)
						player.sendMessage(GHolder.gString() + "Guilds reloaded!");
					else
						player.sendMessage(GHolder.gString() + "Failed to reload guilds!");
				}
				else if (split[1].equalsIgnoreCase("join") && gholder.getTps().canPlayerUseCommand(player.getName(), "join"))
				{
					if (split.length > 2)
					{
						player.sendMessage(gholder.preJoinGuild(split[2], player.getName()));
					}
					else
						player.sendMessage(GHolder.gString() + "No guild name defined!");
				}
				else if (split[1].equalsIgnoreCase("leave")&& gholder.getTps().canPlayerUseCommand(player.getName(), "leave"))
				{
					player.sendMessage(gholder.removePlayerFromGuild(gholder.getPlayerGuild(player.getName()), player.getName()));
				}
				else if (split[1].equalsIgnoreCase("invite") && gholder.getTps().canPlayerUseCommand(player.getName(), "invite"))
				{
					if (split.length > 2)
					{
						if (!split[2].equals(""))
							player.sendMessage(gholder.inviteToGuild(gholder.getPlayerGuild(player.getName()), split[2], player.getName()));
						else
							player.sendMessage(GHolder.gString() + "No username defined!");
					}
					else
						player.sendMessage(GHolder.gString() + "No username defined!");
				}
				else if (split[1].equalsIgnoreCase("kick")&& gholder.getTps().canPlayerUseCommand(player.getName(), "kick"))
				{
					if (split.length > 2)
					{
						if (!split[2].equals(""))
						{
							player.sendMessage(gholder.kickPlayerFromGuild(gholder.getPlayerGuild(player.getName()), split[2], player.getName()));
						}
						else
							player.sendMessage(GHolder.gString() + "No username defined!");
					}
					else
						player.sendMessage(GHolder.gString() + "No username defined!");
				}
				else if (split[1].equalsIgnoreCase("sethome")&& gholder.getTps().canPlayerUseCommand(player.getName(), "sethome"))
				{
					player.sendMessage(GHolder.gString() + gholder.setGuildHome(player));
				}
				else if (split[1].equalsIgnoreCase("setarea") && gholder.getTps().canPlayerUseCommand(player.getName(), "setarea"))
				{
					player.sendMessage(gholder.setArea((int)player.getLocation().getX(), (int)player.getLocation().getZ(), player.getName()));
				}
				else if (split[1].equalsIgnoreCase("home") && gholder.getTps().canPlayerUseCommand(player.getName(), "home"))
				{
					if (!gholder.telePlayerToGuildArea(player))
					{
						player.sendMessage(GHolder.gString() + "You are not in any guild yet!");
					}
				}
				else if (split[1].equalsIgnoreCase("me") && gholder.getTps().canPlayerUseCommand(player.getName(), "me"))
				{
					player.sendMessage(gholder.getGuild(player.getName()));
				}
				else if (split[1].equalsIgnoreCase("accept") && gholder.getTps().canPlayerUseCommand(player.getName(), "accept"))
				{
					String line = "You don't have any guild invites pending!";
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
				else if (split[1].equalsIgnoreCase("decline") && gholder.getTps().canPlayerUseCommand(player.getName(), "decline"))
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
									line = "You declined the guild invite!";
									break;
								}
							}
						}
					}
					player.sendMessage(GHolder.gString() + line);
				}
				else if (split[1].equalsIgnoreCase("promote") && gholder.getTps().canPlayerUseCommand(player.getName(), "promote"))
				{
					if (split.length >= 3)
					{
						String line = "";
						if (split[2] != null)
						{
							if (!split[2].equals(""))
							{
								line = "Failed to promote " + gholder.playerOnline(split[2]) + " ";
								boolean success = false;
								String playerGuild = gholder.getPlayerGuild(player.getName());
								for (Guild guild : gholder.getGuilds())
								{
									if (guild.getName().equalsIgnoreCase(playerGuild))
									{
										if (guild.hasMember(split[2]))
										{
											if ((guild.getPlayerRank(player.getName()) > guild.getPlayerRank(split[2]) && guild.getPlayerRank(player.getName()) >= guild.getPerPromote()) || guild.isPlayerAdmin(split[2]))
											{
												success = true;
												guild.promotePlayer(split[2], player.getName());
												// TODO dsal
											}
											else
												line = "Insufficent rank to promote " + gholder.playerOnline(split[2]) + ", rank " + guild.getPerPromote() + " needed";
										}
										else
											line = gholder.playerOnline(split[2]) + " is not in " + guild.getName();
									}
								}
								if (success)
									line = "Promoted " + gholder.playerOnline(split[2]) + " ";
							}
						}
						player.sendMessage(GHolder.gString() + line);
					}
					else
						player.sendMessage(GHolder.gString() + "No name defined");
					
				}
				else if (split[1].equalsIgnoreCase("demote") && gholder.getTps().canPlayerUseCommand(player.getName(), "demote"))
				{
					if (split.length > 2)
					{
						String line = "";
						if (split[2] != null)
						{
							if (!split[2].equals(""))
							{
								line = "Failed to demote " + gholder.playerOnline(split[2]) + " ";
								boolean success = false;
								for (Guild guild : gholder.getGuilds())
								{
									if (guild.getName().equalsIgnoreCase(gholder.getPlayerGuild(player.getName())))
									{
										if (guild.hasMember(split[2]))
										{
											int demoterRank = guild.getPlayerRank(player.getName());
											int demoteeRank = guild.getPlayerRank(split[2]);
											if (demoteeRank >= 0)
											{
												if ((demoterRank > demoteeRank && demoterRank >= guild.getPerDemote()) || guild.isPlayerAdmin(split[2]))
												{
													success = true;
													guild.demotePlayer(split[2], player.getName());
													// TODO send demote message
												}
												else
													line = "Insufficent rank to demote " + gholder.playerOnline(split[2]) + ", rank " + guild.getPerDemote() + " needed";;
											}
											else
												line = "Can not demote " + gholder.playerOnline(split[2]) + " to negative rank!";
										}
										else
											line = gholder.playerOnline(split[2]) + " is not in " + guild.getName();
									}
								}
								if (success)
									line = "Demoted " + gholder.playerOnline(split[2]) + " ";
							}
						}
						player.sendMessage(GHolder.gString() + line);
					}
					else
						player.sendMessage(GHolder.gString() + "No name defined");
				}
				else if (split[1].equalsIgnoreCase("set") && gholder.getTps().canPlayerUseCommand(player.getName(), "set"))
				{
					if (split.length > 2)
					{
						if (split.length > 3)
						{
							player.sendMessage(GHolder.gString() + gholder.modifyGuildValue(split[2], split[3], gholder.getPlayerGuild(player.getName()), player.getName()));
						}
						else
							player.sendMessage(GHolder.gString() + "You did not define a value!");
					}
					else 
						player.sendMessage(GHolder.gString() + "You did not define a function!");
				}
				else if (split[1].equalsIgnoreCase("permissions") && gholder.getTps().canPlayerUseCommand(player.getName(), "permissions"))
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
				}
				else if (split[1].equalsIgnoreCase("kinds") && gholder.getTps().canPlayerUseCommand(player.getName(), "kinds"))
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
						player.sendMessage(GHolder.gString() + ChatColor.WHITE + "Avaliable guild kinds:" + str);
					else
						player.sendMessage(GHolder.gString() + "There are no guild kinds yet :C");
				}
				else if (split[1].equalsIgnoreCase("functions") && gholder.getTps().canPlayerUseCommand(player.getName(), "functions"))
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
				else if (split[1].equalsIgnoreCase("members") && gholder.getTps().canPlayerUseCommand(player.getName(), "members"))
				{
					if (split.length > 2)
					{
						if (!split[2].equals(""))
						{
							for (Guild guild : gholder.getGuilds())
							{
								if (guild.getName().equalsIgnoreCase(split[2]))
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
									player.sendMessage(GHolder.gString() + "Players in " + guild.getColor() + guild.getName() + ChatColor.WHITE + ":" + str);
									break;
								}
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
					
				}
				else if (split[1].equalsIgnoreCase("admins") && gholder.getTps().canPlayerUseCommand(player.getName(), "admins"))
				{
					if (split.length > 2)
					{
						if (!split[2].equals(""))
						{
							for (Guild guild : gholder.getGuilds())
							{
								if (guild.getName().equalsIgnoreCase(split[2]))
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
									player.sendMessage(GHolder.gString() + "Admins in " + guild.getColor() + guild.getName() + ChatColor.WHITE + ":" + str);
									break;
								}
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
				}
				else if(split[1].equalsIgnoreCase("upgrade") && gholder.getTps().canPlayerUseCommand(player.getName(), "upgrade"))
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
							
						}
					}
					player.sendMessage(GHolder.gString() + "You are not in any guild");
					
				}
				else if(split[1].equalsIgnoreCase("nextlevel") && gholder.getTps().canPlayerUseCommand(player.getName(), "nextlevel"))
				{
					String playerGuild = gholder.getPlayerGuild(player.getName());
					for (Guild guild : gholder.getGuilds())
					{
						if (guild.getName().equalsIgnoreCase(playerGuild))
						{
							guild.printNeededItemsToLevel(player);
							
						}
					}
					player.sendMessage(GHolder.gString() + "You are not in any guild");
					
				}
				else if(split[1].equalsIgnoreCase("turnin") && gholder.getTps().canPlayerUseCommand(player.getName(), "turnin"))
				{
					if (split.length > 2)
					{
						if (split.length > 3)
						{
							String playerGuild = gholder.getPlayerGuild(player.getName());
							for (Guild guild : gholder.getGuilds())
							{
								if (guild.getName().equalsIgnoreCase(playerGuild))
								{
									if (GHolder.isIntNumber(split[2]) && GHolder.isIntNumber(split[3]))
									{
										Material item = Material.getMaterial(split[2]);
										int amount = Integer.valueOf(split[3]);
										if (item != null && amount > 0)
										{
											if (gholder.playerHasEnoughItem(player, item, amount))
											{
												guild.turnInItems(player, item, amount);
												gholder.playerTakeItems(player, item, amount);
												gholder.sendTurninItems(player, item, amount);
												
											}
											else
											{
												player.sendMessage(GHolder.gString() + "You don't have enough material to do that!");
												
											}
										}
										else
											player.sendMessage(GHolder.gString() + "Values need to be above 0!");
									}
									else if (GHolder.isIntNumber(split[3]))
									{
										
										Material item = Material.getMaterial(split[2]);
										int amount = Integer.valueOf(split[3]);
										if (item != null)
										{
											if (Integer.valueOf(split[3]) > 0)
											{
												if (gholder.playerHasEnoughItem(player, item, amount))
												{
													guild.turnInItems(player, item, amount);
													gholder.playerTakeItems(player, item, amount);
													gholder.sendTurninItems(player, item, amount);
													
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
								}
							}
							player.sendMessage(GHolder.gString() + "You are not in any guild");
						}
						else
							player.sendMessage(GHolder.gString() + "You need do define an amount");
					}
					else
						player.sendMessage(GHolder.gString() + "You need do define an itemid");
				}
				else if(split[1].equalsIgnoreCase("activate") && gholder.getTps().canPlayerUseCommand(player.getName(), "activate"))
				{
					String playerGuild = gholder.getPlayerGuild(player.getName());
					if (playerGuild.equalsIgnoreCase("unknown guild"))
						player.sendMessage(GHolder.gString() + "You are not in any guild");
					else
					{
						player.sendMessage(GHolder.gString() + gholder.activateGuild(playerGuild));
					}
				}
				else if(split[1].equalsIgnoreCase("pjoin") && gholder.getTps().canPlayerUseCommand(player.getName(), "pjoin"))
				{
					if (split.length > 2)
					{
						if (!split[2].equals(""))
							player.sendMessage(GHolder.gString() + gholder.joinParty(player.getName(), split[2]));
						else
							player.sendMessage(GHolder.gString() + "You need to specify a party name");
					}
					else
					{
						player.sendMessage(GHolder.gString() + "You need to specify a party name");
					}
					
				}
				else if(split[1].equalsIgnoreCase("pstart") && gholder.getTps().canPlayerUseCommand(player.getName(), "pstart"))
				{
					if (split.length > 2)
					{
						if (!split[2].equals(""))
							player.sendMessage(GHolder.gString() + gholder.startParty(player.getName(), split[1]));
						else
							player.sendMessage(GHolder.gString() + "You need to specify a party name");
					}
					else
					{
						player.sendMessage(GHolder.gString() + "You need to specify a party name");
					}
					
				}
				else if(split[1].equalsIgnoreCase("pleave") && gholder.getTps().canPlayerUseCommand(player.getName(), "pleave"))
				{
					player.sendMessage(GHolder.gString() + gholder.leaveParty(player.getName()));
					
				}
				else if(split[1].equalsIgnoreCase("cmd") && gholder.getTps().canPlayerUseCommand(player.getName(), "cmd"))
				{
					if (split.length > 2)
					{
						if (!split[2].equals(""))
						{
							if (GHolder.isIntNumber(split[2]))
								gholder.printAvaliableCommands(player, Integer.valueOf(split[2]));
							else
								player.sendMessage(GHolder.gString() + "You need to define a valid pagenumber!");
						}
					}
					else
						gholder.printAvaliableCommands(player, 1);
				}
				else
					player.sendMessage(GHolder.gString() + "Usage: /guild [function]");
			}
			event.setCancelled(true);
		}
		// Guild chat
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
		// Party chat
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
