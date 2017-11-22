package de.bergtiger.tigerquiz;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

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
			return inventory;
		}
		return null;
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
	 * creates new Instance of this Question
	 * @return
	 */
	abstract Question copy();
}
