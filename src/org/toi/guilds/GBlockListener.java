package org.toi.guilds;
import org.bukkit.block.Block;
import org.bukkit.block.BlockDamageLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;


public class GBlockListener extends BlockListener{

	GHolder gholder;
    public GBlockListener(GHolder gholder) {
    	this.gholder = gholder;
    }
    
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Block blockPlaced = event.getBlock();
		
		for (Guild guild : gholder.getGuilds())
		{
			if (guild.isInArea(blockPlaced.getX(), blockPlaced.getZ()))
			{
				if (guild.getPlayers().size() > 0)
				{
					for (GPlayer gp: guild.getPlayers())
					{
						if (player.getName().equalsIgnoreCase(gp.getName()))
						{
							if (gp.getRank() >= guild.getPerBuild() || guild.isPlayerAdmin(gp.getName()))
							{
								break;
							}
							else
							{
								event.setCancelled(true);
								break;
							}
						}
						else
						{
							event.setCancelled(true);
						}
					}
					break;
				}
			}
		}
	}
	
	public void onBlockDamage(BlockDamageEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (event.getDamageLevel().equals(BlockDamageLevel.BROKEN))
		for (Guild guild : gholder.getGuilds())
		{
			if (guild.isInArea(block.getX(), block.getZ()))
			{
				if (guild.getPlayers().size() > 0)
				{
					for (GPlayer gp: guild.getPlayers())
					{
						if (player.getName().equalsIgnoreCase(gp.getName()))
						{
							if (gp.getRank() >= guild.getPerDestroy() || guild.isPlayerAdmin(gp.getName()))
							{
								break;
							}
							else
							{
								event.setCancelled(true);
							}
						}
						else
						{
							event.setCancelled(true);
						}
					}
					break;
				}
				else
				{
					event.setCancelled(true);
				}
			}
		}
	}
}
