package valorless.valorlessutils.havenbags;

import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.ValorlessUtils.*;
import valorless.valorlessutils.nbt.NBT;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HavenBagsPlacementBlocker implements Listener {
	
	static YamlConfiguration config = null;
	
	public static void init() {
		config = getConfig();
		if(config == null) return;
		Log.Debug(ValorlessUtils.plugin, "Registering HavenBagsPlacementBlocker.");
		Bukkit.getServer().getPluginManager().registerEvents(new HavenBagsPlacementBlocker(), ValorlessUtils.plugin);
	}
	
	static YamlConfiguration getConfig() {
		try {
			return YamlConfiguration.loadConfiguration(new File("plugins/HavenBags/config.yml"));
		}catch(Exception e) {
			return null;
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// Ignore if HavenBags is present.
		if(Bukkit.getServer().getPluginManager().getPlugin("HavenBags") != null) {
			if(Bukkit.getServer().getPluginManager().getPlugin("HavenBags").isEnabled()) {
				return;
			}
		}
		
		if(IsBag(event.getItemInHand())) {
			event.setCancelled(true); 
			return;
		}
		
		Block block = event.getBlockPlaced();
		ItemStack item = event.getItemInHand();
		ItemMeta nbt = item.getItemMeta();
		
		if(nbt != null) {
			if(IsBag(item)) {
				block.setType(Material.AIR);
				event.setCancelled(true);
				return;
			}
			if(IsSkinToken(item)) {
				block.setType(Material.AIR);
				event.setCancelled(true);
				return;
			}
		}
		//Fallback
		if(item.getType() == Material.AIR) {
			if(config.getString("bag-type").equalsIgnoreCase("ITEM")) {
			if(block.getType() == Material.valueOf(config.getString("bag-material"))) {
				block.setType(Material.AIR);
				event.setCancelled(true);
				return;
			}
			}else if(config.getString("bag-type").equalsIgnoreCase("HEAD")) {
				if(block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD) {
					block.setType(Material.AIR);
					event.setCancelled(true);
					return;
				}
			} 
		}
	}
	
	public static Boolean IsBag(ItemStack item) {
		if(item == null) return false;
		if(item.hasItemMeta()) {
			if(NBT.Has(item, "bag-uuid")) {
				return true;
			}
		}
		return false;
	}

	public static Boolean IsSkinToken(ItemStack item) {
		if(item == null) return false;
		if(item.hasItemMeta()) {
			if(NBT.Has(item, "bag-token-skin")) {
				return true;
			}
		}
		return false;
	}
}
