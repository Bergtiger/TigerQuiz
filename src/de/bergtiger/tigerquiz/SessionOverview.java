package de.bergtiger.tigerquiz;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class SessionOverview {

	private TigerQuiz plugin;
	private HashMap<String, Session> sessions;
	
	/**
	 * starts a Session
	 * @param quiz - quizname
	 * @param p - player
	 * @return false (player in quiz, quiz not exists) true else
	 */
	public boolean startSession(String quiz, Player p) {
		return false;
	}
}
