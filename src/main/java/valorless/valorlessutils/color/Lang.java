package valorless.valorlessutils.color;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import valorless.annotations.Nullable;
import valorless.valorlessutils.config.Config;
import valorless.valorlessutils.logging.Log;
import valorless.valorlessutils.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Language/message utility for plugin text formatting and lookup.
 * <p>
 * This class can:
 * <ul>
 *   <li>load translatable strings from {@code lang.yml},</li>
 *   <li>resolve PlaceholderAPI placeholders when available,</li>
 *   <li>apply/remove legacy color formatting and hex colors,</li>
 *   <li>convert legacy formatted strings to MiniMessage.</li>
 * </ul>
 * </p>
 */
public class Lang {

	/** Plugin instance used for resource access and logging context. */
	private final JavaPlugin plugin;

	/**
	 * Backing language configuration loaded from {@code lang.yml}.
	 * <p>
	 * Can be {@code null} when no bundled {@code lang.yml} exists in the plugin JAR.
	 * </p>
	 */
	private final Config lang;

	/**
	 * Creates a language helper for a plugin.
	 * <p>
	 * If a bundled {@code lang.yml} resource exists, it is loaded via {@link Config}.
	 * Otherwise a warning is logged and language lookup methods ({@link #get(String, Player...)},
	 * {@link #getList(String, Player...)}, {@link #getRaw(String)}) are unavailable.
	 * </p>
	 *
	 * @param plugin owning plugin instance
	 */
	public Lang(JavaPlugin plugin) {
		this.plugin = plugin;
		if (plugin.getResource("lang.yml") != null) {
			lang = new Config(plugin, "lang.yml");
		} else {
			Logger.getLogger("Minecraft").log(Level.WARNING, "[" + plugin.getName() + "] " +
					"Default lang.yml not found in JAR – get()/getList()/getRaw() will be unavailable.");
			lang = null;
		}
	}

	/**
	 * Parses a message by applying placeholders and color formatting.
	 * <p>
	 * Operations performed (in order):
	 * <ol>
	 *   <li>PlaceholderAPI resolution when available (using the provided player, or a fallback)</li>
	 *   <li>Convert hex colors of the form #RRGGBB to Minecraft color sequences</li>
	 *   <li>Translate legacy color codes (&amp; to §)</li>
	 *   <li>Unescape newlines ("\\n" to newline)</li>
	 *   <li>Replace %player% with the player's name when a player is provided</li>
	 * </ol>
	 * If player is null, an arbitrary offline player is used as a best-effort fallback for
	 * PlaceholderAPI parsing; if that fails, placeholders are resolved with null.
	 * </p>
	 *
	 * @param text   the raw message to parse; if null/empty, returns as-is
	 * @param player the player context for placeholder resolution; may be null
	 * @return the formatted and placeholder-resolved message
	 */
	public String parse(String text, @Nullable OfflinePlayer player) {
		if(!Utils.IsStringNullOrEmpty(text)) {

			if(player != null) {
				text = parsePlaceholderAPI(text, player);
			} else {
				text = parsePlaceholderAPI(text, null);
			}
		
			text = hex(text);
			text = text.replace("&", "§");
			text = text.replace("\\n", "\n");
			if(player != null) {
				text = text.replace("%player%", player.getName());
			}
		}
		return text;
	}

	/**
	 * Parses a message by applying placeholders and color formatting, replacing %player% with the provided name.
	 * <p>
	 * This method is similar to {@link #parse(String, OfflinePlayer)}, but instead of using an OfflinePlayer
	 * for placeholder resolution, it directly replaces the %player% token with the given playerName string.
	 * PlaceholderAPI parsing is still performed with a null player context. This is useful when you want to
	 * format a message with a specific player name without needing an actual player object.
	 * </p>
	 *
	 * @param text       the raw message to parse; if null/empty, returns as-is
	 * @param playerName the player name to replace %player% with; may be null
	 * @return the formatted message with %player% replaced by playerName
	 */
	public String parseWithName(String text, String playerName) {
		if(!Utils.IsStringNullOrEmpty(text)) {
			text = parsePlaceholderAPI(text, null);

			text = hex(text);
			text = text.replace("&", "§");
			text = text.replace("\\n", "\n");
			if(playerName != null) {
				text = text.replace("%player%", playerName);
			}
		}
		return text;
	}
	
