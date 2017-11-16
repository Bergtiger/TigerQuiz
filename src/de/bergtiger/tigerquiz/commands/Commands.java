package de.bergtiger.tigerquiz.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.bergtiger.tigerquiz.TigerQuiz;
import de.bergtiger.tigerquiz.data.MyPermission;
import de.bergtiger.tigerquiz.data.MyString;

public class Commands implements CommandExecutor{

	private TigerQuiz plugin;
	
	public Commands(TigerQuiz plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(args.length > 0) {
			switch (args[0].toLowerCase()) {
				case "start" : this.startQuiz(cs, args); break;
				
				case "list" : this.list(cs, args); break;
				
				case "createquiz"		: this.createQuiz(cs, args); break;
				case "createquestion"	: this.createQuestion(cs, args); break;
				case "createanswer"		: this.createAnswer(cs, args); break;
				
				case "deletequiz"		: this.deleteQuiz(cs, args); break;
				case "deletequestion"	: this.deleteQuestion(cs, args); break;
				case "deleteanswer"		: this.deleteAnswer(cs, args); break;
				
				case "reload" : this.reload(cs); break;
				default: this.commands(cs); break;
			}
		} else {
			this.commands(cs);
		}
		return true;
	}

	/**
	 * shows all posible commands
	 * @param cs - CommandSender
	 */
	private void commands(CommandSender cs){
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.USER.get()) || cs.hasPermission(MyPermission.COMMANDS.get())){
			cs.sendMessage(MyString.COMMANDS_HEAD.colored());
			if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.USER.get())) cs.sendMessage(MyString.COMMANDS_HELP.colored());
			if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.RELOAD.get())) cs.sendMessage(MyString.COMMANDS_RELOAD.colored());
		}
	}
	
	/**
	 * Starts Quiz for Player
	 * @param cs
	 * @param args - [quiz] [player]
	 */
	private void startQuiz(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.USER.get()) || cs.hasPermission(MyPermission.QUIZ_START_OTHER.get()) || cs.hasPermission(MyPermission.QUIZ_START.get())) {
			if((args.length == 2) && (cs instanceof Player)) {
				Player p = (Player) cs;
				this.startQuiz(cs, args[1], p);
			} else if ((args.length == 3) && (cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_START_OTHER.get()) || (cs.getName().equals(args[2])))){
				Player p = this.getPlayer(args[2]);
				this.startQuiz(cs, args[1], p);
			} else {
				//TODO -> commandhelp
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * errors and double code -> startQuiz(cs, args)
	 * @param cs
	 * @param quiz
	 * @param p
	 */
	private void startQuiz(CommandSender cs, String quiz, Player p) {
		//TODO
		if(p != null) {
			//getQuiz
			//startQuiz
		} else {
			cs.sendMessage(MyString.QUIZ_START_NOPLAYER.colored());
		}
	}
	
	/**
	 * list quiz / questions / answers
	 * @param cs
	 * @param args
	 */
	private void list(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get())) {
			//tigerquiz list(0) [quiz/question/answer](1) [quiz](2) [question](3)
			if(args.length >= 3) {
				switch(args[1].toLowerCase()) {
				case "quiz"		: this.listQuiz(cs); break;
				case "question"	: if(args.length >= 3) this.listQuestion(cs, args[2]); break;
				case "answer"	: if(args.length >= 4) this.listAnswer(cs, args[2], args[3]); break;
				}
			} else {
				//commandhelp
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * list all quiz
	 * @param cs
	 */
	private void listQuiz(CommandSender cs) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_LIST.get())) {
			List<String> quizes = this.plugin.getQuiz().getQuizes();
			cs.sendMessage(MyString.QUIZ_LIST_HEAD_QUIZ.colored());
			if((quizes != null) && (!quizes.isEmpty())) {
				for(String args : quizes) {
					cs.sendMessage(MyString.QUIZ_LIST_LIST.colored().replace("-list-", args));
				}
			} else {
				cs.sendMessage(MyString.QUIZ_LIST_EMPTY.colored());
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * list all questions from quiz
	 * @param cs
	 * @param quiz - name/id of quiz
	 */
	private void listQuestion(CommandSender cs, String quiz) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get())) {
			if(this.plugin.getQuiz().isQuiz(quiz)) {
				List<String> questions = this.plugin.getQuiz().getQuestions(quiz);
				cs.sendMessage(MyString.QUIZ_LIST_HEAD_QUESTION.colored());
				if((questions != null) && (!questions.isEmpty())) {
					for(String args : questions) {
						cs.sendMessage(MyString.QUIZ_LIST_LIST.colored().replace("-list-", args));
					}
				} else {
					cs.sendMessage(MyString.QUIZ_LIST_EMPTY.colored());
				}
			} else {
				cs.sendMessage(MyString.NOQUIZ.colored());
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * lists all answers from question from quiz
	 * @param cs
	 * @param quiz - name/id of quiz
	 * @param question - question title/id
	 */
	private void listAnswer(CommandSender cs, String quiz, String question) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get())) {
			if(this.plugin.getQuiz().isQuiz(quiz)) {
				if(this.plugin.getQuiz().isQuestion(quiz, question)) {
					List<String> answers = this.plugin.getQuiz().getAnswers(quiz, question);
					cs.sendMessage(MyString.QUIZ_LIST_HEAD_ANSWER.colored());
					if((answers != null) && (!answers.isEmpty())) {
						for(String args : answers) {
							cs.sendMessage(MyString.QUIZ_LIST_LIST.colored().replace("-list-", args));
						}
					} else {
						cs.sendMessage(MyString.QUIZ_LIST_EMPTY.colored());
					}
				} else {
					cs.sendMessage(MyString.NOQUESTION.colored());
				}
			} else {
				cs.sendMessage(MyString.NOQUIZ.colored());
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	private void createQuiz(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get())) {
			//create(0) [name](1) [size](2) [error](3) [showProgress](4) [ordered](5) [oneTimeUse](6)
			if(args.length >= 2) {
				String name;
				//checkk if quiz with this name exists
				boolean error = true;
				boolean showProgress = true;
				boolean ordered = true;
				boolean oneTimeUse = false;
				if(args.length >= 3) {
					error = Boolean.valueOf(args[3]);
				}
				if(args.length >= 4) {
					showProgress = Boolean.valueOf(args[4]);
				}
				if(args.length >= 5) {
					ordered = Boolean.valueOf(args[5]);
				}
				if(args.length >= 6) {
					ordered = Boolean.valueOf(args[6]);
				}
					
			} else {
				//min size 2
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	private void createQuestion(CommandSender cs, String[] args) {
		
	}
	
	private void createAnswer(CommandSender cs, String[] args) {
		
	}
	
	/**
	 * löscht ein quiz
	 * @param cs
	 * @param args
	 */
	private void deleteQuiz(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get())) {
			//delete [quiz]
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * löscht eine frage von einem quiz
	 * @param cs
	 * @param args
	 */
	private void deleteQuestion(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get())) {
			//delete [quiz] [question]
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * löscht eine antwort von einer frage von einem quiz
	 * @param cs
	 * @param args
	 */
	private void deleteAnswer(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_ANSWER.get())) {
			//delete [quiz] [question] [answer]
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * gets online Player by Name/Uuid
	 * @param args - Playername
	 * @return Player/null
	 */
	private Player getPlayer(String args){
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getName().equals(args) || p.getUniqueId().toString().equals(args)){
				return p;
			}
		}
		return null;
	}
	
	/**
	 * reloads plugin
	 * @param cs - CommandSender
	 */
	private void reload(CommandSender cs) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.RELOAD.get())) {
			this.plugin.reload();
			cs.sendMessage(MyString.RELOAD.colored());
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
}
