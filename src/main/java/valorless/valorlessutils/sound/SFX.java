package valorless.valorlessutils.sound;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.SoundGroup;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import valorless.valorlessutils.Server.Version;
import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.ValorlessUtils.Log;
import valorless.valorlessutils.utils.Utils;

/**
 * Sound effects helper methods.
 *
 * <p>This class provides:</p>
 * <ul>
 *   <li>Convenience methods for playing a sound given a {@link String} identifier for a
 *       {@link Player} or at a {@link Location}.</li>
 *   <li>Support for <em>namespaced</em> sound keys (e.g. {@code "minecraft:block.stone.place"})
 *       via the String-based Bukkit sound API.</li>
 *   <li>Utilities to derive the correct placement/break sound for a block/material using
 *       {@link BlockData#getSoundGroup()}.</li>
 * </ul>
 *
 * <h2>Identifier formats</h2>
 * <ul>
 *   <li><b>Enum style</b>: {@code "BLOCK_STONE_PLACE"} (resolved to {@link Sound} through
 *       {@link #GetSound(String)}).</li>
 *   <li><b>Namespaced</b>: {@code "minecraft:block.stone.place"} (detected by the presence of a
 *       {@code '.'} and played via {@link #PlayNamespace(String, float, float, Player)} or
 *       {@link #PlayNamespace(String, float, float, Location)}).</li>
 * </ul>
 *
 * <h2>Server version notes</h2>
 * <p>On 1.17/1.17.1 this class plays enum sounds using the legacy overload
 * {@link Player#playSound(Location, Sound, float, float)}. On later versions it uses
 * {@link Player#playSound(Player, Sound, float, float)}.</p>
 */
public class SFX {

	/**
	 * Plays a sound for a player.
	 *
	 * <p>Behavior:</p>
	 * <ul>
	 *   <li>If {@code sound} is {@code null} or empty, nothing is played.</li>
	 *   <li>For server versions 1.17/1.17.1: plays the resolved {@link Sound} at the player's
	 *       location.</li>
	 *   <li>For later versions: if {@code sound} contains a dot, treats it as a namespaced key and
	 *       calls {@link #PlayNamespace(String, float, float, Player)}; otherwise resolves to
	 *       {@link Sound} via {@link #GetSound(String)} and plays it to the player.</li>
	 * </ul>
	 *
	 * @param sound  sound identifier (enum style like {@code "BLOCK_NOTE_BLOCK_PLING"}, or a
	 *               namespaced key like {@code "minecraft:entity.player.levelup"})
	 * @param volume volume multiplier
	 * @param pitch  pitch multiplier
	 * @param player target player
	 */
	public static void Play(String sound, float volume, float pitch, Player player) {
	    if (!Utils.IsStringNullOrEmpty(sound)) {
	    	try {
	    		
	    		if(ValorlessUtils.getServerVersion() == Version.v1_17 || ValorlessUtils.getServerVersion() == Version.v1_17_1) {
	    			player.playSound(player.getLocation(), GetSound(sound.toUpperCase()), volume, pitch);
	    		} else {
	    			if(sound.contains(".")) {
		    			PlayNamespace(sound, volume, pitch, player);
		    			return;
		    		}
	    			player.playSound(player, GetSound(sound.toUpperCase()), volume, pitch);
	    		}
	    	} catch (Exception e) {
	    		Log.Error(ValorlessUtils.GetInstance(), e.getMessage());
	    	}
	    }
	}

	/**
	 * Plays a namespaced/string sound for a player in the {@link SoundCategory#MASTER} category.
	 *
	 * @param sound  namespaced sound key (e.g. {@code "minecraft:block.stone.place"})
	 * @param volume volume multiplier
	 * @param pitch  pitch multiplier
	 * @param player target player
	 */
	public static void PlayNamespace(String sound, float volume, float pitch, Player player) {
	    if (!Utils.IsStringNullOrEmpty(sound)) {
	    	try {
	    		player.playSound(player.getLocation(), sound, SoundCategory.MASTER, volume, pitch);
	    	} catch (Exception e) {
	    		Log.Error(ValorlessUtils.GetInstance(), e.getMessage());
	    	}
	    }
	}

