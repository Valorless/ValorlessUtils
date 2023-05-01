package valorless.valorlessutils.json;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * A Gson {@link TypeAdapter} that deserializes any kind of Json primitive (Objects/Maps, Lists,
 * Strings, numbers, etc.), but additionally also deserializes any contained
 * {@link ConfigurationSerializable ConfigurationSerializables}.
 * <p>
 * For objects that are not serialized ConfigurationSerializables, this TypeAdapter behaves similar
 * to Gson's default Object TypeAdapter, with the few exceptions mentioned by
 * {@link YamlLikeObjectTypeAdapter}. This TypeAdapter can therefore be used when the type of the
 * object to deserialize is not known in advance, but may contain serialized
 * ConfigurationSerializables.
 * <p>
 * This TypeAdapter can be registered with a {@link Gson} instance, which is then able to serialize
 * any ConfigurationSerializables. However, since Gson does not allow its default Object TypeAdapter
 * to be overridden, this TypeAdapter has to be explicitly invoked when deserializing objects of
 * unknown type: Calling {@code Gson.fromJson(json, Object.class)} will not invoke this TypeAdapter
 * and therefore also not deserialize any ConfigurationSerializables. If you have a {@link Gson}
 * instance that has this TypeAdapter registered, you can use {@link #fromJson(Gson, String)} as a
 * convenient way to parse a given Json input using this registered TypeAdapter.
 * <p>
 * However, note that even though the serialization and deserialization of
 * ConfigurationSerializables may work in most cases, Json has some differences to Yaml that make it
 * unsuited to reliably serialize and deserialize ConfigurationSerializables in general. Some of
 * these differences are handled by {@link YamlLikeObjectTypeAdapter}. Another noteworthy difference
 * is that Yaml supports the representation of object hierarchies, i.e. the references between
 * objects, via so called 'anchors': If one object is referenced by multiple other objects, Yaml can
 * restore this object hierarchy, whereas Json by default can not. There are some extensions to Json
 * that intend to make it possible to persist such object references. However, neither Gson nor this
 * TypeAdapter implement one of these solutions yet. For maximum compatibility with Bukkit's
 * serialization API, it is therefore recommended sticking to Yaml when the Json format is not
 * strictly required.
 */
// Extends YamlLikeObjectTypeAdapter for better compatibility with Bukkit's serialization API.
public class BukkitAwareObjectTypeAdapter extends YamlLikeObjectTypeAdapter {

	public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
		@SuppressWarnings({ "unchecked", "override.return" })
		@Override
		public <T> @Nullable TypeAdapter<T> create(
				@Nullable Gson gson,
				@Nullable TypeToken<T> type
		) {
			assert gson != null && type != null;
			Unsafe.assertNonNull(gson);
			Unsafe.assertNonNull(type);
			Class<?> rawType = type.getRawType();
			assert rawType != null;
			// The rawType == Object.class case is not expected to actually occur currently, because
			// Gson does not yet allow its default Object TypeAdapter to be overridden. As a
			// consequence, this TypeAdapter has to always be explicitly invoked currently. However,
			// in case this behavior of Gson changes at some point, this TypeAdapter is meant to
			// override the default Object TypeAdapter.
			if (rawType == Object.class
					|| ConfigurationSerializable.class.isAssignableFrom(rawType)) {
				return (TypeAdapter<T>) BukkitAwareObjectTypeAdapter.create(gson);
			}
			return null;
		}
	};

	/**
	 * Creates a {@link BukkitAwareObjectTypeAdapter} for the given {@link Gson} instance.
	 * 
	 * @param gson
	 *            the Gson instance, not <code>null</code>
	 * @return the created TypeAdapter
	 */
	public static TypeAdapter<Object> create(Gson gson) {
		return new BukkitAwareObjectTypeAdapter(gson);
	}

	/**
	 * Shortcut to conveniently parse an object of unknown type from the given Json String using the
	 * {@link BukkitAwareObjectTypeAdapter} from the given {@link Gson} instance.
	 * <p>
	 * The Gson instance is expected to have the {@link BukkitAwareObjectTypeAdapter} registered as
	 * the {@link TypeAdapter} for {@link ConfigurationSerializable}. For best compatibility with
	 * Bukkit's serialization API, it is also recommended for the Gson instance to be configured to
	 * be {@link GsonBuilder#setLenient() lenient}.
	 * 
	 * @param <T>
	 *            the type of the expected object, or {@link Object} if unknown
	 * @param gson
	 *            the Gson instance, not <code>null</code>
	 * @param json
	 *            the Json String to parse
	 * @return the parsed object, or <code>null</code> if the Json input is <code>null</code> or
	 *         empty
	 * @throws IllegalArgumentException
	 *             if the Json could not be parsed or the object could not be deserialized correctly
	 */
	@SuppressWarnings("unchecked")
	public static <T> @Nullable T fromJson(
			Gson gson,
			@Nullable String json
	) throws IllegalArgumentException {
		Validate.notNull(gson, "gson is null");
		if (json == null || json.isEmpty()) {
			// Gson also returns null for empty documents:
			return null;
		}

		// Retrieve the BukkitAwareObjectTypeAdapter:
		TypeAdapter<Object> bukkitAwareObjectTypeAdapter = getBukkitAwareObjectTypeAdapter(gson);

		// Get a JsonReader that is configured according to the given Gson instance:
		JsonReader jsonReader = gson.newJsonReader(new StringReader(json));
		assert jsonReader != null;

		try {
			// Note: Unlike Gson#fromJson, this actually takes the lenient flag of the Gson instance
			// into account.
			T value = (T) bukkitAwareObjectTypeAdapter.read(jsonReader);
			// Ensure that we actually consumed all of the input:
			assertEmptyReader(jsonReader);
			return value;
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not deserialize object from Json!", e);
		}
	}

	private static BukkitAwareObjectTypeAdapter getBukkitAwareObjectTypeAdapter(Gson gson) {
		TypeAdapter<?> typeAdapter = gson.getAdapter(ConfigurationSerializable.class);
		if (!(typeAdapter instanceof BukkitAwareObjectTypeAdapter)) {
			throw new IllegalArgumentException(
					"Could not retrieve the BukkitAwareObjectTypeAdapter from the given Gson instance!"
			);
		}
		return (BukkitAwareObjectTypeAdapter) typeAdapter;
	}

	// This mimics the check done by Gson when using Gson#fromJson.
	private static void assertEmptyReader(JsonReader reader) throws IllegalArgumentException {
		try {
			if (reader.peek() != JsonToken.END_DOCUMENT) {
				throw new IllegalArgumentException("Json document was not fully consumed!");
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	// -----

	protected BukkitAwareObjectTypeAdapter(Gson gson) {
		super(gson);
	}

	@SuppressWarnings({ "unchecked", "override.return" })
	@Override
	public @Nullable Object read(@Nullable JsonReader in) throws IOException {
		assert in != null;
		Object value = super.read(in);
		if (value instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) value;
			if (map.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
				return ConfigUtils.deserialize(map);
			} // Else: This is a regular Map.
		}
		return value;
	}

	@Override
	public void write(@Nullable JsonWriter out, @Nullable Object value) throws IOException {
		assert out != null;
		Object prepared = value;
		if (value instanceof ConfigurationSerializable) {
			prepared = ConfigUtils.serialize((ConfigurationSerializable) value);
		}
		super.write(out, prepared);
	}
}