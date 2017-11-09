package de.bergtiger.tigerquiz.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import de.bergtiger.tigerquiz.TigerQuiz;

public class MyListener implements Listener{

	protected TigerQuiz plugin;
	
	/**
	 * 
	 * @param plugin
	 */
	public MyListener(TigerQuiz plugin) {
		this.plugin = plugin;
	}
	/**
	 * stops listener
	 */
	public void disable() {
		HandlerList.unregisterAll(this);
	}
	
	/**
	 * aktivates Listener
	 */
	public void aktivate() {
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
}
