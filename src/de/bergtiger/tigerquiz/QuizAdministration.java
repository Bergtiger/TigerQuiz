package de.bergtiger.tigerquiz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Warning;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class QuizAdministration {

	private TigerQuiz plugin;
	private LoadQuiz loadQuiz;
	
	public QuizAdministration(TigerQuiz plugin) {
		this.plugin = plugin;
		this.loadQuiz = new LoadQuiz(this.plugin);
	}
	
	public LoadQuiz load() {
		return this.loadQuiz;
	}
	
	/**
	 * checks if quiz with this name exists
	 * @param quiz
	 * @return
	 */
	public boolean isQuiz(String quiz) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) return true;
		return false;
	}
	
	/**
	 * checks if quiz with this name exists and if it has a question
	 * @param quiz
	 * @param question
	 * @return
	 */
	public boolean isQuestion(String quiz, String question) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			//quiz musst be existend
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.getConfigurationSection("Question").contains(question)) return true;
		}
		return false;
	}
	
	/**
	 * checks if quiz with question and answer exists
	 * @param quiz
	 * @param question
	 * @param answer
	 * @return
	 */
	public boolean isAnswer(String quiz, String question, String answer) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			//quiz musst be existend
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.getConfigurationSection("Question").contains(question)) {
				if(cfg.getConfigurationSection("Question." + question + ".Answer").contains(answer)) return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param quiz - name/id of quiz
	 * @param size - max questions (number of different questions musst exists)
	 * @param showProgress - if Inventar get prefix: (x of n) questions
	 * @param ordered - if question will be ask in order or randomisized
	 * @param oneTimeUse - if a player can only once do the quiz
	 * @return
	 */
	public boolean addQuiz(String quiz, int size, boolean showProgress, boolean ordered, boolean oneTimeUse) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(!file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			cfg.addDefault("Size", size);
			cfg.addDefault("showProgress", showProgress);
			cfg.addDefault("Ordered", ordered);
			cfg.addDefault("oneTimeUse", oneTimeUse);
			
			cfg.options().header(quiz + " config");
			cfg.options().copyHeader(true);
			cfg.options().copyDefaults(true);
			
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				this.plugin.getLogger().info("addQuiz: Could not save quiz (" + quiz + ")");
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param quiz - quiz title/id
	 * @param question - question title/id
	 * @param size - how big the shown chest is x * 9
	 * @param survey - quiz or survey
	 * @param obligation - if question musst be answered
	 * @return
	 */
	public boolean addQuestion(String quiz, String question, int size, boolean survey, boolean obligation) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			String prefix = "Question." + question + ".";
			cfg.addDefault(prefix + "Size", size);
			cfg.addDefault(prefix + "Survey", survey);
			cfg.addDefault(prefix + "Obligation", obligation);
			
			cfg.options().copyHeader(true);
			cfg.options().copyDefaults(true);
			
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				this.plugin.getLogger().info("addQuestion: Could not save quiz (" + quiz + ")");
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param quiz - quiz title/id
	 * @param question - question title/id
	 * @param answer - answer title/id
	 * @param function - has a function ? or placeholder -> question
	 * @param correct - if has function if correct
	 * @param name - String - title/answer/question
	 * @param lore - List of String
	 * @param material - material(id)
	 * @param data - material(:0-15)
	 * @param enchantment - List of enchantments (enchantment:value)
	 * @param posX - X coordinate
	 * @param posY - Y coordinate
	 * @return true if it could save
	 */
	public boolean addAnswer(String quiz, String question, String answer, boolean function, boolean correct, String name, List<String> lore, String material, byte data, List<String> enchantment, int posX, int posY) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			//quiz musst be existend
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.getConfigurationSection("Question").contains(question)) {
				String prefix = "Question." + question + ".Answer." + answer + ".";
				
				cfg.addDefault(prefix + "Function", function);
				if(function) {
					cfg.addDefault(prefix + "Correct", correct);
				}
				cfg.addDefault(prefix + "Name", name);
				cfg.addDefault(prefix + "Lore", lore);
				cfg.addDefault(prefix + "Material", material);
				if(data > 0) {
					cfg.addDefault(prefix + "Data", data);
				}
				if((enchantment != null) && (!enchantment.isEmpty())) {
					cfg.addDefault(prefix + "Enchantment", enchantment);
				}
				cfg.addDefault(prefix + "posX", posX);
				cfg.addDefault(prefix + "posY", posY);
				cfg.options().copyHeader(true);
				cfg.options().copyDefaults(true);
				
				try {
					cfg.save(file);
					return true;
				} catch (IOException e) {
					this.plugin.getLogger().info("addAnswer: Could not save quiz (" + quiz + ")");
				}
			}
		}
		return false;
	}
	
	/**
	 * be sure you want delete all of the quiz (Deletes directory of the quiz)
	 * @param quiz - quiz title/id
	 * @return true if folder deleted / false
	 */
	@Warning
	public boolean deleteQuiz(String quiz) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz);
		if(file.exists() && file.isDirectory()) {
			//first remove the player on Session, then delete Quiz
			return file.delete();
		}
		return false;
	}
	
	/**
	 * 
	 * @param quiz - quiz title/id
	 * @param question - question title/id
	 * @return
	 */
	public boolean deleteQuestion(String quiz, String question) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			if(cfg.getConfigurationSection("Question").contains(question)) {
			
				cfg.set("Question." + question, null);
				
				cfg.options().copyHeader(true);
				cfg.options().copyDefaults(true);
			
				try {
					cfg.save(file);
					return true;
				} catch (IOException e) {
					this.plugin.getLogger().info("deleteQuestion: Could not save quiz (" + quiz + ")");
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param quiz - quiz title/id
	 * @param question - question title/id
	 * @param answer - answer title/id
	 * @return
	 */
	public boolean deleteAnswer(String quiz, String question, String answer) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			if(cfg.getConfigurationSection("Question." + question + ".Answer").contains(answer)) {
			
				cfg.set("Question." + question + ".Answer." + answer, null);
				
				cfg.options().copyHeader(true);
				cfg.options().copyDefaults(true);
			
				try {
					cfg.save(file);
					return true;
				} catch (IOException e) {
					this.plugin.getLogger().info("deleteAnswer: Could not save quiz (" + quiz + ")");
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return a List of all quiz (title/id) - else null
	 */
	public List<String> getQuizes() {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz");
		if(file.exists() && file.isDirectory()) {
			List<String> files = new ArrayList<String>();
			for(String args : file.list()) {
				files.add(args);
			}
			if(!files.isEmpty()) {
				return files;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param quiz - quiz title/id
	 * @return a List of all questions of a quiz (title/id) - else null
	 */
	public List<String> getQuestions(String quiz) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.contains("Question")) {
				List<String> files = new ArrayList<String>();
				files.addAll(cfg.getConfigurationSection("Question").getKeys(false));
				if(!files.isEmpty()) {
					return files;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param quiz - quiz title/id
	 * @param question - question title/id
	 * @return a List of all answers of a question (title/id) - else null
	 */
	public List<String> getAnswers(String quiz, String question) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/config.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.contains("Question." + question + ".Answer")) {
				List<String> files = new ArrayList<String>();
				files.addAll(cfg.getConfigurationSection("Question." + question + ".Answer").getKeys(false));
				if(!files.isEmpty()) {
					return files;
				}
			}
		}
		return null;
	}
	
	/**
	 * saves the oneTimeUse players in a file
	 * @param quiz - name of quiz
	 * @param player - player to be saved
	 * @return
	 */
	public boolean savePlayer(String quiz, Player player) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/player.txt");
		if(file.exists()) {
			//uuid:name
			try {
				FileWriter fw = new FileWriter(file, true);
				BufferedWriter bw = new BufferedWriter(fw);
				
				bw.write(player.getName() + "," + player.getUniqueId().toString() + "\n");
				
				bw.close();
				fw.close();
			} catch (IOException e) {
				this.plugin.getLogger().info("checkQuizPlayer: can't read file");
				e.printStackTrace();
			}
		} else {
			//file not exist
			try {
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				
				bw.write(player.getName() + "," + player.getUniqueId().toString() + "\n");
				
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				this.plugin.getLogger().info("savePlayer: can't save file");
				e.printStackTrace();
			}
		}		
		return false;
	}
	
	/**
	 * saves error of the player from the quiz
	 * @param player - player
	 * @param quiz - quizname
	 * @param error - amount of error
	 * @return
	 */
	public boolean savePlayerError(Player player, String quiz, int error) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/errors.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.contains(player.getUniqueId().toString())){
				cfg.set(player.getUniqueId().toString(), error);
			} else {
				cfg.addDefault(player.getUniqueId().toString(), error);
			}
			cfg.options().copyDefaults(true);
			cfg.options().copyHeader(true);
			
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			//create file
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			cfg.addDefault(player.getUniqueId().toString(), error);
			cfg.options().copyDefaults(true);
			cfg.options().copyHeader(true);
			cfg.options().header("SaveFile for Player Errors from Quiz (" + quiz + ")");
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean savePlayerAnswer(String quiz, Player player, String question, String answer) {
		File file = new File("plugins/" + this.plugin.getName() + "/Quiz/" + quiz + "/survey.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.contains(player.getUniqueId().toString() + "." + question)){
				cfg.set(player.getUniqueId().toString() + "." + question, answer);
			} else {
				cfg.addDefault(player.getUniqueId().toString() + "." + question, answer);
			}
			if(cfg.contains(player.getUniqueId().toString() + ".Name")) {
				cfg.set(player.getUniqueId().toString() + ".Name", player.getName());
			} else {
				cfg.addDefault(player.getUniqueId().toString() + ".Name", player.getName());
			}
			cfg.options().copyDefaults(true);
			cfg.options().copyHeader(true);
			
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			//create file
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			cfg.addDefault(player.getUniqueId().toString() + ".Name", player.getName());
			cfg.addDefault(player.getUniqueId().toString() + "." + question, answer);
			cfg.options().copyDefaults(true);
			cfg.options().copyHeader(true);
			cfg.options().header("SaveFile for Player Errors from Quiz (" + quiz + ")");
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * clears loaded Data
	 * @return true when finished
	 */
	public boolean reload() {
		return this.loadQuiz.reload();
	}
}
