package de.bergtiger.tigerquiz;

import org.bukkit.inventory.ItemStack;

/**
 * @author Bergtiger
 *
 */
public class Answer {

	private int slot;
	private boolean correct;
	private ItemStack item;
	
	/**
	 * position in inventory
	 * @return
	 */
	public int getSlot() {
		return this.slot;
	}
	
	/**
	 * if answer has correct
	 * @return
	 */
	public boolean getCorrect() {
		return this.correct;
	}
	
	/**
	 * the item shown in the inventory
	 * @return
	 */
	public ItemStack getItem() {
		return this.item;
	}
}
