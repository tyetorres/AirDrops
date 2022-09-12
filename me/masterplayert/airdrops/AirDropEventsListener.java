package me.masterplayert.airdrops;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class AirDropEventsListener implements Listener{
	
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
	    Player player = event.getPlayer();
	    Action action = event.getAction();
	    Material tempItem = Material.BEACON;
	    ItemStack item = event.getItem();
	    
	    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
	    	if (item != null) {
		    	NamespacedKey key = new NamespacedKey(Drop.getInstance(), "airdrop-id");
				PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
			    if (container.has(key, PersistentDataType.INTEGER)){
		            Location loc = player.getLocation();
					loc.setY(loc.getY() + Drop.getY());
		            int x = (int) loc.getX();
		            int upper = 10;
		            Random random = new Random();
		            int coordinate = random.nextInt(upper);
		            double finalx = Math.round( x * 100.0 ) / 100.0;
		            int z = (int) loc.getZ();
		            double finalz = Math.round( z * 100.0 ) / 100.0;
		            loc.setZ(finalz + 0.5 + coordinate);
		            loc.setX(finalx + 0.5 + coordinate);
		            FallingBlock block = loc.getWorld().spawnFallingBlock(loc, tempItem.createBlockData());
		            item.setAmount(item.getAmount() - 1);
		            Vector speed = new Vector(0, -0.0001, 0);
		            block.setVelocity(speed);
		            block.setDropItem(false);
		            Bukkit.getScheduler().scheduleSyncRepeatingTask(Drop.getInstance(), new Runnable() {
		            	double t = 0;
		        		@Override
		        		public void run() {
		        	        t = t + 0.5;
		        			Location location = block.getLocation();
		        			Vector v = location.getDirection().normalize();
	                        double y = v.getY() * t;
	                        location.add(0, y, 0);
	                        World world = block.getWorld();
	                        world.spawnParticle(Particle.FIREWORKS_SPARK, location, 0, 0, 0, 0, 1);
	                        location.subtract(0 ,y ,0);
		    	            if(block.isOnGround() && t > 30) {
		    	                Location lox = block.getLocation();
		    	            	player.playSound(lox, Sound.BLOCK_GLASS_BREAK, 1, 0);
		    	            	lox.getBlock().setType(Material.CHEST);
		    	            	Drop.createAirDropInv(player, lox);
		    					Chest chest = (Chest)lox.getBlock().getState();
		    					Inventory inv = chest.getInventory();
		    					chest.setCustomName(Drop.getInstance().getConfig().getString("InventoryName"));
		    					chest.update();
		    					Supply.getSupply(inv);
		    	            	Bukkit.getServer().broadcastMessage(ChatColor.RED + "A Supply Drop Has Landed!");
		    	            	Bukkit.getServer().broadcastMessage("x coordinate: " + finalx);
		        	            Bukkit.getServer().broadcastMessage("z coordinate: " + finalz);
		        	            Bukkit.getScheduler().cancelTasks(Drop.getInstance());
		        				Bukkit.getScheduler().scheduleSyncRepeatingTask(Drop.getInstance(), new Runnable() {
		            	        	@Override
		            	        	public void run() {
		            	        		World world = chest.getWorld();
		            	        		Location lox = chest.getLocation();
		            	        		Supply.setAirDropEffects(world, lox);
		        	        	        if (inv.isEmpty()) {
		        	        	            player.updateInventory();
		        	        	            player.playEffect(lox, Effect.STEP_SOUND, Material.CHEST);
		        	        	            lox.getBlock().setType(Material.AIR);
		        	        	            Bukkit.getScheduler().cancelTasks(Drop.getInstance());
		        	        	        }
		            	            }
		            	        },0, 1);
		    	            }
		        		}
		            },0, 1);
			    }
	    	}
	    }
	}
}
