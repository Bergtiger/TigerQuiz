package de.bergtiger.tigerquiz.data;

public enum MyString {
	NOPERMISSIONS ("&cYou are not permitted to use this Command."),
	NOPLAYER ("&cYou are not a Player with chest"),
	
	PLUGININFO_INFO_HEAD ("&a----<(&6TigerQuiz&a)>----"),
	PLUGININFO_INFO_VERSION ("&aVersion: &e-version-"),
	
	PLUGININFO_LIST_HEAD("&aQuiz:"),
	PLUGININFO_LIST_BODY("  -color--quiz-"),
	
	PLUGININFO_COMMANDS_HELP ("&7/plugininfo "),
	PLUGININFO_COMMANDS_HEAD ("&a----<(&6TigerQuiz Commands&a)>----"),
	PLUGININFO_COMMANDS_RELOAD ("&b/tigerlist reload - &7reloads config"),
	
	PLUGININFO_RELOAD_SUCESS ("&aTigerQuiz reloaded"),
	PLUGININFO_RELOAD_ERROR ("&cTigerQuiz reload error"), 
	
	QUIZ_END ("&aQuiz end"),
	QUIZ_CANCEL ("&cQuiz canceled"),
	QUIZ_LOAD_ONETIMEUSE ("&cyou have already done the quiz"),
	QUIZ_LOAD_ERROR ("&cQuiz load error");
	
	private String args;
	
	MyString(String args){
		this.args = args;
	}
	
	/**
	 * sets the String for MyStrings - Type
	 * @param args - new String
	 */
	public void setString(String args){
		if(args.contains("\\n")){
			args = args.replace("\\n", "\n");
		}
		this.args = args;
	}
	
	/**
	 * Gets String
	 * @return
	 */
	public String getString(){
		return this.args;
	}
	
	/**
	 * gets String - transformed with color codes
	 * @return
	 */
	@SuppressWarnings("static-access")
	public String getStringColored(){
		return this.setColor(this.args);
	}
	
	/**
	 * 
	 * @param args - String to be converted
	 * @return
	 */
	public static String setColor(String args){
		return ChatColor.translateAlternateColorCodes('&', args);
	}
}
