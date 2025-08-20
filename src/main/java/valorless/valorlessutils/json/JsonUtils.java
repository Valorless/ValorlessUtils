package valorless.valorlessutils.json;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utility class for working with JSON using Gson.
 * Provides methods for serializing and deserializing objects,
 * including pretty-printing and Bukkit-aware handling.
 */
public final class JsonUtils {

    // Thread-local Gson instance for normal JSON serialization
    @SuppressWarnings("nullness:type.argument")
    private static final ThreadLocal<@NonNull Gson> GSON = ThreadLocal.withInitial(() -> {
        Gson gson = newGsonBuilder().create();
        return Unsafe.assertNonNull(gson);
    });

    // Thread-local Gson instance for pretty-printed JSON serialization
    @SuppressWarnings("nullness:type.argument")
    private static final ThreadLocal<@NonNull Gson> GSON_PRETTY = ThreadLocal.withInitial(() -> {
        Gson gson = newGsonBuilder().setPrettyPrinting().create();
        return Unsafe.assertNonNull(gson);
    });

    /**
     * Creates a new GsonBuilder with common default configuration:
     * - HTML escaping disabled
     * - Special floating-point values serialized
     * - Lenient parsing
     * - Registers Bukkit-aware type adapter factory
     * @return A configured GsonBuilder instance
     */
    private static GsonBuilder newGsonBuilder() {
        GsonBuilder builder = new GsonBuilder()
                .disableHtmlEscaping()
                .serializeSpecialFloatingPointValues()
                .setLenient()
                .registerTypeAdapterFactory(BukkitAwareObjectTypeAdapter.FACTORY);
        assert builder != null;
        return builder;
    }

    /**
     * Converts an object to a JSON string using the standard Gson instance.
     * The object may be a primitive, Map, List, null, or custom supported types.
     * @param object The object to serialize (nullable)
     * @return JSON string representation
     */
    public static String toJson(@Nullable Object object) {
        return toJson(GSON.get(), object);
    }

    /**
     * Converts an object to a pretty-printed JSON string.
     * @param object The object to serialize (nullable)
     * @return JSON string with pretty formatting
     */
    public static String toPrettyJson(@Nullable Object object) {
        return toJson(GSON_PRETTY.get(), object);
    }

    /**
     * Internal helper to convert an object to JSON using a given Gson instance.
     * @param gson Gson instance to use
     * @param object The object to serialize (nullable)
     * @return JSON string representation
     */
    private static String toJson(Gson gson, @Nullable Object object) {
        String json = gson.toJson(Unsafe.nullableAsNonNull(object));
        return Unsafe.assertNonNull(json);
    }

    /**
     * Deserializes a JSON string into a plain Java object.
     * Does not handle ConfigurationSerializable objects.
     * @param <T> The expected return type
     * @param json JSON string to deserialize
     * @return Deserialized object
     * @throws IllegalArgumentException if deserialization fails
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromPlainJson(@Nullable String json) throws IllegalArgumentException {
        Gson gson = GSON.get();
        try {
            // Uses Gson's default Object TypeAdapter instead of Bukkit-aware one
            return (T) gson.fromJson(Unsafe.nullableAsNonNull(json), Object.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not deserialize object from Json!", e);
        }
    }

    /**
     * Deserializes a JSON string into an object using Bukkit-aware handling.
     * Can deserialize ConfigurationSerializable objects.
     * @param <T> The expected return type
     * @param json JSON string to deserialize
     * @return Deserialized object or null if input is null
     * @throws IllegalArgumentException if deserialization fails
     */
    public static <T> @Nullable T fromJson(@Nullable String json) throws IllegalArgumentException {
        Gson gson = GSON.get();
        return BukkitAwareObjectTypeAdapter.fromJson(gson, json);
    }

    /** Private constructor to prevent instantiation of this utility class. */
    private JsonUtils() {
    }
}
