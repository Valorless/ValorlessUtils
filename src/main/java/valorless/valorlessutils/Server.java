package valorless.valorlessutils;

import org.bukkit.Bukkit;

import valorless.valorlessutils.ValorlessUtils.Log;

/**
 * Version of the server.<br>
 * I.e 1_19_4, 1_21
 */
public class Server {
	public enum Version { 
		NULL,
		v1_17,
		v1_17_1,
		v1_18,
		v1_18_1,
		v1_18_2,
		v1_19,
		v1_19_1,
		v1_19_2,
		v1_19_3,
		v1_19_4,
		v1_20,
		v1_20_1,
		v1_20_3,
		v1_20_4,
		v1_20_5,
		v1_20_6,
		v1_21,
		v1_21_1,
		v1_21_2,
		v1_21_3,
	}
	
	/**
	 * Compares two ServerVersion enums to determine their order based on their declaration.
	 *
	 * @param version    The ServerVersion to compare.
	 * @param compareTo  The ServerVersion to compare against.
	 * @return          A negative integer, zero, or a positive integer as the first argument
	 *                  is less than, equal to, or greater than the second argument.
	 */
	public static int VersionCompare(Version version, Version compareTo) {
	    if (version.ordinal() < compareTo.ordinal()) {
	        return -1;  // version is less than compareTo
	    } else if (version.ordinal() > compareTo.ordinal()) {
	        return 1;   // version is greater than compareTo
	    } else {
	        return 0;   // version is equal to compareTo
	    }
	}
	
	public static Version ResolveVersion() {
    	try {
    		Log.Info(ValorlessUtils.plugin, Bukkit.getVersion());
    		Log.Info(ValorlessUtils.plugin, Bukkit.getBukkitVersion());
    		String v = Bukkit.getBukkitVersion().split("-")[0];
    		return Version.valueOf("v" + v.replace(".", "_"));
    		//Log.Debug(plugin, server.toString());
    	} catch (Exception e) {
    		Log.Error(ValorlessUtils.plugin, "Failed to resolve server version, some functions might not work correctly.");
    		return Version.NULL;
    	}
    }
}
