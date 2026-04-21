package valorless.valorlessutils.logging;

import org.bukkit.plugin.java.JavaPlugin;
import valorless.color.McToAnsi;
import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.config.Config;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for writing plugin-prefixed messages to the Minecraft logger.
 * <p>
 * Each logging method preprocesses text by parsing language/placeholders via
 * {@link ValorlessUtils#getLang()} and converting Minecraft formatting to ANSI
 * with {@link McToAnsi#convert(String)} before delegating to
 * {@link Logger#getLogger(String)}.
 * </p>
 * <p>
 * This class provides convenience wrappers for info, warning, error, and
 * debug logging. Debug logging is controlled per plugin through
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
     * <p>
     * The message is parsed using the shared language parser and then converted
     * to ANSI before being emitted at {@link Level#INFO}.
     * </p>
     *
     * @param caller the plugin sending the log
     * @param msg the message to write
     */
    public static void info(JavaPlugin caller, String msg) {
        msg = McToAnsi.convert(ValorlessUtils.getLang().parse(msg));
        Logger.getLogger("Minecraft").log(Level.INFO, "[" + caller.getName() + "] " + msg);
    }

    /**
     * Logs a warning message for the calling plugin.
     * <p>
     * The message is parsed using the shared language parser and then converted
     * to ANSI before being emitted at {@link Level#WARNING}.
     * </p>
     *
     * @param caller the plugin sending the log
     * @param msg the message to write
     */
    public static void warning(JavaPlugin caller, String msg) {
        msg = McToAnsi.convert(ValorlessUtils.getLang().parse(msg));
        Logger.getLogger("Minecraft").log(Level.WARNING, "[" + caller.getName() + "] " + msg);
    }

    /**
     * Logs an error message for the calling plugin.
     * <p>
     * The message is parsed using the shared language parser and then converted
     * to ANSI before being emitted at {@link Level#SEVERE}.
     * </p>
     *
     * @param caller the plugin sending the log
     * @param msg the message to write
     */
    public static void error(JavaPlugin caller, String msg) {
        msg = McToAnsi.convert(ValorlessUtils.getLang().parse(msg));
        Logger.getLogger("Minecraft").log(Level.SEVERE, "[" + caller.getName() + "] " + msg);
    }

    /**
     * Logs a debug message when the plugin has {@code debug: true} in
     * {@code config.yml}.
     * <p>
     * On first use per plugin, this method loads and caches a
     * {@link Config} for {@code config.yml}. If the file cannot be loaded,
     * the method exits quietly without logging.
     * </p>
     * <p>
     * When {@code debug} is enabled, the message is parsed and converted to
     * ANSI before being emitted at {@link Level#WARNING} with a
     * {@code [DEBUG]} prefix.
     * </p>
     * <p>
     * This method uses {@link Config#getBool(String)} to evaluate the debug
     * toggle.
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
            msg = McToAnsi.convert(ValorlessUtils.getLang().parse(msg));
            Logger.getLogger("Minecraft").log(Level.WARNING, "[DEBUG][" + caller.getName() + "] " + msg);
        }
    }
}