package valorless.valorlessutils.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Modified version of Gson's default Object {@link TypeAdapter}.
 * <p>
 * Bukkit's serialization API is built around Yaml, and specifically SnakeYaml. Objects that are
 * deserialized via Bukkit's serialization API may have strict expectations regarding the types of
 * data they encounter during deserialization. This also applies to some of Bukkit's built-in
 * serializable types, such as ItemMeta. For compatibility reasons, this {@link TypeAdapter} is
 * supposed to produce types that more closely match those produced by SnakeYaml. Most notable
 * differences to Gson's default types are:
 * <ul>
 * <li>We produce LinkedHashMap's instead of Gson's LinkedTreeMap.
 * <li>We delegate the parsing of numbers to SnakeYaml, which produces integers, longs, or
 * BigInteger for whole numbers and doubles for fractional numbers, whereas Gson always produces
 * doubles by default.
 * <li>Gson represents special numbers like {@link Double#NaN} or infinities as Strings, but doesn't
 * automatically return them as double anymore. We check if a loaded String can be parsed as one of
 * these special numbers, and then return this number instead.
 * </ul>
 * <p>
 * Gson does not allow the default Object TypeAdapter to be overridden. This TypeAdapter therefore
 * has to be explicitly invoked whenever it is supposed to be used.
 * <p>
 * Another alternative could be to deserialize objects from Json via a Yaml parser: In principle,
 * since Json is supposed to be a subset of Yaml, this should work. However, there is no guarantee
 * that the produced Json actually conforms to standard Json: For instance, Json does not support
 * special numbers like {@link Double#NaN} or infinities. Their representation is therefore specific
 * to the used Json generator and there is no guarantee that a certain Yaml parser is then able to
 * parse those numbers correctly.
 * <p>
 * There are further limitations that make Json less suited for the serialization of
 * {@link ConfigurationSerializable ConfigurationSerializables}: See
 * {@link BukkitAwareObjectTypeAdapter}.
 */
public class YamlLikeObjectTypeAdapter extends TypeAdapter<Object> {

	public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
		@SuppressWarnings("override.return")
		@Override
		public <T> @Nullable TypeAdapter<T> create(
				@Nullable Gson gson,
				@Nullable TypeToken<T> type
		) {
			assert gson != null && type != null;
			Unsafe.assertNonNull(gson);
			Unsafe.assertNonNull(type);
			// This is not actually expected to work currently, since Gson doesn't allow its default
			// Object TypeAdapter to be overridden yet.
			if (type.getRawType() == Object.class) {
				return Unsafe.cast(YamlLikeObjectTypeAdapter.create(gson));
			}
			return null;
		}
	};

	public static TypeAdapter<Object> create(Gson gson) {
		return new YamlLikeObjectTypeAdapter(gson);
	}

	private final Gson gson;

	protected YamlLikeObjectTypeAdapter(Gson gson) {
		Validate.notNull(gson, "gson is null");
		this.gson = gson;
	}

	@SuppressWarnings("override.return")
	@Override
	public @Nullable Object read(@Nullable JsonReader in) throws IOException {
		assert in != null;
		Unsafe.assertNonNull(in);
		JsonToken token = in.peek();
		switch (token) {
		case BEGIN_ARRAY:
			List<@Nullable Object> list = new ArrayList<>();
			in.beginArray();
			while (in.hasNext()) {
				// This recursively uses this custom Object TypeAdapter:
				list.add(this.read(in));
			}
			in.endArray();
			return list;
		case BEGIN_OBJECT:
			// We use a LinkedHashMap instead of Gson's LinkedTreeMap:
			Map<@NonNull String, @Nullable Object> map = new LinkedHashMap<>();
			in.beginObject();
			while (in.hasNext()) {
				// This recursively uses this custom Object TypeAdapter:
				map.put(Unsafe.assertNonNull(in.nextName()), this.read(in));
			}
			in.endObject();
			return map;
		case STRING:
			String string = in.nextString();
			assert string != null;
			if (in.isLenient()) {
				// Check if we can parse the String as one of the special numbers (NaN and
				// infinities):
				try {
					double number = Double.parseDouble(string);
					if (!Double.isFinite(number)) {
						return number;
					} // Finite numbers are not expected to be represented as String
				} catch (NumberFormatException e) {
				}
			}
			return string;
		case NUMBER:
			// We delegate the number parsing to the Yaml parser:
			String numberString = in.nextString();
			assert numberString != null;
			Object number = YamlUtils.fromYaml(numberString);
			if (!(number instanceof Number)) {
				throw new IllegalStateException("Could not parse number: " + numberString);
			}
			return number;
		case BOOLEAN:
			return in.nextBoolean();
		case NULL:
			in.nextNull();
			return null;
		default:
			throw new IllegalStateException();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(@Nullable JsonWriter out, @Nullable Object value) throws IOException {
		assert out != null;
		Unsafe.assertNonNull(out);
		if (value == null) {
			out.nullValue();
			return;
		}

		// Check if we find a more specific and therefore better suited TypeAdapter for the given
		// object:
		Class<?> clazz = value.getClass();
		TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) gson.getAdapter(clazz);
		// If there is no TypeAdapter registered that is more specific than Object, and if this
		// custom Object TypeAdapter has been registered and was able to replace Gson's default
		// Object TypeAdapter (which is not expected to be the case currently), we break the cycle
		// and output an empty object:
		if (typeAdapter instanceof YamlLikeObjectTypeAdapter) {
			out.beginObject();
			out.endObject();
			return;
		}

		// Delegate to the found TypeAdapter:
		typeAdapter.write(out, value);
	}
}