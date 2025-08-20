package valorless.valorlessutils;

import org.bukkit.Bukkit;
import valorless.valorlessutils.ValorlessUtils.Log;

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
        v1_20, v1_20_1, v1_20_3, v1_20_4, v1_20_5, v1_20_6, 
        v1_21, v1_21_1, v1_21_2, v1_21_3, v1_21_4, v1_21_5, v1_21_6, v1_21_7, v1_21_8, v1_21_9, 
        v1_22
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
     * Attempts to resolve the current running server version.
     * <p>
     * Uses {@link Bukkit#getBukkitVersion()} to determine the server version and
     * converts it to the corresponding {@link Version} enum value.
     * </p>
     *
     * @return The resolved {@link Version}, or {@link Version#NULL} if the resolution fails.
     */
    public static Version ResolveVersion() {
        try {
            // Log the Bukkit version for debugging
            Log.Info(ValorlessUtils.plugin, Bukkit.getVersion());
            Log.Info(ValorlessUtils.plugin, Bukkit.getBukkitVersion());

            // Parse version string (e.g., "1.19.4-R0.1-SNAPSHOT") into enum format
            String v = Bukkit.getBukkitVersion().split("-")[0];
            return Version.valueOf("v" + v.replace(".", "_"));
        } catch (Exception e) {
            Log.Error(ValorlessUtils.plugin, "Failed to resolve server version, some functions might not work correctly.");
            return Version.NULL;
        }
    }
    
    /**
     * Checks if the current server version is higher than or equal to a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version is higher than or equal to {@code version}; {@code false} otherwise.
     */
    public static Boolean VersionHigherOrEqualTo(Version version) {
        return VersionCompare(ValorlessUtils.getServerVersion(), version) >= 0;
    }

    /**
     * Checks if the current server version is exactly equal to a specified version.
     *
     * @param version The version to compare against.
     * @return {@code true} if the current server version equals {@code version}; {@code false} otherwise.
     */
    public static Boolean VersionEqualTo(Version version) {
        return ValorlessUtils.getServerVersion() == version;
    }
}
