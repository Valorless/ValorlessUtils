package valorless.valorlessutils.nbt;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class NBT {
	
	// SET
	
	public static void SetString(ItemStack item, String key, String value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setString(key, value);
	}		
	
	public static void SetInt(ItemStack item, String key, Integer value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setInteger(key, value);
	}
	
	public static void SetIntArray(ItemStack item, String key, int[] value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setIntArray(key, value);
	}
	
	public static void SetFloat(ItemStack item, String key, Float value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setFloat(key, value);
	}
	
	public static void SetDouble(ItemStack item, String key, Double value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setDouble(key, value);
	}
	
	public static void SetBool(ItemStack item, String key, boolean value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setBoolean(key, value);
	}
	
	public static void SetUUID(ItemStack item, String key, UUID value) {
		NBTItem nbt = new NBTItem(item, true);
		nbt.setUUID(key, value);
	}
	
	// GET
	
	public static String GetString(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getString(key);
	}
	
	public static Integer GetInt(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getInteger(key);
	}
	
	public static int[] GetIntArray(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getIntArray(key);
	}
	
	public static Float GetFloat(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getFloat(key);
	}
	
	public static Double GetDouble(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getDouble(key);
	}
	
	public static boolean GetBool(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getBoolean(key);
	}
	
	public static UUID GetUUID(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.getUUID(key);
	}
	
	//HAS
	
	public static boolean Has(ItemStack item, String key) {
		NBTItem nbt = new NBTItem(item, true);
		return nbt.hasTag(key);
	}

}
