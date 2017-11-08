package de.bergtiger.tigerquiz;

import org.bukkit.plugin.java.JavaPlugin;

public class TigerQuiz extends JavaPlugin {
	
	@Override
	public void onEnable(){
		this.getLogger().info("starting");
	}
	
	@Override
	public void onDisable(){
		this.getLogger().info("end");
	}

}