	/**
	 * Plays a sound at a location.
	 *
	 * <p>If {@code sound} contains a dot, it is treated as a namespaced key and this method delegates
	 * to {@link #PlayNamespace(String, float, float, Location)}. Otherwise {@code sound} is resolved
	 * to a {@link Sound} via {@link #GetSound(String)} and played in the location's world.</p>
	 *
	 * @param sound    sound identifier (enum style or namespaced)
	 * @param volume   volume multiplier
	 * @param pitch    pitch multiplier
	 * @param location target location
	 */
	public static void Play(String sound, float volume, float pitch, Location location) {
	    if (!Utils.IsStringNullOrEmpty(sound)) {
	    	try {
	    		if(sound.contains(".")) {
	    			PlayNamespace(sound, volume, pitch, location);
	    			return;
	    		}
	    		Bukkit.getWorld(location.getWorld().getName()).playSound(location, GetSound(sound.toUpperCase()), volume, pitch);
	    	} catch (Exception e) {
	    		Log.Error(ValorlessUtils.GetInstance(), e.getMessage());
	    	}
	    }
	}

	/**
	 * Plays a namespaced/string sound at a location in the {@link SoundCategory#MASTER} category.
	 *
	 * @param sound    namespaced sound key (e.g. {@code "minecraft:block.stone.place"})
	 * @param volume   volume multiplier
	 * @param pitch    pitch multiplier
	 * @param location target location (must have a non-null world)
	 */
	public static void PlayNamespace(String sound, float volume, float pitch, Location location) {
	    if (!Utils.IsStringNullOrEmpty(sound)) {
	    	try {
	    		location.getWorld().playSound(location, sound, SoundCategory.MASTER, volume, pitch);
	    	} catch (Exception e) {
	    		Log.Error(ValorlessUtils.GetInstance(), e.getMessage());
	    	}
	    }
	}

	/**
	 * Resolves a string sound name to a {@link Sound} from {@link Registry#SOUNDS}.
	 *
	 * <p>Matching is performed using {@link String#equalsIgnoreCase(String)} against
	 * {@link Sound#toString()}.</p>
	 *
	 * @param sound enum-like sound name (typically uppercase), e.g. {@code "BLOCK_STONE_PLACE"}
	 * @return the resolved {@link Sound}, or {@code null} when not found
	 */
	public static Sound GetSound(String sound) {
		for (Sound key : Registry.SOUNDS) {
			if(key.toString().equalsIgnoreCase(sound)) return key;
		}
		return null;
	}

	private static final String DEFAULT_PLACE = "BLOCK_STONE_PLACE";
    private static final String DEFAULT_BREAK = "BLOCK_STONE_BREAK";
	
	/**
	 * Returns a placement sound identifier for the given block.
	 *
	 * <p>Uses {@link BlockData#getSoundGroup()} and reflectively invokes
	 * {@code SoundGroup#getPlaceSound()} to remain compatible with server implementations where the
	 * concrete return type may change. If a sound cannot be determined, a safe default is returned.</p>
	 *
	 * @param block block to inspect
	 * @return a sound identifier (usually enum-style); never {@code null}
	 */
	public static String getSoundPlace(Block block) {
        if (block == null) return DEFAULT_PLACE;
        try {
            BlockData blockData = block.getBlockData();
            if (blockData == null) return DEFAULT_PLACE;

            // Use reflection to avoid static linkage to org.bukkit.Sound
            java.lang.reflect.Method getPlaceSound = SoundGroup.class.getMethod("getPlaceSound");
            Object soundGroup = blockData.getSoundGroup();
            if (soundGroup == null) return DEFAULT_PLACE;

            Object placeSound = getPlaceSound.invoke(soundGroup);
            if (placeSound == null) return DEFAULT_PLACE;

            return extractSoundKey(placeSound, DEFAULT_PLACE);
        } catch (ReflectiveOperationException e) {
            // If anything goes wrong, fall back safely
            e.printStackTrace();
            return DEFAULT_PLACE;
        }
    }

