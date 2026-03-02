package valorless.valorlessutils.reloader;

import org.bukkit.Bukkit;
import org.bukkit.block.data.type.Door;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
    public static void reloadPlugin(Plugin target, long delay, boolean silent) {
    	reloadPlugin(target, delay, silent, null);
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
     * @param sender CommandSender to send status messages to during the reload process
     */
    public static void reloadPlugin(Plugin target, long delay, boolean silent, CommandSender sender) {
        if (target == null) {
            // Print stack trace if the plugin reference was invalid
            new Exception("Plugin not found!").printStackTrace();
            return;
        }
    	
        PluginManager pm = Bukkit.getPluginManager();
        String pluginName = target.getName();
        
        Plugin found = pm.getPlugin(pluginName);

        // Log before disabling (if not silent)
        if (!silent) {
            Log.Info(ValorlessUtils.thisPlugin, 
                String.format("Attempting to disable %s, re-enabling in %s ticks", pluginName, delay));
            if(sender != null) sender.sendMessage("§fPlugin " + pluginName + " is being reloaded, please wait...");
        }

        // Disable immediately
        pm.disablePlugin(found);
        
        new BukkitRunnable() {
            @Override
            public void run() {
            	if(pm.isPluginEnabled(found)) return;
            	if (found != null) {
                    if (!silent) {
                        Log.Info(ValorlessUtils.thisPlugin, 
                            String.format("Attempting to re-enable %s.", pluginName));
                    }
                    pm.enablePlugin(found);
                } else {
                    if (!silent) {
                        Log.Warning(ValorlessUtils.thisPlugin, 
                            "Plugin " + pluginName + " could not be found for re-enable.");
                    }
                }
            	if (!silent && sender != null) sender.sendMessage("§aPlugin " + pluginName + " has been reloaded.");
            	this.cancel(); // Stop the task after attempting to re-enable
            }
        }.runTaskTimerAsynchronously(ValorlessUtils.thisPlugin, delay, 20L); // Check every second (20 ticks) until re-enabled
    }
}
