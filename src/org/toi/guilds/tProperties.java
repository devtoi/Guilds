package org.toi.guilds;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
    public String getString(String key) {
    	String str = this.props.get(key);
        if (str != null)
        	return str;
        else
        	return "";
    }

    public String getString(String key, String value) {
    	String str = this.props.get(key);
        if (str != null)
        	return str;

        this.props.put(key, value);
        return value;
    }

    public int getInt(String key) {
    	int i;
    	try{
	        i = Integer.parseInt(this.props.get(key));
	        return i;
    	}
    	catch(NumberFormatException nfe){
    		return 0;
    	}
    }

    public int getInt(String key, int value) {
    	int i;
    	try{
	        i = Integer.parseInt(this.props.get(key));
	        return i;
    	}
    	catch(NumberFormatException nfe){
    		this.props.put(key, String.valueOf(value));
    		return value;
    	}
    	catch(NullPointerException nfe){
    		this.props.put(key, String.valueOf(value));
    		return value;
    	}
    }

    public double getDouble(String key) {
    	double d;
    	try{
	        d = Double.parseDouble(this.props.get(key));
	        return d;
    	}
    	catch(NumberFormatException nfe){
    		return 0;
    	}
    }

    public double getDouble(String key, double value) {
    	double d;
    	try{
	        d = Double.parseDouble(this.props.get(key));
	        return d;
    	}
    	catch(NumberFormatException nfe){
    		this.props.put(key, String.valueOf(value));
    		return value;
    	}
    	catch(NullPointerException nfe){
    		this.props.put(key, String.valueOf(value));
    		return value;
    	}
    }

    public long getLong(String key) {
    	long l;
    	try{
	        l = Long.parseLong(this.props.get(key));
	        return l;
    	}
    	catch(NumberFormatException nfe){
    		return 0;
    	}
    }

    public long getLong(String key, long value) {
    	long l;
    	try{
	        l = Long.parseLong(this.props.get(key));
	        return l;
    	}
    	catch(NumberFormatException nfe){
    		this.props.put(key, String.valueOf(value));
    		return value;
    	}
    	catch(NullPointerException nfe){
    		this.props.put(key, String.valueOf(value));
    		return value;
    	}
    }
    
    public float getFloat(String key) {
    	float f;
    	try{
	        f = Float.parseFloat(this.props.get(key));
	        return f;
    	}
    	catch(NumberFormatException nfe){
    		return 0;
    	}
    }

    public float getFloat(String key, float value) {
    	float f;
    	try{
	        f = Float.parseFloat(this.props.get(key));
	        return f;
    	}
    	catch(NumberFormatException nfe){
    		this.props.put(key, String.valueOf(value));
    		return value;
    	}
    	catch(NullPointerException nfe){
    		this.props.put(key, String.valueOf(value));
    		return value;
    	}
    }

    public boolean getBoolean(String key) {
    	boolean b = false;
    	if (this.isBoolean(this.props.get(key)))
		{
			b = Boolean.parseBoolean(this.props.get(key));
		}
    	return b;
    }

    public boolean getBoolean(String key, boolean value) {
    	boolean b;
		if (this.isBoolean(this.props.get(key)))
		{
			try{
				b = Boolean.parseBoolean(this.props.get(key));
		        return b;
			}
	    	catch(NullPointerException nfe){
	    		b = false;
	    		return false;
	    	}
		}
		else
		{
			this.props.put(key, String.valueOf(value));
			return value;
		}
    }

    private boolean isBoolean(String str)
    {
    	if (str == null)
    		return false;
    	if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false"))
    		return true;
    	else
    		return false;
    }
}
