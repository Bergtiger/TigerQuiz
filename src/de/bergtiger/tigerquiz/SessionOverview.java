package de.bergtiger.tigerquiz;

import java.util.HashMap;

import org.bukkit.entity.Player;

import de.bergtiger.tigerquiz.data.MyString;

public class SessionOverview {

	private TigerQuiz plugin;
	private HashMap<String, Session> sessions;
	
	public SessionOverview(TigerQuiz plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * starts a Session
	 * @param quiz - quizname
	 * @param p - player
	 * @return false (player in quiz, quiz not exists) true else
	 */
	public boolean startSession(String quiz, Player p) {
		if(!this.sessions.containsKey(p.getName())) {
			if(p.hasPermission("tigerquiz." + quiz)) {
				
			} else {
				p.sendMessage(MyString.NOPERMISSIONS.colored());
			}
		} else {
			//TODO
			p.sendMessage("hasQuiz");
		}
		return false;
	}
	
	/**
	 * check if player has a session
	 * @param p - player to check
	 * @return true if has session - false else
	 */
	public boolean hasSession(Player p) {
		if((this.sessions != null) && (!this.sessions.isEmpty())) {
			return this.sessions.containsKey(p.getName());
		}
		return false;
	}
	
	/**
	 * 
	 * @param p - player
	 * @return
	 */
	public Session getSession(Player p) {
		if((this.sessions != null) && (!this.sessions.isEmpty())) {
			return this.sessions.get(p);
		}
		return null;
	}
}
