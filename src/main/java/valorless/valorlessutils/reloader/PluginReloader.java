package valorless.valorlessutils.reloader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.ValorlessUtils.Log;

/**
 * Utility class for safely reloading plugins at runtime.
 * <p>
 * This reloader works by disabling a plugin immediately and then scheduling
 * its re-enablement after a configurable delay. Useful for development or
 * for plugins that support hot-reloading.
 * </p>
 *
 * <p><strong>Note:</strong> Reloading plugins at runtime may cause side effects
 * if the plugin is not designed to handle being disabled and re-enabled properly.</p>
 */
public class PluginReloader {

    /**
     * Reloads a plugin by name, disabling it immediately and scheduling it
     * to be re-enabled after the given delay.
     *
     * @param pluginName The name of the plugin to reload
     * @param delayTicks The delay in ticks before re-enabling the plugin
     * @param silent Whether to suppress log messages during reload
     */
    public static void reloadPlugin(String pluginName, long delayTicks, boolean silent) {
        PluginManager pm = Bukkit.getPluginManager();
        reload(pm.getPlugin(pluginName), delayTicks, silent);
    }
    
    /**
     * Reloads a plugin from a {@link JavaPlugin} reference.
     *
     * @param plugin The plugin to reload
     * @param delayTicks The delay in ticks before re-enabling the plugin
     * @param silent Whether to suppress log messages during reload
     */
    public static void reloadPlugin(JavaPlugin plugin, long delayTicks, boolean silent) {
        reload(plugin, delayTicks, silent);
    }
    
    /**
     * Reloads a plugin from a generic {@link Plugin} reference.
     *
     * @param plugin The plugin to reload
     * @param delayTicks The delay in ticks before re-enabling the plugin
     * @param silent Whether to suppress log messages during reload
     */
    public static void reloadPlugin(Plugin plugin, long delayTicks, boolean silent) {
        reload(plugin, delayTicks, silent);
    }
    
    /**
     * Handles the actual plugin reload logic.
     * <p>
     * Disables the target plugin immediately, then schedules a re-enable
     * after the given delay. If the plugin is not found, an error is logged.
     * </p>
     *
     * @param target The plugin to reload
     * @param delay The delay in ticks before re-enabling
     * @param silent Whether to suppress log messages during reload
     */
    static void reload(Plugin target, long delay, boolean silent) {
        if (target == null) {
            // Print stack trace if the plugin reference was invalid
            new Exception("Plugin not found!").printStackTrace();
            return;
        }
    	
        PluginManager pm = Bukkit.getPluginManager();
        String pluginName = target.getName();

        // Log before disabling (if not silent)
        if (!silent) {
            Log.Info(ValorlessUtils.thisPlugin, 
                String.format("Attempting to disable %s, re-enabling in %s ticks", pluginName, delay));
        }

        // Disable immediately
        pm.disablePlugin(target);

        // Schedule re-enable after delay
        Bukkit.getScheduler().runTaskLater(ValorlessUtils.thisPlugin, () -> {
            Plugin reEnable = pm.getPlugin(pluginName); // fetch reference again
            
            if (reEnable != null) {
                if (!silent) {
                    Log.Info(ValorlessUtils.thisPlugin, 
                        String.format("Attempting to re-enable %s.", pluginName));
                }
                pm.enablePlugin(reEnable);
            } else {
                if (!silent) {
                    Log.Warning(ValorlessUtils.thisPlugin, 
                        "Plugin " + pluginName + " could not be found for re-enable.");
                }
            }
        }, delay);
    }
}
