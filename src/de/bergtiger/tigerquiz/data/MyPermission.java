package de.bergtiger.tigerquiz.data;

public enum MyPermission {
	ADMIN	("tigerquiz.admin"),
	USER	("tigerquiz.user"),
	
	COMMANDS	("tigerquiz.commands"),
	PLUGININFO	("tigerquiz.info"),
	RELOAD		("tigerquiz.reload"),
	
	QUIZ_CREATE				("tigerquiz.quiz.create"),
	QUIZ_CREATE_QUESTION	("tigerquiz.quiz.create.question"),
	QUIZ_CREATE_ANSWER		("tigerquiz.quiz.create.answer"),
	
	QUIZ_DELETE				("tigerquiz.quiz.delte"),
	QUIZ_DELETE_QUESTION	("tigerquiz.quiz.delete.question"),
	QUIZ_DELETE_ANSWER		("tigerquiz.quiz.delete.andwer"),
	
	QUIZ_LIST				("tigerquiz.quiz.list"),
	QUIZ_LIST_QUESTION		("tigerlist.quiz.list.question"),
	QUIZ_LIST_ANSWER		("tigerlist.quiz.list.answer"),
	
	QUIZ_START ("tigerquiz.quiz.start"),
	QUIZ_START_OTHER ("tigerquiz.quiz.start.other");
	
	private String permission;
	
	MyPermission(String args){
		this.permission = args;
	}
	
	/**
	 * permission as it really is
	 * @return
	 */
	public String get() {
		return this.permission;
	}
	
	/**
	 * permission with .* - all permissions of this (all quiz)
	 * @return String
	 */
	public String all() {
		return this.permission + ".*";
	}
	
	/**
	 * permission with .quiz - only permitted for this quiz
	 * @param quiz
	 * @return String
	 */
	public String quiz(String quiz) {
		return this.permission + "." + quiz;
	}
}
