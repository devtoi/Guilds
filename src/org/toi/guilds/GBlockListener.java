package org.toi.guilds;
import org.bukkit.Block;
import org.bukkit.BlockDamageLevel;
import org.bukkit.Player;
import org.bukkit.event.block.BlockDamagedEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlacedEvent;


public class GBlockListener extends BlockListener{

	GHolder gholder;
    public GBlockListener(GHolder gholder) {
    	this.gholder = gholder;
    }
    
	public void onBlockPlaced(BlockPlacedEvent event)
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
	
	public void onBlockDamage(BlockDamagedEvent event)
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
