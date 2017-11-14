package de.bergtiger.tigerquiz.data;

import org.bukkit.ChatColor;

public enum MyString {
	NOPERMISSIONS ("&cYou are not permitted to use this Command."),
	NOPLAYER ("&cYou are not a Player with chest"),
	
	PLUGININFO_INFO_HEAD ("&a----<(&6TigerQuiz&a)>----"),
	PLUGININFO_INFO_VERSION ("&aVersion: &e-version-"),
	
	PLUGININFO_LIST_HEAD("&aQuiz:"),
	PLUGININFO_LIST_BODY("  -color--quiz-"),
	
	COMMANDS_HELP ("&7/plugininfo "),
	COMMANDS_HEAD ("&a----<(&6TigerQuiz Commands&a)>----"),
	COMMANDS_RELOAD ("&b/tigerlist reload - &7reloads config"),
	
	RELOAD ("&aTigerQuiz reloaded"), 
	
	QUIZ_END ("&aQuiz end"),
	QUIZ_CANCEL ("&cQuiz canceled"),
	QUIZ_LOAD_ONETIMEUSE ("&cyou have already done the quiz"),
	QUIZ_LOAD_ERROR ("&cQuiz load error"),
	QUIZ_START_ERROR ("&cQuiz could not start. Player in quiz/no quiz"),
	QUIZ_START_NOPLAYER ("&cNot a Player");
	
	private String args;
	
	MyString(String args) {
		this.args = args;
	}
	
	/**
	 * sets the String for MyStrings - Type
	 * @param args - new String
	 */
	public void setString(String args) {
		if(args.contains("\\n")){
			args = args.replace("\\n", "\n");
		}
		this.args = args;
	}
	
	/**
	 * Get Text
	 * @return String
	 */
	public String get() {
		return this.args;
	}
	
	/**
	 * get Text - transformed with color codes
	 * @return String
	 */
	@SuppressWarnings("static-access")
	public String colored(){
		return this.setColor(this.args);
	}
	
	/**
	 * set Textcolores
	 * @param args - String to be converted
	 * @return String
	 */
	public static String setColor(String args){
		return ChatColor.translateAlternateColorCodes('&', args);
	}
}
