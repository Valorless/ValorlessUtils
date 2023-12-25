package valorless.valorlessutils.sound;

import org.bukkit.entity.Player;

import valorless.valorlessutils.utils.Utils;

public class SFX {
	/**
	 * Plays a specified sound for a player with the given volume and pitch.
	 *
	 * @param sound   The name of the sound to play.
	 * @param volume  The volume of the sound (1.0 is normal volume).
	 * @param pitch   The pitch of the sound (1.0 is normal pitch).
	 * @param player  The player for whom the sound will be played.
	 */
	public static void Play(String sound, float volume, float pitch, Player player) {
	    if (!Utils.IsStringNullOrEmpty(sound)) {
	        player.playSound(player, org.bukkit.Sound.valueOf(sound.toUpperCase()), volume, pitch);
	    }
	}
}