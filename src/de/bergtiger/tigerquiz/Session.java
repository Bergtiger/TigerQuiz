package de.bergtiger.tigerquiz;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public abstract class Session {

	private Player player;
	protected List<Question> questions;
	private int quizSize;
	private int quizMaxSize;
	private boolean prefix;
	private boolean ordered;
	
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
	private Question getQuestion() {
		if(this.ordered) {
			if(this.questions.size() < this.quizSize){
				return this.questions.get(this.quizSize);
			}
		} else {
			if((this.questions != null) && (!this.questions.isEmpty())){
				List<Question> questions = new ArrayList<Question>();
				for(Question question : this.questions){
					if(!question.getDemand()) {
						questions.add(question);
					}
				}
				if(!questions.isEmpty()) return questions.get((int)(Math.random() * questions.size()));
			}
		}
		return null;
	}
	
	/**
	 * shows status how many questions left
	 * @return "(x/n)" - ""
	 */
	private String getPrefix() {
		if(this.prefix)	return "(" + this.quizSize + "/" + this.quizMaxSize + ") ";
		return "";
	}
	
	/**
	 * ends session immediately
	 */
	public void exit() {
		//TODO
	}
}
