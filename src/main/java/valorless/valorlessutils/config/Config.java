package valorless.valorlessutils.config;

import valorless.annotations.MarkedForRemoval;
import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.ValorlessUtils.Log;
import valorless.valorlessutils.utils.Utils;
import valorless.valorlessutils.file.YamlFile;
import valorless.valorlessutils.types.Vector2;
import valorless.valorlessutils.types.Vector3;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * YAML-backed configuration wrapper for ValorlessUtils and dependent plugins.
 *
 * <p>This class wraps {@link YamlFile} and exposes convenience getters/setters for common Bukkit and
 * utility types, while also providing a lightweight <em>validation</em> mechanism to ensure that
 * required keys exist in the configuration.</p>
 *
 * <h2>File lifecycle</h2>
 * <ul>
 *   <li>{@link #Config(JavaPlugin, String)}: creates/loads a config located in the plugin's data
 *       folder. If missing, the resource is copied from the plugin JAR via
 *       {@link JavaPlugin#saveResource(String, boolean)}.</li>
 *   <li>{@link #Config(File)}, {@link #Config(Path)}, {@link #Config(String)}: load an existing file
 *       by location; if the file does not exist, an {@link IllegalArgumentException} is thrown.</li>
 * </ul>
 *
 * <h2>Supported value types</h2>
 * <p>In addition to primitive and boxed types that Bukkit/YAML supports by default, this wrapper
 * includes helpers for:</p>
 * <ul>
 *   <li>{@link valorless.valorlessutils.types.Vector2} and {@link valorless.valorlessutils.types.Vector3}
 *       stored as nested keys (e.g. {@code myVector.x}, {@code myVector.y}, {@code myVector.z}).</li>
 *   <li>{@link org.bukkit.Material} stored by its {@link Enum#name()} value.</li>
 *   <li>{@link org.bukkit.inventory.ItemStack} stored using Bukkit's built-in serialization.</li>
 *   <li>Lists (string/int/double and generic lists) using Bukkit's configuration API.</li>
 * </ul>
 *
 * <h2>Validation</h2>
 * <p>Validation entries can be registered via {@link #AddValidationEntry(String, Object)} (including
 * vector overloads). When {@link #Validate()} runs, missing keys are added with their default values
 * and the configuration is saved. {@link #Reload()} also triggers validation.</p>
 *
 * <h2>Notes</h2>
 * <ul>
 *   <li>Most setters update the in-memory configuration only; call {@link #SaveConfig()} to persist.</li>
 *   <li>{@link #HasKey(String)} and section accessors log errors and may return {@code null} when
 *       invoked with a {@code null} or empty key.</li>
 * </ul>
 */
public class Config {

	/** The YAML configuration file. */
	private YamlFile file;

	/** The plugin instance. */
	private final JavaPlugin plugin;

	/**
	 * Constructs a new Config object.
	 * <p>
	 * If the file does not exist, it will be copied from the plugin's resources.
	 * </p>
	 *
	 * @param plugin The JavaPlugin instance.
	 * @param file   The name of the configuration file.
	 */
	public Config(JavaPlugin plugin, String file) {
		this.plugin = plugin;
		this.file = new YamlFile(new File(plugin.getDataFolder(), file));
		if (!this.file.fileExists()) {
			plugin.saveResource(file, false);
			this.file = new YamlFile(new File(plugin.getDataFolder(), file));
		}
		this.Validate();
	}
	
	/**
	 * Constructs a new Config object.
	 * <p>
	 * If the file does not exist, an error is thrown.
	 * </p>
	 *
	 * @param file   The File of the configuration file.
	 */
	public Config(File file) {
		plugin = ValorlessUtils.plugin;
		this.file = new YamlFile(file);
		if (!this.file.fileExists()) {
			throw new IllegalArgumentException("Config was initialized with a file that does not exist: " + file.getPath());
		}
		this.Validate();
	}
	
	/**
	 * Constructs a new Config object.
	 * <p>
	 * If the file does not exist, an error is thrown.
	 * </p>
	 *
	 * @param path   The Path of the configuration file.
	 */
	public Config(Path path) {
		plugin = ValorlessUtils.plugin;
		this.file = new YamlFile(path.toString());
		if (!this.file.fileExists()) {
			throw new IllegalArgumentException("Config was initialized with a file that does not exist: " + path.toString());
		}
		this.Validate();
	}
	
	/**
	 * Constructs a new Config object.
	 * <p>
	 * If the file does not exist, an error is thrown.
	 * </p>
	 *
	 * @param path   The path of the configuration file.
	 */
	public Config(String path) {
		plugin = ValorlessUtils.plugin;
		this.file = new YamlFile(path);
		if (!this.file.fileExists()) {
			throw new IllegalArgumentException("Config was initialized with a file that does not exist: " + path);
		}
		this.Validate();
	}

	/**
	 * Returns the plugin instance associated with this config.
	 *
	 * <p>When this {@link Config} instance was created without an explicit plugin (e.g. via the
	 * {@link #Config(File)}, {@link #Config(Path)} or {@link #Config(String)} constructors), the
	 * implementation currently logs an error and may return {@code null}.</p>
	 *
	 * @return the attached {@link JavaPlugin}, or {@code null} when no plugin is attached.
	 */
	public JavaPlugin GetPlugin() {
		if(ValorlessUtils.plugin.getName().equalsIgnoreCase(this.plugin.getName())) {
			Log.Error(ValorlessUtils.plugin, "config.GetPlugin() was called but no plugin was attached to the instance.");
			return null;
		}
		return this.plugin;
	}

	/**
	 * Gets a string value.
	 *
	 * @param key config path
	 * @return string value or {@code null} if not present
	 */
	public String GetString(String key) {
		return this.file.getConfig().getString(key);
	}
	
	/**
	 * Sets a string value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #SaveConfig()
	 */
	public void SetString(String key, String value) {
		Set(key, value);
	}

	/**
	 * Gets a boolean value.
	 *
	 * @param key config path
	 * @return boolean value (defaults to {@code false} if missing)
	 */
	public Boolean GetBool(String key) {
		return this.file.getConfig().getBoolean(key);
	}
	
	/**
	 * Sets a boolean value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #SaveConfig()
	 */
	public void SetBool(String key, Boolean value) {
		Set(key, value);
	}

	/**
	 * Gets an integer value.
	 *
	 * @param key config path
	 * @return integer value (defaults to {@code 0} if missing)
	 */
	public Integer GetInt(String key) {
		return this.file.getConfig().getInt(key);
	}
	
	/**
	 * Sets an integer value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #SaveConfig()
	 */
	public void SetInt(String key, Integer value) {
		Set(key, value);
	}

	/** 
	 * Retrieves a float (double) value from the config.
	 * @deprecated This method is outdated and will be removed in future versions.
	 */
	@Deprecated @MarkedForRemoval
	public Double GetFloat(String key) {
		return this.file.getConfig().getDouble(key);
	}

	/**
	 * Gets a double value.
	 *
	 * @param key config path
	 * @return double value (defaults to {@code 0.0} if missing)
	 */
	public Double GetDouble(String key) {
		return this.file.getConfig().getDouble(key);
	}
	
	/**
	 * Sets a double value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #SaveConfig()
	 */
	public void SetDouble(String key, Double value) {
		Set(key, value);
	}

	/**
	 * Gets a 2D vector.
	 *
	 * <p>Values are read from nested keys: {@code key + ".x"} and {@code key + ".y"}.</p>
	 *
	 * @param key base config path
	 * @return vector composed from the stored x/y values (may contain {@code null}s if keys are missing)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Number> Vector2<T> GetVector2(String key) {
		return new Vector2((T) this.file.getConfig().get(key + ".x"), (T) this.file.getConfig().get(key + ".y"));
	}
	
	/**
	 * Sets a 2D vector in-memory.
	 *
	 * <p>Values are written to nested keys: {@code key + ".x"} and {@code key + ".y"}.</p>
	 *
	 * @param key base config path
	 * @param value vector to store
	 * @see #SaveConfig()
	 */
	public <T extends Number> void SetVector2(String key, Vector2<T> value) {
		Set(key + ".x", value.x);
		Set(key + ".y", value.y);
	}

	/**
	 * Gets a 3D vector.
	 *
	 * <p>Values are read from nested keys: {@code key + ".x"}, {@code key + ".y"}, {@code key + ".z"}.</p>
	 *
	 * @param key base config path
	 * @return vector composed from the stored x/y/z values (may contain {@code null}s if keys are missing)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Number> Vector3<T> GetVector3(String key) {
		return new Vector3(
				(T) this.file.getConfig().get(key + ".x"),
				(T) this.file.getConfig().get(key + ".y"),
				(T) this.file.getConfig().get(key + ".z")
				);
	}
	
	/**
	 * Sets a 3D vector in-memory.
	 *
	 * <p>Values are written to nested keys: {@code key + ".x"}, {@code key + ".y"}, {@code key + ".z"}.</p>
	 *
	 * @param key base config path
	 * @param value vector to store
	 * @see #SaveConfig()
	 */
	public <T extends Number> void SetVector3(String key, Vector3<T> value) {
		Set(key + ".x", value.x);
		Set(key + ".y", value.y);
		Set(key + ".z", value.z);
	}

	/**
	 * Gets a {@link Material} by name.
	 *
	 * @param key config path containing the material name
	 * @return resolved material, or {@code null} if the name is missing/invalid
	 */
	public Material GetMaterial(String key) {
		return Material.getMaterial(GetString(key));
	}
	
	/**
	 * Stores a {@link Material} as its enum name.
	 *
	 * @param key config path
	 * @param material material to store
	 * @see #SaveConfig()
	 */
	public void SetMaterial(String key, Material material) {
		Set(key, material.toString());
	}

	/**
	 * Gets a raw value from the underlying Bukkit configuration.
	 *
	 * @param key config path
	 * @return stored value, or {@code null} if missing
	 */
	public Object Get(String key) {
		return this.file.getConfig().get(key);
	}

	/**
	 * Sets a raw value in the underlying Bukkit configuration (in-memory).
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #SaveConfig()
	 */
	public void Set(String key, Object value) {
		this.file.getConfig().set(key, value);
	}

	/**
	 * Gets a list of strings.
	 *
	 * @param key config path
	 * @return list (never {@code null}; may be empty)
	 */
	public List<String> GetStringList(String key) {
		return this.file.getConfig().getStringList(key);
	}
	
	/**
	 * Sets a list of strings in-memory.
	 *
	 * @param key config path
	 * @param value list to store
	 * @see #SaveConfig()
	 */
	public void SetStringList(String key, List<String> value) {
		Set(key, value);
	}

	/**
	 * Gets a list of integers.
	 *
	 * @param key config path
	 * @return list (never {@code null}; may be empty)
	 */
	public List<Integer> GetIntList(String key) {
		return this.file.getConfig().getIntegerList(key);
	}
	
	/**
	 * Sets a list of integers in-memory.
	 *
	 * @param key config path
	 * @param value list to store
	 * @see #SaveConfig()
	 */
	public void SetIntList(String key, List<Integer> value) {
		Set(key, value);
	}

	/**
	 * Gets a list of doubles.
	 *
	 * @param key config path
	 * @return list (never {@code null}; may be empty)
	 */
	public List<Double> GetDoubleList(String key) {
		return this.file.getConfig().getDoubleList(key);
	}
	
	/**
	 * Sets a list of doubles in-memory.
	 *
	 * @param key config path
	 * @param value list to store
	 * @see #SaveConfig()
	 */
	public void SetDoubleList(String key, List<Double> value) {
		Set(key, value);
	}

	/**
	 * Gets a raw list.
	 *
	 * @param key config path
	 * @return list, or {@code null} if missing
	 */
	public List<?> GetList(String key) {
		return this.file.getConfig().getList(key);
	}
	
	/**
	 * Sets a raw list in-memory.
	 *
	 * @param key config path
	 * @param value list to store
	 * @see #SaveConfig()
	 */
	public void SetList(String key, List<?> value) {
		Set(key, value);
	}

	/**
	 * Gets an {@link ItemStack}.
	 *
	 * @param key config path
	 * @return deserialized item, or {@code null} if missing
	 */
	public ItemStack GetItemStack(String key) {
		return this.file.getConfig().getItemStack(key);
	}
	
	/**
	 * Stores an {@link ItemStack} (in-memory).
	 *
	 * @param key config path
	 * @param item item to store
	 * @see #SaveConfig()
	 */
	public void SetItemStack(String key, ItemStack item) {
		Set(key, item);
	}

	/**
	 * Checks whether a key exists.
	 *
	 * @param key config path
	 * @return {@code true} if present; {@code false} if not present; or {@code null} when {@code key}
	 *         is {@code null}/empty (also logs an error)
	 */
	public Boolean HasKey(String key) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.HasKey() was called with a null or empty key!");
			return null;
		}
		return this.file.getConfig().contains(key, true);
	}

	/**
	 * Gets a {@link ConfigurationSection} at the given key.
	 *
	 * <p>This first attempts {@link YamlFile#getConfigurationSection(String)}. If that returns
	 * {@code null}, it falls back to {@link #GetSection(String)} for compatibility with different
	 * underlying YAML implementations.</p>
	 *
	 * @param key config path
	 * @return the configuration section for {@code key}, or {@code null} if {@code key} is
	 *         {@code null}/empty (logs an error) or the section does not exist
	 */
	public ConfigurationSection GetConfigurationSection(String key) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.GetConfigurationSection() was called with a null or empty key!");
			return null;
		}
		
		ConfigurationSection section = GetFile().getConfigurationSection(key);
		return section != null ? section : GetSection(key);
	}

	/**
	 * Gets a section at the given key.
	 *
	 * @param key config path
	 * @return the section, or {@code null} if missing/invalid key
	 */
	public ConfigurationSection GetSection(String key) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.GetSection() was called with a null or empty key!");
			return null;
		}
		return GetFile().getSection(key);
	}

	/**
	 * Gets comments associated with a key.
	 *
	 * @param key config path
	 * @return comment lines, or {@code null} if {@code key} is {@code null}/empty
	 */
	public List<String> GetComments(String key) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.GetComment() was called with a null or empty key!");
			return null;
		}
		return GetFile().getComments(key);
	}

	/**
	 * Sets comments for a key.
	 *
	 * @param key config path
	 * @param comments comment lines to store
	 */
	public void SetComments(String key, List<String> comments) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.SetComment() was called with a null or empty key!");
		}
		GetFile().setComments(key, comments);
	}

	/**
	 * Returns the underlying YAML file wrapper.
	 *
	 * @return backing {@link YamlFile}
	 */
	public YamlFile GetFile() {
		return this.file;
	}

	/**
	 * Reloads the configuration from disk and runs {@link #Validate()}.
	 */
	public void Reload() {
		this.file.reload();
		this.Validate();
	}

	/**
	 * Saves the configuration to disk.
	 */
	public void SaveConfig() {
		this.file.save();
	}

	// ====================
	// Validation
	// ====================

	/**
	 * Represents a key-value entry for configuration validation.
	 */
	public class ValidationListEntry {
		public String key;
		public Object defaultValue;
		public List<String> comments;	

		/** Constructs a ValidationListEntry with a key and default value. */
		public ValidationListEntry(String key, Object defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}
		
		 /** Constructs a ValidationListEntry with a key, default value, and comments. */
		public ValidationListEntry(String key, Object defaultValue, List<String> comments) {
			this.key = key;
			this.defaultValue = defaultValue;
			this.comments = comments;
		}	
	}

	/** List of entries used for validating the config. */
	List<ValidationListEntry> validationList = new ArrayList<>();

	/**
	 * Adds a validation entry.
	 *
	 * <p>During {@link #Validate()}, missing keys will be created with {@code value}.</p>
	 *
	 * @param key config path
	 * @param value default value to write when missing
	 */
	public void AddValidationEntry(String key, Object value) {
		this.validationList.add(new ValidationListEntry(key, value));
	}
	
	/**
	 * Adds a validation entry with comments.
	 *
	 * @param key config path
	 * @param value default value to write when missing
	 * @param comments comment lines to associate with the key
	 */
	public void AddValidationEntry(String key, Object value, List<String> comments) {
		this.validationList.add(new ValidationListEntry(key, value, comments));
	}

	/**
	 * Adds validation entries for a {@link Vector2} under {@code key}.
	 *
	 * <p>This registers {@code key + ".x"} and {@code key + ".y"}.</p>
	 *
	 * @param key base config path
	 * @param value vector providing default x/y values
	 */
	public <T extends Number> void AddValidationEntry(String key, Vector2<T> value) {
		this.validationList.add(new ValidationListEntry(key + ".x", value.x));
		this.validationList.add(new ValidationListEntry(key + ".y", value.y));
	}

	/**
	 * Adds validation entries for a {@link Vector3} under {@code key}.
	 *
	 * <p>This registers {@code key + ".x"}, {@code key + ".y"} and {@code key + ".z"}.</p>
	 *
	 * @param key base config path
	 * @param value vector providing default x/y/z values
	 */
	public <T extends Number> void AddValidationEntry(String key, Vector3<T> value) {
		this.validationList.add(new ValidationListEntry(key + ".x", value.x));
		this.validationList.add(new ValidationListEntry(key + ".y", value.y));
		this.validationList.add(new ValidationListEntry(key + ".z", value.z));
	}

	/**
	 * Ensures all registered validation keys exist.
	 *
	 * <p>If any keys are missing, they are added with their defaults and the file is saved.</p>
	 */
	public void Validate() {
		Boolean missing = false;
		if (this.GetBool("debug")) {
			Log.Debug(plugin, "Validating " + file.getFile().getName() + "...");
		}

		for (ValidationListEntry item : this.validationList) {
			if (!this.HasKey(item.key)) {
				this.Set(item.key, item.defaultValue);
				missing = true;
			}
		}

		if (missing) {
			Log.Warning(plugin, "New or missing config values have been added.");
			this.SaveConfig();
		}
	}
}
