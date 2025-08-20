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
        config.AddValidationEntry("debug", false);

        if (config.GetBool("debug")) {
            Log.Info(this, "Debugging enabled.");
            Log.Info(this, "§aEnabled!");
        }

        // Register commands
        AddCommand("valorlessutils", "vu");

        // Initialize HavenBags placement blocker
        HavenBagsPlacementBlocker.init();
    }

    @Override
    public void onDisable() {
        if (config.GetBool("debug")) {
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

    // Deprecated Utility Class
    /**
     * Deprecated utility methods.
     *
     * @deprecated Use {@link valorless.valorlessutils.utils.Utils} instead.
     */
    @Deprecated
    public static class Utils {

        /**
         * Checks if a string is null, empty, blank, or only whitespace.
         *
         * @param string The string to check.
         * @return true if the string is null or empty; false otherwise.
         * @deprecated Will be removed in a future version.
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
     * Logging utility methods.
     */
    public static class Log {

        /**
         * Logs an informational message.
         *
         * @param caller Plugin sending the log.
         * @param msg    Message to log.
         */
        public static void Info(JavaPlugin caller, String msg) {
            Logger.getLogger("Minecraft").log(Level.INFO, "[" + caller.getName() + "] " + msg);
        }

        /**
         * Logs a warning message.
         *
         * @param caller Plugin sending the log.
         * @param msg    Message to log.
         */
        public static void Warning(JavaPlugin caller, String msg) {
            Logger.getLogger("Minecraft").log(Level.WARNING, "[" + caller.getName() + "] " + msg);
        }

        /**
         * Logs an error message.
         *
         * @param caller Plugin sending the log.
         * @param msg    Message to log.
         */
        public static void Error(JavaPlugin caller, String msg) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, "[" + caller.getName() + "] " + msg);
        }

        /**
         * Logs a debug message if debug mode is enabled.
         *
         * @param caller Plugin sending the log.
         * @param msg    Debug message to log.
         */
        public static void Debug(JavaPlugin caller, String msg) {
            if (Bukkit.getPluginManager().getPlugin(caller.getName()).getConfig().getBoolean("debug")) {
                Logger.getLogger("Minecraft").log(Level.WARNING, "[DEBUG][" + caller.getName() + "] " + msg);
            }
        }
    }

    /**
     * Deprecated tags utility class for PersistentDataContainer operations.
     *
     * @deprecated Replaced with {@link valorless.valorlessutils.tags.Tags}.
     */
    @Deprecated
    public static class Tags {

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static void Set(JavaPlugin caller, PersistentDataContainer container, String key, Object value, PersistentDataType type) {
            container.set(new NamespacedKey(caller, key), type, value);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static Object Get(JavaPlugin caller, PersistentDataContainer container, String key, PersistentDataType type) {
            return container.get(new NamespacedKey(caller, key), type);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static Boolean Has(JavaPlugin caller, PersistentDataContainer container, String key, PersistentDataType type) {
            try {
                return container.has(new NamespacedKey(caller, key), type);
            } catch (Exception e) {
                return false;
            }
        }
    }
}
