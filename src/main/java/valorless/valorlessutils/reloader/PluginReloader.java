package valorless.valorlessutils.reloader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.ValorlessUtils.Log;

public class PluginReloader {

    public static void reloadPlugin(String pluginName, long delayTicks) {
    	PluginManager pm = Bukkit.getPluginManager();
        reload(pm.getPlugin(pluginName), delayTicks);
    }
    
    public static void reloadPlugin(JavaPlugin plugin, long delayTicks) {
        reload(plugin, delayTicks);
    }
    
    public static void reloadPlugin(Plugin plugin, long delayTicks) {
        reload(plugin, delayTicks);
    }
    
    static void reload(Plugin target, long delay) {
    	if (target == null) {
    		new Exception("Plugin not found!").printStackTrace();
    		return;
        }
    	
    	PluginManager pm = Bukkit.getPluginManager();
    	String pluginName = target.getName();

    	Log.Info(ValorlessUtils.thisPlugin, String.format("Attempting to disable %s.\nre-enabling in %s ticks", pluginName, delay));
        // Disable immediately
        pm.disablePlugin(target);
        

        // Schedule re-enable after delay
        Bukkit.getScheduler().runTaskLater(ValorlessUtils.thisPlugin, () -> {
            Plugin reEnable = pm.getPlugin(pluginName); // get reference again
            if (reEnable != null) {
            	Log.Info(ValorlessUtils.thisPlugin, String.format("Attempting to re-enable %s.", pluginName));
                pm.enablePlugin(reEnable);
            } else {
            	Log.Warning(ValorlessUtils.thisPlugin, "Plugin " + pluginName + " could not be found for re-enable.");
            }
        }, delay);
    }
}