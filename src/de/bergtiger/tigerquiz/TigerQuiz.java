package de.bergtiger.tigerquiz;

import org.bukkit.plugin.java.JavaPlugin;

import de.bergtiger.tigerquiz.commands.Commands;
import de.bergtiger.tigerquiz.listener.MyListenerOverview;

public class TigerQuiz extends JavaPlugin {
	
	private SessionOverview sessionOverview;
	private QuizAdministration quizAdministration;
	private MyListenerOverview myListenerOverview;
	private Commands commands;
	private Config config;
	
	@Override
	public void onEnable() {
		this.getLogger().info("starting");
		
		this.config = new Config(this);
		this.sessionOverview = new SessionOverview(this);
		this.quizAdministration = new QuizAdministration(this);
		this.myListenerOverview = new MyListenerOverview(this);
		this.commands = new Commands(this);
		this.getLogger().info("initialized");
		
		this.getCommand("tigerquiz").setExecutor(commands);
		this.getLogger().info("started");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("close open Quizzes");
		this.sessionOverview.reload();
		this.myListenerOverview.deaktivate();
		this.quizAdministration.reload();
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
	 * class with all listeners
	 * @return
	 */
	public MyListenerOverview getListener() {
		return this.myListenerOverview;
	}
	
	/**
	 * reloads Plugin (Data)
	 */
	public void reload(){
		//TODO
		this.sessionOverview.reload();
		this.myListenerOverview.deaktivate();
		this.quizAdministration.reload();
	}
}
