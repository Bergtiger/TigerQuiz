package de.bergtiger.tigerquiz.data;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public enum MyText {;
	/**
	 * 
	 * @param cs - Player to be send
	 * @param text - text to be shown in chat
	 * @param args - text to be shown as hover or make to command
	 * @param action - hover/cmd
	 */
	public static void sendText(CommandSender cs, String text, String args, String action){
		if(text != null){
			if((args != null) && (action != null) && (cs instanceof Player)){
				switch (action.toLowerCase()){
					case "cmd" : sendCommand(cs, text, args); break;
					case "hover": sendHover(cs, text, args); break;
				}
			} else {
				cs.sendMessage(text);
			}
		}
	}
	
	/**
	 * makes a text in chat - onClick a command will be run
	 * @param cs - Player to be send
	 * @param args - text to be shown in chat
	 * @param cmd - text wich be interpreted as command
	 */
	public static void sendCommand(CommandSender cs, String args, String cmd){
		if(args != null){
			if((cmd != null) && (cs instanceof Player)){
				Player p = (Player) cs;
				TextComponent text = new TextComponent();
				text.setText(args);
				text.setClickEvent(new ClickEvent(Action.RUN_COMMAND, cmd));
				p.spigot().sendMessage(text);
			} else {
				cs.sendMessage(args);
			}
		}
	}
	
	/**
	 * 
	 * @param cs - Player to be send
	 * @param text - text to be shown in chat
	 * @param hover - hovertext
	 */
	public static void sendHover(CommandSender cs, String text, String hover){
		if(text != null){
			if((hover != null) && (cs instanceof Player)){
				Player player = (Player) cs;
				TextComponent test = new TextComponent();
				test.setText(text);
				test.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
				player.spigot().sendMessage(test);
			} else {
				cs.sendMessage(text);
			}
		}
	}
}
