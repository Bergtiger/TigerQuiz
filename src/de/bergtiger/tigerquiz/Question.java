package de.bergtiger.tigerquiz;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

/** @author Bergtiger
 */

public abstract class Question {
	
	protected List<Answer> answers;
	private boolean demand = false;
	private int size;
	private String title;
	
	
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
			return inventory;
		}
		return null;
	}
	
	/**
	 * 
	 * @param slot
	 * @return true if return, false else
	 */
	public boolean isReturn(int slot){
		return (slot == (Math.max(1, 9 * this.size) - 1)) ? true : false;
	}
	
	/**
	 * 
	 * @param slot - item clicked by player
	 * @return if the answer was correct
	 */
	abstract boolean getCorrect(int slot);
	
	/**
	 * when Question should only shown once
	 * @return
	 */
	public boolean getDemand(){
		return this.demand;
	}
	
	/**
	 * when question is shown set Demand
	 */
	public void setDemand(){
		this.demand = true;
	}
}
