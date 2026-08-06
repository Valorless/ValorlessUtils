package valorless.valorlessutils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import valorless.annotations.NotNull;
import valorless.valorlessutils.cache.PlayerCache;
import valorless.valorlessutils.cache.SkinCache;
import valorless.valorlessutils.color.Lang;
import valorless.valorlessutils.config.Config;
import valorless.valorlessutils.havenbags.HavenBagsPlacementBlocker;
import valorless.valorlessutils.logging.Log;

/**
 * Standalone class for initializing the ValorlessUtils plugin in a non-standard environment.
 * <p>
 *     This class is used to set up the ValorlessUtils plugin with a provided JavaPlugin instance,
 *     allowing it to function outside of its normal plugin context.<br>
 *     I.e. Shaded
 * </p>
 */
public class Standalone {

    private static boolean loaded = false;
    private static boolean enabled = false;

    /**
     * Initializes the ValorlessUtils plugin with the provided JavaPlugin instance.
     * Sets up the language handler and resolves the server version.
     * <p>
     *     Since ValorlessUtils is normally a real plugin, a substitute is needed to run it instead.<br>
     *     This simply mimics what ValorlessUtils does on startup, to ensure all is ready.
     * </p>
     * <p>Call during onLoad()</p>
     *
     * @param substitute The JavaPlugin instance to initialize ValorlessUtils with.
     */
    public static void onLoad(@NotNull JavaPlugin substitute) {
        if(loaded) {
            Log.info(substitute, "ValorlessUtils standalone onLoad() called multiple times.");
            Debug.PrintStackTrace(substitute);
            return;
        }
        loaded = true;
        ValorlessUtils.plugin =  substitute;
        ValorlessUtils.lang = new Lang(substitute);
        Log.info(substitute, "Initializing ValorlessUtils in standalone mode.");
        ValorlessUtils.version = Server.resolveVersion();
    }


    /**
     * Loads the ValorlessUtils plugin with the provided JavaPlugin instance.
     * Sets up the various listeners and remaining tasks.
     * <p>
     *     Since ValorlessUtils is normally a real plugin, a substitute is needed to run it instead.<br>
     *     This simply mimics what ValorlessUtils does on startup, to ensure all is ready.
     * </p>
     * <p>Call during onEnable()</p>
     *
     * @param substitute The JavaPlugin instance to initialize ValorlessUtils with.
     */
    public static void onEnable(@NotNull JavaPlugin substitute) {
        if(enabled) {
            Log.info(substitute, "ValorlessUtils standalone onEnable() called multiple times.");
            Debug.PrintStackTrace(substitute);
            return;
        }
        enabled = true;
        Log.info(substitute, "Loading ValorlessUtils in standalone mode.");
        HavenBagsPlacementBlocker.init();

        PlayerCache.init();
        SkinCache.init();

        // Used purely for Log.debug, to confirm debug is enabled.
        BukkitRunnable configReloadTask = new BukkitRunnable() {
            // This runs every second.
            @Override
            public void run() {
                for(Config c : ValorlessUtils.pluginConfigs.values()) {
                    c.reload();
                }
            }
        };
        configReloadTask.runTaskTimer(substitute, 0L, 100L);
    }

}
