package org.toi.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

public class tPermissions {
	
	private File file = new File("Guilds" + File.separator + "permissions.txt");
	private Hashtable<String, ArrayList<String>> hashPerms = new Hashtable<String, ArrayList<String>>(); 
	
	public tPermissions(String filePath)
	{
		file = new File(filePath);
	}

	public boolean canPlayerUseCommand(String player, String command)
	{
		if (this.hashPerms.containsKey(command))
		{
			ArrayList<String> alstr = this.hashPerms.get(command);
			if (alstr.contains(player) || alstr.contains("*"))
				return true;
			else
				return false;
		}
		return false;
	}
	
	public void addCmd(String cmd)
	{
		if (!this.hashPerms.containsKey(cmd))
		{
			ArrayList<String> als = new ArrayList<String>();
			als.add("---"); // Hashtables doesn't allow nulls
			this.hashPerms.put(cmd, als);
		}
	}
	
	public void loadPermissions()
	{
		if (this.file.exists())
		{
			if (this.file.isFile())
			{
				try
				{
					BufferedReader br = new BufferedReader(new FileReader(this.file));
					try
					{
						String line = br.readLine();
						if (line != null)
						{
							while (line != null)
							{
								if (!line.startsWith("//") && !line.startsWith(";") && !line.startsWith("#") && !line.equals(""))
								{
									String[] splitted = line.split("=");
									if (splitted.length >= 2)
									{
										String[] pSplit = splitted[1].split(",");
										if (hashPerms.containsKey(splitted[0]))
										{
											this.hashPerms.put(splitted[0], new ArrayList<String>(Arrays.asList(pSplit)));
										}
									}
								}
								line = br.readLine();
							}
						}
					}
					finally
					{
						br.close();
					}
				}
				catch (IOException ioe)
				{
					System.out.println("[tPermissions] Failed to read " + this.file.getPath() + "!");
				}
			}
			else
				System.out.println("[tPermissions] " + this.file.getPath() + " is not a file!");
		}
		else
		{
			savePermissions();
		}
	}
	
	public void savePermissions()
	{
		File dirs = new File(this.file.getPath().replace(this.file.getName(), ""));
		if (!dirs.exists())
			dirs.mkdirs();
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.file));
			
			try
			{
				for (Enumeration e = this.hashPerms.keys(); e.hasMoreElements();)
				{
					String key = (String)e.nextElement();
					ArrayList<String> alstr = this.hashPerms.get(key);
					bw.write(key + "=");
					boolean f = true;
					for (String str : alstr)
					{
						if (!str.equals("---"))
						{
							if (f)
							{
								bw.write(str);
								f = false;
							}
							else
								bw.write("," + str);
						}
					}
					bw.newLine();
				}
			}
			finally
			{
				bw.flush();
				bw.close();
			}
		}
		catch (IOException ioe)
		{
			System.out.println("[tPermissions] Unable to save file " + this.file.getPath());
		}
	}
}
