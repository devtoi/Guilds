package org.toi.guilds;
import java.io.File;
import java.io.IOException;

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
	    
	    public Guilds(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
	        super(pluginLoader, instance, desc, folder, plugin, cLoader);
	        name = "Guilds";
	        version = "v1.3.0 (Bleaucha)";
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
	        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.High, this);
	        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.High, this);
	        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACED, blockListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_INTERACT, blockListener, Priority.Normal, this);
	        getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGEDBY_ENTITY, entityListener, Priority.Normal, this);
	    }

	    public void initiateGuildCommands()
	    {
	        gholder.addCmd("cmd", "", "Shows this list");
	    	gholder.addCmd("create", "[guildname] [kind]", "Adds an empty guild with you as admin (leaving your old one)");
	        gholder.addCmd("removeany", "[guild]", "Removes any guild");
	        gholder.addCmd("kinds", "", "Shows a list of avaliable guild kinds");
	        gholder.addCmd("remove", "", "Removes your guild");
	        gholder.addCmd("save", "[guild]", "Saves a guild to a file");
	        gholder.addCmd("load", "[guild]", "Loads and adds a guild from a file");
	        gholder.addCmd("saveall", "", "Saves all guilds to their files");
	        gholder.addCmd("list", "", "Shows list of existing guilds joinable");
	        gholder.addCmd("reloadall", "", "Reloads the guilds from their files");
	        gholder.addCmd("join", "[guild]", "Joins a guild");
	        gholder.addCmd("leave", "", "Leaves a guild");
	        gholder.addCmd("invite", "[player]", "Invites a player to a guild");
	        gholder.addCmd("kick", "[player]", "Kicks a player from a guild");
	        gholder.addCmd("setarea", "", "Set the guild area");
	        gholder.addCmd("me", "", "Shows your guild");
	        gholder.addCmd("set", "[function] [value] ", "Change guild values");
	        gholder.addCmd("permissions", "", "List guild permissions");
	        gholder.addCmd("functions", "", "List guild functions that is adjustable");
	        gholder.addCmd("members", "<guild>", "List members in a guild");
	        gholder.addCmd("admins", "<guild>", "List admins in a guild");
	        gholder.addCmd("promote", "[player]", "Promote a player in your guild");
	        gholder.addCmd("demote", "[player]", "Demote a player in your guild");
	        gholder.addCmd("accept", "", "Accept a guild invite");
	        gholder.addCmd("decline", "", "Decline a guild invite");
	        gholder.addCmd("upgrade", "", "Try to upgrade your guild");
	        gholder.addCmd("nextlevel", "", "Print out what is needed for the next guild level");
	        gholder.addCmd("turnin", "[itemid] [amount]", "Turn in stuff to your \"guild leveler\"");
	        gholder.addCmd("sethome", "", "Sets the guild warp point");
	        gholder.addCmd("home", "", "Teleport to your guild area");
	        gholder.addCmd("activate", "", "Activates a guild so it is loaded on restart");
	        gholder.addCmd("pstart", "[partyname]", "Starts a party");
	        gholder.addCmd("pjoin", "[party]", "Joins a party");
	        gholder.addCmd("pleave", "", "Leaves a party");
	    }
}
