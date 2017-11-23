package de.bergtiger.tigerquiz;

import java.io.File;

public class Config {
	
	private TigerQuiz plugin;
	
	public Config(TigerQuiz plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * loads quiz
	 * @param quiz - name/id of quiz
	 */
	public void loadQuiz(String quiz) {
		File file = new File("/plugins" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			//TODO
		} else {
			//TODO
		}
	}
	
	///creates new quiz
	public boolean createQuiz(String quiz) {
		File file = new File("/plugins" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(!file.exists()) {
			//TODO
		}
		return false;
	}
	
	//creates new question in a quiz
	public boolean createQuestion(String quiz, Question question) {
		File file = new File("/plugins" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			//TODO
		}
		return false;
	}
}
