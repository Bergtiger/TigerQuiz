package de.bergtiger.tigerquiz;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LoadQuiz {

	private TigerQuiz plugin;
	private HashMap<String, Session> quizzes;
	
	
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
			int maxError = -1;
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
			if(cfg.contains("maxError")) {
				try {
					maxError = Integer.valueOf(cfg.getString("maxError"));
				} catch (NumberFormatException e) {
					this.plugin.getLogger().info("wrong argument maxError: " + cfg.getString("maxError"));
					return;
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
			this.quizzes.put(quiz, new Session(quiz, size, showProgress, ordered, oneTimeUse));
		}
	}
	
	/**
	 * 
	 * @param quiz - name/id of quiz
	 * @param player - should be uuid
	 * @return -1 if nothing there
	 */
	public int checkQuizPlayer(String quiz, String player) {
		File file = new File("/plugins/" + this.plugin.getName() + "/quiz/" + quiz + "player.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.contains(player)){
				return cfg.getInt(player);
			}
		}
		return -1;
	}
	
	private void loadQuizQuestion(String quiz) {
		File file = new File("/plugins/" + this.plugin.getName() + "/quiz/" + quiz + "config.yml");
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			Set<String> questions = cfg.getConfigurationSection("Question").getKeys(false);
			Iterator<String> i = questions.iterator();
			while(i.hasNext()) {
				String question = i.next(); //id
				//load question
				
				int size = -1;
			//	String function = "quiz"; //quiz/survey
				List<Answer> answers = new ArrayList<Answer>();
				
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
					ItemStack item = this.getItem(name, lore, this.getMaterial(material), data, this.getEnchantment(enchantment));
					
					if((position > 0) && (item != null)) {
						Answer answer = new Answer(position, item, function, correct);
						if(answer != null) {
							answers.add(answer);
						}
					}
				}
				
				//resize
				size = this.resize(size, answers);
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
	/**
	 * Delete a Session on hashmap
	 * @param key key of Session on hashmap 
	 */
	public void deleteSessionOnHashmap(String key) {
		if(!quizzes.isEmpty() && quizzes.containsKey(key)) {
			//find session which key contains
			Session evtSession = quizzes.get(key);
			String quizname = evtSession.getQuizname();
			QuizAdministration adm = new QuizAdministration(plugin);
			//delete quizfile
			adm.deleteQuiz(quizname);
			//remove the session on hashmap
			quizzes.remove(key);
			plugin.getLogger().info("The Session with key (" + 
					key + ") is deleted");
		}
	}
}
