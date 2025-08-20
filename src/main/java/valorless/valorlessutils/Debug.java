package valorless.valorlessutils;

import org.bukkit.plugin.java.JavaPlugin;
import valorless.valorlessutils.ValorlessUtils.Log;

/**
 * Utility class for debugging purposes.
 * <p>
 * Provides helper methods to assist in logging and tracking issues within plugins.
 * Currently, it allows printing the current thread's stack trace to the plugin's error log.
 * </p>
 */
public class Debug {

    /**
     * Prints the full stack trace of the current thread to the plugin's error log.
     * <p>
     * Each element of the stack trace is logged individually using {@link ValorlessUtils.Log#Error(JavaPlugin, String)}.
     * This can be useful for diagnosing the source of unexpected behavior in a plugin.
     * </p>
     *
     * @param caller The {@link JavaPlugin} instance that is invoking this method.
     *               This is used to associate the log messages with the correct plugin.
     */
    public static void PrintStackTrace(JavaPlugin caller) {
        // Iterate through each element of the current thread's stack trace
        for (StackTraceElement stack : Thread.currentThread().getStackTrace()) {
            // Log each stack trace element as an error
            Log.Error(caller, stack.toString());
        }
    }
    
}
