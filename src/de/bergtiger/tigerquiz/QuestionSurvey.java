package de.bergtiger.tigerquiz;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

public class QuestionSurvey extends Question{
	
	private TigerQuiz plugin;
	
	public QuestionSurvey(String title, boolean obligation, List<Answer> answers, int size, TigerQuiz plugin) {
		super(title, obligation, answers, size);
	}

	@Override
	boolean getCorrect(int slot, Session session) {
		//TODO save answer
		Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable(){

			@Override
			public void run() {
				//prevent save time error
				saveAnswer(slot, session);
			}
			
		});
		return true;
	}

	@Override
	public Question copy() {
		List<Answer> answers = null;
		if((this.answers != null) && (!this.answers.isEmpty())) {
			answers = new ArrayList<Answer>();
			for(Answer answer : this.answers) {
				answers.add(answer.copy());
			}
		}
		return new QuestionSurvey(this.title, this.obligation, answers, this.size, this.plugin);
	}
	
	/**
	 * saves the answer from the player
	 * @param slot
	 * @param session
	 */
	private void saveAnswer(int slot, Session session) {
		String answerId = null;
		for(Answer answer : this.answers) {
			if(answer.getSlot() == slot) {
				answerId = answer.getId();
				break;
			}
		}
		this.plugin.getQuiz().savePlayerAnswer(session.getQuizName(), session.getPlayer(), this.title, answerId);
	}
}
