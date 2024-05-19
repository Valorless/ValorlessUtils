package valorless.valorlessutils.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class NBT {
	
	// SET
	
	/**
	 * Sets a string value in the NBT data of an ItemStack.
	 * @param item The ItemStack to modify.
	 * @param key The key to set.
	 * @param value The string value to set.
	 */
	public static void SetString(ItemStack item, String key, String value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setString(key, value);
	}
	/**
	 * Sets a List(String) value in the NBT data of an ItemStack.
	 * @param item The ItemStack to modify.
	 * @param key The key to set.
	 * @param value The List(String) value to set.
	 */
	public static void SetStringList(ItemStack item, String key, List<String> value) {
		NBTItem nbt = new NBTItem(item, true);
		String m_value = "";
		for(int i = 0; i < value.size(); i++) {
			if(i != 0) {
				m_value = m_value + "◊" + value.get(i);
			}else {
				m_value = value.get(i);
			}
		}
		nbt.setString(key, m_value);
	}		
	
	/**
	 * Sets an integer value in the NBT data of an ItemStack.
	 * @param item The ItemStack to modify.
	 * @param key The key to set.
	 * @param value The integer value to set.
	 */
	public static void SetInt(ItemStack item, String key, Integer value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setInteger(key, value);
	}
	
	/**
	 * Sets an array of integers in the NBT data of an ItemStack.
	 * @param item The ItemStack to modify.
	 * @param key The key to set.
	 * @param value The integer array value to set.
	 */
	public static void SetIntArray(ItemStack item, String key, int[] value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setIntArray(key, value);
	}
	
	/**
	 * Sets a float value in the NBT data of an ItemStack.
	 * @param item The ItemStack to modify.
	 * @param key The key to set.
	 * @param value The float value to set.
	 */
	public static void SetFloat(ItemStack item, String key, Float value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setFloat(key, value);
	}
	
	/**
	 * Sets a double value in the NBT data of an ItemStack.
	 * @param item The ItemStack to modify.
	 * @param key The key to set.
	 * @param value The double value to set.
	 */
	public static void SetDouble(ItemStack item, String key, Double value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setDouble(key, value);
	}
	
	/**
	 * Sets a boolean value in the NBT data of an ItemStack.
	 * @param item The ItemStack to modify.
	 * @param key The key to set.
	 * @param value The boolean value to set.
	 */
	public static void SetBool(ItemStack item, String key, boolean value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setBoolean(key, value);
	}
	
	/**
	 * Sets a UUID value in the NBT data of an ItemStack.
	 * @param item The ItemStack to modify.
	 * @param key The key to set.
	 * @param value The UUID value to set.
	 */
	public static void SetUUID(ItemStack item, String key, UUID value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setUUID(key, value);
	}
	
	// GET
	
	/**
	 * Gets a string value from the NBT data of an ItemStack.
	 * @param item The ItemStack to retrieve from.
	 * @param key The key to retrieve.
	 * @return The string value or null if not present.
	 */
	public static String GetString(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getString(key);
	}
	
	/**
	 * Gets a List(String) value from the NBT data of an ItemStack.
	 * @param item The ItemStack to retrieve from.
	 * @param key The key to retrieve.
	 * @return The List(String) value or null if not present.
	 */
	public static List<String> GetStringList(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		List<String> list = new ArrayList<String>();
		try {
			String[] split = nbt.getString(key).split("◊");
			for(String s : split) {
				list.add(s);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Gets an integer value from the NBT data of an ItemStack.
	 * @param item The ItemStack to retrieve from.
	 * @param key The key to retrieve.
	 * @return The integer value or null if not present.
	 */
	public static Integer GetInt(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getInteger(key);
	}
	
	/**
	 * Gets an array of integers from the NBT data of an ItemStack.
	 * @param item The ItemStack to retrieve from.
	 * @param key The key to retrieve.
	 * @return The integer array or null if not present.
	 */
	public static int[] GetIntArray(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getIntArray(key);
	}
	
	/**
	 * Gets a float value from the NBT data of an ItemStack.
	 * @param item The ItemStack to retrieve from.
	 * @param key The key to retrieve.
	 * @return The float value or null if not present.
	 */
	public static Float GetFloat(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getFloat(key);
	}
	
	/**
	 * Gets a double value from the NBT data of an ItemStack.
	 * @param item The ItemStack to retrieve from.
	 * @param key The key to retrieve.
	 * @return The double value or null if not present.
	 */
	public static Double GetDouble(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getDouble(key);
	}
	
	/**
	 * Gets a boolean value from the NBT data of an ItemStack.
	 * @param item The ItemStack to retrieve from.
	 * @param key The key to retrieve.
	 * @return The boolean value or null if not present.
	 */
	public static boolean GetBool(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getBoolean(key);
	}
	
	/**
	 * Gets a UUID value from the NBT data of an ItemStack.
	 * @param item The ItemStack to retrieve from.
	 * @param key The key to retrieve.
	 * @return The UUID value or null if not present.
	 */
	public static UUID GetUUID(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getUUID(key);
	}
	
	//HAS
	
	/**
	 * Checks if a key exists in the NBT data of an ItemStack.
	 * @param item The ItemStack to check.
	 * @param key The key to check for.
	 * @return true if the key is present, false otherwise.
	 */
	public static boolean Has(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.hasTag(key);
	}
}
