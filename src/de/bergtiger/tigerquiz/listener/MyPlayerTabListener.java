package de.bergtiger.tigerquiz.listener;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.TabCompleteEvent;

import de.bergtiger.tigerquiz.TigerQuiz;
import de.bergtiger.tigerquiz.data.MyPermission;

public class MyPlayerTabListener extends MyListener {

	public MyPlayerTabListener(TigerQuiz plugin) {
		super(plugin);
	}

	@EventHandler
	public void onTab(TabCompleteEvent e) {
		if(e.getBuffer().toLowerCase().startsWith("/tigerquiz ")) {
			//tigerquiz [help/reload/start/create/delete/list](0) [quiz/question/answer](1) [title/id](2)
			String[] message = e.getBuffer().split(" ");
			CommandSender cs = e.getSender();
			List<String> completions = e.getCompletions();
			if(message.length == 1) {//tigerquiz ...
				completions = this.getCommands(cs, completions);
			} else if((message.length == 2) && (!e.getBuffer().endsWith(" "))) {//tigerquiz []...
				completions = this.getCommands(cs, completions, message[1].toLowerCase());
			} else if(message.length == 2) {//tigerquiz [xyz] ...
				switch(message[1].toLowerCase()) {
					case "list" : case "create" : case "delete" : completions = this.getSecond(cs, completions, message[1]); break;
//permissions ?		case "start" : completions = this.getQuiz(cs, completions, "start", null); break; //get quizzes
				}
			} else if((message.length == 3) && (!e.getBuffer().endsWith(" "))) {//tigerquiz [list/create/delete] []...
				completions = this.getSecond(cs, completions, message[1], message[2]);
			} else if(message.length == 3) {//tigerquiz [list/create/delete] [quiz/question/answer] ...
				
			} else if((message.length == 4) && (!e.getBuffer().endsWith(" "))) {//tigerquiz [list/create/delete] [quiz/question/answer] []...
				
			}
			e.setCompletions(completions);
		}
	}
	
	/**
	 * commands /tigerquiz ...
	 * @param cs
	 * @param completions
	 * @return
	 */
	private List<String> getCommands(CommandSender cs, List<String> completions) {
		completions.clear();
		//help
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.USER.get())) completions.add("help");
		//reload
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.RELOAD.get())) completions.add("reload");
		//start
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.USER.get()) || cs.hasPermission(MyPermission.QUIZ_START_OTHER.get()) || cs.hasPermission(MyPermission.QUIZ_START.get())) completions.add("start");
		//list
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_LIST.get())) completions.add("list");
		//create
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_ANSWER.get())) completions.add("create");
		//delete
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_ANSWER.get())) completions.add("delete");
		return completions;
	}
	
	/**
	 * commands /tigerquiz [c.../d.../h.../l.../r.../s...]
	 * @param cs
	 * @param completions
	 * @param message beginn string of command
	 * @return
	 */
	private List<String> getCommands(CommandSender cs, List<String> completions, String message) {
		completions.clear();
		switch(message.charAt(0)) {
		case 'h' : if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.USER.get())) completions.add("help"); break;
		case 'r' : if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.RELOAD.get())) completions.add("reload"); break;
		case 's' : if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.USER.get()) || cs.hasPermission(MyPermission.QUIZ_START_OTHER.get()) || cs.hasPermission(MyPermission.QUIZ_START.get())) completions.add("start"); break;
		case 'l' : if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_LIST.get())) completions.add("list"); break;
		case 'c' : if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_ANSWER.get())) completions.add("create"); break;
		case 'd' : if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_ANSWER.get())) completions.add("delete"); break;
		}
		return completions;
	}
	
	/**
	 * subcommands /tigerquiz list/create/delete ...
	 * @param cs
	 * @param completions
	 * @param command command [becuase of permissions]
	 * @return
	 */
	private List<String> getSecond(CommandSender cs, List<String> completions, String command) {
		completions.clear();
		switch (command.toLowerCase()) {
			case "list" : {
					if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_LIST.get())) completions.add("quiz");
					if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get())) completions.add("question");
					if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get())) completions.add("answer");
				} break;
			case "create" : {
					if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get())) completions.add("quiz");
					if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get())) completions.add("question");
					if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_ANSWER.get())) completions.add("answer");
				} break;
			case "delete" : {
					if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get())) completions.add("quiz");
					if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get())) completions.add("question");
					if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_ANSWER.get())) completions.add("answer");
				} break;
		}
		return completions;
	}
	
	/**
	 * subcommands /tigerquiz list/create/delete [a.../q.../qu...]
	 * @param cs
	 * @param completions
	 * @param command command [becuase of permissions]
	 * @param message beginn string of subcommand
	 * @return
	 */
	private List<String> getSecond(CommandSender cs, List<String> completions, String command, String message) {
		completions.clear();
		switch (command.toLowerCase()) {
		case "list" : {
				switch (message.toLowerCase().charAt(0)) {
					case 'a' : if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get())) completions.add("answer"); break;
					case 'q' : {
						if(message.length() > 2) {
							//que - qui
							if(message.startsWith("que")) {
								if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get())) completions.add("question");
							} else {
								if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_LIST.get())) completions.add("quiz");
							}
						} else {
							//both
							if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_LIST.get())) completions.add("quiz");
							if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get())) completions.add("question");
						}
					}
				}
			} break;
		case "create" : {
				switch (message.toLowerCase().charAt(0)) {
					case 'a' : if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_ANSWER.get())) completions.add("answer");
					case 'q' : {
						if(message.length() > 2) {
							if(message.startsWith("que")) {
								if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get())) completions.add("question");
							} else {
								if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get())) completions.add("quiz");
							}
						} else {
							//both
							if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get())) completions.add("quiz");
							if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get())) completions.add("question");
						}
					}
				}
			} break;
		case "delete" : {
				switch (message.toLowerCase().charAt(0)) {
					case 'a' : if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_ANSWER.get())) completions.add("answer");
					case 'q' : {
						if(message.length() > 2) {
							if(message.startsWith("que")) {
								if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get())) completions.add("question");
							} else {
								if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get())) completions.add("quiz");
							}
						} else {
							//both
							if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get())) completions.add("quiz");
							if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get())) completions.add("question");
						}
					}
				}
			} break;
	}
		return completions;
	}
	
	
	private List<String> getThird(CommandSender cs, List<String> completions) {
		//tigerlist list [quiz/question/answer] || tigerlist create [question/answer] || tigerlist delete [quiz/question/answer]
		return completions;
	}
	
	private List<String> getQuiz(CommandSender cs, List<String> completions, String command, String subCommand) {
		completions.clear();
		completions.addAll(this.plugin.getQuiz().getQuizes());
		return completions;
	}
	
	private List<String> getQuestion(CommandSender cs, List<String> completions, String command, String subCommand, String quiz) {
		completions.clear();
		completions.addAll(this.plugin.getQuiz().getQuestions(quiz));
		return completions;
	}
	
	private List<String> getAnswer(CommandSender cs, List<String> completions, String command, String subCommand, String quiz, String question) {
		completions.clear();
		completions.addAll(this.plugin.getQuiz().getAnswers(quiz, question));
		return completions;
	}
}
