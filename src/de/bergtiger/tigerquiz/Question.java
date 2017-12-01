package de.bergtiger.tigerquiz;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.bergtiger.tigerquiz.data.MyString;

/** @author Bergtiger
 */

public abstract class Question {
	
	protected List<Answer> answers;
	protected int size;
	protected String title;
	protected boolean obligation;
	
	public Question(String title, boolean obligation, List<Answer> answers, int size) {
		this.title = title;
		this.obligation = obligation;
		this.answers = answers;
		this.size = size;
	}
	
	/**
	 * sets the posible answers;
	 * @param answers
	 */
	public void setAnswers(List<Answer> answers){
		this.answers = answers;
	}
	
	/**
	 * 
	 * @return a inventory witch represents the question
	 */
	public Inventory getInventory(String prefix){
		if((this.answers != null) && (!this.answers.isEmpty())) {
			//autorezise
			Inventory inventory = Bukkit.createInventory(null, Math.max(1, 9 * this.size), prefix + this.title);			
			for (int i = 0; i < this.answers.size(); i++) {
				Answer answer = this.answers.get(i);
				inventory.setItem(answer.getSlot(), answer.getItem());
			}
			inventory.setItem(inventory.getSize() - 1, this.getReturn());
			return inventory;
		}
		return null;
	}
	
	/**
	 * return item
	 * @return
	 */
	private ItemStack getReturn() {
		ItemStack item = new ItemStack(Material.CONCRETE, 1, (short)14); //red concrete
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(MyString.setColor("&4Return/Zurück"));
		item.setItemMeta(meta);
		return item;
	}
	
	/**
	 * when question musst be asked
	 * @return true when obligation - musst be asked
	 */
	public boolean isObligation() {
		return this.obligation;
	}
	
	/**
	 * 
	 * @param slot
	 * @return true if return, false else
	 */
	public boolean isReturn(int slot) {
		return (slot == (Math.max(1, 9 * this.size) - 1)) ? true : false;
	}
	
	public boolean hasFunction(int slot) {
		for(Answer answer : this.answers) {
			if(answer.slot == slot) {
				return answer.hasFunction();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param slot - item clicked by player
	 * @return if the answer was correct
	 */
	abstract boolean getCorrect(int slot, Session session);
	
	/**
	 * creates new Instance of this Question
	 * @return
	 */
	abstract Question copy();
}
