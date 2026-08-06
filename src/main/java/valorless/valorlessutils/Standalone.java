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

    private static boolean running = false;

    /**
     * Initializes the ValorlessUtils plugin with the provided JavaPlugin instance.
     * Sets up the language handler and resolves the server version.
     * <p>
     *     Since ValorlessUtils is normally a real plugin, a substitute is needed to run it instead.<br>
     *     This simply mimics what ValorlessUtils does on startup, to ensure all is ready.
     * </p>
     *
     * @param substitute The JavaPlugin instance to initialize ValorlessUtils with.
     */
    public static void init(@NotNull JavaPlugin substitute) {
        if(running) {
            Log.info(substitute, "ValorlessUtils standalone init() called multiple times.");
            Debug.PrintStackTrace(substitute);
            return;
        }
        running = true;

        Log.info(substitute, "Initializing ValorlessUtils in standalone mode.");
        ValorlessUtils.plugin =  substitute;
        ValorlessUtils.lang = new Lang(substitute);
        ValorlessUtils.version = Server.resolveVersion();

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