    /**
     * Returns a placement sound identifier for the given material.
     *
     * @param material material to inspect
     * @return a sound identifier; {@code null} if {@code material} is {@code null} or not a block
     */
    public static String getSoundPlace(Material material) {
        if (material == null || !material.isBlock()) return null;
        try {
            BlockData data = Bukkit.createBlockData(material);
            if (data == null) return DEFAULT_PLACE;

            java.lang.reflect.Method getPlaceSound = SoundGroup.class.getMethod("getPlaceSound");
            Object soundGroup = data.getSoundGroup();
            if (soundGroup == null) return DEFAULT_PLACE;

            Object placeSound = getPlaceSound.invoke(soundGroup);
            if (placeSound == null) return DEFAULT_PLACE;

            return extractSoundKey(placeSound, DEFAULT_PLACE);
        } catch (IllegalArgumentException iae) {
            // createBlockData can throw for some materials; treat as no-sound
            return DEFAULT_PLACE;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return DEFAULT_PLACE;
        }
    }

    /**
     * Returns a break sound identifier for the given block.
     *
     * @param block block to inspect
     * @return a sound identifier (usually enum-style); never {@code null}
     */
    public static String getSoundBreak(Block block) {
        if (block == null) return DEFAULT_BREAK;
        try {
            BlockData blockData = block.getBlockData();
            if (blockData == null) return DEFAULT_BREAK;

            java.lang.reflect.Method getBreakSound = SoundGroup.class.getMethod("getBreakSound");
            Object soundGroup = blockData.getSoundGroup();
            if (soundGroup == null) return DEFAULT_BREAK;

            Object breakSound = getBreakSound.invoke(soundGroup);
            if (breakSound == null) return DEFAULT_BREAK;

            return extractSoundKey(breakSound, DEFAULT_BREAK);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return DEFAULT_BREAK;
        }
    }

    /**
     * Returns a break sound identifier for the given material.
     *
     * @param material material to inspect
     * @return a sound identifier; {@code null} if {@code material} is {@code null} or not a block
     */
    public static String getSoundBreak(Material material) {
        if (material == null || !material.isBlock()) return null;
        try {
            BlockData data = Bukkit.createBlockData(material);
            if (data == null) return DEFAULT_BREAK;

            java.lang.reflect.Method getBreakSound = SoundGroup.class.getMethod("getBreakSound");
            Object soundGroup = data.getSoundGroup();
            if (soundGroup == null) return DEFAULT_BREAK;

            Object breakSound = getBreakSound.invoke(soundGroup);
            if (breakSound == null) return DEFAULT_BREAK;

            return extractSoundKey(breakSound, DEFAULT_BREAK);
        } catch (IllegalArgumentException iae) {
            return DEFAULT_BREAK;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return DEFAULT_BREAK;
        }
    }

    /**
     * Extracts a stable identifier from a sound-like object returned by reflective calls.
     *
     * <p>Depending on Bukkit/Paper version, the sound value returned by {@code SoundGroup} methods may
     * be a {@link Sound}, a keyed object, or another wrapper. This method attempts multiple strategies
     * (via reflection) and falls back to {@code fallback} when none work.</p>
     *
     * @param soundObj object to inspect
     * @param fallback fallback value
     * @return extracted identifier, or {@code fallback} when extraction fails
     */
    private static String extractSoundKey(Object soundObj, String fallback) {
        if (soundObj == null) return fallback;
        try {
            // Try getKeyOrThrow() -> getKey()
            try {
                java.lang.reflect.Method getKeyOrThrow = soundObj.getClass().getMethod("getKeyOrThrow");
                Object keyObj = getKeyOrThrow.invoke(soundObj);
                if (keyObj != null) {
                    try {
                        java.lang.reflect.Method getKey = keyObj.getClass().getMethod("getKey");
                        Object keyVal = getKey.invoke(keyObj);
                        if (keyVal != null) return keyVal.toString();
                    } catch (NoSuchMethodException ignored) {}
                    if (keyObj.toString() != null) return keyObj.toString();
                }
            } catch (NoSuchMethodException ignored) {}

            // Try getKey()
            try {
                java.lang.reflect.Method getKey = soundObj.getClass().getMethod("getKey");
                Object key = getKey.invoke(soundObj);
                if (key != null) return key.toString();
            } catch (NoSuchMethodException ignored) {}

            // Try name() (enum)
            try {
                java.lang.reflect.Method name = soundObj.getClass().getMethod("name");
                Object enumName = name.invoke(soundObj);
                if (enumName != null) return enumName.toString();
            } catch (NoSuchMethodException ignored) {}

            // Fall back to toString()
            String s = soundObj.toString();
            return (s == null || s.isEmpty()) ? fallback : s;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return fallback;
        }
    }
}