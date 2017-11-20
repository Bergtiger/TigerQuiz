package de.bergtiger.tigerquiz;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Session {

	private String quizName;
	private Player player;
	protected List<Question> questions;
	protected List<Question> questionsRest;
	private int quizSize = 0;
	private int quizMaxSize;
	private boolean prefix;
	private boolean ordered;
	private boolean oneTimeUse;
	
	protected Question currentQuestion; //latest question
	
	public Session(String quizName, int quizMaxSize, boolean prefix, boolean ordered, boolean oneTimeUse) {
		this.quizName = quizName;
		this.quizMaxSize = quizMaxSize;
		this.prefix = prefix;
		this.ordered = ordered;
		this.oneTimeUse = oneTimeUse;
	}
	
	/**
	 * save player set - if there is a player in this session you can not set an other one
	 * @param player
	 * @return if set was succesful
	 */
	public boolean setPlayer(Player player) {
		if(this.player == null) {
			this.player = player;
			return true;
		}
		return false;
	}
	
	/**
	 * sets the questions
	 * @param questions
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	/**
	 * if quiz is onetimeuse
	 * @return
	 */
	public boolean getOneTimeUse() {return this.oneTimeUse;}
	
	/**
	 * 
	 * @return new Instance of this Quiz
	 */
	public Session copy() {
		List<Question> questions = null;
		if((this.questions != null) && (!this.questions.isEmpty())) {
			questions = new ArrayList<Question>();
			for (Question question : this.questions) {
				questions.add(question.copy());
			}
		}
		return new Session(this.quizName, this.quizMaxSize, this.prefix, this.ordered, this.oneTimeUse);
	}
	
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
	
	public void penaltyQuestion() {
		this.quizMaxSize++;
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
				this.player.openInventory(question.getInventory(this.getTitle()));
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
		if(this.questions.size() < this.quizSize) {
			return this.questions.remove(0);
		} else if((this.questionsRest != null) && (!this.questionsRest.isEmpty())) {
			if(this.ordered) {
				return this.questionsRest.remove(0);
			} else {
				return this.questionsRest.remove((int)(Math.random() * questions.size()));
			}
		}
		return null;
	}
	
	/**
	 * shows status how many questions left
	 * @return "(x/n)" - ""
	 */
	private String getTitle() {
		if(this.prefix)	return "(" + this.quizSize + "/" + this.quizMaxSize + ") " + this.quizName + ": ";
		return this.quizName + ": ";
	}
	
	/**
	 * ends session immediately
	 */
	public void exit() {
		//TODO
	}
	/**
	 * get quizname of Session
	 * 
	 * @return quizname
	 */
	public String getQuizname() {
		return this.quizName;
	}
}
