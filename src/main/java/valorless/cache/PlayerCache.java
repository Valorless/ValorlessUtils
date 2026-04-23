package valorless.cache;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import valorless.annotations.DoNotCall;
import valorless.annotations.Internal;
import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.logging.Log;

import java.util.HashMap;
import java.util.UUID;

/**
 * A cache that maps player usernames to their {@link UUID}s.
 * <p>
 * The cache is populated asynchronously on initialization with all known offline players,
 * and is kept up-to-date as players join the server.
 * </p>
 */
public class PlayerCache implements Listener {

	private static boolean init = false;
	private static final HashMap<String, UUID> cache = new HashMap<>();

	/**
	 * Initializes the PlayerCache by registering the listener and asynchronously
	 * populating the cache with all known offline players.
	 * <p>
	 * This method is for internal use only and should not be called manually.
	 * </p>
	 */
	@Internal @DoNotCall
	public static void init() {
		if(init) return;
		Bukkit.getPluginManager().registerEvents(new PlayerCache(), ValorlessUtils.plugin);
		Bukkit.getScheduler().runTaskAsynchronously(ValorlessUtils.plugin, () -> {
			for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
				UUID uuid = player.getUniqueId();
				String name = player.getName();
				cache.put(name, uuid);
			}
			Log.info(ValorlessUtils.plugin, "PlayerCache initialized with " + cache.size() + " entries.");
		});
		init = true;
	}

	/**
	 * Retrieves the {@link UUID} associated with the given username from the cache.
	 *
	 * @param username the player's username to look up
	 * @return the {@link UUID} of the player, or {@code null} if not found in the cache
	 */
	public static UUID getUUID(String username) {
		for (String key : cache.keySet()) {
			if (key.equals(username)) {
				return cache.get(key);
			}
		}
		return null;
	}

	/**
	 * Retrieves the {@link OfflinePlayer} associated with the given username from the cache.
	 *
	 * @param username the player's username to look up
	 * @return the {@link OfflinePlayer} instance, or {@code null} if not found in the cache
	 */
	public static OfflinePlayer getPlayer(String username) {
		for (String key : cache.keySet()) {
			if (key.equals(username)) {
				UUID uuid = cache.get(key);
				return Bukkit.getOfflinePlayer(uuid);
			}
		}
		return null;
	}

	/**
	 * Returns a shallow copy of the player cache.
	 *
	 * @return a {@link HashMap} copy mapping usernames to their {@link UUID}s
	 */
	public static HashMap<String, UUID> getCache() {
		return new HashMap<>(cache);
	}

	/**
	 * Handles the {@link PlayerJoinEvent} by adding or updating the joining player's
	 * username and {@link UUID} in the cache.
	 *
	 * @param event the player join event
	 */
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		String name = event.getPlayer().getName();
		cache.put(name,  uuid);
	}

	/**
	 * Adds a player to the cache with the given username and {@link UUID}.
	 *
	 * @param username the player's username
	 * @param uuid the player's {@link UUID}
	 */
	public static void addPlayer(String username, UUID uuid) {
		cache.put(username, uuid);
	}

}
