package de.bergtiger.tigerquiz;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LoadQuiz {

	private TigerQuiz plugin;
	private HashMap<String, Session> quizzes;
	private HashMap<String, List<Question>> questions;
	
	
	public LoadQuiz(TigerQuiz plugin) {
		this.plugin = plugin;
	}
	
	public Session loadQuiz(String quiz) {
		if(!this.quizzes.containsKey(quiz)) {
			//loadQuiz
			this.loadQuizConfig(quiz);
		}
		return this.newQuizSession(quiz);
	}
	
	public boolean loadQuizQuestions(Session session) {
		if((this.questions != null) && (!this.questions.isEmpty())) {
			if(!this.questions.containsKey(session.getQuizName())) {
				this.loadQuizQuestion(session.getQuizName());
			}
			List<Question> questions = this.newQuizQuestion(session.getQuizName());
			if(questions != null) {
				List<Question> questionhauptfragen = new ArrayList<Question>(); //fragen die standartmäßig gestellt werden
				//fragen die beantwortet werden müssen
				for(int i = 0; i < questions.size(); i++) {
					if(questions.get(i).isObligation()) {
						questionhauptfragen.add(questions.remove(i));
						i--;
					}
				}
				//auffüllen
				while (questionhauptfragen.size() < session.getSize()) {
					if(!questions.isEmpty()) {
						//sorted ?
						if(session.isOrdered()) {
							questionhauptfragen.add(questions.remove(0));
						} else {
							questionhauptfragen.add(questions.remove((int) (Math.random() * questions.size())));
						}
					} else {
						break;
					}
				}
				//in order -> else randomise
				if(!session.isOrdered()) {
					//randomise
					try {
						Collections.shuffle(questionhauptfragen);
					} catch (Exception e) {
						this.plugin.getLogger().info("can't shuffle List of questions - using ordered as default");
					}
				}
				//add questions to session
				session.setQuestions(questionhauptfragen, questions);
				return true;
			}
		}
		return false;
	}
	
	/** 
	 * creates a new List of Questions (real not only pointers)
	 * @param quiz
	 * @return
	 */
	private List<Question> newQuizQuestion(String quiz) {
		List<Question> questions = this.questions.get(quiz);
		if((questions != null) && (questions.isEmpty())) {
			List<Question> args = new ArrayList<Question>();
			for(Question question : questions) {
				args.add(question.copy());
			}
			return args;
		}
		return null;
	}
	
	/**
	 * returns a new Session or null if quiz not exists
	 * @param quiz
	 * @return
	 */
	private Session newQuizSession(String quiz) {
		if(this.quizzes.containsKey(quiz)) {
			return quizzes.get(quiz).copy(); 
		}
		return null;
	}
	
	/**
	 * 
	 * @param quiz
	 */
	private void loadQuizConfig(String quiz) {
		File file = new File("/plugins/" + this.plugin.getName() + "/quiz/" + quiz + "config.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			int size = -1;
			boolean showProgress = true;
			boolean ordered = true;
			boolean oneTimeUse = false;
			List<String> reward = null;
			
			if(cfg.contains("Size")) {
				String args = cfg.getString("Size");
				if(!args.equalsIgnoreCase("autosize")){
					try {
						size = Integer.valueOf(size);
					} catch (Exception e) {
						this.plugin.getLogger().info("wrong argument Size: " + args);
						return;
					}
				}
			}
			if(cfg.contains("showProgress")) {
				String args = cfg.getString("showProgress");
				if(args.equalsIgnoreCase("true") || args.equalsIgnoreCase("false")){
					showProgress = cfg.getBoolean("showProgress");
				} else {
					this.plugin.getLogger().info("wrong argument showProgress: " + cfg.getString("showProgress"));
					return;
				}
			}
			if(cfg.contains("Ordered")) {
				String args = cfg.getString("Ordered");
				if(args.equalsIgnoreCase("true") || args.equalsIgnoreCase("false")){
					showProgress = cfg.getBoolean("Ordered");
				} else {
					this.plugin.getLogger().info("wrong argument Ordered: " + cfg.getString("Ordered"));
					return;
				}
			}
			if(cfg.contains("oneTimeUse")) {
				String args = cfg.getString("oneTimeUse");
				if(args.equalsIgnoreCase("true") || args.equalsIgnoreCase("false")){
					showProgress = cfg.getBoolean("oneTimeUse");
				} else {
					this.plugin.getLogger().info("wrong argument oneTimeUse: " + cfg.getString("oneTimeUse"));
					return;
				}
			}
			if(cfg.contains("Reward")) {
				reward = cfg.getStringList("Reward");
			}
			this.quizzes.put(quiz, new Session(this.plugin, quiz, size, showProgress, ordered, oneTimeUse, reward));
		}
	}
	
	/**
	 * gets the number of errors from the player
	 * @param quiz - name/id of quiz
	 * @param player - should be uuid
	 * @return -1 if nothing there
	 */
	public int getQuizPlayerError(String quiz, Player player) {
		File file = new File("/plugins/" + this.plugin.getName() + "/quiz/" + quiz + "errors.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.contains(player.getUniqueId().toString())){
				return cfg.getInt(player.getUniqueId().toString());
			}
		}
		return -1;
	}
	
	/**
	 * checks if player succesfuully performed the quiz
	 * @param quiz
	 * @param player
	 * @return
	 */
	public boolean checkQuizPlayer(String quiz, Player player) {
		File file = new File("/plugins/" + this.plugin.getName() + "/quiz/" + quiz + "errors.txt");
		if(file.exists()) {
			//uuid:name
		}
		return false;
	}
	
	private void loadQuizQuestion(String quiz) {
		File file = new File("/plugins/" + this.plugin.getName() + "/quiz/" + quiz + "config.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			Set<String> questions = cfg.getConfigurationSection("Question").getKeys(false);
			Iterator<String> i = questions.iterator();
			List<Question> questionsAll = new ArrayList<Question>();
			while(i.hasNext()) {
				String question = i.next(); //id
				//load question
				
				boolean survey = false; //quiz/survey
				boolean obligation = false;
				int size = -1;
				List<Answer> answers = new ArrayList<Answer>();
				
				//load
				if(cfg.contains("Question." + question + ".Obligation")) {
					String args = cfg.getString("Question." + question + ".Obligation");
					if(args.equalsIgnoreCase("true") || args.equalsIgnoreCase("false")){
						obligation = cfg.getBoolean("Question." + question + ".Obligation");
					} else {
						this.plugin.getLogger().info("wrong argument Obligation: " + cfg.getString("Question." + question + ".Obligation") + " using false as default");
					}
				}
				
				if(cfg.contains("Question." + question + ".Size")) {
					String args = cfg.getString("Question." + question + ".Size");
					if(!args.equalsIgnoreCase("autosize")){
						try {
							size = Integer.valueOf(size);
						} catch (Exception e) {
							this.plugin.getLogger().info("wrong argument Size: " + args + " using autosize as default");
						}
					}
				}
				
				if(cfg.contains("Question." + question + ".Survey")) {
					String args = cfg.getString("Question." + question + ".Function");
					if(args.equalsIgnoreCase("true") || args.equalsIgnoreCase("false")){
						survey = cfg.getBoolean("Question." + question + ".Survey");
					} else {
						this.plugin.getLogger().info("wrong argument Survey: " + cfg.getString("Question." + question + ".Survey") + " using false as default");
					}
				}
				
				//load answers
				if(cfg.contains("Question." + question + ".Answer")) {
					Iterator<String> iAnswer = cfg.getConfigurationSection("Question." + question + ".Answer").getKeys(false).iterator();
					while(iAnswer.hasNext()) {
						String answerID = iAnswer.next();
						String name = null;
						boolean function = false;
						boolean correct = false;
						byte data = 0;
						List<String> lore = null;
						String material = null;
						List<String> enchantment = null;
						int posX = 0;
						int posY = 0;
					
						int position = (9 * posY) + posX;
					
						//load answer

						if(cfg.contains("Question." + question + ".Answer." + answerID + "Name")) {
							
						}
						
						if(cfg.contains("Question." + question + ".Answer." + answerID + "Function")) {
							String args = cfg.getString("Question." + question + ".Answer." + answerID + "Function");
							if(args.equalsIgnoreCase("true") || args.equalsIgnoreCase("false")){
								function = cfg.getBoolean("Question." + question + ".Answer." + answerID + "Function");
							} else {
								this.plugin.getLogger().info("wrong argument Function: " + cfg.getString("Question." + question + ".Answer." + answerID + "Function") + " using false as default");
							}
						}
						
						if(cfg.contains("Question." + question + ".Answer." + answerID + "Correct")) {
							String args = cfg.getString("Question." + question + ".Answer." + answerID + "Correct");
							if(args.equalsIgnoreCase("true") || args.equalsIgnoreCase("false")){
								correct = cfg.getBoolean("Question." + question + ".Answer." + answerID + "Correct");
							} else {
								this.plugin.getLogger().info("wrong argument Correct: " + cfg.getString("Question." + question + ".Answer." + answerID + "Correct") + " using false as default");
							}
						}
						
						if(cfg.contains("Question." + question + ".Answer." + answerID + "Data")) {
							try {
								data = Byte.valueOf(cfg.getString("Question." + question + ".Answer." + answerID + "Data"));
							} catch (NumberFormatException e) {
								this.plugin.getLogger().info("wrong argument Data: " + cfg.getString("Question." + question + ".Answer." + answerID + "Data") + " using 0 as default");
							}
						}
						
						if(cfg.contains("Question." + question + ".Answer." + answerID + "Lore")) {
							lore = cfg.getStringList("Question." + question + ".Answer." + answerID + "Lore");
						}
						
						if(cfg.contains("Question." + question + ".Answer." + answerID + "Material")) {
							material = cfg.getString("Question." + question + ".Answer." + answerID + "Material");
						}
						
						if(cfg.contains("Question." + question + ".Answer." + answerID + "Enchantment")) {
							enchantment = cfg.getStringList("Question." + question + ".Answer." + answerID + "Enchantment");
						}
						
						if(cfg.contains("Question." + question + ".Answer." + answerID + "posX")) {
							try {
								posX = Integer.valueOf("Question." + question + ".Answer." + answerID + "posX");
							} catch (Exception e) {
								this.plugin.getLogger().info("wrong argument posX: " + posX + " using 0 as default");
							}
						}
						
						if(cfg.contains("Question." + question + ".Answer." + answerID + "posY")) {
							try {
								posY = Integer.valueOf("Question." + question + ".Answer." + answerID + "posY");
							} catch (Exception e) {
								this.plugin.getLogger().info("wrong argument posY: " + posY + " using 0 as default");
							}
						}
						
						if(cfg.contains("Question." + question + ".Answer." + answerID + "pos")) {
							try {
								position = Integer.valueOf("Question." + question + ".Answer." + answerID + "pos");
							} catch (Exception e) {
								this.plugin.getLogger().info("wrong argument posX: " + position + " using 0 as default");
							}
						}
						
						//get item
					
						ItemStack item = this.getItem(name, lore, this.getMaterial(material), data, this.getEnchantment(enchantment));
					
						if((position > 0) && (item != null)) {
							Answer answer = new Answer(position, item, function, correct);
							if(answer != null) {
								answers.add(answer);
							}
						}
					}
				}
				//resize
				size = this.resize(size, answers);
				
				//create Question
				if(!answers.isEmpty()) {
					if(survey) {
						//survey
						questionsAll.add(new QuestionSurvey(question, obligation, answers, size));
					} else {
						//quiz
						questionsAll.add(new QuestionQuiz(question, obligation, answers, size));
					}
				} else {
					this.plugin.getLogger().info("Answers is Empty - Question (" + question + ") will be ignored");
				}
			}
			if(!questionsAll.isEmpty()) {
				this.questions.put(quiz, questionsAll);
			} else {
				this.plugin.getLogger().info("Questions is Epmty - Quiz (" + quiz + ") has no Questions");
			}
		}
	}
	
	/**
	 * get Item - will be shown in Question Inventory
	 * @param name
	 * @param lore
	 * @param material
	 * @param enchantment
	 * @return
	 */
	private ItemStack getItem(String name, List<String> lore, Material material, byte data, List<String[]> enchantment){
		if(material!= null) {
			ItemStack item = new ItemStack(material, 1, data);
			ItemMeta meta = item.getItemMeta();
			if(name != null) {
				meta.setDisplayName(name);
			}
			if((lore != null) && (!lore.isEmpty())){
				meta.setLore(lore);
			}
			if((enchantment != null) && (!enchantment.isEmpty())) {
				for (int i = 0; i <enchantment.size(); i++) {
					try {
						meta.addEnchant(Enchantment.getByName(enchantment.get(i)[0].toUpperCase()), Integer.valueOf(enchantment.get(i)[1]), true);
					} catch (NumberFormatException e) {
						this.plugin.getLogger().info("getItem: enchantment level error(Ignoring it)");
					}
				}
			}
			item.setItemMeta(meta);
			return item;
		}
		return null;
	}
	
	/**
	 * getMaterial (format id and enum)
	 * @param material
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Material getMaterial(String material) {
		Material m = null;
		try {
			m = Material.getMaterial(Integer.valueOf(material));
		} catch (NumberFormatException e) {
			m = Material.valueOf(material.toUpperCase());
		}
		return m;
	}
	
	/**
	 * changes format and checks if enchantments exists/format correct
	 * @param enchantment
	 * @return
	 */
	private List<String[]> getEnchantment(List<String> enchantment) {
		if((enchantment != null) && (!enchantment.isEmpty())) {
			List<String[]> enchantments = new ArrayList<String[]>();
			for (int i = 0; i < enchantment.size(); i++) {
				String[] args = enchantment.get(i).split(",");
				if(args.length == 2){
					try {
						Enchantment.getByName(args[0].toUpperCase());
						enchantments.add(args);
					} catch (Exception e) {
						this.plugin.getLogger().info("getEnchantment: enchantment not found: " + args[0] + " (Ignoring it)");
					}
				} else {
					this.plugin.getLogger().info("getEnchantment: wrong format (Ignoring it)");
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param size
	 * @param answers
	 * @return
	 */
	private int resize(int size, List<Answer> answers) {
		//size -1 answer neu ordnen - muster ?
		//size >= 0 check if inventory size has enough slots
		int maxSlot = answers.size(); //minimal 1 slot for every answer item
		for (int i = 0; i < answers.size(); i++) {
			if(maxSlot < answers.get(i).getSlot()) maxSlot = answers.get(i).getSlot();
		}
		//TODO
		if(maxSlot > (size * 9)) size = (maxSlot / 9) + 1;
		return size;
	}
}
