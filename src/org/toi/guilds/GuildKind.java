package org.toi.guilds;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


class GuildKind {

	private String name = "Unknown";
	private String description = "";
	private ArrayList<ItemNeeded> itemsNeeded = new ArrayList<ItemNeeded>();
	private int maxLevel = - 1;
	
	public GuildKind(String name)
	{
		this.name = name;
	}
	
	public GuildKind(GuildKind gk)
	{
		this.name = gk.getName();
		this.description = gk.getDescription();
		this.itemsNeeded = gk.getItemsNeeded();
		this.maxLevel = gk.getMaxLevel();
	}
	
	public boolean readFromFile()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("Guilds" + File.separator + "GuildKinds" + File.separator + name + ".txt"));
			try
			{
				String line = br.readLine();
				if (line != null)
				{
					while (line != null)
					{
						if (!line.equalsIgnoreCase("-----Levels-----"))
						{
							if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
							{
								if (line.startsWith("name"))
								{
									String[] name = line.split(":");
									if (!name[1].equalsIgnoreCase("") && name[1] != null)
										this.name = name[1];
								}
								else if (line.startsWith("description"))
								{
									String[] desc = line.split(":");
									if (!desc[1].equalsIgnoreCase("") && desc[1] != null)
										this.description = desc[1];
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
						if (!line.startsWith("#") && !line.startsWith("//") && !line.startsWith(";") && !line.equals(""))
						{
							String[] val = line.split(":");
							if (GHolder.isIntNumber(val[0]))
							{
								int level = Integer.valueOf(val[0]); 
								if (level >= 0)
								{
									if (val.length > 1)
									{
										if (val[1].equalsIgnoreCase("max"))
											this.maxLevel = level - 1;
										else
										{
											for (String str : val[1].split(";"))
											{
												String[] sstr = str.split(",");
												this.itemsNeeded.add(new ItemNeeded(level, Integer.valueOf(sstr[0]), Integer.valueOf(sstr[1])));
											}
										}
									}
								}
							}
						}
						line = br.readLine();
					}
				}
					
				return true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
			finally
			{
				br.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean hasEnoughForNextLevel(int score, ArrayList<ItemNeeded> in)
	{
		int has = 0;
		int need = 0;
		for (ItemNeeded kin : this.itemsNeeded)
		{
			if (kin.getLevel() == score + 1)
			{
				for (ItemNeeded gin : in)
				{
					if (gin.getItemIndex() == kin.getItemIndex())
					{
						if (gin.getAmount() >= kin.getAmount())
							has++;
						need++;
					}
				}
			}
		}
		if (has >= need && need != 0)
		{
			return true;
		}
		else
			return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<ItemNeeded> getItemsNeeded() {
		return itemsNeeded;
	}

	public void setItemsNeeded(ArrayList<ItemNeeded> itemsNeeded) {
		this.itemsNeeded = itemsNeeded;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	
}
