package valorless.valorlessutils.config;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import valorless.annotations.MarkedForRemoval;
import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.logging.Log;
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
	 * <p>Validation entries can be registered via {@link #addValidationEntry(String, Object)} (including
	 * vector overloads). When {@link #validate()} runs, missing keys are added with their default values
	 * and the configuration is saved. {@link #reload()} reloads the file content from disk.</p>
 *
 * <h2>Notes</h2>
 * <ul>
	 *   <li>Most setters update the in-memory configuration only; call {@link #saveConfig()} to persist.</li>
	 *   <li>{@link #hasKey(String)} and section accessors log errors and may return {@code null} when
 *       invoked with a {@code null} or empty key.</li>
 * </ul>
 */
public class Config {

	/** The YAML configuration file. */
	private final YamlFile file;

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
		if (!new File(plugin.getDataFolder(), file).exists()) {
			plugin.saveResource(file, false);
			this.file = new YamlFile(new File(plugin.getDataFolder(), file));
		}else{
			this.file = new YamlFile(new File(plugin.getDataFolder(), file));
		}
		//this.validate();
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
		//this.validate();
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
		//this.validate();
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
		//this.validate();
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
	public JavaPlugin getPlugin() {
		if(ValorlessUtils.plugin.getName().equalsIgnoreCase(this.plugin.getName())) {
			Log.error(ValorlessUtils.plugin, "config.getPlugin() was called but no plugin was attached to the instance.");
			return null;
		}
		return this.plugin;
	}

	/**
	 * @deprecated Use {@link #getPlugin()}.
	 */
	@Deprecated @MarkedForRemoval
	public JavaPlugin GetPlugin() {
		return getPlugin();
	}

	/**
	 * Gets a string value.
	 *
	 * @param key config path
	 * @return string value or {@code null} if not present
	 */
	public String getString(String key) {
		return this.file.getConfig().getString(key);
	}

	/**
	 * @deprecated Use {@link #getString(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public String GetString(String key) {
		return getString(key);
	}

	/**
	 * Sets a string value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #saveConfig()
	 */
	public void setString(String key, String value) {
		set(key, value);
	}

	/**
	 * @deprecated Use {@link #setString(String, String)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetString(String key, String value) {
		setString(key, value);
	}

	/**
	 * Gets a boolean value.
	 *
	 * @param key config path
	 * @return boolean value (defaults to {@code false} if missing)
	 */
	public Boolean getBool(String key) {
		return this.file.getConfig().getBoolean(key);
	}

	/**
	 * @deprecated Use {@link #getBool(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public Boolean GetBool(String key) {
		return getBool(key);
	}

	/**
	 * Sets a boolean value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #saveConfig()
	 */
	public void setBool(String key, Boolean value) {
		set(key, value);
	}

	/**
	 * @deprecated Use {@link #setBool(String, Boolean)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetBool(String key, Boolean value) {
		setBool(key, value);
	}

	/**
	 * Gets an integer value.
	 *
	 * @param key config path
	 * @return integer value (defaults to {@code 0} if missing)
	 */
	public Integer getInt(String key) {
		return this.file.getConfig().getInt(key);
	}

	/**
	 * @deprecated Use {@link #getInt(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public Integer GetInt(String key) {
		return getInt(key);
	}

	/**
	 * Sets an integer value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #saveConfig()
	 */
	public void setInt(String key, Integer value) {
		set(key, value);
	}

	/**
	 * @deprecated Use {@link #setInt(String, Integer)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetInt(String key, Integer value) {
		setInt(key, value);
	}

	/**
	 * Retrieves a float (double) value from the config.
	 *
	 * @param key config path
	 * @return double value stored at {@code key}
	 * @deprecated This method is outdated and will be removed in future versions. Use {@link #getDouble(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public Double getFloat(String key) {
		return this.file.getConfig().getDouble(key);
	}

	/**
	 * Retrieves a float (double) value from the config.
	 *
	 * @param key config path
	 * @return double value stored at {@code key}
	 * @deprecated Use {@link #getFloat(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public Double GetFloat(String key) {
		return getFloat(key);
	}

	/**
	 * Gets a double value.
	 *
	 * @param key config path
	 * @return double value (defaults to {@code 0.0} if missing)
	 */
	public Double getDouble(String key) {
		return this.file.getConfig().getDouble(key);
	}

	/**
	 * @deprecated Use {@link #getDouble(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public Double GetDouble(String key) {
		return getDouble(key);
	}

	/**
	 * Sets a double value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #saveConfig()
	 */
	public void setDouble(String key, Double value) {
		set(key, value);
	}

	/**
	 * @deprecated Use {@link #setDouble(String, Double)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetDouble(String key, Double value) {
		setDouble(key, value);
	}

	/** Gets an OfflinePlayer value.
	 *
	 * @param key config path
	 * @return OfflinePlayer value or {@code null} if not present
	 */
	public OfflinePlayer getOfflinePlayer(String key) {
		return this.file.getConfig().getOfflinePlayer(key);
	}

	/** Sets an OfflinePlayer value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #saveConfig()
	 */
	public void setOfflinePlayer(String key, OfflinePlayer value) {
		set(key, value);
	}

	/** Sets a ConfigurationSerializable value in-memory.
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #saveConfig()
	 */
	public void setSerializable(String key, ConfigurationSerializable value) {
		this.file.getConfig().set(key, value);
	}

	/** Gets a ConfigurationSerializable value.
	 *
	 * @param key config path
	 * @param type class of the value to get
	 * @return deserialized value, or {@code null} if not present
	 */
	public <T extends ConfigurationSerializable> T getSerializable(String key, Class<T> type) {
		return this.file.getConfig().getSerializable(key, type);
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
	public <T extends Number> Vector2<T> getVector2(String key) {
		return new Vector2((T) this.file.getConfig().get(key + ".x"), (T) this.file.getConfig().get(key + ".y"));
	}

	/**
	 * @deprecated Use {@link #getVector2(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public <T extends Number> Vector2<T> GetVector2(String key) {
		return getVector2(key);
	}

	/**
	 * Sets a 2D vector in-memory.
	 *
	 * <p>Values are written to nested keys: {@code key + ".x"} and {@code key + ".y"}.</p>
	 *
	 * @param key base config path
	 * @param value vector to store
	 * @see #saveConfig()
	 */
	public <T extends Number> void setVector2(String key, Vector2<T> value) {
		set(key + ".x", value.x);
		set(key + ".y", value.y);
	}

	/**
	 * @deprecated Use {@link #setVector2(String, Vector2)}.
	 */
	@Deprecated @MarkedForRemoval
	public <T extends Number> void SetVector2(String key, Vector2<T> value) {
		setVector2(key, value);
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
	public <T extends Number> Vector3<T> getVector3(String key) {
		return new Vector3(
				(T) this.file.getConfig().get(key + ".x"),
				(T) this.file.getConfig().get(key + ".y"),
				(T) this.file.getConfig().get(key + ".z")
				);
	}

	/**
	 * @deprecated Use {@link #getVector3(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public <T extends Number> Vector3<T> GetVector3(String key) {
		return getVector3(key);
	}

	/**
	 * Sets a 3D vector in-memory.
	 *
	 * <p>Values are written to nested keys: {@code key + ".x"}, {@code key + ".y"}, {@code key + ".z"}.</p>
	 *
	 * @param key base config path
	 * @param value vector to store
	 * @see #saveConfig()
	 */
	public <T extends Number> void setVector3(String key, Vector3<T> value) {
		set(key + ".x", value.x);
		set(key + ".y", value.y);
		set(key + ".z", value.z);
	}

	/**
	 * @deprecated Use {@link #setVector3(String, Vector3)}.
	 */
	@Deprecated @MarkedForRemoval
	public <T extends Number> void SetVector3(String key, Vector3<T> value) {
		setVector3(key, value);
	}

	/**
	 * Gets a {@link Material} by name.
	 *
	 * @param key config path containing the material name
	 * @return resolved material, or {@code null} if the name is missing/invalid
	 */
	public Material getMaterial(String key) {
		return Material.getMaterial(getString(key));
	}

	/**
	 * @deprecated Use {@link #getMaterial(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public Material GetMaterial(String key) {
		return getMaterial(key);
	}
	
	/**
	 * Stores a {@link Material} as its enum name.
	 *
	 * @param key config path
	 * @param material material to store
	 * @see #saveConfig()
	 */
	public void setMaterial(String key, Material material) {
		set(key, material.toString());
	}

	/**
	 * @deprecated Use {@link #setMaterial(String, Material)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetMaterial(String key, Material material) {
		setMaterial(key, material);
	}

	/**
	 * Gets a raw value from the underlying Bukkit configuration.
	 *
	 * @param key config path
	 * @return stored value, or {@code null} if missing
	 */
	public Object get(String key) {
		return this.file.getConfig().get(key);
	}

	/**
	 * @deprecated Use {@link #get(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public Object Get(String key) {
		return get(key);
	}

	/**
	 * Sets a raw value in the underlying Bukkit configuration (in-memory).
	 *
	 * @param key config path
	 * @param value value to set
	 * @see #saveConfig()
	 */
	public void set(String key, Object value) {
		this.file.getConfig().set(key, value);
	}

	/**
	 * @deprecated Use {@link #set(String, Object)}.
	 */
	@Deprecated @MarkedForRemoval
	public void Set(String key, Object value) {
		set(key, value);
	}

	/**
	 * Gets a list of strings.
	 *
	 * @param key config path
	 * @return list (never {@code null}; may be empty)
	 */
	public List<String> getStringList(String key) {
		return this.file.getConfig().getStringList(key);
	}

	/**
	 * @deprecated Use {@link #getStringList(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public List<String> GetStringList(String key) {
		return getStringList(key);
	}

	/**
	 * Sets a list of strings in-memory.
	 *
	 * @param key config path
	 * @param value list to store
	 * @see #saveConfig()
	 */
	public void setStringList(String key, List<String> value) {
		set(key, value);
	}

	/**
	 * @deprecated Use {@link #setStringList(String, List)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetStringList(String key, List<String> value) {
		setStringList(key, value);
	}

	/**
	 * Gets a list of integers.
	 *
	 * @param key config path
	 * @return list (never {@code null}; may be empty)
	 */
	public List<Integer> getIntList(String key) {
		return this.file.getConfig().getIntegerList(key);
	}

	/**
	 * @deprecated Use {@link #getIntList(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public List<Integer> GetIntList(String key) {
		return getIntList(key);
	}

	/**
	 * Sets a list of integers in-memory.
	 *
	 * @param key config path
	 * @param value list to store
	 * @see #saveConfig()
	 */
	public void setIntList(String key, List<Integer> value) {
		set(key, value);
	}

	/**
	 * @deprecated Use {@link #setIntList(String, List)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetIntList(String key, List<Integer> value) {
		setIntList(key, value);
	}

	/**
	 * Gets a list of doubles.
	 *
	 * @param key config path
	 * @return list (never {@code null}; may be empty)
	 */
	public List<Double> getDoubleList(String key) {
		return this.file.getConfig().getDoubleList(key);
	}

	/**
	 * @deprecated Use {@link #getDoubleList(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public List<Double> GetDoubleList(String key) {
		return getDoubleList(key);
	}

	/**
	 * Sets a list of doubles in-memory.
	 *
	 * @param key config path
	 * @param value list to store
	 * @see #saveConfig()
	 */
	public void setDoubleList(String key, List<Double> value) {
		set(key, value);
	}

	/**
	 * @deprecated Use {@link #setDoubleList(String, List)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetDoubleList(String key, List<Double> value) {
		setDoubleList(key, value);
	}

	/**
	 * Gets a raw list.
	 *
	 * @param key config path
	 * @return list, or {@code null} if missing
	 */
	public List<?> getList(String key) {
		return this.file.getConfig().getList(key);
	}

	/**
	 * @deprecated Use {@link #getList(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public List<?> GetList(String key) {
		return getList(key);
	}

	/**
	 * Sets a raw list in-memory.
	 *
	 * @param key config path
	 * @param value list to store
	 * @see #saveConfig()
	 */
	public void setList(String key, List<?> value) {
		set(key, value);
	}

	/**
	 * @deprecated Use {@link #setList(String, List)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetList(String key, List<?> value) {
		setList(key, value);
	}

	/**
	 * Gets an {@link ItemStack}.
	 *
	 * @param key config path
	 * @return deserialized item, or {@code null} if missing
	 */
	public ItemStack getItemStack(String key) {
		return this.file.getConfig().getItemStack(key);
	}

	/**
	 * @deprecated Use {@link #getItemStack(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public ItemStack GetItemStack(String key) {
		return getItemStack(key);
	}

	/**
	 * Stores an {@link ItemStack} (in-memory).
	 *
	 * @param key config path
	 * @param item item to store
	 * @see #saveConfig()
	 */
	public void setItemStack(String key, ItemStack item) {
		set(key, item);
	}

	/**
	 * @deprecated Use {@link #setItemStack(String, ItemStack)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetItemStack(String key, ItemStack item) {
		setItemStack(key, item);
	}

	/**
	 * Checks whether a key exists.
	 *
	 * @param key config path
	 * @return {@code true} if present; {@code false} if not present; or {@code null} when {@code key}
	 *         is {@code null}/empty (also logs an error)
	 */
	public Boolean hasKey(String key) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.hasKey() was called with a null or empty key!");
			return null;
		}
		return this.file.getConfig().contains(key, true);
	}

	/**
	 * @deprecated Use {@link #hasKey(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public Boolean HasKey(String key) {
		return hasKey(key);
	}

	/**
	 * Gets a {@link ConfigurationSection} at the given key.
	 *
	 * <p>This first attempts {@link YamlFile#getConfigurationSection(String)}. If that returns
	 * {@code null}, it falls back to {@link #getSection(String)} for compatibility with different
	 * underlying YAML implementations.</p>
	 *
	 * @param key config path
	 * @return the configuration section for {@code key}, or {@code null} if {@code key} is
	 *         {@code null}/empty (logs an error) or the section does not exist
	 */
	public ConfigurationSection getConfigurationSection(String key) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.getConfigurationSection() was called with a null or empty key!");
			return null;
		}
		
		ConfigurationSection section = getFile().getConfigurationSection(key);
		return section != null ? section : getSection(key);
	}

	/**
	 * @deprecated Use {@link #getConfigurationSection(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public ConfigurationSection GetConfigurationSection(String key) {
		return getConfigurationSection(key);
	}

	/**
	 * Gets a section at the given key.
	 *
	 * @param key config path
	 * @return the section, or {@code null} if missing/invalid key
	 */
	public ConfigurationSection getSection(String key) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.getSection() was called with a null or empty key!");
			return null;
		}
		return getFile().getSection(key);
	}

	/**
	 * @deprecated Use {@link #getSection(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public ConfigurationSection GetSection(String key) {
		return getSection(key);
	}

	/**
	 * Gets comments associated with a key.
	 *
	 * @param key config path
	 * @return comment lines, or {@code null} if {@code key} is {@code null}/empty
	 */
	public List<String> getComments(String key) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.getComment() was called with a null or empty key!");
			return null;
		}
		return getFile().getConfig().getComments(key);
	}

	/**
	 * @deprecated Use {@link #getComments(String)}.
	 */
	@Deprecated @MarkedForRemoval
	public List<String> GetComments(String key) {
		return getComments(key);
	}

	/**
	 * Sets comments for a key.
	 *
	 * @param key config path
	 * @param comments comment lines to store
	 */
	public void setComments(String key, List<String> comments) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.setComment() was called with a null or empty key!");
		}
		//getFile().setComments(key, comments);
		getFile().getConfig().setComments(key, comments);
	}

	/**
	 * @deprecated Use {@link #setComments(String, List)}.
	 */
	@Deprecated @MarkedForRemoval
	public void SetComments(String key, List<String> comments) {
		setComments(key, comments);
	}

	/**
	 * Gets inline comments associated with a key.
	 *
	 * @param key config path
	 * @return inline comment lines, or {@code null} if {@code key} is {@code null}/empty
	 */
	public List<String> getInlineComment(String key) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.getInlineComment() was called with a null or empty key!");
			return null;
		}
		return getFile().getConfig().getInlineComments(key);
	}

	/**
	 * Sets inline comments for a key.
	 *
	 * @param key config path
	 * @param comments inline comment lines to store
	 */
	public void setInlineComment(String key, List<String> comments) {
		if (Utils.IsStringNullOrEmpty(key)) {
			Log.error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.setInlineComment() was called with a null or empty key!");
		}
		getFile().getConfig().setInlineComments(key, comments);
	}

	/**
	 * Returns the underlying YAML file wrapper.
	 *
	 * @return backing {@link YamlFile}
	 */
	public YamlFile getFile() {
		return this.file;
	}

	/**
	 * @deprecated Use {@link #getFile()}.
	 */
	@Deprecated @MarkedForRemoval
	public YamlFile GetFile() {
		return getFile();
	}

	/**
	 * Reloads the configuration from disk.
	 */
	public void reload() {
		this.file.reload();
		//this.validate();
	}

	/**
	 * @deprecated Use {@link #reload()}.
	 */
	@Deprecated @MarkedForRemoval
	public void Reload() {
		reload();
	}

	/**
	 * Saves the configuration to disk.
	 */
	public void saveConfig() {
		this.file.save();
	}

	/**
	 * @deprecated Use {@link #saveConfig()}.
	 */
	@Deprecated @MarkedForRemoval
	public void SaveConfig() {
		saveConfig();
	}

	// ====================
	// Validation
	// ====================

	/**
	 * Represents a key-value entry for configuration validation.
	 */
	public static class ValidationListEntry {
		public String key;
		public Object defaultValue;
		public List<String> comments;
		public boolean inline = false;

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

		/** Constructs a ValidationListEntry with a key, default value, comments, and inline flag. */
		public ValidationListEntry(String key, Object defaultValue, List<String> comments, boolean inline) {
			this.key = key;
			this.defaultValue = defaultValue;
			this.comments = comments;
			this.inline = inline;
		}

	}

	/** List of entries used for validating the config. */
	List<ValidationListEntry> validationList = new ArrayList<>();

	/**
	 * Adds a validation entry.
	 *
	 * <p>During {@link #validate()}, missing keys will be created with {@code value}.</p>
	 *
	 * @param key config path
	 * @param value default value to write when missing
	 */
	public void addValidationEntry(String key, Object value) {
		this.validationList.add(new ValidationListEntry(key, value));
	}

	/**
	 * @deprecated Use {@link #addValidationEntry(String, Object)}.
	 */
	@Deprecated @MarkedForRemoval
	public void AddValidationEntry(String key, Object value) {
		addValidationEntry(key, value);
	}

	/**
	 * Adds a validation entry with comments.
	 *
	 * @param key config path
	 * @param value default value to write when missing
	 * @param comments comment lines to associate with the key
	 */
	public void addValidationEntry(String key, Object value, List<String> comments) {
		this.validationList.add(new ValidationListEntry(key, value, comments));
	}

	/**
	 * @deprecated Use {@link #addValidationEntry(String, Object, List)}.
	 */
	@Deprecated @MarkedForRemoval
	public void AddValidationEntry(String key, Object value, List<String> comments) {
		addValidationEntry(key, value, comments);
	}

	/**
	 * Adds a validation entry with a single comment line.
	 *
	 * @param key config path
	 * @param value default value to write when missing
	 * @param comment comment line to associate with the key
	 */
	public void addValidationEntry(String key, Object value, String comment) {
		this.validationList.add(new ValidationListEntry(key, value, comment != null ? List.of(comment) : null));
	}

	/**
	 * @deprecated Use {@link #addValidationEntry(String, Object, String)}.
	 */
	@Deprecated @MarkedForRemoval
	public void AddValidationEntry(String key, Object value, String comment) {
		addValidationEntry(key, value, comment);
	}

	/** Adds a validation entry with comments and an inline flag.
	 *
	 * @param key config path
	 * @param value default value to write when missing
	 * @param comments comment lines to associate with the key
	 * @param inline whether the comments should be inline (true) or block (false)
	 */
	public void addValidationEntry(String key, Object value, List<String> comments, boolean inline) {
		this.validationList.add(new ValidationListEntry(key, value, comments, inline));
	}

	/** Adds a validation entry with a single comment line and an inline flag.
	 *
	 * @param key config path
	 * @param value default value to write when missing
	 * @param comment comment line to associate with the key
	 * @param inline whether the comment should be inline (true) or block (false)
	 */
	public void addValidationEntry(String key, Object value, String comment, boolean inline) {
		this.validationList.add(new ValidationListEntry(key, value, comment != null ? List.of(comment) : null, inline));
	}

	/**
	 * Adds validation entries for a {@link Vector2} under {@code key}.
	 *
	 * <p>This registers {@code key + ".x"} and {@code key + ".y"}.</p>
	 *
	 * @param key base config path
	 * @param value vector providing default x/y values
	 */
	public <T extends Number> void addValidationEntry(String key, Vector2<T> value) {
		this.validationList.add(new ValidationListEntry(key + ".x", value.x));
		this.validationList.add(new ValidationListEntry(key + ".y", value.y));
	}

	/**
	 * @deprecated Use {@link #addValidationEntry(String, Vector2)}.
	 */
	@Deprecated @MarkedForRemoval
	public <T extends Number> void AddValidationEntry(String key, Vector2<T> value) {
		addValidationEntry(key, value);
	}

	/**
	 * Adds validation entries for a {@link Vector3} under {@code key}.
	 *
	 * <p>This registers {@code key + ".x"}, {@code key + ".y"} and {@code key + ".z"}.</p>
	 *
	 * @param key base config path
	 * @param value vector providing default x/y/z values
	 */
	public <T extends Number> void addValidationEntry(String key, Vector3<T> value) {
		this.validationList.add(new ValidationListEntry(key + ".x", value.x));
		this.validationList.add(new ValidationListEntry(key + ".y", value.y));
		this.validationList.add(new ValidationListEntry(key + ".z", value.z));
	}

	/**
	 * @deprecated Use {@link #addValidationEntry(String, Vector3)}.
	 */
	@Deprecated @MarkedForRemoval
	public <T extends Number> void AddValidationEntry(String key, Vector3<T> value) {
		addValidationEntry(key, value);
	}

	/**
	 * Ensures all registered validation keys exist.
	 *
	 * <p>If any keys are missing, they are added with their defaults and the file is saved.</p>
	 */
	public void validate() {
		boolean missing = false;
		String parent = new File(file.getFile().getParent()).getName();
		Log.debug(plugin, "Validating " + parent + "/" + file.getFile().getName() + "...");

		for (ValidationListEntry item : this.validationList) {
			if (!this.hasKey(item.key)) {
				this.set(item.key, item.defaultValue);
				if(item.comments != null && !item.comments.isEmpty()) {
					if(item.inline){
						this.setInlineComment(item.key, item.comments);
					}else {
						this.setComments(item.key, item.comments);
					}
				}
				missing = true;
			}
		}

		if (missing) {
			Log.warning(plugin, "New or missing config values have been added.");
			this.saveConfig();
		}
	}

	/**
	 * @deprecated Use {@link #validate()}.
	 */
	@Deprecated @MarkedForRemoval
	public void Validate() {
		validate();
	}
}
