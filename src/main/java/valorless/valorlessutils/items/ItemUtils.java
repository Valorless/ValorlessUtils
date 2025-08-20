package valorless.valorlessutils.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utility class for interacting with {@link ItemMeta} fields introduced in Minecraft 1.20.5+.
 * <p>
 * These helper methods allow for easy access to new item metadata, such as:
 * <ul>
 *     <li>Item name (internal vs. display name)</li>
 *     <li>Item rarity</li>
 *     <li>Custom max stack size</li>
 *     <li>Item model (resource key)</li>
 * </ul>
 * </p>
 *
 * <p><strong>Note:</strong> These methods rely on APIs only available in 1.20.5 and above.
 * Always check the server version before calling these methods to avoid errors on older servers.</p>
 */
public class ItemUtils {

    /**
     * Checks if the item has an internal item name.
     *
     * @param item The {@link ItemStack} to check
     * @return True if the item has an internal name, false otherwise
     */
    public static Boolean HasItemName(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().hasItemName();
    }

    /**
     * Gets the internal item name.
     *
     * @param item The {@link ItemStack} to get the name from
     * @return The item name string
     */
    public static String GetItemName(ItemStack item) {
        return item.getItemMeta().getItemName();
    }

    /**
     * Sets the internal item name.
     *
     * @param item The {@link ItemStack} to modify
     * @param name The name to set
     */
    public static void SetItemName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(name);
        item.setItemMeta(meta);
    }

    /**
     * Checks if the item has a custom display name.
     *
     * @param item The {@link ItemStack} to check
     * @return True if the item has a display name, false otherwise
     */
    public static Boolean HasCustomName(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().hasDisplayName();
    }

    /**
     * Gets the custom display name.
     *
     * @param item The {@link ItemStack} to get the display name from
     * @return The display name string
     */
    public static String GetCustomName(ItemStack item) {
        return item.getItemMeta().getDisplayName();
    }

    /**
     * Sets the custom display name.
     *
     * @param item The {@link ItemStack} to modify
     * @param name The display name to set
     */
    public static void SetCustomName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    /**
     * Checks if the item has a rarity value.
     *
     * @param item The {@link ItemStack} to check
     * @return True if the item has a rarity, false otherwise
     */
    public static Boolean HasRarity(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().hasRarity();
    }

    /**
     * Gets the rarity of the item as a string.
     *
     * @param item The {@link ItemStack} to get rarity from
     * @return The rarity as a string (e.g., "COMMON", "RARE", "EPIC")
     */
    public static String GetRarity(ItemStack item) {
        return item.getItemMeta().getRarity().toString();
    }

    /**
     * Sets the rarity of the item.
     *
     * @param item The {@link ItemStack} to modify
     * @param rarity The rarity as a string (must match {@link ItemRarity} name)
     */
    public static void SetRarity(ItemStack item, String rarity) {
        ItemMeta meta = item.getItemMeta();
        meta.setRarity(ItemRarity.valueOf(rarity));
        item.setItemMeta(meta);
    }

    /**
     * Checks if the item has a custom max stack size.
     *
     * @param item The {@link ItemStack} to check
     * @return True if a custom max stack size is set, false otherwise
     */
    public static Boolean HasMaxStackSize(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().hasMaxStackSize();
    }

    /**
     * Gets the maximum stack size defined on the item.
     *
     * @param item The {@link ItemStack} to get the value from
     * @return The maximum stack size
     */
    public static Integer GetMaxStackSize(ItemStack item) {
        return item.getItemMeta().getMaxStackSize();
    }

    /**
     * Sets a custom maximum stack size for the item.
     *
     * @param item The {@link ItemStack} to modify
     * @param value The maximum stack size value
     */
    public static void SetMaxStackSize(ItemStack item, Integer value) {
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(value);
        item.setItemMeta(meta);
    }

    /**
     * Gets the namespaced key of the item's model.
     *
     * @param item The {@link ItemStack} to query
     * @return The {@link NamespacedKey} representing the item model
     */
    public static NamespacedKey GetItemModel(ItemStack item) {
        return item.getItemMeta().getItemModel();
    }

    /**
     * Sets the item model using a {@link NamespacedKey}.
     *
     * @param item The {@link ItemStack} to modify
     * @param value The {@link NamespacedKey} of the model
     */
    public static void SetItemModel(ItemStack item, NamespacedKey value) {
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(value);
        item.setItemMeta(meta);
    }

    /**
     * Sets the item model using a string in the format "namespace:key".
     *
     * @param item The {@link ItemStack} to modify
     * @param value The string representing the model key
     */
    public static void SetItemModel(ItemStack item, String value) {
        ItemMeta meta = item.getItemMeta();
        String[] split = value.split(":");
        if (split.length != 2) return; // invalid format, ignore

        NamespacedKey key = new NamespacedKey(split[0], split[1]);
        meta.setItemModel(key);
        item.setItemMeta(meta);
    }
}
