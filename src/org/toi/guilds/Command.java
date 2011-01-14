package org.toi.guilds;

public class Command {
	private String command;
	private String syntaxes;
	private String description;
	
	public Command (String command, String syntaxes, String description)
	{
		this.command = command;
		this.syntaxes = syntaxes;
		this.description = description;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getSyntaxes() {
		return syntaxes;
	}

	public void setSyntaxes(String syntaxes) {
		this.syntaxes = syntaxes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
