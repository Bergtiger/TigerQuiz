package de.bergtiger.tigerquiz.data;

import org.bukkit.ChatColor;

public enum MyString {
	NOPERMISSIONS ("&cYou are not permitted to use this Command."),
	NOPLAYER ("&cYou are not a Player with chest"),
	
	NOQUIZ		("&cNot a quiz &4(&e-quiz-&4)"),
	NOQUESTION	("&cNot a question &4(&e-question-&4)"),
	NOANSWER	("&cNot a answer &4(&e-answer-&4)"),
	
	PLUGININFO_INFO_HEAD	("&a----<(&6TigerQuiz&a)>----"),
	PLUGININFO_INFO_VERSION	("&aVersion: &e-version-"),
	
	PLUGININFO_LIST_HEAD	("&aQuiz:"),
	PLUGININFO_LIST_BODY	("  -color--quiz-"),
	
	COMMANDS_HELP	("&7/plugininfo "),
	COMMANDS_HEAD	("&a----<(&6TigerQuiz Commands&a)>----"),
	COMMANDS_START	("&e/tigerquiz start &6[&fquiz&6] [&fplayer&6] &8- &7starts a quiz"),
	COMMANDS_LIST	("&e/tigerquiz list &6[&fquiz&6/&fquestion&6/&fanswer&6] &8- &7list all items"),
	COMMANDS_CREATE	("&e/tigerquiz create &6[&fquiz&6/&fquestion&6/&fanswer&6] &8- &7create a new item"),
	COMMANDS_DELETE	("&e/tigerquiz delete &6[&fquiz&6/&fquestion&f/&fanswer&6] &8- &7deletes a item"),
	COMMANDS_RELOAD	("&e/tigerlist reload &8- &7reloads plugin"),
	
	HELP_START	(""),
	HELP_LIST	("&a/tigerquiz list &6[&equiz &6/ &equestion &6/ &eanswer&6] &8[&7quiz&8] [&7question&8]\n  &eThe first parameter describes the type to be listed.\n  &eThe rest specifies the items to be listed."),
	HELP_CREATE	("&a/tigerquiz create &6[&equiz &6/ &equestion &6/ &eanswer&6] &8[&7quiz&8] [&7question&8] [&7answer&8] &6[&eparameter&6]\n  &eThe first parameter describes the type to be created.\n  &eThe rest specifies the items to be created.\n  &efor a list of parameter &6see parameter"),
	HELP_DELETE	("&a/tigerquiz delete &6[&equiz &6/ &equestion &6/ &eanswer&6] &8[&7quiz&8] [&7question&8] [&7answer&8]\n  &eThe first parameter describes the type to be deleted.\n  &eThe rest specifies the item to be deleted."),
	
	RELOAD ("&aTigerQuiz reloaded"), 
	
	QUIZ_LIST_HEAD_QUIZ		("&a----<(&6All Quiz&a)>----"),
	QUIZ_LIST_HEAD_QUESTION	("&a---<(&6-quiz- Questions&a)>---"),
	QUIZ_LIST_HEAD_ANSWER	("&a--<(&6-quiz- -question- Answers&a)>--"),
	QUIZ_LIST_LIST			("  &e-list-"),
	QUIZ_LIST_EMPTY			("  &8Nothing here"),
	
	QUIZ_DELETE_QUIZ		("&aDeletet quiz&6(&e-quiz-)"),
	QUIZ_DELETE_QUESTION	("&aDeletet question&6(&e-question-&6) &afrom quiz&6(&e-quiz-&6)"),
	QUIZ_DELETE_ANSWER		("&aDeletet answer&6(&e-answer-&6) &afromquestion&6(&e-quiz-&6:&e-question-&6)"),
	QUIZ_DELETE_ERROR		("&cCould not save quiz."),
	
	QUIZ_DONE_SELF			("&cYou already did this quiz &6(&e-quiz-&6)"),
	QUIZ_DONE_OTHER			("&e-player- &chas already done this quiz &6(&e-quiz-&6)"),
	QUIZ_INSIDE				("&e-player- &cis inside a Quiz. He cant make both at the same time."),
	
	QUIZ_END				("&aQuiz end"),
	QUIZ_CANCEL				("&cQuiz canceled"),
	QUIZ_LOAD_ONETIMEUSE	("&cyou have already done the quiz"),
	QUIZ_LOAD_ERROR			("&cQuiz load error"),
	QUIZ_START_ERROR		("&cQuiz could not start. Player in quiz/no quiz"),
	QUIZ_START_NOPLAYER		("&cNot a Player"),
	
	QUIZ_RELOAD	("&cYour Quiz was reloaded. Please try it again.");
	
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
	public String colored() {
		return this.setColor(this.args);
	}
	
	/**
	 * set Textcolores
	 * @param args - String to be converted
	 * @return String
	 */
	public static String setColor(String args) {
		return ChatColor.translateAlternateColorCodes('&', args);
	}
}
