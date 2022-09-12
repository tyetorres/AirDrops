package me.masterplayert.airdrops;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.*;

public class Commands implements CommandExecutor
{
	public static Drop plugin;
    public static String noPermMSG;
    public static String notPlayer;
    
    static {
        Commands.noPermMSG = ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "Group Checker" + ChatColor.GRAY + "] " + ChatColor.DARK_RED + "You don't have permission to use that command!";
        Commands.notPlayer = ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "Group Checker" + ChatColor.GRAY + "] " + ChatColor.DARK_RED + "You must be a player to use that command";
    }
    
    // Player Commands //
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player player = (Player)sender;
        if (args.length == 0) {
        	
        	if (sender.hasPermission("airdrop-use")) {
        		// if nothing is said such as /ad or /airdrops. (There is no 2nd args such as help)
                return true;
            } 
        	else {
                sender.sendMessage(noPermMSG);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
        	if (sender.hasPermission("airdrop-use")) {
        		sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "[AirDrop] " 
        	            + ChatColor.GRAY + "] " + ChatColor.DARK_RED + ChatColor.RED + "add, reload, help");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
        	if (sender.hasPermission("reload")) {
        		plugin.reloadConfig();
        		return true;
            } else {
                sender.sendMessage(noPermMSG);
                return true;
            }
        } 
        else if (args[0].equalsIgnoreCase("add")) {
        	if (sender instanceof Player) {
	        	if (sender.hasPermission("airdrop-use")) {
	    		    String mat = Drop.getInstance().getConfig().getString("ClickBait");
	    		    String matName = Drop.getInstance().getConfig().getString("ClickBaitName");
	    		    Material chooseMat = Material.getMaterial(mat);
	    		    ItemStack is = new ItemStack(chooseMat);
	    		    Supply.setAsSupplier(is);
	    		    ItemMeta im = is.getItemMeta();
	    		    im.setDisplayName(matName);
	    		    is.setItemMeta(im);
	    		    player.getInventory().addItem(is);
	        		return true;
	            } else {
	                sender.sendMessage(noPermMSG);
	                return true;
	            }
        	} 
        	else {
	            sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "[AirDrop] " 
	            + ChatColor.GRAY + "] " + ChatColor.DARK_RED + ChatColor.RED + "Unknown command!");
	            return true;
        	}
        }
        return false;
    }
}
