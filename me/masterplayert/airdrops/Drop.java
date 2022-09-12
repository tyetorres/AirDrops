package me.masterplayert.airdrops;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Drop extends JavaPlugin {
	private static int Y = 100;
    private static Drop plugin;
    public void onEnable() {
    	plugin = this;
    	plugin.getCommand("ad").setExecutor(new Commands());
    	plugin.getCommand("airdrop").setExecutor(new Commands());
		PluginManager pm = getServer().getPluginManager();
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
		pm.registerEvents(new AirDropEventsListener(), plugin);
	}
    
    public static void createAirDropInv(Player player, Location lox) {
    	NamespacedKey key = new NamespacedKey(Drop.getInstance(), "airdrops-chest-id");
    	Chest chest = ((Chest) lox.getBlock().getState());
		PersistentDataContainer container = chest.getPersistentDataContainer();
		if(!container.has(key, PersistentDataType.INTEGER)) {
			container.set(key, PersistentDataType.INTEGER, 1);
		}
		chest.update();
    }
    
    public static int getY() {
    	return Y;
    }
    
    public static Drop getInstance(){
        return plugin;
    }
}