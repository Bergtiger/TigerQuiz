package de.bergtiger.tigerquiz.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.bergtiger.tigerquiz.LoadQuiz;
import de.bergtiger.tigerquiz.Session;
import de.bergtiger.tigerquiz.TigerQuiz;
import de.bergtiger.tigerquiz.data.MyPermission;
import de.bergtiger.tigerquiz.data.MyString;
import de.bergtiger.tigerquiz.data.MyText;

public class Commands implements CommandExecutor{

	private TigerQuiz plugin;
	
	public Commands(TigerQuiz plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(args.length > 0) {
			switch (args[0].toLowerCase()) {
				case "start"	: this.startQuiz(cs, args); break;
				case "list"		: this.list(cs, args); break;
				case "create"	: this.create(cs, args); break;
				case "delete"	: this.delete(cs, args); break;
				case "reload"	: this.reload(cs); break;
//				case "opendialog":this.openDialog(cs); break;
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
			if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.USER.get()) || cs.hasPermission(MyPermission.QUIZ_START_OTHER.get()) || cs.hasPermission(MyPermission.QUIZ_START.get())) cs.sendMessage(MyString.COMMANDS_START.colored());
			if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_LIST.get())) {
				//spezial Text - CommandOnClick
				//cs.sendMessage(MyString.COMMANDS_LIST.colored());
				MyText.sendCommand(cs, MyString.COMMANDS_LIST.colored(), "/tigerquiz list");
			}
			if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_ANSWER.get())) {
				//spezial Text - CommandOnClick
				//cs.sendMessage(MyString.COMMANDS_CREATE.colored());
				MyText.sendCommand(cs, MyString.COMMANDS_CREATE.colored(), "/tigerquiz create");
			}
			if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_ANSWER.get())) {
				//spezial Text - CommandOnClick
				//cs.sendMessage(MyString.COMMANDS_DELETE.colored());
				MyText.sendCommand(cs, MyString.COMMANDS_DELETE.colored(), "/tigerquiz delete");
			}
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
		if(p != null) {
			//getQuiz
			if(!this.plugin.getSessions().hasSession(p)) {
				Session session = this.plugin.getQuiz().load().loadQuiz(quiz);
				if(session != null) {
					if(session.getOneTimeUse() && this.plugin.getQuiz().load().checkQuizPlayer(quiz, p)) {
						//has done
						if(cs.getName().equalsIgnoreCase(p.getName())) {
							cs.sendMessage(MyString.QUIZ_DONE_SELF.colored().replace("-quiz-", quiz));
						} else {
							cs.sendMessage(MyString.QUIZ_DONE_OTHER.colored().replace("-quiz-", quiz).replace("-player-", p.getName()));
						}
					} else {
						//startQuiz
						int error = this.plugin.getQuiz().load().getQuizPlayerError(quiz, p);
						session.setPlayer(p, error);
						this.plugin.getQuiz().load().loadQuizQuestions(session); //multiply returns
						this.plugin.getSessions().startSession(session);
					}
				} else {
					//noQuiz
					cs.sendMessage(MyString.NOQUIZ.colored().replace("-quiz-", quiz).replace("-quiz-", quiz));
				}
			} else {
				//has session
				cs.sendMessage(MyString.QUIZ_INSIDE.colored().replace("-player-", p.getName()));
			}
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
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_ANSWER.get()) || cs.hasPermission(MyPermission.QUIZ_LIST_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_LIST.get())) {
			//tigerquiz list(0) [quiz/question/answer](1) [quiz](2) [question](3)
			if(args.length >= 2) {
				switch(args[1].toLowerCase()) {
					case "quiz"		: this.listQuiz(cs); break;
					case "question"	:
						if(args.length >= 3) {
							this.listQuestion(cs, args[2]);
							break;
						}
					case "answer"	:
						if(args.length >= 4) {
							this.listAnswer(cs, args[2], args[3]);
							break;
						}
					default: cs.sendMessage(MyString.HELP_LIST.colored());
				}
			} else {
				//commandhelp
				cs.sendMessage(MyString.HELP_LIST.colored());
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
				cs.sendMessage(MyString.QUIZ_LIST_HEAD_QUESTION.colored().replace("-quiz-", quiz));
				if((questions != null) && (!questions.isEmpty())) {
					for(String args : questions) {
						cs.sendMessage(MyString.QUIZ_LIST_LIST.colored().replace("-list-", args));
					}
				} else {
					cs.sendMessage(MyString.QUIZ_LIST_EMPTY.colored());
				}
			} else {
				cs.sendMessage(MyString.NOQUIZ.colored().replace("-quiz-", quiz));
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
					cs.sendMessage(MyString.QUIZ_LIST_HEAD_ANSWER.colored().replace("-quiz-", quiz).replace("-question-", question));
					if((answers != null) && (!answers.isEmpty())) {
						for(String args : answers) {
							cs.sendMessage(MyString.QUIZ_LIST_LIST.colored().replace("-list-", args));
						}
					} else {
						cs.sendMessage(MyString.QUIZ_LIST_EMPTY.colored());
					}
				} else {
					cs.sendMessage(MyString.NOQUESTION.colored().replace("-quiz-", quiz).replace("-question-", question));
				}
			} else {
				cs.sendMessage(MyString.NOQUIZ.colored().replace("-quiz-", quiz));
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	private void create(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_ANSWER.get())) {
			//tigerquiz create(0) [quiz/question/answer](1)
			if(args.length >= 2) {
				switch(args[1].toLowerCase()) {
					case "quiz"		: this.createQuiz(cs, args); break;
					case "question"	: this.createQuestion(cs, args); break;
					case "answer"	: this.createAnswer(cs, args); break;
					default: cs.sendMessage(MyString.HELP_CREATE.colored());
				}
			} else {
				//commandhelp
				cs.sendMessage(MyString.HELP_CREATE.colored());
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	private void createQuiz(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get())) {
			//create(0) [quiz](1) [name](2) [size](3) [error](4) [showProgress](5) [ordered](6) [oneTimeUse](7)
			//create(0) [quiz](1) [name:value] [size:value] [error:value] [showProgress:value] [ordered:value] [oneTimeUse:value]
			if(args.length >= 3) {
				String name = null;
				//checkk if quiz with this name exists
				int size = 1;
//				boolean error = true;
				boolean showProgress = true;
				boolean ordered = true;
				boolean oneTimeUse = false;

				for(int i = 2; i < args.length; i++) {
					String[] parameter = args[i].split(":");
					if(parameter.length == 2) {
						switch(parameter[0].toLowerCase()) {
							case "name"			: name = parameter[1]; break;
							case "size"			:
								try {
									size = Integer.valueOf(parameter[1]);
									break;
								} catch (NumberFormatException e) {
									//no number
								}
//							case "error"		:
//								try {
//									error = Boolean.valueOf(parameter[1]);
//									break;
//								} catch (Exception e) {
//									//no boolean
//								}
							case "showprogress"	:
								try {
									showProgress = Boolean.valueOf(parameter[1]);
									break;
								} catch (Exception e) {
									//no boolean
								}
							case "ordered"		:
								try {
									ordered = Boolean.valueOf(parameter[1]);
									break;
								} catch (Exception e) {
									//no boolean
								}
							case "onetimeuse"	:
								try {
									oneTimeUse = Boolean.valueOf(parameter[1]);
									break;
								} catch (Exception e) {
									//no boolean
								}
							default: //not a value
						}
					} else {
						//not a value check parameter (size != 2)
					}
				}
				//werte geladen
				
				if(this.plugin.getQuiz().addQuiz(name, size, showProgress, ordered, oneTimeUse)) {
					//funktioniert
					System.out.println("Created");
				} else {
					//ging nicht
					System.out.println("created error");
				}
			} else {
				//min size 2
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	private void createQuestion(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get())) {
			//create(0) [question](1) [quizname](2) [question/id](3) [size](4) [error](5) [showProgress](6) [ordered](7) [oneTimeUse](8)
			//create(0) [question](1) [quizname](2) [question/id:value](3) [size:value] [error:value] [showProgress:value] [ordered:value] [oneTimeUse:value]
			String quiz = args[2];
			if(args.length >= 4) {
				if(this.plugin.getQuiz().isQuiz(quiz)) {
					String question = null; //id
					boolean survey = false; //quiz/survey
					boolean obligation = false;
					int size = -1;
			
					for(int i = 2; i < args.length; i++) {
						String[] parameter = args[i].split(":");
						if(parameter.length == 2) {
							switch(parameter[0].toLowerCase()) {
								case "name"			: question = parameter[1]; break;
									
								case "survey"		:
									try {
										survey = Boolean.valueOf(parameter[1]);
										break;
									} catch (Exception e) {
										// no boolean
									}
								case "obligation"	:
									try {
										obligation = Boolean.valueOf(parameter[1]);
										break;
									} catch (Exception e) {
										// no boolean
									}
								case "size"		:
									try {
										size = Integer.valueOf(parameter[1]);
										break;
									} catch (Exception e) {
										// no boolean
									}
								default: //not a value
							}
						} else {
							//not a value check parameter (size != 2)
						}
					}
					
					//add
					if(this.plugin.getQuiz().addQuestion(quiz, question, size, survey, obligation)) {
						//worked
					} else {
						//not worked
					}
				} else {
					//not a Quiz
					cs.sendMessage(MyString.NOQUIZ.colored().replace("-quiz-", quiz));
				}
			} else {
				
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	private void createAnswer(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_CREATE_ANSWER.get())) {
			//create(0) [question](1) [quizname](2) [question/id](3) [answer/id](4) [text:value] [function:value] [correct:value]
			if(args.length >= 5) {
				String quiz = args[2];
				String question = args[3]; //id
				if(this.plugin.getQuiz().isQuiz(quiz)) {
					if(this.plugin.getQuiz().isQuestion(quiz, question)) {
						String answer = args[4];
						String name = null;
						boolean function = false;
						boolean correct = false;
						byte data = 0;
						List<String> lore = null;
						String material = null;
						List<String> enchantment = null;
						int posX = 0;
						int posY = 0;
			
						for(int i = 2; i < args.length; i++) {
							String[] parameter = args[i].split(":");
							if(parameter.length == 2) {
								switch(parameter[0].toLowerCase()) {
									case "text"		: name = parameter[1]; break;
									
									case "material"	: material = parameter[1]; break;
									
									case "function"	:
										try {
											function = Boolean.valueOf(parameter[1]);
											break;
										} catch (Exception e) {
											// no boolean
										}
									
									case "correct"	:
										try {
											correct = Boolean.valueOf(parameter[1]);
											if(!function) function = true;
											break;
										} catch (Exception e) {
											// no boolean
										}
										
									case "data" :
										try {
											data = Byte.valueOf(parameter[1]);
										} catch (NumberFormatException e) {
											//no int
										}
										
									case "posx" : case "x" :
										try {
											posX = Integer.valueOf(parameter[1]);
										} catch (NumberFormatException e) {
											//no int
										}
									case "posy" : case "y" :
										try {
											posX = Integer.valueOf(parameter[1]);
										} catch (NumberFormatException e) {
											//no int
										}
									case "pos" :
										try {
											int pos = Integer.valueOf(parameter[1]);
											posX = pos % 9; //zeile
											posY = pos / 9; //spalte
										} catch (NumberFormatException e) {
											//no int
										}
										
									case "lore" :
										String[] blubb = parameter[1].split(";");
										lore = new ArrayList<String>();
										for(int j = 0; j < blubb.length; j++) {
											lore.add(blubb[j]);
										}
									case "enchantment" :
										String[] blub = parameter[1].split(";");
										enchantment = new ArrayList<String>();
										for(int j = 0; j < blub.length; j++) {
											enchantment.add(blub[j]);
										}
									default:
								}
							}
						}
						
						//add
						if(this.plugin.getQuiz().addAnswer(quiz, question, answer, function, correct, name, lore, material, data, enchantment, posX, posY)) {
							//worked
						} else {
							//not worked
						}
						
					} else {
						cs.sendMessage(MyString.NOQUESTION.colored().replace("-question-", question));
					}
				} else {
					cs.sendMessage(MyString.NOQUIZ.colored().replace("-quiz-", quiz));
				}
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * sortiert an welche untercommand weitergeleitet werden soll
	 * @param cs - commandsender
	 * @param args - list of parameter 
	 */
	private void delete(CommandSender cs, String[] args) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_ANSWER.get())) {
			//tigerquiz delete(0) [quiz/question/answer](1) [quiz](2) [question](3) [answer](4)
			if(args.length >= 2) {
				switch(args[1].toLowerCase()) {
					case "quiz"	:
							if(args.length >= 3) {
								this.deleteQuiz(cs, args[2]);
								break;
							}
					case "question"	:
							if(args.length >= 4) {
								this.deleteQuestion(cs, args[2], args[3]);
								break;
							}
					case "answer"	:
							if(args.length >= 5) {
								this.deleteAnswer(cs, args[2], args[3], args[4]);
								break;
							}
					default: cs.sendMessage(MyString.HELP_DELETE.colored());
				}
			} else {
				//commandhelp
				cs.sendMessage(MyString.HELP_DELETE.colored());
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * löscht ein quiz
	 * @param cs
	 * @param args
	 */
	private void deleteQuiz(CommandSender cs, String quiz) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get())) {
			//delete [quiz]
			//yes question warning ?
			if(this.plugin.getQuiz().isQuiz(quiz)) {
				if(this.plugin.getQuiz().deleteQuiz(quiz)) {
					cs.sendMessage(MyString.QUIZ_DELETE_QUIZ.colored().replace("-quiz-", quiz));
				} 
				if(this.plugin.getQuiz().load().deleteQuestions(quiz)) {
					//deleted saved questions
				}
				if(this.plugin.getSessions().removeSessions(quiz)) {
					//removed aktiv players
				}
				else {
					cs.sendMessage(MyString.QUIZ_DELETE_ERROR.colored());
				}
			} else {
				cs.sendMessage(MyString.NOQUIZ.colored().replace("-quiz-", quiz));
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * löscht eine frage von einem quiz
	 * @param cs
	 * @param args
	 */
	private void deleteQuestion(CommandSender cs, String quiz, String question) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get())) {
			//tigerquiz deleteQuestion(0) [quiz](1) [question](2)
			if(this.plugin.getQuiz().isQuiz(quiz)) {
				if(this.plugin.getQuiz().isQuestion(quiz, question)) {
					if(this.plugin.getQuiz().deleteQuestion(quiz, question)) {
						cs.sendMessage(MyString.QUIZ_DELETE_QUESTION.colored().replace("-quiz-", quiz).replace("-question-", question));
					} else {
						cs.sendMessage(MyString.QUIZ_DELETE_ERROR.colored());
					}
				} else {
					cs.sendMessage(MyString.NOQUESTION.colored().replace("-question-", question));
				}
			} else {
				cs.sendMessage(MyString.NOQUIZ.colored().replace("-quiz-", quiz));
			}
		} else {
			cs.sendMessage(MyString.NOPERMISSIONS.colored());
		}
	}
	
	/**
	 * löscht eine antwort von einer frage von einem quiz
	 * @param cs
	 * @param args
	 */
	private void deleteAnswer(CommandSender cs, String quiz, String question, String answer) {
		if(cs.hasPermission(MyPermission.ADMIN.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_QUESTION.get()) || cs.hasPermission(MyPermission.QUIZ_DELETE_ANSWER.get())) {
			//tigerquiz deleteanswer(0) [quiz](1) [question](2) [answer](3)
			if(this.plugin.getQuiz().isQuiz(quiz)) {
				if(this.plugin.getQuiz().isQuestion(quiz, question)) {
					if(this.plugin.getQuiz().isAnswer(quiz, question, answer)) {
						cs.sendMessage(MyString.QUIZ_DELETE_ANSWER.colored().replace("-quiz-", quiz).replace("-question-", question).replace("-answer-", answer));
					} else {
						cs.sendMessage(MyString.NOANSWER.colored().replace("-answer-", answer));
					}
				} else {
					cs.sendMessage(MyString.NOQUESTION.colored().replace("-question-", question));
				}
			} else {
				cs.sendMessage(MyString.NOQUIZ.colored().replace("-quiz-", quiz));
			}
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
	
//	private void openDialog(CommandSender cs) {
//		if(cs.hasPermission(MyPermission.ADMIN.get())) {
//			if(cs instanceof Player) {
//				Player  p = (Player)cs;
//				System.out.println("test");
//				Inventory inventory = Bukkit.createInventory(null, InventoryType.CREATIVE, "TigerQuiz Itemauswahl");
//				p.openInventory(inventory);
//			} else {
//				cs.sendMessage(MyString.NOPLAYER.colored());
//			}
//		} else {
//			cs.sendMessage(MyString.NOPERMISSIONS.colored());
//		}
//	}
}
