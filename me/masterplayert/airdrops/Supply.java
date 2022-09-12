package me.masterplayert.airdrops;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.google.common.collect.Lists;

public class Supply implements Listener{
	
    public static ConfigurationSection getConfigurationSectionZ() {
    	ConfigurationSection section = Drop.getInstance().getConfig().getConfigurationSection("ArraySize");
    	return section;
    }
    
    public static ConfigurationSection getItemSection(String item) {
    	  ConfigurationSection items = getConfigurationSectionZ().getConfigurationSection(item);
    	  return items;
    }
    
    public static void setAirDropEffects(World world, Location lox) {
    	List<Location> particleLocs = getHollowCube(lox, 0.01);
    	for (int i = 0; i < particleLocs.size(); i++) {
    		world.spawnParticle(Particle.DRIPPING_HONEY, particleLocs.get(i), 1, 0, 0, 0);
    	}
    }
    
    public static List<Location> getHollowCube(Location loc, double particleDistance) {
    	List<Location> result = Lists.newArrayList();
        World world = loc.getWorld();
        double minX = loc.getBlockX();
        double minY = loc.getBlockY();
        double minZ = loc.getBlockZ();
        double maxX = loc.getBlockX()+1;
        double maxY = loc.getBlockY()+1;
        double maxZ = loc.getBlockZ()+1;

        for (double x = minX; x <= maxX; x = Math.round((x + particleDistance) * 1e2) / 1e2) {
            for (double y = minY; y <= maxY; y = Math.round((y + particleDistance) * 1e2) / 1e2) {
                for (double z = minZ; z <= maxZ; z = Math.round((z + particleDistance) * 1e2) / 1e2) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }
        return result;
    }
    
    public static void setAsSupplier(ItemStack item) {
    	NamespacedKey key = new NamespacedKey(Drop.getInstance(), "airdrop-id");
    	ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(key, PersistentDataType.INTEGER, 1);
        item.setItemMeta(meta);
    }
    
    public static void getSupply(Inventory chestinv) {
    	Random random = new Random();
    	for (String item: getConfigurationSectionZ().getKeys(false)) {
    		ConfigurationSection items = getItemSection(item);
    		Material material = Material.getMaterial(items.getString(".Material"));
    		
    		//The only thing from 1.13+ version is the enchanted golden apple code
    		if (items.getString(".Material") == "ENCHANTED_GOLDEN_APPLE") {
    			material = Material.ENCHANTED_GOLDEN_APPLE;
    		}
    		else {
    			material = Material.getMaterial(items.getString(".Material"));
    		}
    	    int chance = items.getInt(".Chance");
    	    //Add 1 to the high because random num gen will exclude the topmost number from being picked
        	int high = items.getInt(".MaxAmount");
        	//Lowest number is include in random num gen picking
        	int low = items.getInt(".MinAmount");
        	//Resultant random num picked between the low and high bounds
        	int result = low;
        	if (low != high) {
        		high = high + 1;
        		result = random.nextInt(high-low) + low;
        	}
            ItemStack is = new ItemStack(material, result);
            //is.addUnsafeEnchantment(Enchantment.getByName(items.getString(".EnchantName")), items.getInt(".Level"));
            //setAsSupplier(is);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(items.getString(".ItemName"));
            is.setItemMeta(im);
            if (random.nextInt(100) <= chance) {
            	chestinv.addItem(is);
            }
    	}
    }
}
