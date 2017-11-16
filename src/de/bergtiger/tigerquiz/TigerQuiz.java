package de.bergtiger.tigerquiz;

import org.bukkit.plugin.java.JavaPlugin;

import de.bergtiger.tigerquiz.commands.Commands;

public class TigerQuiz extends JavaPlugin {
	
	private SessionOverview sessionOverview;
	private QuizAdministration quizAdministration;
	private Commands commands;
	
	@Override
	public void onEnable() {
		this.getLogger().info("starting");
		
		this.sessionOverview = new SessionOverview(this);
		this.quizAdministration = new QuizAdministration(this);
		this.commands = new Commands(this);
		this.getLogger().info("initialized");
		
		this.getCommand("tigerquiz").setExecutor(commands);
		this.getLogger().info("started");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("end");
	}

	/**
	 * class with all sessions from the players
	 * @return
	 */
	public SessionOverview getSessions() {
		return this.sessionOverview;
	}
	
	/**
	 * class with a lot of quiz functions(check/add)
	 * @return
	 */
	public QuizAdministration getQuiz() {
		return this.quizAdministration;
	}
	
	/**
	 * reloads Plugin (Data)
	 */
	public void reload(){
		//TODO
	}
}
