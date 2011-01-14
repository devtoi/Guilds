package org.toi.guilds;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;


public class Guilds extends JavaPlugin{

	    private String name;
	    private String version;
	    private GHolder gholder = new GHolder();
	    private GPlayerListener playerListener = new GPlayerListener(gholder);
	    private GBlockListener blockListener = new GBlockListener(gholder);
	    private GEntityListener entityListener = new GEntityListener(gholder);
	    
	    public Guilds(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File plugin, ClassLoader cLoader) {
	        super(pluginLoader, instance, desc, plugin, cLoader);
	        name = "Guilds";
	        version = "v1.0 (Codger)";
	        GHolder.plugin = this;

	        registerEvents();
	        this.createFiles();
	        gholder.loadConfig();
	        this.initiateGuildCommands();
	        gholder.getTps().loadPermissions();
	        gholder.getTps().savePermissions();
	        gholder.loadGuildKinds();
	        gholder.loadGuilds();
	        System.out.println(name + " " + version + " initialized");
	    }

	    private void createFiles()
	    {
	    	try
	    	{
		    	// Plugin Folder
		    	File pf = new File("Guilds");
		        if (!pf.exists())
		        	pf.mkdirs();
		        // Active guilds file
		    	File g = new File("Guilds" + File.separator + "active-guilds.txt");
				if (!g.exists())
					g.createNewFile();
				// Guilds kinds folder
				File gkf = new File("Guilds" + File.separator + "GuildKinds");
		        if (!gkf.exists())
		        	gkf.mkdirs();
				// Guilds kinds file
				File gk = new File("Guilds" + File.separator + "GuildKinds" + File.separator + "active-kinds.txt");
				if (!gk.exists())
					gk.createNewFile();
	    	}
	    	catch (IOException ioe)
	    	{
	    		ioe.printStackTrace();
	    	}
	    }
	    
	    public void onDisable() {
	        
	    }

	    public void onEnable() {
	    	
	    }

	    private void registerEvents() {
	        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACED, blockListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGEDBY_ENTITY, entityListener, Priority.Normal, this);
	    }

	    public void initiateGuildCommands()
	    {
	    	gholder.addCmd("/addguild", "[guildname] [kind]", "Adds an empty guild with you as admin (leaving your old one)");
	        gholder.addCmd("/removeanyguild", "[guild]", "Removes any guild");
	        gholder.addCmd("/gkindlist", "", "Shows a list of avaliable guild kinds");
	        gholder.addCmd("/removeguild", "", "Removes your guild");
	        gholder.addCmd("/saveguild", "[guild]", "Saves a guild to a file");
	        gholder.addCmd("/loadguild", "[guild]", "Loads and adds a guild from a file");
	        gholder.addCmd("/saveallguilds", "", "Saves all guilds to their files");
	        gholder.addCmd("/guildlist", "", "Shows list of existing guilds joinable");
	        gholder.addCmd("/reloadguilds", "", "Reloads the guilds from their files");
	        gholder.addCmd("/joinguild", "[guild]", "Joins a guild");
	        gholder.addCmd("/leaveguild", "", "Leaves a guild");
	        gholder.addCmd("/guildinvite", "[player]", "Invites a player to a guild");
	        gholder.addCmd("/guildkick", "[player]", "Kicks a player from a guild");
	        gholder.addCmd("/setguildarea", "", "Set the guild area");
	        gholder.addCmd("/myguild", "", "Shows your guild");
	        gholder.addCmd("/gset", "[function] [value] ", "Change guild values");
	        gholder.addCmd("/gpermlist", "", "List guild permissions");
	        gholder.addCmd("/gfunclist", "", "List guild functions that is adjustable");
	        gholder.addCmd("/gmembers", "<guild>", "List members in a guild");
	        gholder.addCmd("/gadmins", "<guild>", "List admins in a guild");
	        gholder.addCmd("/gpromote", "[player]", "Promote a player in your guild");
	        gholder.addCmd("/gdemote", "[player]", "Demote a player in your guild");
	        gholder.addCmd("/gjyes", "", "Answer yes on a guild invite");
	        gholder.addCmd("/gjno", "", "Answer no on a guild invite");
	        gholder.addCmd("/gupgrade", "", "Try to upgrade your guild");
	        gholder.addCmd("/gnextlevel", "", "Print out what is needed for the next guild level");
	        gholder.addCmd("/gturnin", "[itemid] [amount]", "Turn in stuff to your \"guild leveler\"");
	        gholder.addCmd("/setguildhome", "", "Sets the guild warp point");
	        gholder.addCmd("/guildhome", "", "Teleport to your guild area");
	        gholder.addCmd("/gactivate", "", "Activates a guild so it is loaded on restart");
	        gholder.addCmd("/pstart", "[partyname]", "Starts a party");
	        gholder.addCmd("/pjoin", "[party]", "Joins a party");
	        gholder.addCmd("/pleave", "", "Leaves a party");
	    }
}
