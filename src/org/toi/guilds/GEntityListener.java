package org.toi.guilds;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;


public class GEntityListener extends EntityListener{

	GHolder gholder;
    public GEntityListener(GHolder gholder) {
    	this.gholder = gholder;
    }
	
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent event)
	{
		DamageCause cause = event.getCause();
		if (cause == DamageCause.ENTITY_ATTACK)
		{
			if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
			{
				Player attacker = (Player)event.getDamager();
				Player defender = (Player)event.getEntity();
				
				for (Guild fac : gholder.getGuilds())
				{
					boolean df = false;
					boolean af = false;
					for (int i = 0; i < fac.getPlayers().size(); i++)
					{
						if (fac.getPlayers().get(i).getName().equalsIgnoreCase(defender.getName()))
						{
							df = true;
						}
						else if (fac.getPlayers().get(i).getName().equalsIgnoreCase(attacker.getName()))
						{
							af = true;
						}
					}
					if (df && af)
						event.setCancelled(true);
				}
				for (Party party : gholder.getPartys())
				{
					boolean df = false;
					boolean af = false;
					for (String plr : party.getPlayers())
					{
						if (plr.equalsIgnoreCase(defender.getName()))
							df = true;
						else if (plr.equalsIgnoreCase(attacker.getName()))
							af = true;
					}
					if (df && af)
						event.setCancelled(true);
				}
			}
		}
	}
}
