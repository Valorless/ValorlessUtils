package valorless.valorlessutils;

import org.bukkit.Bukkit;

import valorless.annotations.MarkedForRemoval;
import valorless.valorlessutils.color.Lang;
import valorless.valorlessutils.color.McToAnsi;
import valorless.valorlessutils.logging.Log;

import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling and comparing Minecraft server versions.
 * <p>
 * Provides methods to resolve the current server version, compare versions,
 * and check version relationships (higher/equal). Versions are represented
 * as the {@link Version} enum.
 * </p>
 * Example versions: 1_19_4, 1_21, etc.
 */
public class Server {

    /**
     * Enum representing supported Minecraft server versions.
     * <p>
     * Versions are ordered according to release progression.
     * Can be used for comparisons via {@link #VersionCompare(Version, Version)}.
     * </p>
     */
    public enum Version { 
        NULL, 
        v1_17, v1_17_1, v1_18, v1_18_1, v1_18_2, 
        v1_19, v1_19_1, v1_19_2, v1_19_3, v1_19_4, 
        v1_20, v1_20_1, v1_20_2, v1_20_3, v1_20_4, v1_20_5, v1_20_6, 
        v1_21, v1_21_1, v1_21_2, v1_21_3, v1_21_4, v1_21_5, v1_21_6, v1_21_7, v1_21_8, v1_21_9, v1_21_10, v1_21_11,
        v26_1, v26_1_1, v26_1_2, v26_1_3, v26_1_4, v26_2, v26_3, v26_4,
        v27_1, v27_2, v27_3, v27_4,
		v28_1, v28_2, v28_3, v28_4,
		v29_1, v29_2, v29_3, v29_4,
		v30_1, v30_2, v30_3, v30_4
    }
    
    /**
     * Compares two {@link Version} enums based on their ordinal order.
     * <p>
     * Useful for determining if one version comes before, after, or is equal to another.
     * </p>
     *
     * @param version   The version to compare.
     * @param compareTo The version to compare against.
     * @return          -1 if {@code version} is lower than {@code compareTo}, 
     *                  1 if {@code version} is higher, or 0 if equal.
     */
    @Deprecated @MarkedForRemoval
    public static int VersionCompare(Version version, Version compareTo) {
        if (version.ordinal() < compareTo.ordinal()) {
            return -1; // version is less than compareTo
        } else if (version.ordinal() > compareTo.ordinal()) {
            return 1;  // version is greater than compareTo
        } else {
            return 0;  // version is equal to compareTo
        }
    }

    /**
     * Compares two {@link Version} enums based on their ordinal order.
     * <p>
     * Useful for determining if one version comes before, after, or is equal to another.
     * </p>
     *
     * @param version   The version to compare.
     * @param compareTo The version to compare against.
     * @return          -1 if {@code version} is lower than {@code compareTo},
     *                  1 if {@code version} is higher, or 0 if equal.
     */
    public static int versionCompare(Version version, Version compareTo) {
        if (version.ordinal() < compareTo.ordinal()) {
            return -1; // version is less than compareTo
        } else if (version.ordinal() > compareTo.ordinal()) {
            return 1;  // version is greater than compareTo
        } else {
            return 0;  // version is equal to compareTo
        }
    }

    /**
     * Attempts to resolve the current running server version.
     * <p>
     * Uses {@link Bukkit#getBukkitVersion()} to determine the server version and
     * converts it to the corresponding {@link Version} enum value.
     * </p>
     *
     * @return The resolved {@link Version}, or {@link Version#NULL} if the resolution fails.
     */
    @Deprecated @MarkedForRemoval
    public static Version ResolveVersion() {
        return resolveVersion();
    }
    
    /**
     * Attempts to resolve the current running server version.
     * <p>
     * Uses {@link Bukkit#getBukkitVersion()} to determine the server version and
     * converts it to the corresponding {@link Version} enum value.
     * </p>
     *
     * @return The resolved {@link Version}, or {@link Version#NULL} if the resolution fails.
     */
    public static Version resolveVersion() {
        try {
            // Log the Bukkit version for debugging
            Logger.getLogger("Minecraft").log(Level.INFO, "[" + ValorlessUtils.plugin.getName() + "] " + Bukkit.getVersion());
            Logger.getLogger("Minecraft").log(Level.INFO, "[" + ValorlessUtils.plugin.getName() + "] " + Bukkit.getBukkitVersion());

            String[] split = Bukkit.getBukkitVersion().split("\\.");
            //Logger.getLogger("Minecraft").log(Level.SEVERE, "[" + ValorlessUtils.plugin.getName() + "] " + String.join(", ", split));

            String i = split[0];
            if(Integer.parseInt(i) == 1) {
                // Parse version string (e.g., "1.19.4-R0.1-SNAPSHOT") into enum format
                String v = Bukkit.getBukkitVersion().split("-")[0];
                return Version.valueOf("v" + v.replace(".", "_"));
            }else if (Integer.parseInt(i) >= 26) {
                // Parse version string (e.g., "26.1.2.build.63-stable") into enum format
                int major = Integer.parseInt(split[0]);
                int minor = Integer.parseInt(split[1]);
                int hotfix = 0;
                try{
                    hotfix = Integer.parseInt(split[2]); // Incase of 26.1.build, if its not 26.1.0
                }catch (NumberFormatException e){
                    hotfix = 0;
                }
                String v = String.format("%s.%s.%s", major, minor, hotfix);
                return Version.valueOf("v" + v.replace(".", "_"));
            }else{
                String msg = McToAnsi.convert("Failed to resolve server version, some functions might not work correctly.");
                Logger.getLogger("Minecraft").log(Level.SEVERE, "[" + ValorlessUtils.plugin.getName() + "] " + msg);
                //Log.error(ValorlessUtils.plugin, "Failed to resolve server version, some functions might not work correctly.");
                return Version.NULL;
            }
        } catch (Exception e) {
            String msg = McToAnsi.convert("Failed to resolve server version, some functions might not work correctly.");
            Logger.getLogger("Minecraft").log(Level.SEVERE, "[" + ValorlessUtils.plugin.getName() + "] " + msg);
            return Version.NULL;
        }
    }

