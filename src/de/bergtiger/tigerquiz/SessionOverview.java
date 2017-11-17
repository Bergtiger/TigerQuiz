package de.bergtiger.tigerquiz;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class SessionOverview {

	private TigerQuiz plugin;
	private HashMap<String, Session> sessions;
	
	public SessionOverview(TigerQuiz plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * starts a Session
	 * @param session
	 * @return
	 */
	public boolean startSession(Session session) {
		if(this.sessions == null) {
			this.sessions = new HashMap<String, Session>();
		}
		if(!this.sessions.containsKey(session.getPlayer().getName())) {
			this.sessions.put(session.getPlayer().getName(), session);
			//getQuestions
			session.start();
			return true;
		} else {
			this.plugin.getLogger().info("Could not start Quiz. Did you check the player session before?");
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
	
	/**
	 * removes Session from List
	 * @param p
	 * @return
	 */
	public boolean removeSession(Player p) {
		if((this.sessions != null) && (!this.sessions.isEmpty())) {
			if(this.sessions.remove(p.getName()) != null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * adds Session to list without starting (needed ?)
	 * @param session
	 * @return
	 */
	public boolean addSession(Session session) {
		if(this.sessions == null) {
			this.sessions = new HashMap<String, Session>();
		}
		if(!this.sessions.containsKey(session.getPlayer().getName())) {
			this.sessions.put(session.getPlayer().getName(), session);
			return true;
		} else {
			this.plugin.getLogger().info("Could not start Quiz. Did you check the player session before?");
		}
		return false;
	}
}
