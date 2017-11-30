package de.bergtiger.tigerquiz.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import de.bergtiger.tigerquiz.TigerQuiz;

public class CheckVersion {

	protected TigerQuiz plugin;
	protected String pluginname = null; //"tigerquiz."; //number will be there after publishing
	private boolean latestVersion = false;
	
	public CheckVersion (TigerQuiz plugin){
		this.plugin = plugin;
		this.checkVersion();
	}
	
	private String getName(String document) {
		if(document != null) {
			int start = document.indexOf(this.plugin.getName().toLowerCase());
//			int start = document.indexOf("tigerlove");
			int end = document.indexOf("/", Math.max(0, start));
			if(start > 0 && end < document.length()) {
				return document.substring(start, end);
			}
		} else {
			this.plugin.getLogger().info("getName: could not read webside");
		}
		return null;
	}
	
	//Version
	public boolean getLatestVersion(){
		return this.latestVersion;
	}
	
	/*
	 * search if there is a newer version
	 */
	public void checkVersion(){
		//this.searchTable(this.getStrFromUrl(this.pluginname));
		if(this.pluginname != null) {
			this.searchTable(this.getStringFromUrl("https://www.spigotmc.org/resources/" + this.pluginname + "/history"));
		} else {
			String name = this.getName(this.getStringFromUrl("https://www.spigotmc.org/resources/authors/bergtiger.368055/"));
			if(name != null) {
				this.pluginname = name;
				this.plugin.getLogger().info("CheckVersion: SpigotID = " + name);
				this.searchTable(this.getStringFromUrl("https://www.spigotmc.org/resources/" + this.pluginname + "/history"));
			} else {
				this.plugin.getLogger().info("CheckVersion: can't find resource name.");
			}
		}
	}
	
	private void searchTable(String document){
		if(document != null){
			int start = document.indexOf("<table class=");
			int end = document.indexOf("</table>", Math.max(0, start));
			String table = document.substring(start, end);
			String[] columns = table.split("dataRow  ");
			String[] versions = new String[columns.length - 1];
			for(int i = 1; i < columns.length; i++){
				String[] column = columns[i].split("<td class=");
				versions[i - 1] = this.getVersion(column);
			}
			this.getLatest(versions);
		}
	}
	
	private void getLatest(String[] versions){
		if(versions != null){
			String latest = this.plugin.getDescription().getVersion();
			for(int i = 0; i < versions.length; i++){
				latest = this.checkLatest(latest, versions[i]);
			}
			if(!latest.equalsIgnoreCase(this.plugin.getDescription().getVersion())){
				System.out.println("[" + this.plugin.getName() + "] Latest Version: " + latest);
				System.out.println("[" + this.plugin.getName() + "] Update avaible ('" + "https://www.spigotmc.org/resources/" + this.pluginname + "/history" + "')");
				this.latestVersion = true;
			} else {
				this.latestVersion = false;
			}
		}
	}
	
	private String checkLatest(String oldVersion, String newVersion){
		if(oldVersion == null){
			return newVersion;
		} else if(newVersion != null){
			String[] latest = split(oldVersion);
			String[] recent = split(newVersion);
			int max = Math.max(latest.length, recent.length);
			for(int i = 0; i < max; i++){
				try {
					int latest_number = Integer.parseInt(latest[i]);
					int recent_number = Integer.parseInt(recent[i]);
					if(latest_number > recent_number){
						return oldVersion;
					} else if (latest_number < recent_number){
						return newVersion;
					}
				} catch (NumberFormatException e) {
					System.out.println("Error on version check");
					return null;
				} catch (Exception e){
					if(latest.length > recent.length){
						return oldVersion;
					} else {
						return newVersion;
					}
				}
			}
		}
		return oldVersion;
	}
	
	private String[] split(String args){
		if(args != null){
			List<String> array = new ArrayList<String>();
			int start = 0;
			for(int i = 0; i < args.length(); i++){
				if(args.charAt(i) == '.'){
					array.add(args.substring(start, (i)));
					start = i + 1;
				}
			}
			array.add(args.substring(start));
			String[] splitted = new String[array.size()];
			for(int i = 0; i < array.size(); i++){
				splitted[i] = array.get(i);
			}
			return splitted;
		}
		return null;
	}
	
	private String getVersion(String[] column){
		for(int j = 0; j < column.length; j++){
			if(column[j].contains("version")){
				return column[j].substring(column[j].indexOf("version") + 9, (column[j].length() - 6));
			}
		}
		return null;
	}
	
	/**
	 * get a String representing the webside
	 * @param url - "https://www.spigotmc.org/resources/" + pluginname + "/history"
	 * 				"https://www.spigotmc.org/resources/authors/bergtiger.368055/"
	 * @return String or null if not found
	 */
	private String getStringFromUrl(String args) {
		final String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.8.1.12) Gecko/20080201 Firefox/2.0.0.12";
		try {
			URL url = new URL(args);
			URLConnection conn = url.openConnection();
			conn.addRequestProperty("User-Agent", userAgent);
		
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String str;
			StringBuilder builder = new StringBuilder(1024);
			while ((str = in.readLine()) != null) {
				builder.append(str);
				builder.append("\n"); //damit es hinterher auch so aussieht wie vorher ;-)
			}
			in.close();
			return builder.toString();
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
