package de.bergtiger.tigerquiz.data;

public enum MyPermission {
	ADMIN ("tigerquiz.admin"),
	USER ("tigerquiz.user"),
	
	COMMANDS ("tigerquiz.commands"),
	PLUGININFO ("tigerquiz.info"),
	RELOAD ("tigerquiz.reload"),
	QUIZ_CREATE ("tigerquiz.quiz.create"),
	QUIZ_START ("tigerquiz.quiz.start"),
	QUIZ_START_OTHER ("tigerquiz.quiz.start.other");
	
	private String permission;
	
	MyPermission(String args){
		this.permission = args;
	}
	
	public String get(){
		return this.permission;
	}
}
