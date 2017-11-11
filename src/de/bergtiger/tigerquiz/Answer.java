package de.bergtiger.tigerquiz;

import org.bukkit.inventory.ItemStack;

/**
 * @author Bergtiger
 *
 */
public class Answer {

	protected int slot;
	private boolean function;
	private boolean correct;
	private ItemStack item;
	
	/**
	 * 
	 * @param slot - position 0 - 8 first row - + 9 ech row
	 * @param item - itemstack, this will be the shown item
	 * @param function - if it has a function - like true/false
	 * @param correct - the true/false value
	 */
	public Answer(int slot, ItemStack item, boolean function, boolean correct){
		this.slot = slot;
		this.item = item;
		this.function = function;
		this.correct = correct;
	}
	
	/**
	 * get a new Instance of this answer
	 * @return
	 */
	public Answer copy() {
		return new Answer(this.slot, this.item, this.function, this.correct);
	}
	
	/**
	 * position in inventory
	 * @return
	 */
	public int getSlot() {
		return this.slot;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasFunction() {
		return this.function;
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
