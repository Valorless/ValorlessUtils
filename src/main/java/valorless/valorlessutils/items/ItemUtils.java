package valorless.valorlessutils.items;

import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utility class to get certain ItemMeta values on versions < 1.21.
 */
public class ItemUtils {

	public static Boolean HasItemName(ItemStack item) {
		if(!item.hasItemMeta()) {
			return false;
		}
		return item.getItemMeta().hasItemName();
	}
	
	public static String GetItemName(ItemStack item) {
		return item.getItemMeta().getItemName();
	}

	public static void SetItemName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setItemName(name);
		item.setItemMeta(meta);
	}

	public static Boolean HasCustomName(ItemStack item) {
		if(!item.hasItemMeta()) {
			return false;
		}
		return item.getItemMeta().hasDisplayName();
	}

	public static String GetCustomName(ItemStack item) {
		return item.getItemMeta().getDisplayName();
	}

	public static void SetCustomName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	public static Boolean HasRarity(ItemStack item) {
		if(!item.hasItemMeta()) {
			return false;
		}
		return item.getItemMeta().hasRarity();
	}

	public static String GetRarity(ItemStack item) {
		return item.getItemMeta().getRarity().toString();
	}

	public static void SetRarity(ItemStack item, String rarity) {
		ItemMeta meta = item.getItemMeta();
		meta.setRarity(ItemRarity.valueOf(rarity));
		item.setItemMeta(meta);
	}
	

	
}