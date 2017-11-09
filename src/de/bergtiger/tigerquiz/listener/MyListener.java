package de.bergtiger.tigerquiz.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import de.bergtiger.tigerquiz.TigerQuiz;

public class MyListener implements Listener{

	protected TigerQuiz plugin;
	
	/**
	 * stops listener
	 */
	public void disable() {
		//TODO
	}
	
	/**
	 * aktivates Listener
	 */
	public void aktivate() {
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
}
