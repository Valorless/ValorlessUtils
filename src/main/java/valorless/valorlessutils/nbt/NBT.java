package valorless.valorlessutils.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

public class NBT {
    
    // ------------------------
    // SET METHODS
    // ------------------------

    /**
     * Sets a string value in the NBT data of an ItemStack.
     * @param item The ItemStack to modify.
     * @param key The key to set.
     * @param value The string value to set.
     */
    public static void SetString(ItemStack item, String key, String value) {
        de.tr7zw.changeme.nbtapi.NBT.modify(item, nbt -> {
            nbt.setString(key, value);
        });
    }

    /**
     * Sets a List of strings in the NBT data of an ItemStack.
     * The list is stored as a single string joined by "◊".
     * @param item The ItemStack to modify.
     * @param key The key to set.
     * @param value The List<String> value to set.
     */
    public static void SetStringList(ItemStack item, String key, List<String> value) {
        de.tr7zw.changeme.nbtapi.NBT.modify(item, nbt -> {
            String m_value = "";
            for(int i = 0; i < value.size(); i++) {
                if(i != 0) {
                    m_value = m_value + "◊" + value.get(i);
                }else {
                    m_value = value.get(i);
                }
            }
            nbt.setString(key, m_value);
        });
    }

    /**
     * Sets an integer value in the NBT data of an ItemStack.
     * @param item The ItemStack to modify.
     * @param key The key to set.
     * @param value The integer value to set.
     */
    public static void SetInt(ItemStack item, String key, Integer value) {
        de.tr7zw.changeme.nbtapi.NBT.modify(item, nbt -> {
            nbt.setInteger(key, value);
        });
    }

    /**
     * Sets an integer array in the NBT data of an ItemStack.
     * @param item The ItemStack to modify.
     * @param key The key to set.
     * @param value The integer array to set.
     */
    public static void SetIntArray(ItemStack item, String key, int[] value) {
        de.tr7zw.changeme.nbtapi.NBT.modify(item, nbt -> {
            nbt.setIntArray(key, value);
        });
    }

    /**
     * Sets a float value in the NBT data of an ItemStack.
     * @param item The ItemStack to modify.
     * @param key The key to set.
     * @param value The float value to set.
     */
    public static void SetFloat(ItemStack item, String key, Float value) {
        de.tr7zw.changeme.nbtapi.NBT.modify(item, nbt -> {
            nbt.setFloat(key, value);
        });
    }

    /**
     * Sets a double value in the NBT data of an ItemStack.
     * @param item The ItemStack to modify.
     * @param key The key to set.
     * @param value The double value to set.
     */
    public static void SetDouble(ItemStack item, String key, Double value) {
        de.tr7zw.changeme.nbtapi.NBT.modify(item, nbt -> {
            nbt.setDouble(key, value);
        });
    }

    /**
     * Sets a boolean value in the NBT data of an ItemStack.
     * @param item The ItemStack to modify.
     * @param key The key to set.
     * @param value The boolean value to set.
     */
    public static void SetBool(ItemStack item, String key, boolean value) {
        de.tr7zw.changeme.nbtapi.NBT.modify(item, nbt -> {
            nbt.setBoolean(key, value);
        });
    }

    /**
     * Sets a UUID value in the NBT data of an ItemStack.
     * @param item The ItemStack to modify.
     * @param key The key to set.
     * @param value The UUID value to set.
     */
    public static void SetUUID(ItemStack item, String key, UUID value) {
        de.tr7zw.changeme.nbtapi.NBT.modify(item, nbt -> {
            nbt.setUUID(key, value);
        });
    }

    // ------------------------
    // GET METHODS
    // ------------------------

    /**
     * Retrieves a string value from the NBT data of an ItemStack.
     * @param item The ItemStack to retrieve from.
     * @param key The key to retrieve.
     * @return The string value or null if not present.
     */
    public static String GetString(ItemStack item, String key) {
        return de.tr7zw.changeme.nbtapi.NBT.readNbt(item).getString(key);
    }

    /**
     * Retrieves a List<String> value from the NBT data of an ItemStack.
     * Splits the stored string by "◊".
     * @param item The ItemStack to retrieve from.
     * @param key The key to retrieve.
     * @return The List<String> value.
     */
    public static List<String> GetStringList(ItemStack item, String key) {
        String nbt = de.tr7zw.changeme.nbtapi.NBT.readNbt(item).getString(key);
        List<String> list = new ArrayList<String>();
        try {
            String[] split = nbt.split("◊");
            for(String s : split) {
                list.add(s);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves an integer value from the NBT data of an ItemStack.
     * @param item The ItemStack to retrieve from.
     * @param key The key to retrieve.
     * @return The integer value.
     */
    public static Integer GetInt(ItemStack item, String key) {
        return de.tr7zw.changeme.nbtapi.NBT.readNbt(item).getInteger(key);
    }

    /**
     * Retrieves an integer array from the NBT data of an ItemStack.
     * @param item The ItemStack to retrieve from.
     * @param key The key to retrieve.
     * @return The list of integers.
     */
    public static List<Integer> GetIntArray(ItemStack item, String key) {
        return de.tr7zw.changeme.nbtapi.NBT.readNbt(item).getIntegerList(key).toListCopy();
    }

    /**
     * Retrieves a float value from the NBT data of an ItemStack.
     * @param item The ItemStack to retrieve from.
     * @param key The key to retrieve.
     * @return The float value.
     */
    public static Float GetFloat(ItemStack item, String key) {
        return de.tr7zw.changeme.nbtapi.NBT.readNbt(item).getFloat(key);
    }

    /**
     * Retrieves a double value from the NBT data of an ItemStack.
     * @param item The ItemStack to retrieve from.
     * @param key The key to retrieve.
     * @return The double value.
     */
    public static Double GetDouble(ItemStack item, String key) {
        return de.tr7zw.changeme.nbtapi.NBT.readNbt(item).getDouble(key);
    }

    /**
     * Retrieves a boolean value from the NBT data of an ItemStack.
     * @param item The ItemStack to retrieve from.
     * @param key The key to retrieve.
     * @return The boolean value.
     */
    public static boolean GetBool(ItemStack item, String key) {
        return de.tr7zw.changeme.nbtapi.NBT.readNbt(item).getBoolean(key);
    }

    /**
     * Retrieves a UUID value from the NBT data of an ItemStack.
     * @param item The ItemStack to retrieve from.
     * @param key The key to retrieve.
     * @return The UUID value.
     */
    public static UUID GetUUID(ItemStack item, String key) {
        return de.tr7zw.changeme.nbtapi.NBT.readNbt(item).getUUID(key);
    }

    // ------------------------
    // HAS / REMOVE METHODS
    // ------------------------

    /**
     * Checks if the NBT data of an ItemStack contains a given key.
     * @param item The ItemStack to check.
     * @param key The key to check.
     * @return True if the key exists, false otherwise.
     */
    public static boolean Has(ItemStack item, String key) {
        return de.tr7zw.changeme.nbtapi.NBT.readNbt(item).hasTag(key);
    }

    /**
     * Removes a key from the NBT data of an ItemStack.
     * @param item The ItemStack to modify.
     * @param key The key to remove.
     * @return False (always returns false currently; actual removal happens inside the modify call).
     */
    public static boolean Remove(ItemStack item, String key) {
        de.tr7zw.changeme.nbtapi.NBT.modify(item, nbt -> {
            nbt.removeKey(key);
            return true;
        });
        return false;
    }
}
