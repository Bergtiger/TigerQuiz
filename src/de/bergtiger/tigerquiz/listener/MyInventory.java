package de.bergtiger.tigerquiz.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.bergtiger.tigerquiz.TigerQuiz;

public class MyInventory extends MyListener {

	public MyInventory(TigerQuiz plugin) {
		super(plugin);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(this.plugin.getSessions().hasSession(p)){
			if(e.getClickedInventory() != null){
				if((e.getSlot() == e.getRawSlot()) && ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() != Material.AIR))){	//itemslot not playerinventory and item not air
					this.plugin.getSessions().getSession(p).receiveAnswer(e.getRawSlot());
				} else {
					e.setCancelled(true);
				}
			}
		}
	}
	
	//abbruch
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		this.exit((Player) e.getPlayer());
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		this.exit((Player) e.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		this.exit((Player) e.getPlayer());
	}
	
	/**
	 * 
	 * @param p - player to end session
	 */
	private void exit(Player p) {
		if(this.plugin.getSessions().hasSession(p)) {
			this.plugin.getSessions().getSession(p).exit();
		}
	}
}
