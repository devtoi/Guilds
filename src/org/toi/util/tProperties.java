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


public class tProperties {
	
	private File propertyFile = new File("default.properties");
	private Hashtable<String, String> props = new Hashtable<String, String>();
	
	public tProperties(String filepath)
	{
		filepath = this.proccessPath(filepath);
		propertyFile = new File(filepath);
	}
	
	private String proccessPath (String path)
	{
		path = path.replace("\\", File.separator);
		path = path.replace("/", File.separator);
		return path;
	}

	public void load() throws IOException
	{
		if (this.propertyFile.exists())
		{
			if (this.propertyFile.isFile())
			{
				BufferedReader br = new BufferedReader(new FileReader(this.propertyFile));
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
									this.props.put(splitted[0], splitted[1]);
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
			else
				System.out.println("[tProperties] " + this.propertyFile.getPath() + " is not a file!");
		}
		else
		{
			save();
		}
	}
	
	public void save()
	{
		File dirs = new File(this.propertyFile.getPath().replace(this.propertyFile.getName(), ""));
		if (!dirs.exists())
			dirs.mkdirs();
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.propertyFile));
			
			try
			{
				for (Enumeration e = this.props.keys(); e.hasMoreElements();)
				{
					String key = (String)e.nextElement();
					String val = (String)this.props.get(key);
					bw.write(key + "=" + val);
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
			System.out.println("[tProperties] Unable to save file " + this.propertyFile.getPath());
		}
	}
	
	public String getString(String key)
	{
        if (this.props.containsKey(key))
        {
            return this.props.get(key);
        }
        return "";
    }

    public String getString(String key, String value)
    {
        if (this.props.containsKey(key))
        {
            return this.props.get(key);
        }
        setString(key, value);
        return value;
    }

    public void setString(String key, String value)
    {
        props.put(key, value);
    }

    public ArrayList<String> getStringList(String key)
    {
    	if (this.props.containsKey(key))
        {
    		String sal = this.props.get(key);
    		ArrayList<String> al = new ArrayList<String>(Arrays.asList(sal));
            return al;
        }
        return new ArrayList<String>();
    }
    
    public ArrayList<String> getStringList(String key, ArrayList<String> value)
    {
    	if (this.props.containsKey(key))
        {
    		String sal = this.props.get(key);
    		ArrayList<String> al = new ArrayList<String>(Arrays.asList(sal));
            return al;
        }
        setStringList(key, value);
        return value;
    }
    
    public void setStringList(String key, ArrayList<String> value)
    {
    	String str = "";
    	boolean first = true;
    	for (String s : value)
    	{
    		if (first)
    		{
    			str += s;
    			first = false;
    		}
    		else
    			str += "," + s;
    	}
    	this.props.put(key, str);
    }
    
    public int getInt(String key)
    {
        if (this.props.containsKey(key))
        {
            return Integer.parseInt(this.props.get(key));
        }
        return 0;
    }

    public int getInt(String key, int value)
    {
        if (this.props.containsKey(key))
        {
            return Integer.parseInt(this.props.get(key));
        }
        setInt(key, value);
        return value;
    }

    public void setInt(String key, int value)
    {
        props.put(key, String.valueOf(value));
    }

    public double getDouble(String key)
    {
        if (this.props.containsKey(key))
        {
            return Double.parseDouble(this.props.get(key));
        }
        return 0;
    }

    public double getDouble(String key, double value)
    {
        if (this.props.containsKey(key))
        {
            return Double.parseDouble(this.props.get(key));
        }
        setDouble(key, value);
        return value;
    }

    public void setDouble(String key, double value)
    {
        props.put(key, String.valueOf(value));
    }

    public long getLong(String key)
    {
        if (this.props.containsKey(key))
        {
            return Long.parseLong(this.props.get(key));
        }
        return 0;
    }

    public long getLong(String key, long value)
    {
        if (this.props.containsKey(key))
        {
            return Long.parseLong(this.props.get(key));
        }
        setLong(key, value);
        return value;
    }

    public void setLong(String key, long value)
    {
        props.put(key, String.valueOf(value));
    }

    public boolean getBoolean(String key)
    {
        if (this.props.containsKey(key))
        {
            return Boolean.parseBoolean(this.props.get(key));
        }
        return false;
    }

    public boolean getBoolean(String key, boolean value)
    {
        if (this.props.containsKey(key)) {
            return Boolean.parseBoolean(this.props.get(key));
        }
        setBoolean(key, value);
        return value;
    }

    public void setBoolean(String key, boolean value)
    {
        props.put(key, String.valueOf(value));
    }
}