	/**
	 * Parses a message by applying placeholders and color formatting without a player context.
	 * <p>
	 * This is a convenience method that calls {@link #parse(String, OfflinePlayer)} with null.
	 * </p>
	 * 
	 * @param text the raw message to parse
	 * @return the formatted message
	 */
	public String parse(String text) {
		return parse(text, null);
	}

	/**
	 * Parses a message and optionally converts it to MiniMessage format.
	 * <p>
	 * If minimessage is true, the result of {@link #parse(String, OfflinePlayer)} is further
	 * processed by {@link #convertToMiniMessage(String)} to convert legacy color codes and hex
	 * sequences into MiniMessage tags. If minimessage is false, this method simply delegates to
	 * {@link #parse(String, OfflinePlayer)} with a null player context.
	 * </p>
	 *
	 * @param text        the raw message to parse
	 * @param minimessage whether to convert the parsed message to MiniMessage format
	 * @return the formatted message, optionally converted to MiniMessage
	 */
	public String parse(String text, boolean minimessage) {
		return minimessage ? convertToMiniMessage(parse(text, null)) : parse(text, null);
	}

	/**
	 * Parses a message and optionally converts it to MiniMessage format.
	 * <p>
	 * This overload currently behaves the same as {@link #parse(String, boolean)} and
	 * parses with a {@code null} player context.
	 * </p>
	 *
	 * @param text the raw message to parse
	 * @param player currently ignored
	 * @param minimessage whether to convert parsed output to MiniMessage
	 * @return the parsed message, optionally converted to MiniMessage
	 */
	public String parse(String text, OfflinePlayer player, boolean minimessage) {
		return minimessage ? convertToMiniMessage(parse(text, null)) : parse(text, null);
	}
	
	/**
	 * Retrieves a localized string from lang.yml by key, with optional player context.
	 * <p>
	 * If the key is missing from the configuration, logs an error and returns "§4error".
	 * When a player is provided, player-specific placeholders can be resolved.
	 * If {@code lang.yml} was not bundled and could not be loaded, this method is unavailable.
	 * </p>
	 *
	 * @param key    the lookup key in lang.yml
	 * @param player optional player context for placeholder resolution
	 * @return the configured value with parsing applied, or "§4error" if the key doesn't exist
	 */
	public String get(String key, Player... player) {
		if(lang.get(key) == null) {
			Log.error(plugin, String.format("Lang.yml is missing the key '%s'!", key));
			return "§4error";
		}
		if(player != null && player.length != 0) {
			return parse(lang.getString(key), player[0]);
		}
		else return parse(lang.getString(key), null);
	}

	/**
	 * Retrieves a list of localized strings from lang.yml by key, with optional player context.
	 * <p>
	 * If the key is missing from the configuration, logs an error and returns a list containing "§4error".
	 * When a player is provided, player-specific placeholders can be resolved in each line.
	 * If {@code lang.yml} was not bundled and could not be loaded, this method is unavailable.
	 * </p>
	 *
	 * @param key    the lookup key in lang.yml
	 * @param player optional player context for placeholder resolution
	 * @return the configured list of values with parsing applied, or a list containing "§4error" if the key doesn't exist
	 */
	public List<String> getList(String key, Player... player) {
		if(!lang.hasKey(key)) {
			Log.error(plugin, String.format("Lang.yml is missing the key '%s'!", key));
			return List.of("§4error");
		}
		if(player != null && player.length != 0) {
			List<String> list = new ArrayList<String>();
			for(String line : lang.getStringList(key)) {
				list.add(parse(line, player[0]));
			}
			return list;
		}
		else {
			List<String> list = new ArrayList<String>();
			for(String line : lang.getStringList(key)) {
				list.add(parse(line, null));
			}
			return list;
		}
	}
	
	/**
	 * Retrieves a raw localized string from lang.yml by key, without any parsing.
	 * <p>
	 * Logs an error and returns a visible error token ("§4error") if the key is missing.
	 * If {@code lang.yml} was not bundled and could not be loaded, this method is unavailable.
	 * </p>
	 *
	 * @param key the lookup key in lang.yml
	 * @return the configured value as-is
	 */
	public String getRaw(String key) {
		if(lang.get(key) == null) {
			Log.error(plugin, String.format("Lang.yml is missing the key '%s'!", key));
			return "§4error";
		}
		return lang.getString(key);
	}
	
