package valorless.valorlessutils.json;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonUtils {

	@SuppressWarnings("nullness:type.argument")
	private static final ThreadLocal<@NonNull Gson> GSON = ThreadLocal.withInitial(() -> {
		Gson gson = newGsonBuilder().create();
		return Unsafe.assertNonNull(gson);
	});
	@SuppressWarnings("nullness:type.argument")
	private static final ThreadLocal<@NonNull Gson> GSON_PRETTY = ThreadLocal.withInitial(() -> {
		Gson gson = newGsonBuilder().setPrettyPrinting().create();
		return Unsafe.assertNonNull(gson);
	});

	// A GsonBuilder with the common default configuration applied:
	private static GsonBuilder newGsonBuilder() {
		GsonBuilder builder = new GsonBuilder()
				.disableHtmlEscaping()
				.serializeSpecialFloatingPointValues()
				.setLenient()
				.registerTypeAdapterFactory(BukkitAwareObjectTypeAdapter.FACTORY);
		assert builder != null;
		return builder;
	}

	// The object may be one of the primitives supported by Gson by default (primitive, Map, List,
	// null, ...), or one of our custom supported types such as ConfigurationSerializable. However,
	// see the limitations mentioned in BukkitAwareObjectTypeAdapter.
	public static String toJson(@Nullable Object object) {
		return toJson(GSON.get(), object);
	}

	public static String toPrettyJson(@Nullable Object object) {
		return toJson(GSON_PRETTY.get(), object);
	}

	private static String toJson(Gson gson, @Nullable Object object) {
		String json = gson.toJson(Unsafe.nullableAsNonNull(object));
		return Unsafe.assertNonNull(json);
	}

	// Does not deserialize ConfigurationSerializables.
	@SuppressWarnings("unchecked")
	public static <T> T fromPlainJson(@Nullable String json) throws IllegalArgumentException {
		Gson gson = GSON.get();
		try {
			// This is expected to return Gson's default Object TypeAdapter instead of our custom
			// Bukkit-aware one.
			return (T) gson.fromJson(Unsafe.nullableAsNonNull(json), Object.class);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not deserialize object from Json!", e);
		}
	}

	public static <T> @Nullable T fromJson(@Nullable String json) throws IllegalArgumentException {
		Gson gson = GSON.get();
		return BukkitAwareObjectTypeAdapter.fromJson(gson, json);
	}

	private JsonUtils() {
	}
}