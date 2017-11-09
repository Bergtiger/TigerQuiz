package de.bergtiger.tigerquiz;

import org.bukkit.plugin.java.JavaPlugin;

public class TigerQuiz extends JavaPlugin {
	
	private SessionOverview sessionOverview;
	
	@Override
	public void onEnable(){
		this.getLogger().info("starting");
	}
	
	@Override
	public void onDisable(){
		this.getLogger().info("end");
	}

	/**
	 * class with all sessions from the players
	 * @return
	 */
	public SessionOverview getSessions(){
		return this.sessionOverview;
	}
	
	/**
	 * reloads Plugin (Data)
	 */
	public void reload(){
		//TODO
	}
}
