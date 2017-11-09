package de.bergtiger.tigerquiz;

import java.util.List;

import org.bukkit.entity.Player;

public abstract class Session {

	private Player player;
	protected List<Question> questions;
	private int quizSize;
	private int quizMaxSize;
	
	protected Question currentQuestion; //latest question
	
	/**
	 * perform when player clicked an item
	 * @param slot
	 */
	public void receiveAnswer(int slot) {
		if(currentQuestion != null) {
			if(this.currentQuestion.getCorrect(slot)) {
				this.nextQuestion();
			} else {
				//wrong answer - abort
			}
		}
	}
	
	/**
	 * perform to show next question
	 */
	private boolean nextQuestion() {
		if(currentQuestion != null){
			//close currentQuestion
			//TODO WARNING - openInventory forces closeInventory
		}
		//open new Question
		if(this.quizSize < this.quizMaxSize){
			//next question
			Question question = this.getQuestion();
			if(question != null){
				this.player.openInventory(question.getInventory(this.getPrefix()));
				question.setDemand();
				this.currentQuestion = question;
				this.quizSize++;
			} else {
				//quiz end - no new question avaible
			}
		} else {
			//quiz end
		}
		return false;
	}
	
	/**
	 * different Session types: random question ordered question
	 * @return
	 */
	abstract Question getQuestion();
	
	/**
	 * 
	 * @return
	 */
	abstract String getPrefix();
}
