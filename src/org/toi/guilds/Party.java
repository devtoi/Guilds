package org.toi.guilds;
import java.util.ArrayList;


public class Party {
	private ArrayList<String> players = new ArrayList<String>();
	private String name;
	
	public Party()
	{
		
	}
	
	public Party(String name, String startPlayer)
	{
		this.name = name;
		this.players.add(startPlayer);
	}
	
	public ArrayList<String> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean addPlayer(String playerName)
	{
		boolean hasPlayer = false;
		for (String str : this.players)
		{
			if (str.equalsIgnoreCase(playerName))
				hasPlayer = true;
		}
		if (!hasPlayer)
			this.players.add(playerName);
		return !hasPlayer;
	}
	
	public boolean removePlayer(String playerName)
	{
		boolean hasPlayer = false;
		for (String str : this.players)
		{
			if (str.equalsIgnoreCase(playerName))
				hasPlayer = true;
		}
		if (hasPlayer)
			this.players.remove(playerName);
		return hasPlayer;
	}
}