	/**
	 * Converts hex color codes (#RRGGBB) into legacy color sequences understood by Bukkit,
	 * then applies {@link ChatColor#translateAlternateColorCodes(char, String)}.
	 *
	 * @param message the message possibly containing hex color codes
	 * @return the message with hex and legacy color codes translated
	 */
	@SuppressWarnings("deprecation")
	public String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
           
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }
           
            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
	
	/**
	 * Resolves PlaceholderAPI placeholders when the hook is available.
	 * <p>
	 * Curly braces are converted to percent tokens before delegation, e.g. {foo_bar}
	 * becomes %foo_bar%.
	 * </p>
	 *
	 * @param text   the text possibly containing PlaceholderAPI placeholders
	 * @param player the player context used for placeholder resolution; may be null
	 * @return the text with placeholders resolved, or the original text on failure/not hooked
	 */
	public String parsePlaceholderAPI(String text, OfflinePlayer player) {
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			String t = "";
			text = text.replace("{", "%");
			text = text.replace("}", "%");
			try {
				t =  PlaceholderAPI.setPlaceholders(player, text);  
        	}catch (Exception e) {
        		Log.error(plugin, "Failed to get PlaceholderAPI. Is it up to date?");
        		t = text;
        	}
			return t;
		}else {
			return text;
		}
	}
	
	/**
	 * Removes all color and formatting codes, including hex sequences, from the text.
	 *
	 * @param text the text to sanitize
	 * @return the text without any § formatting codes
	 */
	public String removeColorFormatting(String text) {
		if(!Utils.IsStringNullOrEmpty(text)) {
			text = text.replace("§§", "§");
			text = removeHex(text);
			text = text.replace("§0", "");
			text = text.replace("§1", "");
			text = text.replace("§2", "");
			text = text.replace("§3", "");
			text = text.replace("§4", "");
			text = text.replace("§5", "");
			text = text.replace("§6", "");
			text = text.replace("§7", "");
			text = text.replace("§8", "");
			text = text.replace("§9", "");
			text = text.replace("§0", "");
			text = text.replace("§a", "");
			text = text.replace("§b", "");
			text = text.replace("§c", "");
			text = text.replace("§d", "");
			text = text.replace("§e", "");
			text = text.replace("§f", "");
			text = text.replace("§o", "");
			text = text.replace("§l", "");
			text = text.replace("§k", "");
			text = text.replace("§m", "");
			text = text.replace("§n", "");
			text = text.replace("§r", "");
			text = text.replace("§A", "");
			text = text.replace("§B", "");
			text = text.replace("§C", "");
			text = text.replace("§D", "");
			text = text.replace("§E", "");
			text = text.replace("§F", "");
			text = text.replace("§x", "");
			text = text.replace("§", "");
		}
		return text;
	}
	
	/**
	 * Removes hex color sequences of the form §x§R§R§G§G§B§B from the text.
	 *
	 * @param text the text to sanitize
	 * @return the text without any §x hex color sequences
	 */
	public String removeHex(String text) {
        // Regex to match the pattern of hex color codes
    	//Log.info(Main.plugin, text);
        String hexColorRegex = "§x(§[0-9A-Fa-f]){6}";
        return text.replaceAll(hexColorRegex, "");
    }
	
	/**
	 * Converts legacy color codes (& or §) and hex color sequences into MiniMessage format.
	 * <p>
	 * This allows for better compatibility with MiniMessage-based APIs while preserving
	 * the original formatting intent. Hex colors are converted to <#RRGGBB> tags, and
	 * legacy codes are converted to their corresponding MiniMessage tags (e.g. &c to <red>).
	 * Color codes reset previous formatting as per legacy behavior.
	 * </p>
	 *
	 * @param text the text containing legacy color codes and hex sequences
	 * @return the text converted to MiniMessage format
	 */
	public String convertToMiniMessage(final String text) {
        if (text == null || text.isEmpty()) return text;

        StringBuilder out = new StringBuilder(text.length());
        final int len = text.length();

        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);

            // handle both § and & as legacy prefixes
            if ((c == '§' || c == '&') && i + 1 < len) {
                char code = Character.toLowerCase(text.charAt(i + 1));

                // Legacy hex: prefix x then six pairs of (prefix hexChar) e.g. §x§F§F§0§0§0§0
                if (code == 'x') {
                    // Need 14 characters from current pos for the whole sequence:
                    // prefix + x + 6 * (prefix + hexChar) => total chars = 2 + 6*2 = 14
                    if (i + 13 < len) {
                        StringBuilder hex = new StringBuilder(6);
                        boolean ok = true;
                        int p = i + 2;
                        for (int h = 0; h < 6; h++) {
                            if (p + 1 >= len) { ok = false; break; }
                            char prefixAt = text.charAt(p);
                            char hexChar = text.charAt(p + 1);
                            // must have the same kind of prefix for each hex nibble (either § or &)
                            if ((prefixAt != '§' && prefixAt != '&') || !isHexChar(hexChar)) {
                                ok = false;
                                break;
                            }
                            hex.append(hexChar);
                            p += 2;
                        }
                        if (ok) {
                            out.append("<#").append(hex).append(">");
                            i = p - 1; // skip consumed chars
                            continue;
                        }
                    }
                    // not a valid hex sequence -> skip the 'x' marker so it isn't emitted literally
                    i++; // skip the 'x' code char
                    continue;
                }

                String tag = codeToTag(code);
                if (tag != null) {
                    // color codes reset previous formatting in legacy behavior
                    if (isColorCode(code)) {
                        out.append("<reset>").append(tag);
                    } else if (code == 'r') {
                        out.append("<reset>");
                    } else {
                        out.append(tag);
                    }
                    i++; // consumed the code char
                    continue;
                }

                // Unknown code: skip both prefix and code char (drop them)
                i++; // drop code char as well
                continue;
            }

            // normal character
            out.append(c);
        }

        return out.toString();
    }

	/**
	 * Checks if a character is a valid hexadecimal digit.
	 * 
	 * @param c the character to check
	 * @return true if the character is 0-9, a-f, or A-F
	 */
    private boolean isHexChar(final char c) {
        return (c >= '0' && c <= '9') ||
               (c >= 'a' && c <= 'f') ||
               (c >= 'A' && c <= 'F');
    }

	/**
	 * Checks if a character represents a legacy color code.
	 * 
	 * @param c the character to check
	 * @return true if the character is a legacy color code (0-9, a-f)
	 */
    private boolean isColorCode(final char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f');
    }

	/**
	 * Converts a legacy color/format code character to its MiniMessage tag equivalent.
	 * 
	 * @param code the legacy code character (e.g., 'a', 'l', 'r')
	 * @return the corresponding MiniMessage tag (e.g., "&lt;green&gt;", "&lt;bold&gt;"), or null if unknown
	 */
    private String codeToTag(final char code) {
        return switch (code) {
            // colors
            case '0' -> "<black>";
            case '1' -> "<dark_blue>";
            case '2' -> "<dark_green>";
            case '3' -> "<dark_aqua>";
            case '4' -> "<dark_red>";
            case '5' -> "<dark_purple>";
            case '6' -> "<gold>";
            case '7' -> "<gray>";
            case '8' -> "<dark_gray>";
            case '9' -> "<blue>";
            case 'a' -> "<green>";
            case 'b' -> "<aqua>";
            case 'c' -> "<red>";
            case 'd' -> "<light_purple>";
            case 'e' -> "<yellow>";
            case 'f' -> "<white>";

            // formatting
            case 'k' -> "<obfuscated>";
            case 'l' -> "<bold>";
            case 'm' -> "<strikethrough>";
            case 'n' -> "<underlined>";
            case 'o' -> "<italic>";

            // reset (caller handles insertion too)
            case 'r' -> "<reset>";
            default -> null;
        };
    }

	/**
	 * Removes MiniMessage tags from text while preserving the visible content.
	 * <p>
	 * Examples:
	 * <ul>
	 *   <li>{@code <red>Hello</red>} -> {@code Hello}</li>
	 *   <li>{@code <gradient:red:blue>Hi</gradient>} -> {@code Hi}</li>
	 *   <li>{@code \\<red>} -> {@code <red>}</li>
	 * </ul>
	 * Escaped angle brackets ({@code \<} and {@code \>}) are unescaped and kept as literal
	 * characters. If a {@code <} has no closing {@code >}, it is treated as normal text.
	 * </p>
	 *
	 * @param text the text that may contain MiniMessage tags
	 * @return the input text with MiniMessage tags removed
	 */
	public String stripMiniMessageFormatting(String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}

		StringBuilder out = new StringBuilder(text.length());
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			// Keep escaped angle brackets as visible characters.
			if (c == '\\' && i + 1 < text.length()) {
				char next = text.charAt(i + 1);
				if (next == '<' || next == '>') {
					out.append(next);
					i++;
					continue;
				}
			}

			if (c == '<') {
				int end = text.indexOf('>', i + 1);
				if (end != -1) {
					i = end;
					continue;
				}
			}

			out.append(c);
		}

		return out.toString();
	}
}

