package valorless.valorlessutils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import valorless.valorlessutils.config.Config;
import valorless.valorlessutils.crafting.CraftRecipe;
import valorless.valorlessutils.crafting.CraftRecipe.RecipeType;
import valorless.valorlessutils.crafting.Ingredient;
import valorless.valorlessutils.types.Vector;
import valorless.valorlessutils.types.Vector3;

public final class ValorlessUtils extends JavaPlugin implements Listener {
	public enum Version { NULL, v1_17, v1_17_1, v1_18, v1_18_1, v1_18_2, v1_19, v1_19_1, v1_19_2, v1_19_3, v1_19_4, v1_20, v1_20_1, v1_20_3, v1_20_4, v1_20_5, v1_20_6, v1_21 }
    public static JavaPlugin thisPlugin;
    String Name = "§7[§6Valorless§bUtils§7]§r";
	
	public static class Server {
		public static Version version;
	}

    public static Config config;
    
    /**
     * bread
     */
    public static void bread() {
    	for (Player player : Bukkit.getOnlinePlayers()) {
    		ItemStack bread = new ItemStack(Material.BREAD, 1);
    		ItemMeta meta = bread.getItemMeta();
    		meta.setDisplayName("bread");
    		List<String> lore = new ArrayList<String>();
    		lore.add("bread");
    		meta.setLore(lore);
		    meta.addEnchant(Enchantment.MENDING, 255, true);
		    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		    bread.setItemMeta(meta);
            player.getInventory().addItem(bread);
            player.sendMessage("bread");
        }
    }

    /**
     * Called when the plugin is being loaded.
     */
    public void onLoad() {
        thisPlugin = this;
		Log.Debug(this, Bukkit.getVersion());
		Log.Debug(this, Bukkit.getBukkitVersion());
		ResolveVersion();
    }

    @Override
    public void onEnable() {
        // Initializing metrics for bStats
        int pluginId = 18794;
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, pluginId);

        // Loading and validating the configuration file
        config = new Config(this, "config.yml");
        config.AddValidationEntry("debug", false);

        // Logging debug information if enabled
        if (config.GetBool("debug")) {
            Log.Info(this, "Debugging enabled.");
        }

        // Logging plugin enable message
        if (config.GetBool("debug")) {
            Log.Info(this, "§aEnabled!");
        }

        // Registering commands
        AddCommand("valorlessutils", "vu");
        
