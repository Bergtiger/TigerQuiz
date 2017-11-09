package de.bergtiger.tigerquiz.commands;

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
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(args.length > 0) {
			switch (args[0].toLowerCase()) {
				case "start" : this.startQuiz(cs, args); break;
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
			//no Player
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
