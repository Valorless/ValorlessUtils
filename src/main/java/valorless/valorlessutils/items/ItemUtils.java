package valorless.valorlessutils.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utility class to get certain ItemMeta values on versions < 1.21.<br>
 * Be sure to check if the server is 1.20.5 or above before using,<br>
 * otherwise errors may get thrown.
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

	public static Boolean HasMaxStackSize(ItemStack item) {
		if(!item.hasItemMeta()) {
			return false;
		}
		return item.getItemMeta().hasMaxStackSize();
	}

	public static Integer GetMaxStackSize(ItemStack item) {
		return item.getItemMeta().getMaxStackSize();
	}

	public static void SetMaxStackSize(ItemStack item, Integer value) {
		ItemMeta meta = item.getItemMeta();
		meta.setMaxStackSize(value);
		item.setItemMeta(meta);
	}
	
	public static NamespacedKey GetItemModel(ItemStack item) {
		return item.getItemMeta().getItemModel();
	}
	
	public static void SetItemModel(ItemStack item, NamespacedKey value) {
		ItemMeta meta = item.getItemMeta();
		meta.setItemModel(value);
		item.setItemMeta(meta);
	}
	
	public static void SetItemModel(ItemStack item, String value) {
		ItemMeta meta = item.getItemMeta();
		String[] split = value.split(":");
		if(split.length != 2) return;
		NamespacedKey key = new NamespacedKey(split[0], split[1]);
		meta.setItemModel(key);
		item.setItemMeta(meta);
	}
	
}