        //Testing Function
        //VectorTest();
        //RecipeTest();
    }
    
    void VectorTest() {
    	Vector3<Integer> v = new Vector3<Integer>(1,35,44);
    	Log.Warning(this, v.x.toString());
    	Log.Warning(this, v.y.toString());
    	v.Set(Vector.X, 4);
    	Log.Warning(this, v.x.toString());
    	v.x = 76;
    	Log.Warning(this, v.x.toString());
    	Integer e = v.x + 5;
    	Log.Warning(this, e.toString());
    }
    
    void RecipeTest() {
    	List<String> shape = new ArrayList<String>();
    	shape.add("XXX");
    	shape.add("XDX");
    	shape.add("XXX");
    	
    	List<Ingredient> ingredients = new ArrayList<Ingredient>();
    	ingredients.add(new Ingredient("D", Material.DIRT));
    	
    	CraftRecipe recipe = new CraftRecipe(
    			this, 
    			"TestRecipe1", 
    			RecipeType.Shaped, 
    			ingredients, 
    			new ItemStack(Material.GRASS_BLOCK), 
    			shape);
    	recipe.SetPermission("Test.Permission");
    	recipe.Add();
    	
    }

    /**
     * Called when the plugin is being disabled.
     */
    @Override
    public void onDisable() {
        // Logging plugin disable message
        if (config.GetBool("debug")) {
            Log.Info(this, "§cDisabled!");
        }
        
    }

    /**
     * Gets the instance of the ValorlessUtils plugin.
     * @return The instance of the plugin.
     */
    public static ValorlessUtils GetInstance() {
        return (ValorlessUtils) thisPlugin;
    }

    /**
     * Adds a command executor for the specified command and aliases.
     * @param command The main command.
     * @param alias Additional command aliases.
     */
    public void AddCommand(String command, String... alias) {
        getCommand(command).setExecutor(this);
        //Once deprecated Utils is removed, this'll be renamed to just Utils.
        if (!valorless.valorlessutils.utils.Utils.IsStringNullOrEmpty(alias[0])) { 
            getCommand(alias[0]).setExecutor(this);
        }
    }
    
    void ResolveVersion() {
    	try {
    		String v = Bukkit.getBukkitVersion().split("-")[0];
    		Server.version = Version.valueOf("v" + v.replace(".", "_"));
    		//Log.Debug(plugin, server.toString());
    	} catch (Exception e) {
    		Server.version = Version.NULL;
    		Log.Error(this, "Failed to resolve server version, some functions might not work correctly.");
    	}
    }

    // Functions

    /**
     * Deprecated utility class. Use 'valorless.valorlessutils.utils.Utils' instead.
     * @deprecated Will be removed at a later date.
     */
    @Deprecated
    public static class Utils {

        /**
         * Deprecated utility class. Use 'valorless.valorlessutils.utils.Utils' instead.<br>
         * Checks if a string is null or empty.
         * @param string The string to check.
         * @return true if the string is null or empty, false otherwise.
         * @deprecated Will be removed at a later date.
         */
    	@Deprecated
        public static boolean IsStringNullOrEmpty(String string) {
            if (string == null) return true;
            else if (string.isEmpty()) return true;
            else if (string.isBlank()) return true;
            else if (string.trim().isEmpty()) return true;
            else return false;
        }
    }

    /**
     * Logging utility class for plugin messages.
     */
    public static class Log {

        /**
         * Logs an information message.
         * @param caller The calling plugin.
         * @param msg The message to log.
         */
        public static void Info(JavaPlugin caller, String msg) {
            Logger.getLogger("Minecraft").log(Level.INFO, "[" + caller.getName() + "] " + msg);
        }

        /**
         * Logs a warning message.
         * @param caller The calling plugin.
         * @param msg The message to log.
         */
        public static void Warning(JavaPlugin caller, String msg) {
            Logger.getLogger("Minecraft").log(Level.WARNING, "[" + caller.getName() + "] " + msg);
        }

        /**
         * Logs an error message.
         * @param caller The calling plugin.
         * @param msg The message to log.
         */
        public static void Error(JavaPlugin caller, String msg) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, "[" + caller.getName() + "] " + msg);
        }

        /**
         * Logs a debug message if debugging is enabled.
         * @param caller The calling plugin.
         * @param msg The debug message to log.
         */
        public static void Debug(JavaPlugin caller, String msg) {
            if (Bukkit.getPluginManager().getPlugin(caller.getName()).getConfig().getBoolean("debug") == true) {
                Logger.getLogger("Minecraft").log(Level.WARNING, "[DEBUG][" + caller.getName() + "] " + msg);
            }
        }
    }

    /**
     * Tags utility class for working with PersistentDataContainers.<br>
     * Deprecated utility class, will be moved to their own file, at a later date.
     * @deprecated Will be removed at a later date.
     */
    @Deprecated
    public static class Tags {

        /**
         * Sets a value in the PersistentDataContainer using a specific key.<br>
         * Deprecated utility class, will be moved to their own file, at a later date.
         * @param caller The calling plugin.
         * @param container The PersistentDataContainer to modify.
         * @param key The key to set.
         * @param value The value to set.
         * @param type The PersistentDataType.
     	 * @deprecated Will be removed at a later date.
     	*/
    	@Deprecated
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static void Set(JavaPlugin caller, PersistentDataContainer container, String key, Object value, PersistentDataType type) {
            container.set(new NamespacedKey(caller, key), type, value);
        }

        /**
         * Gets a value from the PersistentDataContainer using a specific key.<br>
         * Deprecated utility class, will be moved to their own file, at a later date.
         * @param caller The calling plugin.
         * @param container The PersistentDataContainer to retrieve from.
         * @param key The key to retrieve.
         * @param type The PersistentDataType.
         * @return The retrieved value.
     	 * @deprecated Will be removed at a later date.
     	*/
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static Object Get(JavaPlugin caller, PersistentDataContainer container, String key, PersistentDataType type) {
            return container.get(new NamespacedKey(caller, key), type);
        }

        /**
         * Checks if a key exists in the PersistentDataContainer.<br>
         * Deprecated utility class, will be moved to their own file, at a later date.
         * @param caller The calling plugin.
         * @param container The PersistentDataContainer to check.
         * @param key The key to check.
         * @param type The PersistentDataType.
         * @return true if the key exists, false otherwise.
     	 * @deprecated Will be removed at a later date.
     	*/
        @SuppressWarnings({ "unchecked", "rawtypes", "unused" })
        public static Boolean Has(JavaPlugin caller, PersistentDataContainer container, String key, PersistentDataType type) {
            boolean result = false;
            try {
                return container.has(new NamespacedKey(caller, key), type);
            } catch (Exception e) {

            }
            return false;
        }
    }
}
