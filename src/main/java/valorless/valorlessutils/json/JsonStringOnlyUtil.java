package valorless.valorlessutils.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tiny JSON helper that treats values strictly as strings.
 *
 * Behavior:
 * - toJson("abc") -> "\"abc\""   (a proper JSON string)
 * - fromJson("\"abc\"") -> "abc"
 * - fromJson("abc") -> "abc"     (unquoted input is returned as-is, NOT parsed to a number)
 * - fromJson("null") -> null
 *
 * - toJsonList(["a","b"]) -> "[\"a\",\"b\"]"
 * - fromJsonList("[\"a\",1,foo]") -> ["a", "1", "foo"]
 *
 * Important:
 * - This utility intentionally does NOT convert numeric tokens into Java numbers.
 *   If the input is an unquoted token (e.g. 1e761457) it will be returned as the literal string "1e761457",
 *   avoiding Double.POSITIVE_INFINITY or other numeric conversions.
 */
public final class JsonStringOnlyUtil {
    private static final Gson GSON = new Gson();

    private JsonStringOnlyUtil() {}

    /**
     * Serialize a Java String into a JSON string literal.
     * Example: toJson("hello") -> "\"hello\""
     *
     * @param s input string (may be null)
     * @return JSON representation (the four characters "null" if s == null)
     */
    public static String toJson(String s) {
        return GSON.toJson(s);
    }

    /**
     * Deserialize a JSON string literal (or raw token) into a Java String.
     *
     * Rules:
     * - If the trimmed input equals "null" -> returns null.
     * - If input begins with a double-quote -> use a proper JSON string parse (handles escapes).
     * - Otherwise -> return the trimmed input exactly as-is (no numeric conversion).
     *
     * This avoids parsing huge scientific-notation numbers into Infinity.
     *
     * @param json JSON input (may be a quoted JSON string, the literal "null", or an unquoted token)
     * @return the Java String or null
     */
    public static String fromJson(String json) {
        if (json == null) return null;
        String t = json.trim();
        if (t.equals("null")) return null;
        if (t.startsWith("\"") && t.endsWith("\"") && t.length() >= 2) {
            // let Gson handle string unescaping
            return GSON.fromJson(t, String.class);
        }
        // Unquoted token: return literal trimmed text (do not convert to number)
        return t;
    }

    /**
     * Serialize a list of strings into a JSON array.
     * Null elements will be serialized as JSON null.
     *
     * @param list list of strings (may be null)
     * @return JSON array text or "null" if list is null
     */
    public static String toJsonList(List<String> list) {
        return GSON.toJson(list);
    }

    /**
     * Deserialize a JSON array into a List<String>.
     *
     * Rules per element:
     * - JSON null -> Java null
     * - JSON string -> unescaped string value
     * - Any other JSON primitive/object/array -> the element's JSON text (element.toString())
     *
     * If the input is not a JSON array but a single token, returns a singleton list containing fromJson(input).
     *
     * @param json JSON input
     * @return List of Strings (empty list if array is empty), or null if input is the literal "null"
     */
    public static List<String> fromJsonList(String json) {
        if (json == null) return null;
        String t = json.trim();
        if (t.equals("null")) return null;

        if (!t.startsWith("[")) {
            // Not an array: treat as single element (use fromJson rules)
            String single = fromJson(t);
            return single == null ? null : Collections.singletonList(single);
        }

        // Parse as JSON array and extract elements safely
        JsonElement root;
        try {
            root = JsonParser.parseString(t);
        } catch (Exception ex) {
            // Malformed JSON: fallback to returning a singleton with the raw trimmed input
            List<String> fallback = new ArrayList<>(1);
            fallback.add(t);
            return fallback;
        }

        if (!root.isJsonArray()) {
            // Not an array after parsing; fallback
            String single = fromJson(t);
            return single == null ? null : Collections.singletonList(single);
        }

        JsonArray arr = root.getAsJsonArray();
        List<String> result = new ArrayList<>(arr.size());
        for (JsonElement el : arr) {
            if (el.isJsonNull()) {
                result.add(null);
            } else if (el.isJsonPrimitive()) {
                JsonPrimitive prim = el.getAsJsonPrimitive();
                if (prim.isString()) {
                    // Proper JSON string -> unescape via Gson
                    result.add(prim.getAsString());
                } else {
                    // Number, boolean, etc. -> return the element's JSON text to preserve original representation
                    result.add(el.toString());
                }
            } else {
                // Object or array element -> return its JSON text
                result.add(el.toString());
            }
        }
        return result;
    }
}