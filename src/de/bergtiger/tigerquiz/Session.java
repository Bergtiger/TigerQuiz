package de.bergtiger.tigerquiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.bergtiger.tigerquiz.data.MyClose;

public class Session {

	private TigerQuiz plugin;
	private String quizName;
	private Player player;
	private List<Question> questions; //normal questions
	private List<Question> questionsRest; //questions which will be used when spezial error behavior (quizlength++)
	private List<String> reward;
	private HashMap<Integer, List<String>> penalties;
	private int quizSize = 0;
	private int quizMaxSize;
	private int penaltySize = 0;
	private int error = 0;
	private boolean prefix;
	private boolean ordered;
	private boolean oneTimeUse;
	
	protected Question currentQuestion; //latest question
	
	public Session(TigerQuiz plugin, String quizName, int quizMaxSize, boolean prefix, boolean ordered, boolean oneTimeUse, List<String> reward, HashMap<Integer, List<String>> penalties) {
		this.plugin = plugin;
		this.quizName = quizName;
		this.quizMaxSize = quizMaxSize;
		this.prefix = prefix;
		this.ordered = ordered;
		this.oneTimeUse = oneTimeUse;
		this.reward = reward;
		this.penalties = penalties;
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
	 * save player set - if there is a player in this session you can not set an other one
	 * @param player
	 * @param error
	 * @return if set was succesful
	 */
	public boolean setPlayer(Player player, int error) {
		if(this.player == null) {
			this.player = player;
			this.error = error;
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
	 * sets Question and Questions rest
	 * @param questions
	 * @param questionsRest
	 */
	public void setQuestions(List<Question> questions, List<Question> questionsRest) {
		this.questions = questions;
		this.questionsRest = questionsRest;
	}
	
	/**
	 * get quizname of Session 
	 * @return quizname
	 */
	public String getQuizName() {
		return this.quizName;
	}
	
	/**
	 * get size of question that will be asked
	 * @return 
	 */
	public int getSize() {
		return this.quizMaxSize;
	}
	
	/**
	 * if quiz is onetimeuse
	 * @return
	 */
	public boolean getOneTimeUse() {
		return this.oneTimeUse;
	}
	
	/**
	 * are the questions from the quiz ordered ?
	 * @return true - ordered
	 */
	public boolean isOrdered() {
		return this.ordered;
	}
	
	/**
	 * 
	 * @return player from this session
	 */
	public Player getPlayer() {
		return this.player;
	}
	
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
		return new Session(this.plugin, this.quizName, this.quizMaxSize, this.prefix, this.ordered, this.oneTimeUse, this.reward, this.penalties);
	}
	
	/**
	 * perform when player clicked an item
	 * @param slot
	 */
	public void receiveAnswer(int slot) {
		if(currentQuestion != null) {
			if(this.currentQuestion.isReturn(slot)) {
				System.out.println("r1 return");
				this.exit(MyClose.RETURN);
			} else {
				if(this.currentQuestion.hasFunction(slot)) {
					System.out.println("r2 has function");
					if(this.currentQuestion.getCorrect(slot)) {
						System.out.println("r3 next question");
						this.nextQuestion();
					} else {
						//wrong answer - abort
						System.out.println("r4 wrong answer");
						this.plugin.getQuiz().savePlayerError(player, this.quizName, this.error++);
						this.penalty();
						this.exit(MyClose.WRONG);
					}
				}
			}
		}
	}
	
	/**
	 * error behavior
	 * list of commands
	 */
	private void penalty() {
		if((this.penalties != null) && (!this.penalties.isEmpty())) {
			List<String> penalties = this.penalties.get(this.error);
			for(String args : penalties) {
				//intern penalties
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), args.replace("-player-", this.player.getName()));
			}
		}
	}
	
	/**
	 * spezial error behavior
	 */
	public void penaltyQuestion() {
		this.penaltySize++;
	}
	
	/**
	 * perform to show next question
	 */
	private boolean nextQuestion() {
		if(currentQuestion != null){
			//close currentQuestion
			//TODO WARNING - openInventory forces closeInventory
			this.closeInventory();
		}
		//open new Question
		if(this.quizSize < (this.quizMaxSize + this.penaltySize)){
			//next question
			Question question = this.getQuestion();
			if(question != null){
				this.player.openInventory(question.getInventory(this.getTitle()));
				this.currentQuestion = question;
				this.quizSize++;
			} else {
				//quiz end - no new question avaible
				this.end();
			}
		} else {
			this.end();
		}
		return false;
	}
	
	private boolean closeInventory() {
//		this.plugin.getSessions().removeSession(this.player);
		this.player.closeInventory();
//		this.plugin.getSessions().addSession(this);
		return true;
	}
	
	/**
	 * different Session types: random question ordered question
	 * @return
	 */
	private Question getQuestion() {
		if(this.questions.size() > this.quizSize) {
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
System.out.println("prefix: " + this.prefix);
		if(this.prefix)	{
			if(this.penaltySize > 0) {
				return "(" + this.quizSize + "/" + this.quizMaxSize + " + " + this.penaltySize + ") " + this.quizName + ": ";
			} else {
				return "(" + this.quizSize + "/" + this.quizMaxSize + ") " + this.quizName + ": ";
			}
		}
		return this.quizName + ": ";
	}
	
	/**
	 * starts first Question
	 */
	public void start() {
		//old penalties ?
		this.nextQuestion();
	}
	
	/**
	 * 
	 */
	private void end() {
		for(String args : this.reward) {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), args.replace("-player-", this.player.getName()));
		}
		//save player
		if(this.oneTimeUse) {
			this.plugin.getQuiz().savePlayer(this.player);
		}
	}
	
	/**
	 * ends session immediately
	 */
	public void exit(MyClose close) {
		//TODO
		if(close != MyClose.CLOSE) {
			this.closeInventory();
			this.plugin.getSessions().removeSession(this.player);
		}
	}
}
