package valorless.valorlessutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import valorless.annotations.MarkedForRemoval;
import valorless.color.Lang;
import valorless.cache.PlayerCache;
import valorless.cache.SkinCache;
import valorless.valorlessutils.Server.Version;
import valorless.valorlessutils.config.Config;
import valorless.valorlessutils.havenbags.HavenBagsPlacementBlocker;

/**
 * Main plugin class for ValorlessUtils.
 * <p>
 * Provides utility methods for server interaction, debugging, item manipulation,
 * logging, and PersistentDataContainer handling. Also contains deprecated
 * legacy methods for backward compatibility.
 * </p>
 */
public final class ValorlessUtils extends JavaPlugin implements Listener {
    /** Instance of this plugin for static access. */
    public static JavaPlugin thisPlugin;
    public static JavaPlugin plugin;

    /** Language handler instance for localization and message parsing. */
    private static Lang lang;
    

    /** Map to store plugin-specific configurations. */
    private static final HashMap<JavaPlugin, Config> pluginConfigs = new HashMap<>();

    /** Prefix used for plugin messages. */
    String Name = "§7[§6Valorless§bUtils§7]§r";

    /** Detected server version. */
    private static Version version;

    /** Plugin configuration handler. */
    public static Config config;

    /**
     * Gives all online players a custom enchanted "bread" item.
     */
    public static void bread() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack bread = new ItemStack(Material.BREAD, 1);
            ItemMeta meta = bread.getItemMeta();
            meta.setDisplayName("bread");
            List<String> lore = new ArrayList<>();
            lore.add("bread");
            meta.setLore(lore);
            meta.addEnchant(Enchantment.MENDING, 255, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            bread.setItemMeta(meta);

            player.getInventory().addItem(bread);
            player.sendMessage("bread");
        }
    }

    /** Returns the language handler instance for this plugin.
     *
     * @return The {@link Lang} instance used for localization.
     */
    public static Lang getLang() {
        return lang;
    }

    /**
     * Called when the plugin is being loaded.
     * <p>
     * Initializes plugin instances and resolves the server version.
     * </p>
     */
    public void onLoad() {
        thisPlugin = this;
        plugin = this;
        version = Server.ResolveVersion();

        lang = new Lang(plugin);
    }

    /**
     * Returns the resolved server version.
     *
     * @return The current {@link Version} of the server.
     */
    public static Version getServerVersion() {
        return version;
    }

    /**
     * Returns the resolved server version as a string without the "v" prefix.
     *
     * @return Server version as a string.
     */
    public static String getServerVersionString() {
        return version.toString().replace("v", "");
    }

    @Override
    public void onEnable() {
        // Initializing metrics for bStats
        int pluginId = 18794;
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, pluginId);

        // Load configuration
        config = new Config(this, "config.yml");
        config.addValidationEntry("debug", false);

        if (config.getBool("debug")) {
            Log.Info(this, "Debugging enabled.");
            Log.Info(this, "§aEnabled!");
        }

        // Register commands
        AddCommand("valorlessutils", "vu");

        // Initialize HavenBags placement blocker
        HavenBagsPlacementBlocker.init();

        PlayerCache.init();
        SkinCache.init();
        
        BukkitRunnable configReloadTask = new BukkitRunnable() {
    		
    		// This runs every second.
    		@Override
    		public void run() {        
    			for(Config config : pluginConfigs.values()) {
					config.reload();
    			}
    		}

    	};
    	
    	configReloadTask.runTaskTimer(this, 0L, 100L);
    }

    @Override
    public void onDisable() {
        if (config.getBool("debug")) {
            Log.Info(this, "§cDisabled!");
        }
    }

    /**
     * Returns the instance of the ValorlessUtils plugin.
     *
     * @return The plugin instance.
     */
    public static ValorlessUtils GetInstance() {
        return (ValorlessUtils) plugin;
    }

    /**
     * Registers a command and optional aliases to this plugin.
     *
     * @param command The main command.
     * @param alias   Optional command aliases.
     */
    public void AddCommand(String command, String... alias) {
        getCommand(command).setExecutor(this);
        if (!valorless.valorlessutils.utils.Utils.IsStringNullOrEmpty(alias[0])) {
            getCommand(alias[0]).setExecutor(this);
        }
    }
    
    /**
     * Logging utility methods.
     * @deprecated Replaced by {@link valorless.valorlessutils.logging.Log}.
     */
    @MarkedForRemoval("Replaced by valorless.valorlessutils.logging.Log") @Deprecated
    public static class Log {

        /**
         * Logs an informational message.
         *
         * @param caller Plugin sending the log.
         * @param msg    Message to log.
         */
        @MarkedForRemoval("Replaced by valorless.valorlessutils.logging.Log.info()") @Deprecated
        public static void Info(JavaPlugin caller, String msg) {
            Logger.getLogger("Minecraft").log(Level.INFO, "[" + caller.getName() + "] " + msg);
        }

        /**
         * Logs a warning message.
         *
         * @param caller Plugin sending the log.
         * @param msg    Message to log.
         */
        @MarkedForRemoval("Replaced by valorless.valorlessutils.logging.Log.warning()") @Deprecated
        public static void Warning(JavaPlugin caller, String msg) {
            Logger.getLogger("Minecraft").log(Level.WARNING, "[" + caller.getName() + "] " + msg);
        }

        /**
         * Logs an error message.
         *
         * @param caller Plugin sending the log.
         * @param msg    Message to log.
         */
        @MarkedForRemoval("Replaced by valorless.valorlessutils.logging.Log.error()") @Deprecated
        public static void Error(JavaPlugin caller, String msg) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, "[" + caller.getName() + "] " + msg);
        }

        /**
         * Logs a debug message if debug mode is enabled.
         *
         * @param caller Plugin sending the log.
         * @param msg    Debug message to log.
         */
        @MarkedForRemoval("Replaced by valorless.valorlessutils.logging.Log.debug()") @Deprecated
        public static void Debug(JavaPlugin caller, String msg) {
        	if(!pluginConfigs.containsKey(caller)) {
        		try {
					pluginConfigs.put(caller, new Config(caller, "config.yml"));
				} catch (Exception e) {
					//Logger.getLogger("Minecraft").log(Level.SEVERE, "[DEBUG][" + caller.getName() + "] Failed to load config for debug logging.", e);
					return;
				}
        	}
        	Config config = pluginConfigs.get(caller);
        	if (config.getBool("debug")) {
                Logger.getLogger("Minecraft").log(Level.WARNING, "[DEBUG][" + caller.getName() + "] " + msg);
            }
        }
    }
}
