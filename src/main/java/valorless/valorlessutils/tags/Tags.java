package valorless.valorlessutils.tags;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Tags utility class for working with PersistentDataContainers.<br>
 */
public class Tags {

	//Set
	
	/**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
     * @param type The TagType.
 	*/
    public static void SetString(JavaPlugin caller, ItemStack item, String key, String value) {
    	PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        container.set(new NamespacedKey(caller, key), PersistentDataType.STRING, value);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
     * @param type The TagType.
 	*/
    public static void SetInteger(JavaPlugin caller, ItemStack item, String key, Integer value) {
    	PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        container.set(new NamespacedKey(caller, key), PersistentDataType.INTEGER, value);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
     * @param type The TagType.
 	*/
    public static void SetDouble(JavaPlugin caller, ItemStack item, String key, Double value) {
    	PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        container.set(new NamespacedKey(caller, key), PersistentDataType.DOUBLE, value);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
     * @param type The TagType.
 	*/
    public static void SetFloat(JavaPlugin caller, ItemStack item, String key, Float value) {
    	PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        container.set(new NamespacedKey(caller, key), PersistentDataType.FLOAT, value);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
     * @param type The TagType.
 	*/
    public static void SetBoolean(JavaPlugin caller, ItemStack item, String key, Boolean value) {
    	PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        container.set(new NamespacedKey(caller, key), PersistentDataType.BOOLEAN, value);
    }
    
    
    //Get
    
    /**
     * Gets a String value from the PersistentDataContainer using a specific key.
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve the value from.
     * @param key The key to retrieve.
     * @return The retrieved String value, or null if not present.
     */
    public static String GetString(JavaPlugin caller, ItemStack item, String key) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.get(new NamespacedKey(caller, key), PersistentDataType.STRING);
    }

    /**
     * Gets an Integer value from the PersistentDataContainer using a specific key.
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve the value from.
     * @param key The key to retrieve.
     * @return The retrieved Integer value, or null if not present.
     */
    public static Integer GetInteger(JavaPlugin caller, ItemStack item, String key) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.get(new NamespacedKey(caller, key), PersistentDataType.INTEGER);
    }

    /**
     * Gets a Double value from the PersistentDataContainer using a specific key.
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve the value from.
     * @param key The key to retrieve.
     * @return The retrieved Double value, or null if not present.
     */
    public static Double GetDouble(JavaPlugin caller, ItemStack item, String key) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.get(new NamespacedKey(caller, key), PersistentDataType.DOUBLE);
    }

    /**
     * Gets a Float value from the PersistentDataContainer using a specific key.
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve the value from.
     * @param key The key to retrieve.
     * @return The retrieved Float value, or null if not present.
     */
    public static Float GetFloat(JavaPlugin caller, ItemStack item, String key) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.get(new NamespacedKey(caller, key), PersistentDataType.FLOAT);
    }

    /**
     * Gets a Boolean value from the PersistentDataContainer using a specific key.
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve the value from.
     * @param key The key to retrieve.
     * @return The retrieved Boolean value, or null if not present.
     */
    public static Boolean GetBoolean(JavaPlugin caller, ItemStack item, String key) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.get(new NamespacedKey(caller, key), PersistentDataType.BYTE) == (byte) 1;
    }
    
    
    //Generic
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
     * @param type The TagType.
 	*/
	
	@SuppressWarnings("unchecked")
	public static void Set(JavaPlugin caller, ItemStack item, String key, Object value, TagType type) {
    	PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
    	@SuppressWarnings("rawtypes")
		PersistentDataType m_type = TagType.GetPersistentDataType(type);
        container.set(new NamespacedKey(caller, key), m_type, value);
    }

    /**
     * Gets a value from the PersistentDataContainer using a specific key.<br>
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve from.
     * @param key The key to retrieve.
     * @param type The TagType.
     * @return The retrieved value.
 	*/
    @SuppressWarnings("unchecked")
	public static Object Get(JavaPlugin caller, ItemStack item, String key, TagType type) {
    	PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
    	@SuppressWarnings("rawtypes")
		PersistentDataType m_type = TagType.GetPersistentDataType(type);
        return container.get(new NamespacedKey(caller, key), m_type);
    }

    /**
     * Checks if a key exists in the PersistentDataContainer.<br>
     * @param caller The calling plugin.
     * @param item ItemStack to check.
     * @param key The key to check.
     * @param type The TagType.
     * @return true if the key exists, false otherwise.
 	*/
    @SuppressWarnings("unchecked")
	public static Boolean Has(JavaPlugin caller, ItemStack item, String key, TagType type) {
        try {
        	PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        	@SuppressWarnings("rawtypes")
			PersistentDataType m_type = TagType.GetPersistentDataType(type);
            return container.has(new NamespacedKey(caller, key), m_type);
        } catch (Exception e) {
        	return false;
        }
    }
}
