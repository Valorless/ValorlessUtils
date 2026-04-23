package valorless.valorlessutils.color;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for converting Minecraft color codes to ANSI escape sequences.
 * <p>
 * Supported inputs include legacy codes such as {@code §a} and hex color
 * sequences such as {@code §x§R§R§G§G§B§B}. The returned text always ends
 * with an ANSI reset sequence.
 * </p>
 */
public class McToAnsi {

    /**
     * Legacy Minecraft color/reset code to ANSI SGR map.
     * <p>
     * Includes {@code 0-9}, {@code a-f}, and {@code r} (reset).
     * </p>
     */
    private static final Map<Character, String> COLOR_MAP = Map.ofEntries(
        Map.entry('0', "\033[30m"),
        Map.entry('1', "\033[34m"),
        Map.entry('2', "\033[32m"),
        Map.entry('3', "\033[36m"),
        Map.entry('4', "\033[31m"),
        Map.entry('5', "\033[35m"),
        Map.entry('6', "\033[33m"),
        Map.entry('7', "\033[37m"),
        Map.entry('8', "\033[90m"),
        Map.entry('9', "\033[94m"),
        Map.entry('a', "\033[92m"),
        Map.entry('b', "\033[96m"),
        Map.entry('c', "\033[91m"),
        Map.entry('d', "\033[95m"),
        Map.entry('e', "\033[93m"),
        Map.entry('f', "\033[97m"),
        Map.entry('r', "\033[0m")
    );

    /**
     * Converts Minecraft formatting codes in a string to ANSI escape sequences.
     * <p>
     * Conversion happens in two passes:
     * <ol>
     *   <li>Hex codes in the format {@code §x§R§R§G§G§B§B} are converted to
     *       {@code \033[38;2;R;G;Bm}.</li>
     *   <li>Legacy color/reset codes ({@code §0-§9}, {@code §a-§f}, {@code §r})
     *       are converted using {@link #COLOR_MAP}.</li>
     * </ol>
     * The returned string is suffixed with {@code \033[0m} to ensure color reset.
     * </p>
     *
     * @param text the Minecraft-formatted input string
     * @return ANSI-formatted text ending with a reset sequence
     */
    public static String convert(String text) {
        // Handle hex colors: §x§R§R§G§G§B§B → \033[38;2;R;G;Bm
        Pattern hexPattern = Pattern.compile("§x(§[0-9A-Fa-f]){6}");
        Matcher hexMatcher = hexPattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (hexMatcher.find()) {
            // Strip all § chars and the leading 'x' to get the 6 hex digits
            String raw = hexMatcher.group().replace("§", "");
            raw = raw.substring(1); // remove the leading 'x'
            int r = Integer.parseInt(raw.substring(0, 2), 16);
            int g = Integer.parseInt(raw.substring(2, 4), 16);
            int b = Integer.parseInt(raw.substring(4, 6), 16);
            hexMatcher.appendReplacement(sb, "\033[38;2;" + r + ";" + g + ";" + b + "m");
        }
        hexMatcher.appendTail(sb);
        text = sb.toString();

        // Handle legacy codes: §0-§f, §r
        Pattern legacyPattern = Pattern.compile("§([0-9A-Fa-fRr])");
        Matcher legacyMatcher = legacyPattern.matcher(text);
        sb = new StringBuffer();

        while (legacyMatcher.find()) {
            char code = Character.toLowerCase(legacyMatcher.group(1).charAt(0));
            String ansi = COLOR_MAP.getOrDefault(code, "");
            legacyMatcher.appendReplacement(sb, Matcher.quoteReplacement(ansi));
        }
        legacyMatcher.appendTail(sb);

        return sb + "\033[0m"; // reset at end
    }
}