package valorless.valorlessutils.sound;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import valorless.valorlessutils.Server.Version;
import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.ValorlessUtils.Log;
import valorless.valorlessutils.utils.Utils;

public class SFX {
	/**
	 * Play a sound at the location of the player with the given volume and pitch.
	 *
	 * @param sound   The name of the sound to play.
	 * @param volume  The volume of the sound (1.0 is normal volume).
	 * @param pitch   The pitch of the sound (1.0 is normal pitch).
	 * @param player  The player for whom the sound will be played.
	 */
	public static void Play(String sound, float volume, float pitch, Player player) {
	    if (!Utils.IsStringNullOrEmpty(sound)) {
	    	try {
	    		if(ValorlessUtils.getServerVersion() == Version.v1_17 || ValorlessUtils.getServerVersion() == Version.v1_17_1) {
	    			player.playSound(player.getLocation(), Sound.valueOf(sound.toUpperCase()), volume, pitch);
	    		} else {
	    			player.playSound(player, Sound.valueOf(sound.toUpperCase()), volume, pitch);
	    		}
	    	} catch (Exception e) {
	    		Log.Error(ValorlessUtils.GetInstance(), e.getMessage());
	    	}
	    }
	}
	
	/**
	 * Play a sound at a location  with the given volume and pitch.
	 *
	 * @param sound   The name of the sound to play.
	 * @param volume  The volume of the sound (1.0 is normal volume).
	 * @param pitch   The pitch of the sound (1.0 is normal pitch).
	 * @param location  The location where the sound will be played.
	 */
	public static void Play(String sound, float volume, float pitch, Location location) {
	    if (!Utils.IsStringNullOrEmpty(sound)) {
	    	try {
	    		Bukkit.getWorld(location.getWorld().getName()).playSound(location, Sound.valueOf(sound.toUpperCase()), volume, pitch);
	    	} catch (Exception e) {
	    		Log.Error(ValorlessUtils.GetInstance(), e.getMessage());
	    	}
	    }
	}
}