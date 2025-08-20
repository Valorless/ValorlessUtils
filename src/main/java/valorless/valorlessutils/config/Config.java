package valorless.valorlessutils.config;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Handles plugin configuration files.
 * <p>
 * Provides utility methods to get, set, and validate values in a YAML configuration file.
 * Supports primitive types, {@link Vector2}, {@link Vector3}, {@link ItemStack}, {@link Material}, and lists.
 * </p>
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

    // ====================
    // Set methods
    // ====================

    /** Sets a key-value pair in the config. */
    public void Set(String key, Object value) {
        this.file.getConfig().set(key, value);
    }

    /** Sets a {@link Vector2} value in the config. */
    public <T extends Number> void SetVector2(String key, Vector2<T> value) {
        Set(key + ".x", value.x);
        Set(key + ".y", value.y);
    }

    /** Sets a {@link Vector3} value in the config. */
    public <T extends Number> void SetVector3(String key, Vector3<T> value) {
        Set(key + ".x", value.x);
        Set(key + ".y", value.y);
        Set(key + ".z", value.z);
    }

    // ====================
    // Get methods
    // ====================

    /** Retrieves a String from the config. */
    public String GetString(String key) {
        return this.file.getConfig().getString(key);
    }

    /** Retrieves a boolean from the config. */
    public Boolean GetBool(String key) {
        return this.file.getConfig().getBoolean(key);
    }

    /** Retrieves an integer from the config. */
    public Integer GetInt(String key) {
        return this.file.getConfig().getInt(key);
    }

    /** 
     * Retrieves a float (double) value from the config.
     * @deprecated This method is outdated and will be removed in future versions.
     */
    @Deprecated
    public Double GetFloat(String key) {
        return this.file.getConfig().getDouble(key);
    }

    /** Retrieves a double from the config. */
    public Double GetDouble(String key) {
        return this.file.getConfig().getDouble(key);
    }

    /** Retrieves a {@link Vector2} from the config. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T extends Number> Vector2<T> GetVector2(String key) {
        return new Vector2((T) this.file.getConfig().get(key + ".x"), (T) this.file.getConfig().get(key + ".y"));
    }

    /** Retrieves a {@link Vector3} from the config. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T extends Number> Vector3<T> GetVector3(String key) {
        return new Vector3(
                (T) this.file.getConfig().get(key + ".x"),
                (T) this.file.getConfig().get(key + ".y"),
                (T) this.file.getConfig().get(key + ".z")
        );
    }

    /** Retrieves a {@link Material} from the config. */
    public Material GetMaterial(String key) {
        return Material.getMaterial(GetString(key));
    }

    /** Retrieves a generic object from the config. */
    public Object Get(String key) {
        return this.file.getConfig().get(key);
    }

    /** Retrieves a list of strings from the config. */
    public List<String> GetStringList(String key) {
        return this.file.getConfig().getStringList(key);
    }

    /** Retrieves a list of integers from the config. */
    public List<Integer> GetIntList(String key) {
        return this.file.getConfig().getIntegerList(key);
    }

    /** Retrieves a list of doubles from the config. */
    public List<Double> GetDoubleList(String key) {
        return this.file.getConfig().getDoubleList(key);
    }

    /** Retrieves a generic list from the config. */
    public List<?> GetList(String key) {
        return this.file.getConfig().getList(key);
    }

    /** Retrieves an {@link ItemStack} from the config. */
    public ItemStack GetItemStack(String key) {
        return this.file.getConfig().getItemStack(key);
    }

    /** Checks if a key exists in the config. */
    public Boolean HasKey(String key) {
        if (Utils.IsStringNullOrEmpty(key)) {
            Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.HasKey() was called with a null or empty key!");
            return null;
        }
        return this.file.getConfig().contains(key, true);
    }

    /** Retrieves a {@link ConfigurationSection} from the config. */
    public ConfigurationSection GetConfigurationSection(String key) {
        if (Utils.IsStringNullOrEmpty(key)) {
            Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.GetConfigurationSection() was called with a null or empty key!");
            return null;
        }
        return this.file.getConfig().getConfigurationSection(key);
    }

    /** Returns the underlying {@link YamlFile} instance. */
    public YamlFile GetFile() {
        return this.file;
    }

    /** Reloads the configuration file and validates it. */
    public void Reload() {
        this.file.reload();
        this.Validate();
    }

    /** Saves the configuration file. */
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

        public ValidationListEntry(String key, Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }
    }

    /** List of entries used for validating the config. */
    List<ValidationListEntry> validationList = new ArrayList<>();

    /** Adds a key-value pair to the validation list. */
    public void AddValidationEntry(String key, Object value) {
        this.validationList.add(new ValidationListEntry(key, value));
    }

    /** Adds a Vector2 entry to the validation list. */
    public <T extends Number> void AddValidationEntry(String key, Vector2<T> value) {
        this.validationList.add(new ValidationListEntry(key + ".x", value.x));
        this.validationList.add(new ValidationListEntry(key + ".y", value.y));
    }

    /** Adds a Vector3 entry to the validation list. */
    public <T extends Number> void AddValidationEntry(String key, Vector3<T> value) {
        this.validationList.add(new ValidationListEntry(key + ".x", value.x));
        this.validationList.add(new ValidationListEntry(key + ".y", value.y));
        this.validationList.add(new ValidationListEntry(key + ".z", value.z));
    }

    /**
     * Validates the configuration file against the validation list.
     * <p>
     * Adds missing keys with their default values and saves the file if necessary.
     * Logs any added or missing entries.
     * </p>
     */
    public void Validate() {
        Boolean missing = false;
        if (this.GetBool("debug")) {
            Log.Debug(plugin, "Validating " + file.getName());
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