    /**
     * Checks if the current server version is higher than or equal to a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version is higher than or equal to {@code version}; {@code false} otherwise.
     */
    @Deprecated @MarkedForRemoval
    public static Boolean VersionHigherOrEqualTo(Version version) {
        return VersionCompare(ValorlessUtils.getServerVersion(), version) >= 0;
    }

    /**
     * Checks if the current server version is strictly higher than a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version is higher than {@code version}; {@code false} otherwise.
     */
    @Deprecated @MarkedForRemoval
    public static Boolean VersionHigherThan(Version version) {
        return VersionCompare(ValorlessUtils.getServerVersion(), version) > 0;
    }

    /**
     * Checks if the current server version is lower than or equal to a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version is lower than or equal to {@code version}; {@code false} otherwise.
     */
    @Deprecated @MarkedForRemoval
    public static Boolean VersionLowerOrEqualTo(Version version) {
        return VersionCompare(ValorlessUtils.getServerVersion(), version) <= 0;
    }

    /**
     * Checks if the current server version is strictly lower than a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version is lower than {@code version}; {@code false} otherwise.
     */
    @Deprecated @MarkedForRemoval
    public static Boolean VersionLowerThan(Version version) {
        return VersionCompare(ValorlessUtils.getServerVersion(), version) < 0;
    }

    /**
     * Checks if the current server version is exactly equal to a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version equals {@code version}; {@code false} otherwise.
     */
    @Deprecated @MarkedForRemoval
    public static Boolean VersionEqualTo(Version version) {
        return ValorlessUtils.getServerVersion() == version;
    }
    
    /**
     * Checks if the current server version is higher than or equal to a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version is higher than or equal to {@code version}; {@code false} otherwise.
     */
    public static Boolean versionHigherOrEqualTo(Version version) {
        return versionCompare(ValorlessUtils.getServerVersion(), version) >= 0;
    }
    
    /**
     * Checks if the current server version is strictly higher than a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version is higher than {@code version}; {@code false} otherwise.
     */
    public static Boolean versionHigherThan(Version version) {
        return versionCompare(ValorlessUtils.getServerVersion(), version) > 0;
    }
    
    /**
     * Checks if the current server version is lower than or equal to a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version is lower than or equal to {@code version}; {@code false} otherwise.
     */
    public static Boolean versionLowerOrEqualTo(Version version) {
        return versionCompare(ValorlessUtils.getServerVersion(), version) <= 0;
    }
    
    /**
     * Checks if the current server version is strictly lower than a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version is lower than {@code version}; {@code false} otherwise.
     */
    public static Boolean versionLowerThan(Version version) {
        return versionCompare(ValorlessUtils.getServerVersion(), version) < 0;
    }

    /**
     * Checks if the current server version is exactly equal to a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version equals {@code version}; {@code false} otherwise.
     */
    public static Boolean versionEqualTo(Version version) {
        return ValorlessUtils.getServerVersion() == version;
    }

    /**
     * Determines if the server has been fully loaded, hinting that the plugin
     * was most likely enabled after startup rather than on initial server start.
     * <p>
     * Uses a 60-second threshold to detect if the server has been running long
     * enough to be considered fully loaded.
     * </p>
     *
     * @return true if the server has been up longer than 60 seconds; false otherwise
     */
    public static boolean isServerLikelyLoaded() {
        return serverHasBeenUpLongerThan(60_000L);
    }

    /**
     * Checks if the server JVM has been running longer than the specified milliseconds.
     * <p>
     * Used to infer whether the server is fully loaded by checking the JVM uptime against
     * a threshold value.
     * </p>
     *
     * @param millis the threshold in milliseconds to check against
     * @return true if the server uptime exceeds the specified threshold; false otherwise
     */
    public static boolean serverHasBeenUpLongerThan(long millis) {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        return uptime > millis;
    }
}
