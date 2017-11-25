package de.bergtiger.tigerquiz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import de.bergtiger.tigerquiz.data.MyString;

public class Config {
	
	private TigerQuiz plugin;
	private static String lang = "lang.";
	
	public Config(TigerQuiz plugin) {
		this.plugin = plugin;
	}
	
	public void reload() {
		this.plugin.reloadConfig();
		this.create();
		this.load();
	}
	
	public void create() {
		this.createConfig();
		this.createReadMe();
	}
	
	public void load() {
		this.loadConfig();
		this.checkReadMe();
	}
	
	private void createConfig() {
		FileConfiguration cfg = this.plugin.getConfig();
		
		for(MyString myString : MyString.values()){
			//			System.out.println(myString.name().replace("_", "."));
			cfg.addDefault(lang + myString.name().replace("_", "."), myString.get());
		}
		
		cfg.options().copyDefaults(true);
		cfg.options().copyHeader(true);
		cfg.options().header(this.plugin.getName() + " - Config");
		
		this.plugin.saveConfig();
	}
	
	private void loadConfig() {
		FileConfiguration cfg = this.plugin.getConfig();
		
		for(MyString myString : MyString.values()){
//			System.out.println(myString.name());
			myString.setString(cfg.getString(lang + myString.name().replace("_", ".")));
		}
	}
	
	private void checkReadMe() {
		File file = new File("plugins/" + this.plugin.getName() + "/README.txt");
		if(file.exists()){
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = null;
				while((line = br.readLine()) != null){
					if(line.toLowerCase().contains("version ")){
						if(!line.equalsIgnoreCase("version " + this.plugin.getDescription().getVersion())){
							br.close();
							fr.close();
							file.delete();
							this.createReadMe();
							return;
						}
						break;
					}
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				this.plugin.getLogger().info("could not save or load ReadME(" + file + ")");
				e.printStackTrace();
			}
		}
	}
	
	private void createReadMe() {
		File file = new File("plugins/" + this.plugin.getName() + "/README.txt");
		if(!file.exists()){
			try {
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("Tiger's Halloween - ReadMe"
					+ "\n"
					+ "\n" + "Version " + this.plugin.getDescription().getVersion()
					+ "\n"
					+ "\n" + "History:"
					+ "\n" + "\t- 1.0"
				);
				bw.close();
				fw.close();
			} catch (IOException e) {
				this.plugin.getLogger().info("could not save ReadME(" + file + ")");
				e.printStackTrace();
			}
		}
	}
}
