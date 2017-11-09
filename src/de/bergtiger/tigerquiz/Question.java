package de.bergtiger.tigerquiz;

import java.util.List;

import org.bukkit.inventory.Inventory;

/** @author Bergtiger
 */

public class Question {
	
	protected List<Answer> answers;
	
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
	public Inventory getInventory(){
		return null;
	}
	
}
