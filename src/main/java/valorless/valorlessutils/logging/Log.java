package valorless.valorlessutils.logging;

import org.bukkit.plugin.java.JavaPlugin;
import valorless.valorlessutils.config.Config;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for writing plugin-prefixed messages to the Minecraft logger.
 * <p>
 * This class provides convenience wrappers for info, warning, error, and
 * debug logging. Debug logging is controlled per plugin through the plugin's
 * {@code config.yml} using the {@code debug} boolean key.
 */
public class Log {

    /**
     * Cached plugin configuration references used to read the {@code debug}
     * setting without recreating a {@link Config} instance on every call.
     */
    private static final HashMap<JavaPlugin, Config> pluginConfigs = new HashMap<>();

    /**
     * Logs an informational message for the calling plugin.
     *
     * @param caller the plugin sending the log
     * @param msg the message to write
     */
    public static void info(JavaPlugin caller, String msg) {
        Logger.getLogger("Minecraft").log(Level.INFO, "[" + caller.getName() + "] " + msg);
    }

    /**
     * Logs a warning message for the calling plugin.
     *
     * @param caller the plugin sending the log
     * @param msg the message to write
     */
    public static void warning(JavaPlugin caller, String msg) {
        Logger.getLogger("Minecraft").log(Level.WARNING, "[" + caller.getName() + "] " + msg);
    }

    /**
     * Logs an error message for the calling plugin.
     *
     * @param caller the plugin sending the log
     * @param msg the message to write
     */
    public static void error(JavaPlugin caller, String msg) {
        Logger.getLogger("Minecraft").log(Level.SEVERE, "[" + caller.getName() + "] " + msg);
    }

    /**
     * Logs a debug message when the plugin has {@code debug: true} in
     * {@code config.yml}.
     * <p>
     * If the plugin configuration cannot be loaded, this method exits quietly
     * and does not write a message.
     *
     * @param caller the plugin sending the log
     * @param msg the debug message to write
     */
    public static void debug(JavaPlugin caller, String msg) {
        if(!pluginConfigs.containsKey(caller)) {
            try {
                pluginConfigs.put(caller, new Config(caller, "config.yml"));
            } catch (Exception e) {
                return;
            }
        }
        Config config = pluginConfigs.get(caller);
        if (config.getBool("debug")) {
            Logger.getLogger("Minecraft").log(Level.WARNING, "[DEBUG][" + caller.getName() + "] " + msg);
        }
    }
}