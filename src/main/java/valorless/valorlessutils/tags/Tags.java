package valorless.valorlessutils.tags;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import valorless.valorlessutils.json.JsonUtils;
import valorless.valorlessutils.utils.Utils;

/**
 * Tags utility class for working with PersistentDataContainers.<br>
 */
public class Tags {

	//Set
	
	/**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
 	*/
    public static void SetString(JavaPlugin caller, ItemStack item, String key, String value) {
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
 	*/
    public static void SetStringList(JavaPlugin caller, ItemStack item, String key, List<String> value) {
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), PersistentDataType.STRING, JsonUtils.toJson(value));
        item.setItemMeta(meta);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
 	*/
    public static void SetInteger(JavaPlugin caller, ItemStack item, String key, Integer value) {
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
 	*/
    public static void SetIntegerList(JavaPlugin caller, ItemStack item, String key, List<Integer> value) {
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), PersistentDataType.STRING, JsonUtils.toJson(value));
        item.setItemMeta(meta);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
 	*/
    public static void SetDouble(JavaPlugin caller, ItemStack item, String key, Double value) {
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), PersistentDataType.DOUBLE, value);
        item.setItemMeta(meta);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
 	*/
    public static void SetDoubleList(JavaPlugin caller, ItemStack item, String key, List<Double> value) {
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), PersistentDataType.STRING, JsonUtils.toJson(value));
        item.setItemMeta(meta);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
 	*/
    public static void SetFloat(JavaPlugin caller, ItemStack item, String key, Float value) {
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), PersistentDataType.FLOAT, value);
        item.setItemMeta(meta);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
 	*/
    public static void SetFloatList(JavaPlugin caller, ItemStack item, String key, List<Float> value) {
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), PersistentDataType.STRING, JsonUtils.toJson(value));
        item.setItemMeta(meta);
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
 	*/
    public static void SetBoolean(JavaPlugin caller, ItemStack item, String key, Boolean value) {
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), PersistentDataType.BOOLEAN, value);
        item.setItemMeta(meta);
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
     * Gets a String List value from the PersistentDataContainer using a specific key.
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve the value from.
     * @param key The key to retrieve.
     * @return The retrieved String List value, or null if not present.
     */
    public static List<String> GetStringList(JavaPlugin caller, ItemStack item, String key) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return JsonUtils.fromJson(container.get(new NamespacedKey(caller, key), PersistentDataType.STRING));
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
     * Gets a Integer List value from the PersistentDataContainer using a specific key.
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve the value from.
     * @param key The key to retrieve.
     * @return The retrieved Integer List value, or null if not present.
     */
    public static List<Integer> GetIntegerList(JavaPlugin caller, ItemStack item, String key) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return JsonUtils.fromJson(container.get(new NamespacedKey(caller, key), PersistentDataType.STRING));
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
     * Gets a Double List value from the PersistentDataContainer using a specific key.
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve the value from.
     * @param key The key to retrieve.
     * @return The retrieved Double List value, or null if not present.
     */
    public static List<Double> GetDoubleList(JavaPlugin caller, ItemStack item, String key) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return JsonUtils.fromJson(container.get(new NamespacedKey(caller, key), PersistentDataType.STRING));
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
     * Gets a Float List value from the PersistentDataContainer using a specific key.
     * @param caller The calling plugin.
     * @param item ItemStack to retrieve the value from.
     * @param key The key to retrieve.
     * @return The retrieved Float List value, or null if not present.
     */
    public static List<Float> GetFloatList(JavaPlugin caller, ItemStack item, String key) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return JsonUtils.fromJson(container.get(new NamespacedKey(caller, key), PersistentDataType.STRING));
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
     * Use before or after setting metadata.
     * @param caller The calling plugin.
     * @param item ItemStack to modify.
     * @param key The key to set.
     * @param value The value to set.
     * @param type The TagType.
 	*/
	
	@SuppressWarnings("unchecked")
	public static void Set(JavaPlugin caller, ItemStack item, String key, Object value, TagType type) {
    	@SuppressWarnings("rawtypes")
		PersistentDataType m_type = TagType.GetPersistentDataType(type);
    	ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(caller, key), m_type, value);
        item.setItemMeta(meta);
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

    /**
     * Checks if a key exists in the PersistentDataContainer.<br>
     * @param caller The calling plugin.
     * @param item ItemStack to check.
     * @param key The key to check.
     * @return true if the key exists, false otherwise.
 	*/
	public static Boolean Has(JavaPlugin caller, ItemStack item, String key) {
        try {
        	PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            return container.has(new NamespacedKey(caller, key));
        } catch (Exception e) {
        	return false;
        }
    }
	public static Boolean Remove(JavaPlugin caller, ItemStack item, String key) {
        try {
        	ItemMeta meta = item.getItemMeta();
        	PersistentDataContainer container = meta.getPersistentDataContainer();
            container.remove(new NamespacedKey(caller, key));
            item.setItemMeta(meta);
        } catch (Exception e) {
        	return false;
        }
        return true;
    }
    
    /**
     * Sets a value in the PersistentDataContainer using a specific key.<br>
     * @param caller The calling plugin.
     * @param container The PersistentDataContainer to modify.
     * @param key The key to set.
     * @param value The value to set.
     * @param type The TagType.
 	*/
    @SuppressWarnings({ "unchecked" })
    public static void Set(JavaPlugin caller, PersistentDataContainer container, String key, Object value, TagType type) {
		@SuppressWarnings("rawtypes")
		PersistentDataType m_type = TagType.GetPersistentDataType(type);
        container.set(new NamespacedKey(caller, key), m_type, value);
    }

    /**
     * Gets a value from the PersistentDataContainer using a specific key.<br>
     * @param caller The calling plugin.
     * @param container The PersistentDataContainer to retrieve from.
     * @param key The key to retrieve.
     * @param type The TagType.
     * @return The retrieved value.
 	*/
    @SuppressWarnings({ "unchecked" })
    public static Object Get(JavaPlugin caller, PersistentDataContainer container, String key, TagType type) {
		@SuppressWarnings("rawtypes")
		PersistentDataType m_type = TagType.GetPersistentDataType(type);
        return container.get(new NamespacedKey(caller, key), m_type);
    }

    /**
     * Checks if a key exists in the PersistentDataContainer.<br>
     * @param caller The calling plugin.
     * @param container The PersistentDataContainer to check.
     * @param key The key to check.
     * @param type The TagType.
     * @return true if the key exists, false otherwise.
 	*/
    @SuppressWarnings("unchecked")
	public static Boolean Has(JavaPlugin caller, PersistentDataContainer container, String key, TagType type) {
        try {
    		@SuppressWarnings("rawtypes")
			PersistentDataType m_type = TagType.GetPersistentDataType(type);
            return container.has(new NamespacedKey(caller, key), m_type);
        } catch (Exception e) {
        	return false;
        }
    }
}
