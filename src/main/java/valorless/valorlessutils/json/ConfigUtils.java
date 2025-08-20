package valorless.valorlessutils.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ConfigUtils {

	// Shared and reused YAML config:
	@SuppressWarnings("nullness:type.argument")
	private static final ThreadLocal<@NonNull YamlConfiguration> YAML_CONFIG = ThreadLocal.withInitial(
			YamlConfiguration::new
	);

	public static Map<@NonNull String, @NonNull Object> getValues(ConfigurationSection section) {
		return Unsafe.castNonNull(section.getValues(false));
	}

	// The given root config section itself is not converted.
	public static void convertSubSectionsToMaps(ConfigurationSection rootSection) {
		rootSection.getValues(false).forEach((key, value) -> {
			assert key != null;
			if (value instanceof ConfigurationSection) {
				ConfigurationSection section = (ConfigurationSection) value;
				// Recursively replace config sections with maps:
				Map<@NonNull String, @NonNull Object> innerSectionMap = getValues(section);
				convertSectionsToMaps(innerSectionMap);
				rootSection.set(key, innerSectionMap);
			}
		});
	}

	// Also converts the given root config section.
	public static Map<@NonNull String, @NonNull Object> convertSectionsToMaps(
			ConfigurationSection rootSection
	) {
		Map<@NonNull String, @NonNull Object> sectionMap = getValues(rootSection);
		convertSectionsToMaps(sectionMap);
		return sectionMap;
	}

	// This requires the given Map to be modifiable.
	public static void convertSectionsToMaps(
			Map<? extends @NonNull String, @NonNull Object> rootMap
	) {
		rootMap.entrySet().forEach(entry -> {
			Object value = entry.getValue();
			if (value instanceof ConfigurationSection) {
				ConfigurationSection section = (ConfigurationSection) value;
				// Recursively replace config sections with maps:
				Map<@NonNull String, @NonNull Object> innerSectionMap = getValues(section);
				convertSectionsToMaps(innerSectionMap);
				entry.setValue(innerSectionMap);
			}
		});
	}

	public static void clearConfigSection(ConfigurationSection configSection) {
		Validate.notNull(configSection, "configSection is null");
		configSection.getKeys(false).forEach(key -> {
			assert key != null;
			configSection.set(key, null);
		});
	}

	public static void setAll(ConfigurationSection configSection, Map<?, ?> map) {
		Validate.notNull(configSection, "configSection is null");
		Validate.notNull(map, "map is null");
		map.forEach((key, value) -> {
			String stringKey = StringUtils.toStringOrNull(key);
			if (stringKey != null) {
				configSection.set(stringKey, value);
			}
		});
	}

	// Mimics Bukkit's serialization. Includes the type key of the given ConfigurationSerializable.
	public static Map<@NonNull String, @NonNull Object> serialize(
			ConfigurationSerializable serializable
	) {
		Validate.notNull(serializable, "serializable is null");
		Map<@NonNull String, @NonNull Object> dataMap = new LinkedHashMap<>();
		dataMap.put(
				ConfigurationSerialization.SERIALIZED_TYPE_KEY,
				ConfigurationSerialization.getAlias(serializable.getClass())
		);
		dataMap.putAll(Unsafe.castNonNull(serializable.serialize()));
		return dataMap;
	}

	// Expects the Map to contain a type key, and any inner serializable data to already be
	// deserialized.
	public static <T extends @NonNull ConfigurationSerializable> @Nullable T deserialize(
			@Nullable Map<? extends @Nullable String, ?> dataMap
	) {
		if (dataMap == null) return null;
		try {
			return Unsafe.cast(ConfigurationSerialization.deserializeObject(
					Unsafe.castNonNull(dataMap)
			));
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Could not deserialize object", ex);
		}
	}

	public static Map<@NonNull String, @NonNull Object> serializeDeeply(
			ConfigurationSerializable serializable
	) {
		Validate.notNull(serializable, "serializable is null");
		Map<@NonNull String, @NonNull Object> dataMap = serialize(serializable);
		serializeDeeply(dataMap);
		return dataMap;
	}

	// This deeply and recursively replaces all serializable elements, as well as
	// ConfigurationSections, in the given Map with their respective serializations. The given Map
	// is expected to be modifiable. But since the inner Maps may be immutable, they may need to be
	// copied.
	public static void serializeDeeply(@Nullable Map<?, @NonNull Object> dataMap) {
		if (dataMap == null) return;
		dataMap.entrySet().forEach(entry -> {
			Object value = entry.getValue();
			if (value instanceof Map) {
				// The Map may be unmodifiable. But since we may need to recursively replace its
				// entries, we need to copy it.
				Map<?, @NonNull Object> innerMap = new LinkedHashMap<>((Map<?, @NonNull ?>) value);
				serializeDeeply(innerMap);
				entry.setValue(innerMap);
			} else if (value instanceof ConfigurationSection) {
				ConfigurationSection section = (ConfigurationSection) value;
				Map<@NonNull String, @NonNull Object> innerSectionMap = getValues(section);
				serializeDeeply(innerSectionMap);
				entry.setValue(innerSectionMap);
			} else if (value instanceof ConfigurationSerializable) {
				ConfigurationSerializable serializable = (ConfigurationSerializable) value;
				Map<@NonNull String, @NonNull Object> innerSerializableData = serializeDeeply(serializable);
				entry.setValue(innerSerializableData);
			}
		});
	}

	// This does not store the given data under any key, but inserts it into the top-level map of a
	// YamlConfiguration.
	// Does not return null, even if the given Map is null.
	// Note: If the given map is the data of a serialized ConfigurationSerializable, and it includes
	// its serialized type key, the produced Yaml output may not be loadable again as a
	// YamlConfiguration, because it will deserialize as a ConfigurationSerializable instead of a
	// Map.
	public static String toFlatConfigYaml(Map<?, ?> map) {
		YamlConfiguration yamlConfig = YAML_CONFIG.get();
		try {
			setAll(yamlConfig, map);
			return yamlConfig.saveToString();
		} finally {
			clearConfigSection(yamlConfig);
		}
	}

	// Does not return null. Returns an empty String if the object is null.
	public static String toConfigYaml(String key, @Nullable Object object) {
		if (object == null) return "";
		YamlConfiguration yamlConfig = YAML_CONFIG.get();
		try {
			yamlConfig.set(key, object);
			return yamlConfig.saveToString();
		} finally {
			yamlConfig.set(key, null);
		}
	}

	public static String toConfigYamlWithoutTrailingNewline(String key, Object object) {
		return StringUtils.stripTrailingNewlines(toConfigYaml(key, object));
	}

	// The input is expected to be a serialized config Map.
	@SuppressWarnings("unchecked")
	public static <T> @Nullable T fromConfigYaml(@Nullable String yamlConfigString, String key) {
		if (StringUtils.isEmpty(yamlConfigString)) return null;
		assert yamlConfigString != null;
		Unsafe.assertNonNull(yamlConfigString);
		YamlConfiguration yamlConfig = YAML_CONFIG.get();
		try {
			yamlConfig.loadFromString(yamlConfigString);
			return (T) yamlConfig.get(key);
		} catch (InvalidConfigurationException e) {
			return null;
		} finally {
			clearConfigSection(yamlConfig);
		}
	}

	// TODO Hack to detect issues during the deserialization of ConfigurationSerializables. Bukkit
	// does not throw exceptions in those cases, but instead only logs an error and then
	// deserializes the value as null.
	// When an error is detected, we wrap it into an InvalidConfigurationException.
	public static void loadConfigSafely(
			FileConfiguration config,
			String contents
	) throws InvalidConfigurationException {
		Validate.notNull(config, "config is null");
		// Get the logger that is used during the deserialization of ConfigurationSerializables:
		Logger configSerializationLogger = Logger.getLogger(ConfigurationSerialization.class.getName());

		// Capture the current logger state:
		Handler[] handlers = configSerializationLogger.getHandlers();
		boolean useParent = configSerializationLogger.getUseParentHandlers();
		try {
			// Disable logging:
			for (Handler handler : handlers) {
				configSerializationLogger.removeHandler(handler);
			}
			configSerializationLogger.setUseParentHandlers(false);

			// Register our own error detection handler:

			// Load the config:
			config.loadFromString(contents);

		} finally {
			// Reset the error detection handler:

			// Restore the previous logger state:
			for (Handler handler : handlers) {
				configSerializationLogger.addHandler(handler);
			}
			configSerializationLogger.setUseParentHandlers(useParent);
		}
	}

	private ConfigUtils() {
	}
}