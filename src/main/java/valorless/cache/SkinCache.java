package valorless.cache;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.profile.PlayerProfile;
import valorless.annotations.DoNotCall;
import valorless.annotations.Internal;
import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.logging.Log;
import valorless.valorlessutils.config.Config;
import valorless.valorlessutils.json.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * A cache that maps player usernames to their {@link PlayerProfile}s, including skin data.
 * <p>
 * The cache is loaded from disk on initialization, populated with currently online players,
 * and persisted back to disk on shutdown. It is kept up-to-date as players join the server.
 * </p>
 */
public class SkinCache implements Listener {

	private static boolean init = false;
	private static Config config;
	private static HashMap<String, PlayerProfile> cache = new HashMap<>();

	/**
	 * Returns a shallow copy of the skin cache.
	 *
	 * @return a {@link HashMap} copy mapping player usernames to their {@link PlayerProfile}s
	 */
	public static HashMap<String, PlayerProfile> getCache() {
		return new HashMap<>(cache);
	}
	
	/**
	 * Initializes the SkinCache by registering the event listener, loading the cache from disk,
	 * and adding all currently online players to the cache.
	 * <p>
	 * This method is for internal use only and should not be called manually.
	 * </p>
	 */
	@Internal @DoNotCall
	public static void init() {
		if(init) return;
		Bukkit.getServer().getPluginManager().registerEvents(new SkinCache(), ValorlessUtils.plugin);
		loadCache();
		for(Player player : Bukkit.getOnlinePlayers()) {
			cache.put(player.getName(), player.getPlayerProfile());
		}
		init = true;
	}

	/**
	 * Saves the skin cache to the {@code skins.yml} file and logs the result.
	 * <p>
	 * If the cache file or its parent directory does not exist, they will be created.
	 * This method is called when the plugin is disabled.
	 * </p>
	 * <p>
	 * This method is for internal use only and should not be called manually.
	 * </p>
	 */
	@Internal @DoNotCall
	public static void shutdown() {
		try {
			long startTime = System.currentTimeMillis();
			Log.info(ValorlessUtils.plugin, "Saving skin cache..");

			if(config == null) {
				File root = new File(ValorlessUtils.plugin.getDataFolder() + "/cache");
				File cacheFile = new File(ValorlessUtils.plugin.getDataFolder() + "/cache/skins.yml");

				if(!root.exists()) root.mkdir();
				if(!cacheFile.exists()) {
					try {
						cacheFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}

					config = new Config(ValorlessUtils.plugin, "/cache/skins.yml");
				} else {
					config = new Config(ValorlessUtils.plugin, "/cache/skins.yml");
				}

				config.set("skins", JsonUtils.toJson(cache));
				config.saveConfig();
			} else {
				config.set("skins", JsonUtils.toJson(cache));
				config.saveConfig();
			}

			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			Log.info(ValorlessUtils.plugin, String.format("Saved %s skins. %sms", cache.size(), duration));
		}catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the skin cache from the {@code cache/skins.yml} file.
	 * <p>
	 * If the file does not exist, a new empty file is created. If it does exist,
	 * the cache is populated from the stored JSON data. Logs the number of entries
	 * loaded and the time taken.
	 * </p>
	 * <p>
	 * This method is for internal use only and should not be called manually.
	 * </p>
	 */
	@Internal @DoNotCall
	static void loadCache() {
		Log.info(ValorlessUtils.plugin, "Loading skin cache..");
		long startTime = System.currentTimeMillis();
		cache.clear();
		File root = new File(ValorlessUtils.plugin.getDataFolder() + "/cache");
		File cacheFile = new File(ValorlessUtils.plugin.getDataFolder() + "/cache/skins.yml");
		
		if(!root.exists()) root.mkdir();
		if(!cacheFile.exists()) {
			try {
				cacheFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			config = new Config(ValorlessUtils.plugin, "/cache/skins.yml");
		} else {
			config = new Config(ValorlessUtils.plugin, "/cache/skins.yml");
			cache = JsonUtils.fromJson(config.getString("skins"));
			if(cache == null) cache = new HashMap<String, PlayerProfile>();
		}
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		Log.info(ValorlessUtils.plugin, String.format("Loaded %s skins. %sms", cache.size(), duration));
	}

	/**
	 * Handles the {@link PlayerJoinEvent} by adding or updating the joining player's
	 * {@link PlayerProfile} in the cache.
	 *
	 * @param e the player join event
	 */
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent e) {
		cache.put(e.getPlayer().getName(), e.getPlayer().getPlayerProfile());
	}
	
	/**
	 * Gets the {@link PlayerProfile} for the given player username.
	 *
	 * @param username the name of the player whose profile is to be retrieved
	 * @return the {@link PlayerProfile} of the specified player, or {@code null} if not found in the cache
	 */
	public static PlayerProfile getProfile(String username) {
		return cache.get(username);
	}
	
	/**
	 * Gets the skin {@link URL} for the given player username.
	 *
	 * @param username the name of the player whose skin URL is to be retrieved
	 * @return the {@link URL} of the player's skin, or {@code null} if not found in the cache
	 */
	public static URL getSkin(String username) {
		return cache.get(username).getTextures().getSkin();
	}

	/**
	 * Removes a player's {@link PlayerProfile} from the cache.
	 *
	 * @param username the name of the player whose profile is to be removed
	 * @return {@code true} if the profile was successfully removed, {@code false} if it was not found in the cache
	 */
	public static boolean removeProfile(String username) {
		return cache.remove(username) != null;
	}

}
