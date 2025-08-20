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

/**
 * A safeguard listener that prevents players from placing "HavenBags" items or skin tokens as blocks
 * when the HavenBags plugin is not enabled.  
 * <p>
 * This ensures items that are meant to function only as bags (or special tokens) cannot be placed
 * in the world as normal blocks, which could otherwise corrupt gameplay or break bag functionality.
 * </p>
 */
public class HavenBagsPlacementBlocker implements Listener {
	
	/** Cached HavenBags configuration file */
	static YamlConfiguration config = null;
	
	/**
	 * Initializes the placement blocker by loading the HavenBags config and registering the event listener.  
	 * If the config cannot be loaded, the blocker will not activate.
	 */
	public static void init() {
		config = getConfig();
		if(config == null) return;
		
		Log.Debug(ValorlessUtils.plugin, "Registering HavenBagsPlacementBlocker.");
		Bukkit.getServer().getPluginManager().registerEvents(new HavenBagsPlacementBlocker(), ValorlessUtils.plugin);
	}
	
	/**
	 * Loads the HavenBags config.yml file from the plugin directory.  
	 * Returns {@code null} if the file cannot be read.
	 *
	 * @return the loaded YamlConfiguration or null if unavailable
	 */
	static YamlConfiguration getConfig() {
		try {
			return YamlConfiguration.loadConfiguration(new File("plugins/HavenBags/config.yml"));
		}catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Event handler that prevents bag items or skin tokens from being placed as blocks if HavenBags is missing or disabled.  
	 * Cancels the block placement if the placed item is recognized as a bag or token.
	 *
	 * @param event the block placement event
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// Ignore placement checks if HavenBags is present and enabled.
		if(Bukkit.getServer().getPluginManager().getPlugin("HavenBags") != null) {
			if(Bukkit.getServer().getPluginManager().getPlugin("HavenBags").isEnabled()) {
				return;
			}
		}
		
		// Cancel placement if the item is a bag
		if(IsBag(event.getItemInHand())) {
			event.setCancelled(true); 
			return;
		}
		
		Block block = event.getBlockPlaced();
		ItemStack item = event.getItemInHand();
		ItemMeta nbt = item.getItemMeta();
		
		// Extra safeguard for NBT-based checks
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

		// Fallback check: prevents incorrect block types from being placed if items become "empty" (AIR)
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
	
	/**
	 * Checks whether the given item is a HavenBag.
	 *
	 * @param item the item to check
	 * @return true if the item contains a {@code bag-uuid} NBT tag
	 */
	public static Boolean IsBag(ItemStack item) {
		if(item == null) return false;
		if(item.hasItemMeta()) {
			if(NBT.Has(item, "bag-uuid")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the given item is a skin token.
	 *
	 * @param item the item to check
	 * @return true if the item contains a {@code bag-token-skin} NBT tag
	 */
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
